package io.github.thealexhong.robotsecurity.wifidirect;

import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.util.Log;

import io.github.thealexhong.robotsecurity.MainActivity;

public class ConnectionListener implements ConnectionInfoListener
{
    private static final String TAG = "ConnectionListener";
    private MainActivity activity;
    public ConnectionListener(MainActivity activity)
    {
        this.activity = activity;
    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info)
    {
        Log.i(TAG, "Group Owner: " + info.groupOwnerAddress.getHostAddress());
        Log.i(TAG, "I am Group Owner: " + info.isGroupOwner);
        Log.i(TAG, "Group is formed: " + info.groupFormed);

        this.activity.setOwnerHost(info);
        if(info.groupFormed && info.isGroupOwner)
        {
            ServerThread thread = new ServerThread(this.activity);
            thread.start();
        }
    }
}
