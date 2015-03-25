package io.github.thealexhong.robotsecurity;

import android.app.Activity;
import android.os.Bundle;

import io.github.thealexhong.robotsecurity.ev3comm.EV3Connector;
import io.github.thealexhong.robotsecurity.fragment.ConnectFragment;


public class MainActivity extends Activity
{
    private EV3Connector ev3Connector;

    public void setEv3Connector(EV3Connector ev3Connector)
    {
        this.ev3Connector = ev3Connector;
    }

    public EV3Connector getEv3Connector()
    {
        return this.ev3Connector;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null)
        {
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new ConnectFragment()).commit();
        }
    }
}
