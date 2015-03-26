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
import io.github.thealexhong.robotsecurity.wifidirect.DeeDeeProtocol;

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
        if(((MainActivity)getActivity()).getGroupOwner())
        {
            getPrevFragment();
            showNotification("Use your other device as the controller.");
            return;
        }
        ((MainActivity)getActivity()).sendMessage(DeeDeeProtocol.CONNECT);
        if (!mConn)
        {
            ev3Connector = new EV3Connector(SERVICE);
            /* TODO: Uncomment to make it work with EV3
            ev3Connector.setBluetooth(EV3Connector.BT_ON);
            if (ev3Connector.connect()) {
                mConn = true;
                showNotification("Successful Connection!");
            } else {
                getPrevFragment();
                showNotification("Failed Connection. Try again");
            }
            */
            ((MainActivity)getActivity()).setEv3Connector(ev3Connector);
        }

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
                // TODO: stop sound
                ev3Connector.halt();
                ((MainActivity)getActivity()).sendMessage(DeeDeeProtocol.NEUTRAL);
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
                ((MainActivity)getActivity()).sendMessage(DeeDeeProtocol.ATTACK);
                if(swordAlarm)
                {
                    ev3Connector.moveForward();
                    ev3Connector.fwdA();
                }
                if (faceAlarm)
                {
                    ((MainActivity)getActivity()).sendMessage(DeeDeeProtocol.ALERT);
                }
                if(soundAlarm)
                {
                    // TODO: Trigger Sound Alert mode based off alert settings
                }
            }

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
                ((MainActivity)getActivity()).sendMessage(DeeDeeProtocol.DISCONNECT);
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
                    ((MainActivity)getActivity()).sendMessage(DeeDeeProtocol.FWD);
                }
                else
                {
                    if((action == MotionEvent.ACTION_UP) ||
                            (action == MotionEvent.ACTION_CANCEL))
                    {
                        if(mConn)
                        {
                            ev3Connector.halt();
                            ((MainActivity)getActivity()).sendMessage(DeeDeeProtocol.NEUTRAL);
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
                    ((MainActivity)getActivity()).sendMessage(DeeDeeProtocol.BWD);
                }
                else
                {
                    if((action == MotionEvent.ACTION_UP) ||
                            (action == MotionEvent.ACTION_CANCEL))
                    {
                        if(mConn)
                        {
                            ev3Connector.halt();
                            ((MainActivity)getActivity()).sendMessage(DeeDeeProtocol.NEUTRAL);
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
                    ((MainActivity)getActivity()).sendMessage(DeeDeeProtocol.LEFT);
                }
                else
                {
                    if((action == MotionEvent.ACTION_UP) ||
                            (action == MotionEvent.ACTION_CANCEL))
                    {
                        if(mConn)
                        {
                            ev3Connector.halt();
                            ((MainActivity)getActivity()).sendMessage(DeeDeeProtocol.NEUTRAL);
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
                    ((MainActivity)getActivity()).sendMessage(DeeDeeProtocol.RIGHT);
                }
                else
                {
                    if((action == MotionEvent.ACTION_UP) ||
                            (action == MotionEvent.ACTION_CANCEL))
                    {
                        if(mConn)
                        {
                            ev3Connector.halt();
                            ((MainActivity)getActivity()).sendMessage(DeeDeeProtocol.NEUTRAL);
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
                    ((MainActivity)getActivity()).sendMessage(DeeDeeProtocol.ATTACK);
                }
                else
                {
                    if((action == MotionEvent.ACTION_UP) ||
                            (action == MotionEvent.ACTION_CANCEL))
                    {
                        if(mConn)
                        {
                            ev3Connector.halt();
                            ((MainActivity)getActivity()).sendMessage(DeeDeeProtocol.NEUTRAL);
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
                    ((MainActivity)getActivity()).sendMessage(DeeDeeProtocol.ATTACK);
                }
                else
                {
                    if((action == MotionEvent.ACTION_UP) ||
                            (action == MotionEvent.ACTION_CANCEL))
                    {
                        if(mConn)
                        {
                            ev3Connector.halt();
                            ((MainActivity)getActivity()).sendMessage(DeeDeeProtocol.NEUTRAL);
                        }
                    }
                }
                return true;
            }
        });
    }
}