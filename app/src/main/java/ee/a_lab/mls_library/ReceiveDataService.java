package ee.a_lab.mls_library;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by giorg_000 on 28.03.2016.
 */
public class ReceiveDataService extends Service {

    public static String UDP_BROADCAST = "UDPBroadcast";
    public static String INTENT_DATA = "mls.intent_data.position";
    public static String PORT_NUMBER = "mls.intent_data.port_number";

    private int portNumber;

    private Thread UDPBroadcastThread;
    private Boolean shouldRestartSocketListen = true;


    DatagramSocket socket;

    private void listenAndWaitAndThrowIntent() throws Exception {


        byte[] recvBuf = new byte[3];
        if (socket == null || socket.isClosed()) {
            socket = new DatagramSocket(portNumber);
            socket.setBroadcast(true);
        }
        //socket.setSoTimeout(1000);
        DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
        Log.e("UDP", "Waiting for UDP broadcast");
        socket.receive(packet);

        String senderIP = packet.getAddress().getHostAddress();
        //String message = new String(packet.getData()).trim();

        int n = (int) packet.getData()[0];

        Log.e("UDP", "Got UDB broadcast from " + senderIP + ", message: " + Integer.toString(n));

        broadcastIntent(n);
        socket.close();
    }

    private void broadcastIntent(int message) {
        Log.e("Broadcast", "broadcasting intent");
        Intent intent = new Intent(UDP_BROADCAST);
        intent.putExtra(INTENT_DATA, message);
        sendBroadcast(intent);
    }


    void startListenForUDPBroadcast() {
        UDPBroadcastThread = new Thread(new Runnable() {
            public void run() {
                try {
                    while (shouldRestartSocketListen) {
                        listenAndWaitAndThrowIntent();
                    }
                    //if (!shouldListenForUDPBroadcast) throw new ThreadDeath();
                } catch (Exception e) {
                    Log.i("UDP", "no longer listening for UDP broadcasts cause of error " + e.getMessage());
                }
            }
        });
        UDPBroadcastThread.start();
    }


    public void stopListen() {
        shouldRestartSocketListen = false;
        if (socket != null)
            socket.close();
    }


    @Override
    public void onDestroy() {
        stopListen();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        portNumber = intent.getIntExtra(PORT_NUMBER, 25001);
        shouldRestartSocketListen = true;
        startListenForUDPBroadcast();
        Log.i("UDP", "Service started");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
