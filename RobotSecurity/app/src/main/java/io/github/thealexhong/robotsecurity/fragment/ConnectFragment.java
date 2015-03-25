package io.github.thealexhong.robotsecurity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import io.github.thealexhong.robotsecurity.R;

public class ConnectFragment extends BaseFragment
{
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_connect, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        setConnectBtn();
        setLaunchCompanionBtn();
    }

    private void setConnectBtn()
    {
        Button btn = (Button) getActivity().findViewById(R.id.btn_connect);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                swapFragment(new ControlFragment());
            }
        });
    }

    private void setLaunchCompanionBtn()
    {
        Button btn = (Button) getActivity().findViewById(R.id.btn_security);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                swapFragment(new DeeDeeFragment());
            }
        });
    }
}
