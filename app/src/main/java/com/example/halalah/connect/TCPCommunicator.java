package com.example.halalah.connect;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.halalah.iso8583.BCDASCII;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

public class TCPCommunicator {
    private static TCPCommunicator uniqInstance;
    private static String serverHost;
    private static int serverPort;
    private static List<TCPListener> allListeners;
    private static DataOutputStream out;
    private static DataInputStream in;
    private static Socket s;
    private static Handler UIHandler;
    private static Context appContext;

    private static int BufferSize = 1024;

    private TCPCommunicator() {
        allListeners = new ArrayList<TCPListener>();
    }

    public static TCPCommunicator getInstance() {
        if (uniqInstance == null) {
            uniqInstance = new TCPCommunicator();
        }
        return uniqInstance;
    }

    public TCPWriterErrors init(String host, int port) {
        setServerHost(host);
        setServerPort(port);
        InitTCPClientTask task = new InitTCPClientTask();
        task.execute(new Void[0]);
        return TCPWriterErrors.OK;
    }

    public static TCPWriterErrors writeToSocket(final String message, Handler handle, Context context) {
        UIHandler = handle;
        appContext = context;
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                try {
                    String outMsg = message + System.getProperty("line.separator");
                    out.writeUTF(outMsg);
                    out.flush();
                    Log.i("TcpClient", "sent: " + outMsg);
                } catch (Exception e) {
                   /* UIHandler.post(new Runnable() {

                        @Override
                        public void run() {
                        //    Toast.makeText(appContext, "a problem has occured, the app might not be able to reach the server", Toast.LENGTH_SHORT).show();
                        }
                    });*/
                }
            }

        };
        Thread thread = new Thread(runnable);
        thread.start();
        return TCPWriterErrors.OK;
    }

    public static TCPWriterErrors writeToSocket(final byte[] buffer, Handler handle, Context context) {
        UIHandler = handle;
        appContext = context;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    out.write(buffer);
                    out.flush();
                    Log.i("TcpClient", "sent: " + buffer.toString());
                } catch (Exception e) {
                    UIHandler.post(new Runnable() {

                        @Override
                        public void run() {
//                            Toast.makeText(appContext, "a problem has occured, the app might not be able to reach the server", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        return TCPWriterErrors.OK;
    }


    public static void addListener(TCPListener listener) {
        allListeners.clear();
        allListeners.add(listener);
    }

    public static void removeAllListeners() {
        allListeners.clear();
    }

    public static void closeStreams() {
        try {
            s.close();
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getServerHost() {
        return serverHost;
    }

    public static void setServerHost(String serverHost) {
        TCPCommunicator.serverHost = serverHost;
    }

    public static int getServerPort() {
        return serverPort;
    }

    public static void setServerPort(int serverPort) {
        TCPCommunicator.serverPort = serverPort;
    }


    public class InitTCPClientTask extends AsyncTask<Void, Void, Void> {
        public InitTCPClientTask() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                s = new Socket(getServerHost(), getServerPort());
                in = new DataInputStream(s.getInputStream());
                out = new DataOutputStream(s.getOutputStream());
                for (TCPListener listener : allListeners)
                    listener.onTCPConnectionStatusChanged(true);

                while (true) {
                  byte[] buffer = new byte[2048];
                    int length = in.read(buffer);

                   /* CharBuffer charBuffer = CharBuffer.allocate(BufferSize);
                     int length = in.read(charBuffer);
                   if (length > 0) {
                        String inMsg = charBuffer.rewind().toString();

                        Log.i("TcpClient", "received: " + inMsg);
*/
                        for (TCPListener listener : allListeners)
                            listener.onTCPMessageRecieved(buffer);

                 //   }
                }

            } catch (UnknownHostException e) {
                e.printStackTrace();
                for (TCPListener listener : allListeners)
                    listener.onTCPConnectionStatusChanged(false);
            } catch (IOException e) {
                e.printStackTrace();
                for (TCPListener listener : allListeners)
                    listener.onTCPConnectionStatusChanged(false);
            }
            return null;

        }

    }

    public enum TCPWriterErrors {UnknownHostException, IOException, otherProblem, OK}
}
