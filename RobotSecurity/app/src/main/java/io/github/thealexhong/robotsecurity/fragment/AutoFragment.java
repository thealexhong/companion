package io.github.thealexhong.robotsecurity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import io.github.thealexhong.robotsecurity.R;

public class AutoFragment extends BaseFragment
{
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_autonomous, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setView();

        // TODO: Listen for voice
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
            public void onClick(View view) { getPrevFragment(); } // TODO: Stop motors, shut face, sound alerts off
        });

        ImageButton btn_help = (ImageButton) getActivity().findViewById(R.id.btn_help);
        btn_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            // TODO: Trigger alarms based on settings
            }
        });

    }
}
