package io.github.thealexhong.robotsecurity.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import io.github.thealexhong.robotsecurity.MainActivity;
import io.github.thealexhong.robotsecurity.R;
import io.github.thealexhong.robotsecurity.ev3comm.EV3Connector;

public class ControlFragment extends BaseFragment
{
    public final String SERVICE = "00:16:53:46:59:8E";
    private boolean mConn = false;
    private EV3Connector ev3Connector;

    private boolean faceAlarm = true;
    private boolean soundAlarm = true;
    private boolean swordAlarm = false;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_control, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (!mConn)
        {
            ev3Connector = new EV3Connector(SERVICE);
            ev3Connector.setBluetooth(EV3Connector.BT_ON);
            if (ev3Connector.connect()) {
                mConn = true;
                showNotification("Successful Connection!");
            } else {
                getPrevFragment();
                showNotification("Failed Connection. Try again");
            }
            ((MainActivity)getActivity()).setEv3Connector(ev3Connector);
        }

        // TODO: Add Wifi direct connection here

        setSettingBtn();
        setAutoBtn();
        setBackBtn();
        setControlBtn();
        setHelpBtn();
        setStopBtn();
        loadSettings();
    }

    private void setStopBtnInvisible()
    {
        ImageButton btn = (ImageButton) getActivity().findViewById(R.id.btn_stop);
        btn.setVisibility(View.GONE);
    }

    private void setStopBtnVisible()
    {
        ImageButton btn = (ImageButton) getActivity().findViewById(R.id.btn_stop);
        btn.setVisibility(View.VISIBLE);
    }

    private void setStopBtn()
    {
        ImageButton btn = (ImageButton) getActivity().findViewById(R.id.btn_stop);
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

    private void setHelpBtn()
    {
        ImageButton btn = (ImageButton) getActivity().findViewById(R.id.btn_help);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                setStopBtnVisible();
                if(swordAlarm)
                {
                    ev3Connector.moveForward();
                    ev3Connector.fwdA();
                }
            }
                // TODO: Trigger Face + Sound Alert mode based off alert settings
        });
    }

    private void setSettingBtn()
    {
        ImageButton btn = (ImageButton) getActivity().findViewById(R.id.btn_setting);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                swapFragment(new AlarmFragment());
            }
        });
    }

    private void setAutoBtn()
    {
        ImageButton btn = (ImageButton) getActivity().findViewById(R.id.btn_auto);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                swapFragment(new AutoFragment());
            }
        });
    }

    private void loadSettings()
    {
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("io.github.thealexhong.robotsecurity.fragment", Context.MODE_PRIVATE);
        faceAlarm = sharedPrefs.getBoolean("FaceSwitch", true);
        soundAlarm = sharedPrefs.getBoolean("SoundSwitch", true);
        swordAlarm = sharedPrefs.getBoolean("SwordSwitch", false);
    }

    /**
     * Disconnect from BT
     */
    private void setBackBtn()
    {
        ImageButton btn_back = (ImageButton) getActivity().findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mConn = false;
                ev3Connector.disconnect();
                getPrevFragment();
                showNotification("Disconnected from Companion");
            }
        });
    }

    /**
     * Action Commands
     */
    private void setControlBtn()
    {
        ImageButton btn_fwd = (ImageButton) getActivity().findViewById(R.id.btn_fwd);
        ImageButton btn_bwd = (ImageButton) getActivity().findViewById(R.id.btn_bwd);
        ImageButton btn_left = (ImageButton) getActivity().findViewById(R.id.btn_left);
        ImageButton btn_right = (ImageButton) getActivity().findViewById(R.id.btn_right);
        ImageButton btn_attackA = (ImageButton) getActivity().findViewById(R.id.btn_atka);
        ImageButton btn_attackB = (ImageButton) getActivity().findViewById(R.id.btn_atkb);

        btn_fwd.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent event)
            {
                int action = event.getAction();
                if(action == MotionEvent.ACTION_DOWN)
                {
                    ev3Connector.moveForward();
                }
                else
                {
                    if((action == MotionEvent.ACTION_UP) ||
                            (action == MotionEvent.ACTION_CANCEL))
                    {
                        if(mConn)
                        {
                            ev3Connector.halt();
                        }
                    }
                }
                return true;
            }
        });

        btn_bwd.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent event)
            {
                int action = event.getAction();
                if(action == MotionEvent.ACTION_DOWN)
                {
                    ev3Connector.moveBackward();
                }
                else
                {
                    if((action == MotionEvent.ACTION_UP) ||
                            (action == MotionEvent.ACTION_CANCEL))
                    {
                        if(mConn)
                        {
                            ev3Connector.halt();
                        }
                    }
                }
                return true;
            }
        });

        btn_left.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent event)
            {
                int action = event.getAction();
                if(action == MotionEvent.ACTION_DOWN)
                {
                    ev3Connector.turnLeft();
                }
                else
                {
                    if((action == MotionEvent.ACTION_UP) ||
                            (action == MotionEvent.ACTION_CANCEL))
                    {
                        if(mConn)
                        {
                            ev3Connector.halt();
                        }
                    }
                }
                return true;
            }
        });

        btn_right.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent event)
            {
                int action = event.getAction();
                if(action == MotionEvent.ACTION_DOWN)
                {
                    ev3Connector.turnRight();
                }
                else
                {
                    if((action == MotionEvent.ACTION_UP) ||
                            (action == MotionEvent.ACTION_CANCEL))
                    {
                        if(mConn)
                        {
                            ev3Connector.halt();
                        }
                    }
                }
                return true;
            }
        });

        btn_attackA.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent event)
            {
                int action = event.getAction();
                if(action == MotionEvent.ACTION_DOWN)
                {
                    ev3Connector.fwdA();
                }
                else
                {
                    if((action == MotionEvent.ACTION_UP) ||
                            (action == MotionEvent.ACTION_CANCEL))
                    {
                        if(mConn)
                        {
                            ev3Connector.halt();
                        }
                    }
                }
                return true;
            }
        });

        btn_attackB.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent event)
            {
                int action = event.getAction();
                if(action == MotionEvent.ACTION_DOWN)
                {
                    ev3Connector.bwdA();
                }
                else
                {
                    if((action == MotionEvent.ACTION_UP) ||
                            (action == MotionEvent.ACTION_CANCEL))
                    {
                        if(mConn)
                        {
                            ev3Connector.halt();
                        }
                    }
                }
                return true;
            }
        });
    }
}