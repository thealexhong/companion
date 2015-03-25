package io.github.thealexhong.robotsecurity.wifidirect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.util.Log;

import io.github.thealexhong.robotsecurity.MainActivity;

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver
{
    public static final String TAG = "WifiDirectBroadcastReceiver";
    private WifiP2pManager manager;
    private Channel channel;
    private MainActivity activity;

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel, MainActivity activity)
    {
        super();
        this.manager = manager;
        this.channel = channel;
        this.activity = activity;

    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();
        if(manager == null)
        {
            return;
        }
        if(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action))
        {
            manager.requestPeers(channel, new PeerListener(manager, channel));
        }
        else
        {
            if(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action))
            {
                NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                if(networkInfo.isConnected())
                {
                    Log.i(TAG, "I am connected!");
                    manager.requestConnectionInfo(channel, new ConnectionListener(this.activity));
                }
                else
                {
                    Log.i(TAG, "I am disconnected");
                }
            }
        }
    }
}
