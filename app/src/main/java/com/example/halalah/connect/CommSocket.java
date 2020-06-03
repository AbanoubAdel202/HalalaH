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

import javax.net.ssl.SSLSocket;

public class CommSocket {
	private static final String TAG = Utils.TAGPUBLIC + CommSocket.class.getSimpleName();

	private SocketChannel mSocketChannel;
	private Selector mSelector;
    private boolean mIsGoOn = true;

    public boolean open(String host, String port) {
    	if (port == null) return false;
    	
        int intPort = Integer.parseInt(port);
		return open(host, intPort);
    }

    public void setStop() {
        mIsGoOn = false;
    }

    public boolean open(String host, int port) {
    	Log.d(TAG, "host="+host+", port="+port);
        if (TextUtils.isEmpty(host) || port <= 0) {
            Log.e(TAG, "host or port error.");
            return false;
        }
        try {
            mSocketChannel = SocketChannel.open();
            mSocketChannel.configureBlocking(false);  
            long mEndtTime = System.currentTimeMillis() + 5000;
            mSocketChannel.connect(new InetSocketAddress(host, port));

            while(!mSocketChannel.finishConnect() && (System.currentTimeMillis() <= mEndtTime)){}
            if(System.currentTimeMillis() > mEndtTime){
                return false;
            }
            mSelector = Selector.open(); 
            mSocketChannel.register(mSelector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public int send(byte[] sendPacket) {
        Log.i(TAG, "send = "+BCDASCII.bytesToHexString(sendPacket));
        int count = 0;
        try {
            mIsGoOn = true;
        	//TODO:
            while (mIsGoOn && mSelector.select() > 0) {
                Iterator it = mSelector.selectedKeys().iterator();
                while (mIsGoOn && it.hasNext()) {
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
        return count;
    }

    public byte[] recv() {
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
                        if (count>0) {
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
        return receive;
    }

    public void close() {
        try {
            if (mSocketChannel != null && mSocketChannel.isConnected()) {
                mSocketChannel.finishConnect();
                mSelector.close();
                mSocketChannel.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
