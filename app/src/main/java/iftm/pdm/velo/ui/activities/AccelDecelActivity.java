package iftm.pdm.velo.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import iftm.pdm.velo.R;

public class AccelDecelActivity extends Activity {

    private TextView txtAccelDecelRes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceldecel);
        this.txtAccelDecelRes = findViewById(R.id.txtAccelDecelRes);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
            this.txtAccelDecelRes.setText(bundle.getString(MainActivity.ACCELDECEL_KEY));
    }

}
