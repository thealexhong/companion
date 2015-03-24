package ece1778.companion_ui;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.FloatMath;

/**
 * Shake listener
 */
public class ShakeEventListener implements SensorEventListener
{
    private static final float SHAKE_THRESHOLD_GRAVITY = 1.3F;
    private static final int SHAKE_SLOP_TIME_MS = 500;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;

    private OnShakeListener mListener;
    private long mShakeTimestamp;
    private int mShakeCount;

    public interface OnShakeListener
    {
        public void onShake(int count);
    }

    public void setOnShakeListener(OnShakeListener listener)
    {
        this.mListener = listener;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if (mListener != null) {
            float x = event.values[0],
                    y = event.values[1],
                    z = event.values[2];
            float gx = x / SensorManager.GRAVITY_EARTH,
                    gy = y / SensorManager.GRAVITY_EARTH,
                    gz = z / SensorManager.GRAVITY_EARTH;

            float gforce = FloatMath.sqrt(gx * gx + gy * gy + gz * gz);
            if (gforce > SHAKE_THRESHOLD_GRAVITY)
            {
                final long now = System.currentTimeMillis();
                /* ignore shake events that are too close */
                if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) { return; }
                /* reset shake count after 3 seconds of no shake */
                if (mShakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) { mShakeCount = 0; }
                mShakeTimestamp = now;
                mShakeCount++;
                mListener.onShake(mShakeCount);
            }
        }
    }
}
