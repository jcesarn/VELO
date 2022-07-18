package iftm.pdm.velo.data;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import iftm.pdm.velo.ui.activities.MainActivity;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_FILENAME = "journeys.db";
    public static final int DB_VERSION = 1;

    public DBHelper() {
        super(MainActivity.getMainContext(), DB_FILENAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DBSchema.JorneyT.getCreationQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

}
