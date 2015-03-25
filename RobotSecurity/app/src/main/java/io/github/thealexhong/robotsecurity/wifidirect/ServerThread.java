package io.github.thealexhong.robotsecurity.wifidirect;

import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;

import io.github.thealexhong.robotsecurity.MainActivity;

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
                /**
                if (recv.equals("forward"))
                {

                }
                else if (recv.equals("turnleft"))
                {

                }
                else if (recv.equals("turnright"))
                {

                }
                else if (recv.equals("backward"))
                {

                }
                else if(recv.equals("attack"))
                {

                }
                else if (recv.equals("deedee"))
                {

                }
                else
                {
                    if (recv.equals("sound")) {
                    }
                }*/
                activity.runOnUiThread(new Runnable()
                {
                    public void run() {
                        Toast.makeText(activity, "Hello", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
