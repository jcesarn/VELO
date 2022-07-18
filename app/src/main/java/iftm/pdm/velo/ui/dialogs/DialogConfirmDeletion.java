package iftm.pdm.velo.ui.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import iftm.pdm.velo.R;

public abstract class DialogConfirmDeletion extends AlertDialog.Builder {

    public DialogConfirmDeletion(Context context) {
        super(context);
        this.setTitle(R.string.removing_journeys);
        this.setMessage(R.string.confirm_deletion);
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
