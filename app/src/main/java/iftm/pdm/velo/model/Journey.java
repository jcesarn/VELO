package iftm.pdm.velo.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class Journey implements Comparable<Journey>, Parcelable {

    private long id, time;
    private String name, distance, avgSpeed, maxSpeed, date, unit;

    @SuppressLint("SimpleDateFormat")
    public Journey(long id, String name, Long time, String date, String distance, String avgSpeed, String maxSpeed, String unit) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.distance = distance;
        this.avgSpeed = avgSpeed;
        this.maxSpeed = maxSpeed;
        this.date = date;
        this.unit = unit;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(String avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public String getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(String maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @SuppressLint("SimpleDateFormat")
    public String getDesc(String[] dsc) {
        String desc;
        desc = "\n" + dsc[0] + ": " + new SimpleDateFormat("mm:ss").format(this.time) + "\n"
                + dsc[1] + ": " + this.distance + " " + this.unit + "\n"
                + dsc[2] + ": " + this.avgSpeed + " " + this.unit + "/h\n"
                + dsc[3] + ": " + this.maxSpeed + " " + this.unit + "/h\n";
        return (desc);
    }

    @NonNull
    @Override
    public String toString() {
        return "Journey{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", distance='" + distance + '\'' +
                ", avgSpeed='" + avgSpeed + '\'' +
                ", maxSpeed='" + maxSpeed + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", unit='" + unit + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Journey journey = (Journey) o;
        return id == journey.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Journey journey) {
        String compare = (this.getDate() + this.getName()).toUpperCase();
        return compare.compareTo((journey.getDate() + journey.getName()).toUpperCase());
    }

    protected Journey(Parcel in) {
        id = in.readLong();
        name = in.readString();
        time = in.readLong();
        distance = in.readString();
        avgSpeed = in.readString();
        maxSpeed = in.readString();
        date = in.readString();
        unit = in.readString();
    }

    public static final Creator<Journey> CREATOR = new Creator<Journey>() {
        @Override
        public Journey createFromParcel(Parcel in) {
            return new Journey(in);
        }

        @Override
        public Journey[] newArray(int size) {
            return new Journey[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.id);
        parcel.writeString(this.name);
        parcel.writeLong(this.time);
        parcel.writeString(this.distance);
        parcel.writeString(this.avgSpeed);
        parcel.writeString(this.maxSpeed);
        parcel.writeString(this.date);
        parcel.writeString(this.unit);
    }

}
