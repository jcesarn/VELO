package iftm.pdm.velo.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import iftm.pdm.velo.R;
import iftm.pdm.velo.data.JourneyDAOSingleton;
import iftm.pdm.velo.ui.list_adapters.JourneyAdapter;

public class EditJourneyActivity extends Activity {

    public static final String EDIT_JOURNEY_KEY = "EditJourneyActivity.JOURNEY";
    private EditText etxName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editjourney);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        this.etxName = this.findViewById(R.id.etxName);
    }

    public void onClickSave(View view) {
        String nName = "", oName = "";
        if(!this.etxName.getText().toString().equals("")) {
            nName = this.etxName.getText().toString();
            Bundle extras = getIntent().getExtras();
            if(JourneyDAOSingleton.getINSTANCE().checkJourneyName(nName)) {
                if (!this.etxName.getText().toString().isEmpty() && extras != null) {
                    nName = this.etxName.getText().toString();
                    oName = extras.getString(JourneyAdapter.JOURNEY_ADAPTER_KEY);
                    Intent sendIntent = new Intent();
                    sendIntent.putExtra(EDIT_JOURNEY_KEY, nName);
                    sendIntent.putExtra(JourneyAdapter.JOURNEY_ADAPTER_KEY, oName);
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
