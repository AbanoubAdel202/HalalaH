package com.example.halalah.connect;

/**
 * Created by miles on 12/16/15.
 * References:
 * http://www.myandroidsolutions.com/2012/07/20/android-tcp-connection-tutorial/
 * http://adblogcat.com/ssl-sockets-android-and-server-using-a-certificate/
 */

import android.os.AsyncTask;
import android.util.Log;

import com.example.halalah.Utils;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyStore;

import javax.net.ssl.SSLSocket;

public class SSLSocketManager implements iConnect {

    public static final String TAG = Utils.TAGPUBLIC + SSLSocketManager.class.getSimpleName();
    private static int connectionStatus = CONNECTION_STATUS_DISCONNECTED;
    private static SSLSocketManager mInstance;
    public SSLSocket socket = null;
    private InputStream mKeyInputStream;
    private char[] keystorepass = "hala123456".toCharArray(); // If your keystore has a password, put it here

    // These handle the I/O
    private PrintWriter out;

    /**
     * Constructor of the class. OnMessagedReceived listens for the messages received from server
     */

    public static SSLSocketManager getInstance(InputStream keyInputStream) {
        if (mInstance == null) {
            mInstance = new SSLSocketManager(keyInputStream);
        }
        return mInstance;
    }

    private SSLSocketManager(InputStream mKeyInputStream) {
        this.mKeyInputStream = mKeyInputStream;
    }

    public boolean connect(String ip, String port) {
        try {
            return connect(ip, Integer.valueOf(port));
        } catch (NumberFormatException e){
            e.printStackTrace();
            updateConnectionStatus(CONNECTION_STATUS_DISCONNECTED);
            return false;
        }
    }

    private boolean connect(String ip, int port) {

        try {
            Log.d(TAG, "Connecting...");
            updateConnectionStatus(CONNECTION_STATUS_IN_PROGRESS);
            //create a socket to make the connection with the server

            KeyStore ks = KeyStore.getInstance("BKS");
            // Load the keystore file
            ks.load(mKeyInputStream, keystorepass);

            // Create a SSLSocketFactory that allows for self signed certs
            SSLSocketFactory socketFactory = new SSLSocketFactory(ks);
            socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            try {
                // Build our socket with the factory and the server info
                socket = (SSLSocket) socketFactory.createSocket(new Socket(ip, port), ip, port, false);
                socket.startHandshake();
                // Create the message sender
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                updateConnectionStatus(CONNECTION_STATUS_CONNECTED);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                updateConnectionStatus(CONNECTION_STATUS_DISCONNECTED);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            updateConnectionStatus(CONNECTION_STATUS_DISCONNECTED);
            return false;
        }
    }

    /**
     * Sends the message entered by client to the server.
     *
     * @param message text entered by client
     */
    public int send(byte[] message) {
        // As of Android 4.0 we have to send to network in another thread...
        if (message != null && message.length > 0 && out != null && !out.checkError()) {
            try {
                out.println(message);
                out.flush();
                return message.length;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                return -1;
            }
        } else {
            return -1;
        }
    }

    public byte[] receive() {
        try {
            // Create the message receiver
            return getBytesFromInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getConnectionStatus() {
        return connectionStatus;
    }

    public void disconnect() {
        if (socket != null && socket.isConnected()){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] getBytesFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[0xFFFF];
        for (int len = is.read(buffer); len != -1; len = is.read(buffer)) {
            os.write(buffer, 0, len);
        }
        return os.toByteArray();
    }

    private void updateConnectionStatus(int status) {
        connectionStatus = status;
    }
}