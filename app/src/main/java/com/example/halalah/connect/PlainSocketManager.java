package com.example.halalah.connect;

import android.text.TextUtils;
import android.util.Log;

import com.example.halalah.Utils;
import com.example.halalah.iso8583.BCDASCII;
import com.example.halalah.iso8583.ISO8583;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class PlainSocketManager implements iConnect{

    private static final String TAG = Utils.TAGPUBLIC + PlainSocketManager.class.getSimpleName();
    private static int connectionStatus = CONNECTION_STATUS_DISCONNECTED;

    private static PlainSocketManager mInstance;
    private static SocketChannel mSocketChannel;
    private Selector mSelector;
    private boolean mIsGoOn = true;

    private PlainSocketManager() {
    }

    private void updateConnectionStatus(int status) {
        connectionStatus = status;
    }

    public int getConnectionStatus(){
        return connectionStatus;
    }

    public static PlainSocketManager getInstance() {
        if (mInstance == null) {
            mInstance = new PlainSocketManager();
        }
        return mInstance;
    }

    public boolean connect(String host, String port) {
        if (port == null) return false;

        int intPort = Integer.parseInt(port);
         boolean isOpended = open(host, intPort);
         return isOpended;
    }

    public boolean open(String host, int port) {
        updateConnectionStatus(CONNECTION_STATUS_IN_PROGRESS);
        Log.d(TAG, "host=" + host + ", port=" + port);
        if (TextUtils.isEmpty(host) || port <= 0) {
            updateConnectionStatus(CONNECTION_STATUS_DISCONNECTED);
            Log.e(TAG, "host or port error.");
            return false;
        }
        try {
            Log.d(TAG, "trying to open channel, " + host + ":" + port);
            mSocketChannel = SocketChannel.open();
            mSocketChannel.configureBlocking(false);
            long mEndtTime = System.currentTimeMillis() + 5000;
            mSocketChannel.connect(new InetSocketAddress(host, port));

            while (!mSocketChannel.finishConnect() && (System.currentTimeMillis() <= mEndtTime)) { }
            if (System.currentTimeMillis() > mEndtTime) {
                updateConnectionStatus(CONNECTION_STATUS_DISCONNECTED);
                Log.d(TAG, "Couldn't open channel, System.currentTimeMillis() > mEndtTime");
                return false;
            }
            mSelector = Selector.open();
            mSocketChannel.register(mSelector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        } catch (IOException e) {
            Log.d(TAG, "Couldn't open channel, " + e.getMessage());
            e.printStackTrace();
            updateConnectionStatus(CONNECTION_STATUS_DISCONNECTED);
            return false;
        }
        Log.d(TAG, "Channel opened.");
        updateConnectionStatus(CONNECTION_STATUS_CONNECTED);
        return true;
    }

    public int send(byte[] sendPacket) {
        updateConnectionStatus(CONNECTION_STATUS_IN_PROGRESS);
        Log.i(TAG, "send = " + BCDASCII.bytesToHexString(sendPacket));
        int count = 0;
        try {
            //TODO:
            while (mSelector.select() > 0) {
                Iterator it = mSelector.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey sk = (SelectionKey) it.next();
                    it.remove();
                    if (sk.isWritable()) {
                        SocketChannel socketChannel = (SocketChannel) sk.channel();
                        ByteBuffer bb = ByteBuffer.wrap(sendPacket, 0, sendPacket.length);
                        count = socketChannel.write(bb);
                        Log.d(TAG, "Send ok.");
                        return count;
                    }
                    mSelector.selectedKeys().remove(sk);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        updateConnectionStatus(CONNECTION_STATUS_CONNECTED);
        return count;
    }

    public byte[] receive() {
        updateConnectionStatus(CONNECTION_STATUS_IN_PROGRESS);
        byte[] receive = null;
        int count = 0;
        try {
            mIsGoOn = true;
            //TODO:
            while (mIsGoOn && mSelector.select() > 0) {
                Iterator it = mSelector.selectedKeys().iterator();
                while (mIsGoOn && it.hasNext()) {
                    SelectionKey sk = (SelectionKey) it.next();
                    it.remove();
                    if (sk.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) sk.channel();
                        ByteBuffer bb = ByteBuffer.allocate(ISO8583.MAXBUFFERLEN);
                        bb.clear();
                        count = socketChannel.read(bb);
                        bb.flip();
                        if (count > 0) {
                            receive = new byte[count];
                            System.arraycopy(bb.array(), 0, receive, 0, count);
                            Log.d(TAG, "recv " + socketChannel.socket().getRemoteSocketAddress() + ": " + BCDASCII.bytesToHexString(receive));
                            return receive;
                        } else {
                            return null;
                        }
                    }
                    mSelector.selectedKeys().remove(sk);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateConnectionStatus(CONNECTION_STATUS_CONNECTED);
        return receive;
    }

    public void disconnect() {
        try {
            if (mSocketChannel != null && mSocketChannel.isConnected()) {
                mSocketChannel.finishConnect();
                mSelector.close();
                mSocketChannel.close();
                updateConnectionStatus(CONNECTION_STATUS_DISCONNECTED);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
