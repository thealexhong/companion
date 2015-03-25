package io.github.thealexhong.robotsecurity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Bundle;
import android.util.Log;

import io.github.thealexhong.robotsecurity.ev3comm.EV3Connector;
import io.github.thealexhong.robotsecurity.fragment.ConnectFragment;
import io.github.thealexhong.robotsecurity.wifidirect.ClientAsyncTask;
import io.github.thealexhong.robotsecurity.wifidirect.WiFiDirectBroadcastReceiver;


public class MainActivity extends Activity
{
    public static final String TAG = "MainActivity";
    public static final int port = 9999;
    private WifiP2pManager manager;
    private final IntentFilter intentFilter = new IntentFilter();
    private Channel channel;
    private BroadcastReceiver receiver = null;
    private String host;

    private EV3Connector ev3Connector;

    public void setEv3Connector(EV3Connector ev3Connector)
    {
        this.ev3Connector = ev3Connector;
    }

    public EV3Connector getEv3Connector()
    {
        return this.ev3Connector;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener()
        {
            @Override
            public void onSuccess()
            {
                Log.i(TAG, "WIFI direct search success");
            }
            @Override
            public void onFailure(int reasonCode)
            {
                Log.i(TAG, "WIFI direct search failure");
            }
        });

        if (savedInstanceState == null)
        {
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new ConnectFragment()).commit();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        receiver = new WiFiDirectBroadcastReceiver(manager, channel, this);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        unregisterReceiver(receiver);
    }

    public void sendMessage(String str)
    {
        ClientAsyncTask client = new ClientAsyncTask(this.host, str);
        client.execute();
        Log.d(TAG, "Sent: " + str);
    }

    public void setOwnerHost(WifiP2pInfo info)
    {
        this.host = info.groupOwnerAddress.getHostAddress();
    }
}
