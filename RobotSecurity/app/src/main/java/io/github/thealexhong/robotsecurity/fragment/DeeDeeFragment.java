package io.github.thealexhong.robotsecurity.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import io.github.thealexhong.robotsecurity.R;

/**
 * Companion screen; FUN FACT: Companion's name is DeeDee
 */
public class DeeDeeFragment extends BaseFragment
{
    private ImageView img;
    private MediaPlayer warningSound;
    private MediaPlayer sirenSound;

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
    }

    private void setView()
    {
        ImageButton btn_back = (ImageButton) getActivity().findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                getPrevFragment();
            }
        });
        img = (ImageView) getActivity().findViewById(R.id.deedee);
        warningSound = MediaPlayer.create(getActivity(), R.raw.warning);
        sirenSound = MediaPlayer.create(getActivity(), R.raw.siren);
        warningSound.setLooping(true);
        sirenSound.setLooping(true);
    }

    public void playSound()
    {
        warningSound.start();
        sirenSound.start();
    }

    public void stopSound()
    {
        if(warningSound.isPlaying()) {
            warningSound.pause();
        }
        if(sirenSound.isPlaying()) {
            sirenSound.pause();
        }

    }


    public void setFace(DeeDee face)
    {
        switch(face)
        {
            case LIKE:
                img.setImageResource(R.drawable.deedee_like);
                break;
            case ANGRY:
                img.setImageResource(R.drawable.deedee_angry);
                break;
            case LEFT:
                img.setImageResource(R.drawable.deedee_left);
                break;
            case RIGHT:
                img.setImageResource(R.drawable.deedee_right);
                break;
            case NEUTRAL:
                img.setImageResource(R.drawable.deedee_neutral);
                break;
            case SAD:
                img.setImageResource(R.drawable.deedee_sad);
                break;
            case SLEEPY:
                img.setImageResource(R.drawable.deedee_sleepy);
                break;
            case SURPRISE:
                img.setImageResource(R.drawable.deedee_surprised);
                break;
            case ALERT:
                img.setImageResource(R.drawable.deedeeangryalert);
                break;
            default:
                img.setImageResource(R.drawable.deedee_happy);
                break;
        }
    }

    public enum DeeDee
    {
        LIKE,
        HAPPY,
        SAD,
        ANGRY,
        LEFT,
        RIGHT,
        NEUTRAL,
        SLEEPY,
        SURPRISE,
        ALERT
    }
}
