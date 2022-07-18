package iftm.pdm.velo.ui.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import iftm.pdm.velo.R;

public abstract class DialogEnableGPS extends AlertDialog.Builder {

    public DialogEnableGPS(Context context) {
        super(context);
        this.setTitle(R.string.gps_disabled);
        this.setMessage(R.string.confirm_gps);
        this.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onClickYes();
            }
        });
        this.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onClickNo();
            }
        });
        this.create().show();
    }

    public abstract void onClickYes();
    public abstract void onClickNo();

}
