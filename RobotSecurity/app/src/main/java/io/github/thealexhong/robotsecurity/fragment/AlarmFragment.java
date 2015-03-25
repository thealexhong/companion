package io.github.thealexhong.robotsecurity.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import io.github.thealexhong.robotsecurity.R;

/**
 * Alarm Settings
 */
public class AlarmFragment extends BaseFragment
{
    // Save alarm settings
    private boolean faceAlarm;
    private boolean soundAlarm;
    private boolean swordAlarm;

    // Switches
    private Switch faceSwitch;
    private Switch soundSwitch;
    private Switch swordSwitch;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alarm, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setView();
        loadSettings();
    }

    /**
     * Set up switches
     */
    private void setView()
    {
        ImageButton btn_back = (ImageButton) getActivity().findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { saveSettings(); getPrevFragment(); }
        });

        faceSwitch = (Switch) getActivity().findViewById(R.id.faceswitch);
        soundSwitch = (Switch) getActivity().findViewById(R.id.soundswitch);
        swordSwitch = (Switch) getActivity().findViewById(R.id.attackswitch);

        faceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                faceAlarm = isChecked;
            }
        });
        soundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                soundAlarm = isChecked;
            }
        });
        swordSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                swordAlarm = isChecked;
            }
        });
    }

    private void saveSettings()
    {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("io.github.thealexhong.robotsecurity.fragment", Context.MODE_PRIVATE).edit();
        editor.putBoolean("FaceSwitch", faceAlarm);
        editor.putBoolean("SoundSwitch", soundAlarm);
        editor.putBoolean("SwordSwitch", swordAlarm);
        editor.apply();
    }

    private void loadSettings()
    {
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("io.github.thealexhong.robotsecurity.fragment", Context.MODE_PRIVATE);
        faceAlarm = sharedPrefs.getBoolean("FaceSwitch", true);
        soundAlarm = sharedPrefs.getBoolean("SoundSwitch", true);
        swordAlarm = sharedPrefs.getBoolean("SwordSwitch", false);
        faceSwitch.setChecked(faceAlarm);
        soundSwitch.setChecked(soundAlarm);
        swordSwitch.setChecked(swordAlarm);
    }
}
