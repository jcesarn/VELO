package iftm.pdm.velo.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import iftm.pdm.velo.R;

public class AboutActivity extends Activity {

    private TextView txtAbout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        this.txtAbout = findViewById(R.id.txtAbout);
        this.aboutSetText();
    }

    private void aboutSetText() {
        String about = "\nIFTM - Instituto Federal do Triângulo Mineiro\n\n" + getString(R.string.ana_and_sys_devel) + "\n\nUberaba-MG\n2020\n\n\n\n" + getString(R.string.about_text) + "\n\n\n\nJúlio César Nomelini, Carlos Henrique Venâncio Filho\n";
        this.txtAbout.setText(about);
    }

}
