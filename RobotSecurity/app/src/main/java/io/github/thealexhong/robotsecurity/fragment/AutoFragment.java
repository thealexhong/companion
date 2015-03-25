package io.github.thealexhong.robotsecurity.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import io.github.thealexhong.robotsecurity.MainActivity;
import io.github.thealexhong.robotsecurity.R;
import io.github.thealexhong.robotsecurity.ev3comm.EV3Connector;

/**
 * Autonomous (Super Hero) mode
 */
public class AutoFragment extends BaseFragment
{
    private EV3Connector ev3Connector;
    private boolean faceAlarm = true;
    private boolean soundAlarm = true;
    private boolean swordAlarm = false;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_autonomous, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setStopBtnBig();
        setView();

        // TODO: Listen for voice
        loadSettings();
        ev3Connector = ((MainActivity)getActivity()).getEv3Connector();
    }

    private void loadSettings()
    {
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("io.github.thealexhong.robotsecurity.fragment", Context.MODE_PRIVATE);
        faceAlarm = sharedPrefs.getBoolean("FaceSwitch", true);
        soundAlarm = sharedPrefs.getBoolean("SoundSwitch", true);
        swordAlarm = sharedPrefs.getBoolean("SwordSwitch", false);
    }

    private void setStopBtnInvisible()
    {
        ImageButton btn = (ImageButton) getActivity().findViewById(R.id.btn_stopbig);
        btn.setVisibility(View.GONE);
    }

    private void setStopBtnVisible()
    {
        ImageButton btn = (ImageButton) getActivity().findViewById(R.id.btn_stopbig);
        btn.setVisibility(View.VISIBLE);
    }

    private void setStopBtnBig()
    {
        ImageButton btn = (ImageButton) getActivity().findViewById(R.id.btn_stopbig);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                // TODO: Stop face and stop sound
                ev3Connector.halt();
                setStopBtnInvisible();
            }
        });
    }

    private void setView()
    {
        ImageButton btn_back = (ImageButton) getActivity().findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { getPrevFragment(); }
        });

        ImageButton btn_stop = (ImageButton) getActivity().findViewById(R.id.btn_stop);
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                // TODO: Stop face and stop sound
                ev3Connector.halt();
                getPrevFragment();
            }
        });

        ImageButton btn_help = (ImageButton) getActivity().findViewById(R.id.btn_help);
        btn_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                setStopBtnVisible();
                if(swordAlarm)
                {
                    ev3Connector.moveForward();
                    ev3Connector.fwdA();
                }
            // TODO: Trigger alarms based on settings
            }
        });

    }
}
