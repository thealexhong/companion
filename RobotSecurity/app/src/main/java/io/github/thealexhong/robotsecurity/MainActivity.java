package io.github.thealexhong.robotsecurity;

import android.app.Activity;
import android.os.Bundle;

import io.github.thealexhong.robotsecurity.fragment.ConnectFragment;


public class MainActivity extends Activity {

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
