package io.github.thealexhong.robotsecurity.wifidirect;


import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PeerListener implements PeerListListener
{
    private static final String TAG = "PeerListener";
    private List<WifiP2pDevice> peerList = new ArrayList<>();
    private WifiP2pManager manager;
    private Channel channel;

    public PeerListener(WifiP2pManager manager, Channel channel)
    {
        this.manager = manager;
        this.channel = channel;
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peers)
    {
        peerList.addAll(peers.getDeviceList());
        if(peerList.size() > 0)
        {
            for (int i = 0; i < peerList.size(); i++)
            {
                WifiP2pDevice device = peerList.get(i);
                if(device.status == WifiP2pDevice.AVAILABLE)
                {
                    WifiP2pConfig config = new WifiP2pConfig();
                    config.deviceAddress = device.deviceAddress;
                    config.wps.setup = WpsInfo.PBC;

                    manager.connect(channel, config, new WifiP2pManager.ActionListener()
                    {
                        @Override
                        public void onSuccess()
                        {
                            Log.i(TAG, "Connection request success");
                        }

                        @Override
                        public void onFailure(int reason)
                        {
                            Log.i(TAG, "Connection request failure");
                        }
                    });
                }
            }
        }
    }
}
