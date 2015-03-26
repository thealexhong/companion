package io.github.thealexhong.robotsecurity.wifidirect;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;

import io.github.thealexhong.robotsecurity.MainActivity;
import io.github.thealexhong.robotsecurity.R;
import io.github.thealexhong.robotsecurity.fragment.DeeDeeFragment;

public class ServerThread extends Thread
{
    private static final String TAG = "ServerThread";
    private MainActivity activity;
    public ServerThread(MainActivity activity)
    {
        this.activity = activity;
    }
    public void run()
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(MainActivity.port);
            while(true)
            {
                Socket socket = serverSocket.accept();
                InputStream inputStream = socket.getInputStream();
                char[] buf = new char[1024];
                Reader reader = new InputStreamReader(inputStream, "UTF-8");
                StringBuilder string = new StringBuilder();
                int len;
                while ((len = reader.read(buf)) != -1)
                {
                    string.append(buf, 0, len);
                }
                inputStream.close();
                String recv = string.toString();
                Log.d(TAG, "Received: " + recv);
                // Communication Protocol for WifiDirect ;)

                if (recv.equals(DeeDeeProtocol.CONNECT))
                {
                    activity.runOnUiThread(new Runnable()
                    {
                        public void run() {
                            DeeDeeFragment frag = (DeeDeeFragment) activity.getFragmentManager().findFragmentById(R.id.fragment_container);
                            frag.setFace(DeeDeeFragment.DeeDee.LIKE);
                        }
                    });
                }
                else if (recv.equals(DeeDeeProtocol.FWD))
                {
                    activity.runOnUiThread(new Runnable()
                    {
                        public void run() {
                            DeeDeeFragment frag = (DeeDeeFragment) activity.getFragmentManager().findFragmentById(R.id.fragment_container);
                            frag.setFace(DeeDeeFragment.DeeDee.HAPPY);
                        }
                    });
                }
                else if (recv.equals(DeeDeeProtocol.LEFT))
                {
                    activity.runOnUiThread(new Runnable()
                    {
                        public void run() {
                            DeeDeeFragment frag = (DeeDeeFragment) activity.getFragmentManager().findFragmentById(R.id.fragment_container);
                            frag.setFace(DeeDeeFragment.DeeDee.LEFT);
                        }
                    });
                }
                else if (recv.equals(DeeDeeProtocol.RIGHT))
                {
                    activity.runOnUiThread(new Runnable()
                    {
                        public void run() {
                            DeeDeeFragment frag = (DeeDeeFragment) activity.getFragmentManager().findFragmentById(R.id.fragment_container);
                            frag.setFace(DeeDeeFragment.DeeDee.RIGHT);
                        }
                    });
                }
                else if (recv.equals(DeeDeeProtocol.BWD))
                {
                    activity.runOnUiThread(new Runnable()
                    {
                        public void run() {
                            DeeDeeFragment frag = (DeeDeeFragment) activity.getFragmentManager().findFragmentById(R.id.fragment_container);
                            frag.setFace(DeeDeeFragment.DeeDee.SAD);
                        }
                    });
                }
                else if(recv.equals(DeeDeeProtocol.ATTACK))
                {
                    activity.runOnUiThread(new Runnable()
                    {
                        public void run() {
                            DeeDeeFragment frag = (DeeDeeFragment) activity.getFragmentManager().findFragmentById(R.id.fragment_container);
                            frag.setFace(DeeDeeFragment.DeeDee.ANGRY);
                        }
                    });
                }
                else if(recv.equals(DeeDeeProtocol.ALERT))
                {
                    activity.runOnUiThread(new Runnable()
                    {
                        public void run() {
                            DeeDeeFragment frag = (DeeDeeFragment) activity.getFragmentManager().findFragmentById(R.id.fragment_container);
                            frag.setFace(DeeDeeFragment.DeeDee.ALERT);
                        }
                    });
                }
                else if (recv.equals(DeeDeeProtocol.NEUTRAL))
                {
                    activity.runOnUiThread(new Runnable()
                    {
                        public void run() {
                            DeeDeeFragment frag = (DeeDeeFragment) activity.getFragmentManager().findFragmentById(R.id.fragment_container);
                            frag.setFace(DeeDeeFragment.DeeDee.NEUTRAL);
                        }
                    });
                }
                else if (recv.equals(DeeDeeProtocol.DISCONNECT))
                {
                    activity.runOnUiThread(new Runnable()
                    {
                        public void run() {
                            DeeDeeFragment frag = (DeeDeeFragment) activity.getFragmentManager().findFragmentById(R.id.fragment_container);
                            frag.setFace(DeeDeeFragment.DeeDee.SLEEPY);
                        }
                    });
                }
                else
                {
                    if (recv.equals(DeeDeeProtocol.SOUND))
                    {
                        activity.runOnUiThread(new Runnable()
                        {
                            public void run() {
                                DeeDeeFragment frag = (DeeDeeFragment) activity.getFragmentManager().findFragmentById(R.id.fragment_container);
                                frag.setFace(DeeDeeFragment.DeeDee.SURPRISE);
                            }
                        });
                    }
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
