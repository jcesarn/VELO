package iftm.pdm.velo.ui.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import iftm.pdm.velo.R;
import iftm.pdm.velo.data.JourneyDAOSingleton;
import iftm.pdm.velo.model.Journey;

public class NewJourneyActivity extends Activity {

    public static final String NEW_JOURNEY_KEY = "NewJourneyActivity.JOURNEY";
    private EditText etxName;
    private long time;
    private String distance, avgSpeed, maxSpeed;
    private boolean useMetrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newjourney);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        this.etxName = this.findViewById(R.id.etxName);
        if(savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                this.time = extras.getLong(MainActivity.TIME_KEY);
                this.distance = extras.getString(MainActivity.DISTANCE_KEY);
                this.avgSpeed = extras.getString(MainActivity.AVGSPEED_KEY);
                this.maxSpeed = extras.getString(MainActivity.MAXSPEED_KEY);
                this.useMetrics = extras.getBoolean(MainActivity.USEMETRICS_KEY);
            }
        } else {
            this.time = (long) savedInstanceState.getSerializable(MainActivity.TIME_KEY);
            this.distance = (String) savedInstanceState.getSerializable(MainActivity.DISTANCE_KEY);
            this.avgSpeed = (String) savedInstanceState.getSerializable(MainActivity.AVGSPEED_KEY);
            this.maxSpeed = (String) savedInstanceState.getSerializable(MainActivity.MAXSPEED_KEY);
            this.useMetrics = (boolean) savedInstanceState.getSerializable(MainActivity.USEMETRICS_KEY);
        }
    }

    public void onClickSave(View view) {
        if(!this.etxName.getText().toString().equals("")) {
            if(JourneyDAOSingleton.getINSTANCE().checkJourneyName(this.etxName.getText().toString())) {
                String nom = this.etxName.getText().toString();
                String unit;
                if(this.useMetrics)
                    unit = "km";
                else
                    unit = "mi";
                @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("hh:mm:ss" + "\n" + "dd/MM/yyyy").format(Calendar.getInstance().getTime());
                Long tim = this.time;
                if (!nom.isEmpty() && !this.distance.isEmpty() && !this.avgSpeed.isEmpty() && !this.maxSpeed.isEmpty()) {
                    Journey journey = new Journey(0, nom, tim, date, this.distance, this.avgSpeed, this.maxSpeed, unit);
                    Intent sendIntent = new Intent();
                    sendIntent.putExtra(NEW_JOURNEY_KEY, journey);
                    setResult(RESULT_OK, sendIntent);
                } else
                    setResult(RESULT_CANCELED, null);
                finish();
            } else
                Toast.makeText(this, R.string.name_found, Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, R.string.name_not_found, Toast.LENGTH_SHORT).show();
    }

}
