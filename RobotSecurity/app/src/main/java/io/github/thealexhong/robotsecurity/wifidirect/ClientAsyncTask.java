package io.github.thealexhong.robotsecurity.wifidirect;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import io.github.thealexhong.robotsecurity.MainActivity;

public class ClientAsyncTask extends AsyncTask<Void, Void, Void>
{
    private String host;
    private String message;

    public ClientAsyncTask(String host, String message)
    {
        this.host = host;
        this.message = message;
    }

    @Override
    protected Void doInBackground(Void... params)
    {
        Socket socket = new Socket();
        try
        {
            socket.bind(null);
            socket.connect((new InetSocketAddress(this.host, MainActivity.port)), 0);
            OutputStream stream = socket.getOutputStream();
            byte[] byteArray = this.message.getBytes("UTF-8");
            stream.write(byteArray, 0, byteArray.length);
            stream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(socket.isConnected())
            {
               try
               {
                   socket.close();
               }
               catch(IOException e)
               {
                   e.printStackTrace();
               }
            }
        }
        return null;
    }
}
