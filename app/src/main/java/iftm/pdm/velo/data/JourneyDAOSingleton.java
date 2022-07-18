package iftm.pdm.velo.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import iftm.pdm.velo.model.Journey;

public class JourneyDAOSingleton {

    private static JourneyDAOSingleton INSTANCE;
    private DBHelper dbHelper;

    private JourneyDAOSingleton() {
        this.dbHelper = new DBHelper();
    }

    public static JourneyDAOSingleton getINSTANCE() {
        if(INSTANCE == null)
            INSTANCE = new JourneyDAOSingleton();
        return INSTANCE;
    }

    public Journey getJourney(long id) {
        SQLiteDatabase db = this.dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBSchema.JorneyT.TABLENAME, null, DBSchema.JorneyT.ID + " = ?", new String[]{Long.toString(id)}, null, null, null);
        cursor.moveToFirst();
        long jid = cursor.getLong(cursor.getColumnIndex(DBSchema.JorneyT.ID));
        String name = cursor.getString(cursor.getColumnIndex(DBSchema.JorneyT.NAME));
        String distance = cursor.getString(cursor.getColumnIndex(DBSchema.JorneyT.DISTANCE));
        String avgspeed = cursor.getString(cursor.getColumnIndex(DBSchema.JorneyT.AVGSPEED));
        String maxspeed = cursor.getString(cursor.getColumnIndex(DBSchema.JorneyT.MAXSPEED));
        long time = Long.parseLong(cursor.getString(cursor.getColumnIndex(DBSchema.JorneyT.TIME)));
        String date = cursor.getString(cursor.getColumnIndex(DBSchema.JorneyT.DATE));
        String unit = cursor.getString(cursor.getColumnIndex(DBSchema.JorneyT.UNIT));
        cursor.close();
        db.close();
        return new Journey(jid, name, time, date, distance, avgspeed, maxspeed, unit);
    }

    public ArrayList<Journey> getJourneys() {
        ArrayList<Journey> journeys = new ArrayList<>();
        SQLiteDatabase db = this.dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBSchema.JorneyT.TABLENAME, null, null, null, null, null, null);
        while(cursor.moveToNext()) {
            long jid = cursor.getLong(cursor.getColumnIndex(DBSchema.JorneyT.ID));
            String name = cursor.getString(cursor.getColumnIndex(DBSchema.JorneyT.NAME));
            String distance = cursor.getString(cursor.getColumnIndex(DBSchema.JorneyT.DISTANCE));
            String avgspeed = cursor.getString(cursor.getColumnIndex(DBSchema.JorneyT.AVGSPEED));
            String maxspeed = cursor.getString(cursor.getColumnIndex(DBSchema.JorneyT.MAXSPEED));
            long time = Long.parseLong(cursor.getString(cursor.getColumnIndex(DBSchema.JorneyT.TIME)));
            String date = cursor.getString(cursor.getColumnIndex(DBSchema.JorneyT.DATE));
            String unit = cursor.getString(cursor.getColumnIndex(DBSchema.JorneyT.UNIT));
            journeys.add(new Journey(jid, name, time, date, distance, avgspeed, maxspeed, unit));
        }
        cursor.close();
        db.close();
        return journeys;
    }

    public void addJourney(Journey journey) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBSchema.JorneyT.NAME, journey.getName());
        cv.put(DBSchema.JorneyT.DISTANCE, journey.getDistance());
        cv.put(DBSchema.JorneyT.AVGSPEED, journey.getAvgSpeed());
        cv.put(DBSchema.JorneyT.MAXSPEED, journey.getMaxSpeed());
        cv.put(DBSchema.JorneyT.TIME, journey.getTime());
        cv.put(DBSchema.JorneyT.DATE, journey.getDate());
        cv.put(DBSchema.JorneyT.UNIT, journey.getUnit());
        db.insert(DBSchema.JorneyT.TABLENAME, null, cv);
        db.close();
    }

    public long editJourney(String oName, String nName) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        Cursor cursor = db.query(DBSchema.JorneyT.TABLENAME, null, DBSchema.JorneyT.NAME + " = ?", new String[]{oName}, null, null, null);
        long id = -1;
        if(cursor.moveToNext()) {
            id = cursor.getLong(cursor.getColumnIndex(DBSchema.JorneyT.ID));
            ContentValues cv = new ContentValues();
            cv.put(DBSchema.JorneyT.NAME, nName);
            db.update(DBSchema.JorneyT.TABLENAME, cv, DBSchema.JorneyT.NAME + " = ?", new String[]{oName});
        }
        cursor.close();
        db.close();
        return id;
    }

    public void removeJourney(long id) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        db.delete(DBSchema.JorneyT.TABLENAME, DBSchema.JorneyT.ID + " = " + id, null);
        db.close();
    }

    public void removeJourneys(ArrayList<Long> idList) {
        for(long id : idList)
            this.removeJourney(id);
    }

    public boolean checkJourneyName(String name) {
        SQLiteDatabase db = this.dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBSchema.JorneyT.TABLENAME, null, DBSchema.JorneyT.NAME + " = ?", new String[]{name}, null, null, null);
        String nm = "";
        if(cursor.moveToFirst())
            nm = cursor.getString(cursor.getColumnIndex(DBSchema.JorneyT.NAME));
        cursor.close();
        db.close();
        return nm.isEmpty();
    }

    public boolean isEmpty() {
        SQLiteDatabase db = this.dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBSchema.JorneyT.TABLENAME, null, null, null, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return !exists;
    }

}
