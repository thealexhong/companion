package io.github.thealexhong.robotsecurity.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import io.github.thealexhong.robotsecurity.MainActivity;
import io.github.thealexhong.robotsecurity.R;
import io.github.thealexhong.robotsecurity.wifidirect.WiFiDirectBroadcastReceiver;

public class DeeDeeFragment extends BaseFragment
{
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    BroadcastReceiver mReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_deedee, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setView();
        setWifiDirect();
    }

    private void setWifiDirect()
    {
        mManager = (WifiP2pManager) getActivity().getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(getActivity(), getActivity().getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, ((MainActivity)getActivity()));
    }

    private void setView()
    {
        ImageButton btn_back = (ImageButton) getActivity().findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                getPrevFragment();
                // TODO: Disconnect from Wifi Direct
            }
        });
    }
}
