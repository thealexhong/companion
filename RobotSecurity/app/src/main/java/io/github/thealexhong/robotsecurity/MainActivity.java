package io.github.thealexhong.robotsecurity;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;

import io.github.thealexhong.robotsecurity.ev3comm.EV3Connector;
import io.github.thealexhong.robotsecurity.fragment.ConnectFragment;


public class MainActivity extends Activity
{
    private EV3Connector ev3Connector;
    private boolean isWifiP2pEnabled = false;
    private final IntentFilter intentFilter = new IntentFilter();
    private WifiP2pManager.Channel channel;
    private WifiP2pManager manager;


    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
        this.isWifiP2pEnabled = isWifiP2pEnabled;
    }

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

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);

        if (savedInstanceState == null)
        {
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new ConnectFragment()).commit();
        }
    }
}
