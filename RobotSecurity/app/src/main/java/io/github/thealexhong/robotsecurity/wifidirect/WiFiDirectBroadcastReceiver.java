package io.github.thealexhong.robotsecurity.wifidirect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import io.github.thealexhong.robotsecurity.MainActivity;

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver
{
    public final String TAG = "WDBR";
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private MainActivity activity;

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel, MainActivity activity)
    {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        this.activity = activity;

    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED)
            {
                activity.setIsWifiP2pEnabled(true);
            }
            else
            {
                activity.setIsWifiP2pEnabled(false);
            }
            Log.d(TAG, "P2P state changed - " + state);
        }
        else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action))
        {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
        }
        else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action))
        {
            // Respond to new connection or disconnections
        }
        else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action))
        {
            // Respond to this device's wifi state changing
        }
    }
}
