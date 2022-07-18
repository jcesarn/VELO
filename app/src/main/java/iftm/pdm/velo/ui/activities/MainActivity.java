package iftm.pdm.velo.ui.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import iftm.pdm.velo.R;
import iftm.pdm.velo.data.JourneyDAOSingleton;
import iftm.pdm.velo.model.CustomLocation;
import iftm.pdm.velo.model.Compass;
import iftm.pdm.velo.model.Journey;
import iftm.pdm.velo.model.SOTWFormatter;
import iftm.pdm.velo.model.Velocimeter;
import iftm.pdm.velo.ui.dialogs.DialogEnableGPS;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private LinearLayout llvAccelDecel, llhButtons;
    private TextView txtVelocity, txtUnit, sotwLabel, txtDistance, txtAvgSpeed, txtMaxSpeed, txtStartSpeed, txtB;
    private EditText etxSpeed;
    private Button bttAccelDecel, bttStart, bttJourneys, bttGo, bttBack;
    private LinearLayout llvCount, llvAccelDecelText, llvAccelDecelData;
    private boolean isStarted, acceldecel, acceldecelStarted;
    private static final String mh = "mi/h", kmh = "km/h";
    public static final String VELOCIMETER_KEY = "MainActivity.VELOCIMETER", TIME_KEY = "MainActivity.VELOCIMETER.Time", DISTANCE_KEY = "MainActivity.VELOCIMETER.Distance", AVGSPEED_KEY = "MainActivity.VELOCIMETER.AvgSpeed", MAXSPEED_KEY = "MainActivity.VELOCIMETER.MaxSPeed", USEMETRICS_KEY = "MainActivity.VELOCIMETER.UseMetrics", ACCELDECEL_KEY = "MainActivity.AccelDecel";
    private static final int REQ_CODE = 101;
    private SOTWFormatter sotwFormatter;
    private Chronometer chronoTime, chronometer;
    private Velocimeter velocimeter;
    private Compass compass;
    private Journey journey;
    private CustomLocation location;
    private long startTime = 0, endTime = 0;

    @SuppressLint("StaticFieldLeak")
    private static MainActivity INSTANCE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        INSTANCE = this;
        this.setupVariables();
        this.setupCompass();
        this.checkPermissions();
        this.updateSpeed(null);
        if(savedInstanceState != null)
            this.velocimeter = savedInstanceState.getParcelable(VELOCIMETER_KEY);
    }

    public static Context getMainContext() {
        return INSTANCE;
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.compass.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.compass.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.compass.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.compass.stop();
    }

    private void setupVariables() {
        this.llvAccelDecel = findViewById(R.id.llvAccelDecel);
        this.llvAccelDecelText = findViewById(R.id.llvAccelDecelText);
        this.llvAccelDecelData = findViewById(R.id.llvAccelDecelData);
        this.llhButtons = findViewById(R.id.llhButtons);
        this.txtVelocity = findViewById(R.id.txtVelocity);
        this.txtUnit = findViewById(R.id.txtUnit);
        this.txtUnit.setText(kmh);
        this.txtStartSpeed = findViewById(R.id.txtStartSpeed);
        this.txtStartSpeed.setText("0.0");
        this.etxSpeed = findViewById(R.id.etxSpeed);
        this.sotwFormatter = new SOTWFormatter(this);
        this.sotwLabel = findViewById(R.id.sotw_label);
        this.txtB = findViewById(R.id.txtB);
        this.bttAccelDecel = findViewById(R.id.bttAccelDecel);
        this.bttStart = findViewById(R.id.bttStart);
        this.bttJourneys = findViewById(R.id.bttJourneys);
        this.bttGo = findViewById(R.id.bttGo);
        this.bttBack = findViewById(R.id.bttBack);
        this.llvCount = findViewById(R.id.llvCount);
        this.chronoTime = findViewById(R.id.chronoTime);
        this.chronometer = findViewById(R.id.chronometer);
        this.txtDistance = findViewById(R.id.txtDistance);
        this.txtDistance.setText("0.0");
        this.txtAvgSpeed = findViewById(R.id.txtAvgSpeed);
        this.txtAvgSpeed.setText("0.0");
        this.txtMaxSpeed = findViewById(R.id.txtMaxSpeed);
        this.txtMaxSpeed.setText("0.0");
        this.isStarted = false;
        this.acceldecel = false;
        acceldecelStarted = false;
        this.velocimeter = new Velocimeter(true);
        this.journey = null;
    }

    private void setupCompass() {
        this.compass = new Compass(this);
        Compass.CompassListener cl = getCompassListener();
        this.compass.setListener(cl);
    }

    private void adjustSotwLabel(float azimuth) {
        this.sotwLabel.setText(sotwFormatter.format(azimuth));
    }

    private Compass.CompassListener getCompassListener() {
        return new Compass.CompassListener() {
            @Override
            public void onNewAzimuth(final float azimuth) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adjustSotwLabel(azimuth);
                    }
                });
            }
        };
    }

    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_CODE);
        else {
            doStuff();
        }
    }

    @SuppressLint("MissingPermission")
    private void doStuff() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if(locationManager != null)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    private void updateSpeed(CustomLocation location) {
        this.location = location;
        if(this.location != null) {
            this.location.setUseMetric(this.velocimeter.isUseMetrics());
            if(this.velocimeter.isUseMetrics()) {
                this.txtUnit.setText(kmh);
                this.velocimeter.setUseMetrics(true);
            }
            else {
                this.txtUnit.setText(mh);
                this.velocimeter.setUseMetrics(false);
            }
            this.velocimeter.setCurrentSpeed(this.location.getSpeed());
        } else {
            this.velocimeter.setCurrentSpeed(0);
        }
        this.txtVelocity.setText(this.velocimeter.getFormatedCurrentSpeedToSpeedometer());
        if(this.isStarted) {
            this.updateStartDistance();
            this.updateStartSpeeds();
        }
    }

    public void onClickStart(View view) {
        if(this.location != null) {
            if (this.llvCount.getVisibility() == View.VISIBLE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                this.bttAccelDecel.setVisibility(View.VISIBLE);
                this.bttJourneys.setVisibility(View.VISIBLE);
                this.bttStart.setBackground(getDrawable(R.drawable.button_drawable));
                this.onClickStop();
                this.resetStart();
            } else {
                this.resetStart();
                this.bttAccelDecel.setVisibility(View.GONE);
                this.bttJourneys.setVisibility(View.GONE);
                this.bttStart.setBackground(getDrawable(R.drawable.button_rounded_drawable));
                this.llvCount.setVisibility(View.VISIBLE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
                this.bttStart.setText(R.string.stop);
                this.isStarted = true;
                this.chronoTime.setBase(SystemClock.elapsedRealtime());
                this.chronoTime.start();
            }
        } else
            Toast.makeText(this, R.string.wait_gps, Toast.LENGTH_SHORT).show();
    }

    public void onClickStop() {
        Intent sendIntent = new Intent(this, NewJourneyActivity.class);
        sendIntent.putExtra(TIME_KEY, SystemClock.elapsedRealtime() - this.chronoTime.getBase());
        sendIntent.putExtra(DISTANCE_KEY, this.velocimeter.getFormatedDistance());
        sendIntent.putExtra(AVGSPEED_KEY, this.velocimeter.getFormatedAvgSpeed());
        sendIntent.putExtra(MAXSPEED_KEY, this.velocimeter.getFormatedMaxSpeed());
        sendIntent.putExtra(USEMETRICS_KEY, this.velocimeter.isUseMetrics());
        startActivityForResult(sendIntent, REQ_CODE);
        this.velocimeter.resetVelocimeter();
    }

    public void onClickReset(View view) {
        this.velocimeter.resetVelocimeter();
        this.updateStartDistance();
        this.updateStartSpeeds();
        this.chronoTime.setBase(SystemClock.elapsedRealtime());
        this.chronoTime.start();
    }

    public void onClickJourneys(View view) {
        if(isStarted) {
            this.resetStart();
        }
        if(!JourneyDAOSingleton.getINSTANCE().isEmpty()) {
            Intent sendIntent = new Intent(this, JourneysActivity.class);
            startActivity(sendIntent);
        } else
            Toast.makeText(this, R.string.journeys_empty, Toast.LENGTH_SHORT).show();
    }

    public void onClickAccelDecel(View view) {
        if (this.location != null) {
            this.acceldecel = true;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
            this.llhButtons.setVisibility(View.GONE);
            this.llvAccelDecel.setVisibility(View.VISIBLE);
            this.txtStartSpeed.setText(this.velocimeter.getFormatedCurrentSpeed());
            this.bttBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(llvAccelDecelText.getVisibility() == View.GONE)
                        resetAccelDecel();
                    else {
                        resetAccelDecel();
                        resetMain();
                    }
                }
            });
            this.bttGo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!etxSpeed.getText().toString().isEmpty()) {
                        if(!etxSpeed.getText().toString().equals(txtStartSpeed.getText().toString())) {
                            acceldecelStarted = true;
                            etxSpeed.setEnabled(false);
                            bttBack.setText(R.string.stop);
                            llvAccelDecelText.setVisibility(View.GONE);
                            llvAccelDecelData.setVisibility(View.GONE);
                            startTime = System.currentTimeMillis();
                            chronometer.setVisibility(View.VISIBLE);
                            chronometer.setBase(SystemClock.elapsedRealtime());
                            chronometer.start();
                        } else
                            Toast.makeText(view.getContext(), R.string.startsp_eq_targetsp, Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(view.getContext(), R.string.no_target_speed, Toast.LENGTH_LONG).show();
                }
            });
        } else
            Toast.makeText(this, R.string.wait_gps, Toast.LENGTH_SHORT).show();
    }

    public void resetStart() {
        this.velocimeter.resetVelocimeter();
        this.llvCount.setVisibility(View.GONE);
        this.bttStart.setText(R.string.start_journey);
        this.isStarted = false;
        this.chronoTime.stop();
    }

    public void resetAccelDecel() {
        this.acceldecelStarted = false;
        this.etxSpeed.setEnabled(true);
        this.txtStartSpeed.setText(this.velocimeter.getFormatedCurrentSpeed());
        this.etxSpeed.setText("");
        this.bttBack.setText(R.string.back);
        this.llvAccelDecelText.setVisibility(View.VISIBLE);
        this.llvAccelDecelData.setVisibility(View.VISIBLE);
        this.chronometer.setVisibility(View.GONE);
    }

    public void resetMain() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        this.llhButtons.setVisibility(View.VISIBLE);
        this.llvAccelDecel.setVisibility(View.GONE);
    }

    public void updateStartDistance() {
        this.txtDistance.setText(this.velocimeter.getFormatedDistance());
    }

    public void updateStartSpeeds() {
        this.velocimeter.updateSpeeds();
        this.txtAvgSpeed.setText(this.velocimeter.getFormatedAvgSpeed());
        this.txtMaxSpeed.setText(this.velocimeter.getFormatedMaxSpeed());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(VELOCIMETER_KEY, this.velocimeter);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == RESULT_OK && data != null) {
            this.journey = data.getParcelableExtra(NewJourneyActivity.NEW_JOURNEY_KEY);
            JourneyDAOSingleton.getINSTANCE().addJourney(journey);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.useMetrics);
        item.setChecked(this.velocimeter.isUseMetrics());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.useMetrics) {
            if(!this.velocimeter.isUseMetrics()) {
                item.setChecked(true);
                this.velocimeter.setUseMetrics(true);
            } else {
                item.setChecked(false);
                this.velocimeter.setUseMetrics(false);
            }
            if(this.isStarted) {
                this.onClickReset(null);
            }
            if(this.acceldecel) {
                if(acceldecelStarted)
                    this.chronometer.stop();
                this.resetAccelDecel();
                this.resetMain();
            }
        }
        if(item.getItemId() == R.id.about) {
            Intent sendIntent = new Intent(this, AboutActivity.class);
            startActivity(sendIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQ_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                this.doStuff();
            else
                Toast.makeText(this, R.string.allow_location, Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onLocationChanged(@NonNull Location location) {
        CustomLocation customLocation = new CustomLocation(location, this.velocimeter.isUseMetrics());
        this.updateSpeed(customLocation);
        if (this.velocimeter.getStartLocation() == null)
            this.velocimeter.setStartLocation(location);
        if(this.velocimeter.isUseMetrics())
            this.velocimeter.setDistance(this.velocimeter.getDistance() + (this.velocimeter.getStartLocation().distanceTo(location) / 1000.00));
        else
            this.velocimeter.setDistance((this.velocimeter.getDistance() + (this.velocimeter.getStartLocation().distanceTo(location) / 1000.00) / 1609));
        this.velocimeter.setStartLocation(location);
        if(this.acceldecelStarted) {
            if(!this.etxSpeed.getText().toString().isEmpty()) {
                int startSpeed = Integer.parseInt(this.txtStartSpeed.getText().toString());
                int endSpeed = Integer.parseInt(this.etxSpeed.getText().toString());
                if (startSpeed < endSpeed) {
                    if (endSpeed <= (int) this.velocimeter.getCurrentSpeed()) {
                        this.endTime = System.currentTimeMillis() - this.startTime;
                        this.chronometer.stop();
                        this.acceldecelStarted = false;
                        String unit;
                        if(this.velocimeter.isUseMetrics())
                            unit = "km/h";
                        else
                            unit = "mi/h";
                        String text = (getString(R.string.acceleration) + " (" + this.txtStartSpeed.getText().toString() + " " + unit + " " + getString(R.string.to) + " " + this.etxSpeed.getText().toString() + " "  + unit + "): \n\n" + new SimpleDateFormat("mm:ss SS").format(this.endTime) + "\n\n");
                        Intent sendIntent = new Intent(this, AccelDecelActivity.class);
                        sendIntent.putExtra(ACCELDECEL_KEY, text);
                        startActivity(sendIntent);
                        this.endTime = 0;
                        this.startTime = 0;
                        this.resetAccelDecel();
                        this.resetMain();
                    }
                }
                if (startSpeed > endSpeed) {
                    if (endSpeed >= (int) this.velocimeter.getCurrentSpeed()) {
                        this.endTime = System.currentTimeMillis() - this.startTime;
                        this.chronometer.stop();
                        this.acceldecelStarted = false;
                        String unit;
                        if(this.velocimeter.isUseMetrics())
                            unit = "km/h";
                        else
                            unit = "mi/h";
                        String text = (getString(R.string.deceleration) + " (" + this.txtStartSpeed.getText().toString() + " " + unit + " " + getString(R.string.to) + " " + this.etxSpeed.getText().toString() + " " + unit + "): \n\n" + new SimpleDateFormat("mm:ss SS").format(this.endTime) + "\n\n");
                        Intent sendIntent = new Intent(this, AccelDecelActivity.class);
                        sendIntent.putExtra(ACCELDECEL_KEY, text);
                        startActivity(sendIntent);
                        this.endTime = 0;
                        this.startTime = 0;
                        this.resetAccelDecel();
                        this.resetMain();
                    }
                }
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        this.updateSpeed(null);
        if(this.isStarted) {
            this.isStarted = false;
            this.resetStart();
            this.bttAccelDecel.setVisibility(View.VISIBLE);
            this.bttJourneys.setVisibility(View.VISIBLE);
            this.bttStart.setBackground(getDrawable(R.drawable.button_drawable));
        }
        if(this.acceldecel) {
            if(this.acceldecelStarted)
                this.chronometer.stop();
            this.acceldecel = false;
            this.resetAccelDecel();
            this.resetMain();
        }
        if(this.velocimeter.getStartLocation() != null)
            this.velocimeter.setStartLocation(null);
        new DialogEnableGPS(this) {
            @Override
            public void onClickYes() {
                this.getContext().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }

            @Override
            public void onClickNo() {

            }
        };
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}