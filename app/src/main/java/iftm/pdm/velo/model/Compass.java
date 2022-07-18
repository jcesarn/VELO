package iftm.pdm.velo.model;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Compass implements SensorEventListener {

    private CompassListener listener;
    private SensorManager sensorManager;
    private Sensor gsensor, msensor;
    private float[] mGravity = new float[3], mGeomagnetic = new float[3], R = new float[9], I = new float[9];
    private int display;

    public interface CompassListener {
        void onNewAzimuth(float azimuth);
    }

    public Compass(Context context) {
        this.display = context.getResources().getConfiguration().orientation;
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.gsensor = this.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.msensor = this.sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void start() {
        this.sensorManager.registerListener(this, this.gsensor, SensorManager.SENSOR_DELAY_GAME);
        this.sensorManager.registerListener(this, this.msensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void stop() {
        this.sensorManager.unregisterListener(this);
    }

    public void setListener(CompassListener l) {
        this.listener = l;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float alpha = 0.97f;

        synchronized (this) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                this.mGravity[0] = alpha * this.mGravity[0] + (1 - alpha) * event.values[0];
                this.mGravity[1] = alpha * this.mGravity[1] + (1 - alpha) * event.values[1];
                this.mGravity[2] = alpha * this.mGravity[2] + (1 - alpha) * event.values[2];
            }

            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                this.mGeomagnetic[0] = alpha * this.mGeomagnetic[0] + (1 - alpha) * event.values[0];
                this.mGeomagnetic[1] = alpha * this.mGeomagnetic[1] + (1 - alpha) * event.values[1];
                this.mGeomagnetic[2] = alpha * this.mGeomagnetic[2] + (1 - alpha) * event.values[2];
            }

            boolean success = SensorManager.getRotationMatrix(this.R, this.I, this.mGravity, this.mGeomagnetic);
            if (success) {
                float[] orientation = new float[3];
                SensorManager.getOrientation(R, orientation);
                float azimuth = (float) Math.toDegrees(orientation[0]);
                if(this.display == Configuration.ORIENTATION_LANDSCAPE) {
                    azimuth = (azimuth + 450) % 360;
                }
                else {
                    azimuth = (azimuth + 360) % 360;
                }
                //this.azimuth = (this.azimuth + 360) % 360;
                if (this.listener != null) {
                    this.listener.onNewAzimuth(azimuth);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}
