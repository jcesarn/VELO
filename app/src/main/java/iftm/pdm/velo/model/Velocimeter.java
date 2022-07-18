package iftm.pdm.velo.model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;

public class Velocimeter implements Parcelable {

    private static final int limit = 100;
    private float currentSpeed, avgSpeed, maxSpeed;
    private double distance;
    private boolean useMetrics;
    private ArrayList<Float> arrayVel;
    private Location startLocation;

    public Velocimeter(boolean useMetrics) {
        this.useMetrics = useMetrics;
        this.currentSpeed = 0;
        this.avgSpeed = 0;
        this.maxSpeed = 0;
        this.distance = 0;
        this.arrayVel = new ArrayList<>();
    }

    public void updateSpeeds() {
        if(this.arrayVel.size() < limit)
            this.arrayVel.add(currentSpeed);
        else {
            this.arrayVel.clear();
            this.arrayVel.add(avgSpeed);
        }
        float count = 0, value = 0;
        for(float i : arrayVel) {
            if (i > this.maxSpeed)
                this.maxSpeed = i;
            value += i;
            count ++;
        }
        this.avgSpeed = value / count;
    }

    public void resetVelocimeter() {
        this.avgSpeed = 0;
        this.maxSpeed = 0;
        this.distance = 0;
        this.startLocation = null;
        this.arrayVel.clear();
    }

    public String getFormatedCurrentSpeedToSpeedometer() {
        Formatter fmt = new Formatter();
        fmt.format(Locale.US, "%3.0f", this.currentSpeed);
        String strCurrentSpeed = fmt.toString();
        strCurrentSpeed = strCurrentSpeed.replace(" ", "0");
        return strCurrentSpeed;
    }

    public String getFormatedCurrentSpeed() {
        Formatter fmt = new Formatter();
        fmt.format(Locale.US, "%.0f", this.currentSpeed);
        return fmt.toString();
    }

    public String getFormatedAvgSpeed() {
        Formatter fmt = new Formatter(new StringBuilder());
        fmt.format(Locale.US, "%.1f", this.avgSpeed);
        return fmt.toString();
    }

    public String getFormatedMaxSpeed() {
        Formatter fmt = new Formatter(new StringBuilder());
        fmt.format(Locale.US, "%.1f", this.maxSpeed);
        return fmt.toString();
    }

    public String getFormatedDistance() {
        Formatter fmt = new Formatter(new StringBuilder());
        fmt.format(Locale.US, "%.1f", this.distance);
        return fmt.toString();
    }

    public float getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(float currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public float getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(float avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public boolean isUseMetrics() {
        return useMetrics;
    }

    public void setUseMetrics(boolean useMetrics) {
        this.useMetrics = useMetrics;
    }

    public ArrayList<Float> getArrayVel() {
        return arrayVel;
    }

    public void setArrayVel(ArrayList<Float> arrayVel) {
        this.arrayVel = arrayVel;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    @NonNull
    @Override
    public String toString() {
        return "Velocimeter{" +
                "currentSpeed=" + currentSpeed +
                ", avgSpeed=" + avgSpeed +
                ", maxSpeed=" + maxSpeed +
                ", distance=" + distance +
                ", useMetrics=" + useMetrics +
                ", arrayVel=" + arrayVel +
                ", startLocation=" + startLocation +
                '}';
    }

    protected Velocimeter(Parcel in) {
        currentSpeed = in.readFloat();
        avgSpeed = in.readFloat();
        maxSpeed = in.readFloat();
        distance = in.readDouble();
        useMetrics = in.readByte() != 0;
        startLocation = in.readParcelable(Location.class.getClassLoader());
    }

    public static final Creator<Velocimeter> CREATOR = new Creator<Velocimeter>() {
        @Override
        public Velocimeter createFromParcel(Parcel in) {
            return new Velocimeter(in);
        }

        @Override
        public Velocimeter[] newArray(int size) {
            return new Velocimeter[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(currentSpeed);
        parcel.writeFloat(avgSpeed);
        parcel.writeFloat(maxSpeed);
        parcel.writeDouble(distance);
        parcel.writeByte((byte) (useMetrics ? 1 : 0));
        parcel.writeParcelable(startLocation, i);
    }

}
