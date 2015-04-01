package io.github.thealexhong.robotsecurity.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm;
import io.github.thealexhong.robotsecurity.MainActivity;
import io.github.thealexhong.robotsecurity.R;
import io.github.thealexhong.robotsecurity.ev3comm.EV3Connector;
import io.github.thealexhong.robotsecurity.wifidirect.DeeDeeProtocol;

/**
 * Autonomous (Super Hero) mode
 */
public class AutoFragment extends BaseFragment
{
    public static final String TAG = "AutoFragment";
    private EV3Connector ev3Connector;
    private boolean faceAlarm = true;
    private boolean soundAlarm = true;
    private boolean swordAlarm = false;
    private Thread listenThread;
    private AudioDispatcher dispatcher;

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
                ((MainActivity)getActivity()).sendMessage(DeeDeeProtocol.NEUTRAL);
                ev3Connector.halt();
                setStopBtnInvisible();
            }
        });
    }

    private void setView()
    {
        // Set Microphone listener
        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050,1024,0);
        dispatcher.addAudioProcessor(new PitchProcessor(PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {
                final float pitchInHz = pitchDetectionResult.getPitch();
                if(getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.v(TAG, "" + pitchInHz);
                            if (pitchInHz > 800)
                            {
                                setStopBtnVisible();
                                ((MainActivity)getActivity()).sendMessage(DeeDeeProtocol.ATTACK);
                                if(swordAlarm)
                                {
                                    ev3Connector.moveForward();
                                    ev3Connector.fwdA();
                                }
                                if(soundAlarm)
                                {
                                    ((MainActivity)getActivity()).sendMessage(DeeDeeProtocol.SOUND);
                                }
                                if (faceAlarm)
                                {
                                    ((MainActivity)getActivity()).sendMessage(DeeDeeProtocol.ALERT);
                                }
                            }
                        }
                    });
                }
            }
        }));
        listenThread = new Thread(dispatcher,"Audio Dispatcher");
        listenThread.start();

        ImageButton btn_back = (ImageButton) getActivity().findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).sendMessage(DeeDeeProtocol.NEUTRAL);
                ev3Connector.halt();
                listenThread.interrupt();
                dispatcher.stop();
                getPrevFragment(); }
        });

        ImageButton btn_stop = (ImageButton) getActivity().findViewById(R.id.btn_stop);
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ((MainActivity)getActivity()).sendMessage(DeeDeeProtocol.NEUTRAL);
                ev3Connector.halt();
                listenThread.interrupt();
                dispatcher.stop();
                getPrevFragment();
            }
        });

        ImageButton btn_help = (ImageButton) getActivity().findViewById(R.id.btn_help);
        btn_help.setOnClickListener(new View.OnClickListener() {
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
                if(soundAlarm)
                {
                    ((MainActivity)getActivity()).sendMessage(DeeDeeProtocol.SOUND);
                }
                if (faceAlarm)
                {
                    ((MainActivity)getActivity()).sendMessage(DeeDeeProtocol.ALERT);
                }
            }
        });
    }
}
