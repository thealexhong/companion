package iocompanion.github.thealexhong.companion;

import android.app.Activity;
import android.os.Bundle;

import iocompanion.github.thealexhong.companion.fragment.ConnectFragment;


public class MainActivity extends Activity
{
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
