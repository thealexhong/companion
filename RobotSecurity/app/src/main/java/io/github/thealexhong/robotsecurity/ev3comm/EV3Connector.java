package io.github.thealexhong.robotsecurity.ev3comm;

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

    public static final boolean BT_ON = true;
    public static final boolean BT_OFF = false;

    // BT variables
    public BluetoothAdapter bluetoothAdapter;
    public BluetoothSocket bluetoothSocket;
    public String address;

    // Motor ports for the EV3 brick
    public static final String LEGO_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    public static final byte A = ((byte) 0x01);
    public static final byte B = ((byte) 0x02);
    public static final byte C = ((byte) 0x04);
    public static final byte D = ((byte) 0x08);

    public enum MotorDir { Forward, Neutral, Backward }
    public enum MotorKind { LeftMotor, RightMotor }

    /**
     * Constructor for connecting to EV3 brick
     * @param address
     */
    public EV3Connector(String address)
    {
        this.address = address;
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * Enabling bluetooth
     * @param state BT_ON or BT_OFF
     */
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

    /**
     * Connect to BT device
     * @return TRUE if connected, false otherwise
     */
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

    /**
     * Read from BT socket
     * @return message from BT device
     */
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

    /**
     * High level motor command to EV3 : FORWARD
     * @param id Port ID
     * @param speed speed output
     * @return send status
     */
    public boolean forward(byte id, byte speed) {
        byte[] request =
                {
                    OUTPUT_SPEED, LAYER_MASTER, id, speed,
                    OUTPUT_START, LAYER_MASTER, id
                };
        return setOutputState(request);
    }

    /**
     * High level motor command to EV3 : BACKWARD
     * @param id Port ID
     * @param speed speed output
     * @return send status
     */
    public boolean backward(byte id, byte speed) {
        byte[] request =
                {
                    OUTPUT_SPEED, LAYER_MASTER, id, negative(speed),
                    OUTPUT_START, LAYER_MASTER, id
                };
        return setOutputState(request);
    }

    /**
     * Negative bytes
     * @param power power/speed output
     * @return negative power
     */
    private byte negative(byte power) { return (byte) (-power & 0x3f); }

    /**
     * High level motor command to EV3 : BRAKE
     * @param id Port ID
     * @param mode mode of motor
     * @return send status
     */
    public boolean stop(byte id, byte mode) {
        byte[] request =
                {
                    OUTPUT_STOP, LAYER_MASTER, id, mode
                };
        return setOutputState(request);
    }

    /**
     * Sets speed of motor
     * @param speed speed of motor
     * @return speed of motor
     */
    public byte setSpeed(int speed)
    {
        speed = (int) (speed / 100.0 * 31); // 0 ~ 31
        return (byte) speed;
    }

    public void move(MotorDir leftMotorDir, MotorDir rightMotorDir)
    {
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

    /**
     * Auxiliary Ports (ports not used by wheels)
     */
    public void fwdA() { forward(A, setSpeed(100)); }
    public void bwdA() { backward(A, setSpeed(100)); }
    public void fwdD() { forward(D, setSpeed(100)); }
    public void bwdD() { backward(D, setSpeed(100)); }

    /**
     * High-level robot commands
     */
    public void moveForward() { move(MotorDir.Forward, MotorDir.Forward); }
    public void moveBackward() { move(MotorDir.Backward, MotorDir.Backward); }
    public void turnLeft() { move(MotorDir.Backward, MotorDir.Forward); }
    public void turnRight() { move(MotorDir.Forward, MotorDir.Backward); }
    public void halt()
    {
        move(MotorDir.Neutral, MotorDir.Neutral);
        stop(A, BRAKE); stop(D, BRAKE);
    }

    /**
     * Prepares bytes to be sent to EV3
     * @param request EV3 commands
     * @return preprocessed bytes
     */
    public boolean setOutputState(byte[] request)
    {
        ByteArrayBuffer buffer = new ByteArrayBuffer(request.length + 3);
        buffer.append(DIRECT_COMMAND_NOREPLY); // Command Types
        byte[] replySize =
                {
                        0x00, 0x00
                };
        buffer.append(replySize, 0, replySize.length);
        buffer.append(request, 0, request.length);
        return sendRequest(buffer.toByteArray());
    }

    /**
     * Sends command to EV3
     * @param request bytes to EV3 brick
     * @return send success status
     * @throws RuntimeException
     */
    private boolean sendRequest(byte[] request) throws RuntimeException {
        boolean verify = true; // default of 0 means success
        try
        {
            sendData(request);
        }
        catch (RuntimeException e)
        {
            verify = false;
        }

        return verify;
    }

    /**
     * Sends bytes to EV3
     * @param request bytes to the EV3 brick
     * @throws RuntimeException
     */
    public void sendData(byte[] request) throws RuntimeException
    {
        /* Calculate the size of request and set them in little-endian order */
        int bodyLength = request.length + 2; // add 2 for identification codes.
        byte[] header =
                {
                    (byte) (bodyLength & 0xff), (byte) ((bodyLength >>> 8) & 0xff),
                    0x00, 0x00
                };
        try
        {
            OutputStream mOutputStream = bluetoothSocket.getOutputStream();
            mOutputStream.write(header);
            mOutputStream.write(request);
        }
        catch (IOException e)
        {
            Log.e(TAG, "Send failed.", e);
            throw new RuntimeException(e);
        }
        Log.v(TAG, "Sent: " + request);
    }
}
