package ece1778.companion_ui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import org.apache.http.util.ByteArrayBuffer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.UUID;

public class EV3Connector implements EV3Protocol
{
    public static final String TAG = "EV3Connector";
    public static final String LEGO_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    public static final boolean BT_ON = true;
    public static final boolean BT_OFF = false;
    public static final byte A = ((byte) 0x01);
    public static final byte B = ((byte) 0x02);
    public static final byte C = ((byte) 0x04);
    public static final byte D = ((byte) 0x08);

    public BluetoothAdapter bluetoothAdapter;
    public BluetoothSocket bluetoothSocket;
    public String address;

    public EV3Connector(String address)
    {
        this.address = address;
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void setBluetooth(boolean state)
    {
        if(state)
        {
            if(!this.bluetoothAdapter.isEnabled())
            {
                this.bluetoothAdapter.enable();
                while(!this.bluetoothAdapter.isEnabled()) {}
                Log.d(TAG, "Bluetooth turned on");
            }
        }
        else
        {
            if(this.bluetoothAdapter.isEnabled())
            {
                this.bluetoothAdapter.disable();
                while(this.bluetoothAdapter.isEnabled()) {}
                Log.d(TAG, "Bluetooth turned off");
            }
        }
    }

    public boolean connect()
    {
        boolean connected = false;
        BluetoothDevice nxt = this.bluetoothAdapter.getRemoteDevice(this.address);
        try
        {
            this.bluetoothSocket = nxt.createRfcommSocketToServiceRecord(UUID.fromString(LEGO_UUID));
            this.bluetoothSocket.connect();
            connected = true;
        }
        catch(IOException e)
        {
            connected = false;
            e.printStackTrace();
        }
        return connected;
    }

    public Integer readMessage()
    {
        Integer message;
        if (this.bluetoothSocket != null)
        {
            try
            {
                InputStreamReader input = new InputStreamReader(this.bluetoothSocket.getInputStream());
                message = input.read();
                Log.d(TAG, "Successfully read message");
            }
            catch (IOException e)
            {
                message = null;
                Log.d(TAG, "Could not read message");
                e.printStackTrace();
            }
        }
        else
        {
            message = null;
            Log.d(TAG, "Could not read message");
        }
        return message;
    }

    public void write(byte[] buffer) {
        try {
            OutputStream out = bluetoothSocket.getOutputStream();
            out.write(buffer);
        } catch (IOException e) {
            Log.d(TAG, "Could not write message");
            e.printStackTrace();
        }
    }

    public boolean setOutputState(byte[] request) {
        ByteArrayBuffer buffer = new ByteArrayBuffer(request.length + 3);
        buffer.append(DIRECT_COMMAND_NOREPLY);
        byte[] replySize = {
                0x00, 0x00
        };
        buffer.append(replySize, 0, replySize.length);
        buffer.append(request, 0, request.length);
        return sendRequest(buffer.toByteArray());
    }

    public boolean forward(byte id, byte power) {
        byte[] request = {
                OUTPUT_POWER, LAYER_MASTER, id, (byte)(power & 0x3f),
                OUTPUT_START, LAYER_MASTER, id
        };
        return setOutputState(request);
    }

    public boolean backward(byte id, byte power) {
        byte[] request = {
                OUTPUT_POWER, LAYER_MASTER, id, negative(power),
                OUTPUT_START, LAYER_MASTER, id
        };
        return setOutputState(request);
    }

    private byte negative(byte power) {
        return (byte) (-power & 0x3f);
    }

    public boolean stop(byte id, byte mode) {
        byte[] request = {
                OUTPUT_STOP, LAYER_MASTER, id, mode
        };
        return setOutputState(request);
    }

    public enum MotorDir {
        Forward, Neutral, Backward
    };

    public enum MotorKind {
        LeftMotor, RightMotor
    };

    public void shoot() {

    }

    public void moveForward() {
        move(MotorDir.Forward, MotorDir.Forward);
    }

    public void moveBackward() {
        move(MotorDir.Backward, MotorDir.Backward);
    }

    public void turnLeft() {
        move(MotorDir.Backward, MotorDir.Forward);
    }

    public void turnRight() {
        move(MotorDir.Forward, MotorDir.Backward);
    }

    public void halt() {
        move(MotorDir.Neutral, MotorDir.Neutral);
    }

    public byte setSpeed(int speed) {
        speed = (int) (speed / 100.0 * 31);
        return (byte) speed;
    }

    public void move(MotorDir leftMotorDir, MotorDir rightMotorDir) {
        switch (leftMotorDir) {
            case Forward:
                forward(B, setSpeed(100));
                break;
            case Neutral:
                stop(B, BRAKE);
                break;
            case Backward:
                backward(B, setSpeed(100));
                break;
        }

        switch (rightMotorDir) {
            case Forward:
                forward(C, setSpeed(100));
                break;
            case Neutral:
                stop(C, BRAKE);
                break;
            case Backward:
                backward(C, setSpeed(100));
                break;
        }
    }

    private boolean sendRequest(byte[] request) throws RuntimeException {
        boolean verify = true;
        try {
            sendData(request);
        }
        catch (RuntimeException e) {
            verify = false;
        }

        return verify;
    }

    public void sendData(byte[] request) throws RuntimeException {
        int bodyLength = request.length + 2;
        byte[] header = {
                (byte) (bodyLength & 0xff), (byte) ((bodyLength >>> 8) & 0xff),
                0x00, 0x00
        };
        try {
            OutputStream mOutputStream = bluetoothSocket.getOutputStream();
            mOutputStream.write(header);
            mOutputStream.write(request);
        }
        catch (IOException e) {
            Log.e(TAG, "Send failed.", e);
            throw new RuntimeException(e);
        }
        Log.v(TAG, "Sent: " + request);
    }

    public void nxtmotors(byte l, byte r, boolean speedReg, boolean motorSync)
    {
        byte[] data = {0x0c, 0x00, (byte) 0x80, 0x04, 0x02, 0x32, 0x07, 0x00, 0x00, 0x20, 0x00, 0x00, 0x00, 0x00,
                0x0c, 0x00, (byte) 0x80, 0x04, 0x01, 0x32, 0x07, 0x00, 0x00, 0x20, 0x00, 0x00, 0x00, 0x00 };
        data[5] = l;
        data[19] = r;
        if (speedReg) {
            data[7] |= 0x01;
            data[21] |= 0x01;
        }
        if (motorSync) {
            data[7] |= 0x02;
            data[21] |= 0x02;
        }
        write(data);
    }
}
