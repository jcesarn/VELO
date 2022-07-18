package iftm.pdm.velo.model;

import android.location.Location;

public class CustomLocation extends Location {

    private boolean useMetric;

    public CustomLocation(Location location, boolean useMetric) {
        super(location);
        this.useMetric = useMetric;
    }

    public boolean getUseMetric() {
        return useMetric;
    }

    public void setUseMetric(boolean useMetric) {
        this.useMetric = useMetric;
    }

    @Override
    public float getSpeed() {
        float speed = super.getSpeed() * 3.6f;
        if(!this.getUseMetric()) {
            speed = super.getSpeed() * 2.23693629f;
        }
        return speed;
    }

}
