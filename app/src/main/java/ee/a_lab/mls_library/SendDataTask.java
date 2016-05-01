package ee.a_lab.mls_library;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by giorg_000 on 27.03.2016.
 */
public class SendDataTask extends AsyncTask<Float, Void, Void> {

    private int mPort;
    private InetAddress inet_addr;
    private DatagramSocket socket;

    private final String LOG_TAG = this.getClass().getSimpleName();

    public String getIp() {
        return inet_addr.toString();
    }

    public boolean setIp(String ip) {
        try {
            inet_addr = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            Log.e("Send data task", e.getMessage());
            return false;
        }
        return true;
    }

    public int getPort() {
        return mPort;
    }

    public boolean setPort(int port) {
        if (port > 0 && port <= 65535) {
            this.mPort = port;
            return true;
        }
        return false;
    }


    @Override
    protected Void doInBackground(Float... params) {
        float x = params[0];

        Log.i(LOG_TAG, "Sending data");
        byte[] buffer = new byte[]{(byte) x};
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, inet_addr, mPort);
        try {
            socket = new DatagramSocket();
            socket.send(packet);
            Log.i(LOG_TAG, "it worked idk");
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        return null;
    }
}
