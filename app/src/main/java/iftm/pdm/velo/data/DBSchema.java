package iftm.pdm.velo.data;

public class DBSchema {

    public static final class JorneyT {

        public static final String TABLENAME = "journey";
        public static final String ID = "j_id";
        public static final String NAME = "j_name";
        public static final String DISTANCE = "j_distance";
        public static final String AVGSPEED = "j_avgspeed";
        public static final String MAXSPEED = "j_maxspeed";
        public static final String TIME = "j_time";
        public static final String DATE = "j_date";
        public static final String UNIT = "j_usemetric";

        public static String getCreationQuery() {
            return "CREATE TABLE IF NOT EXISTS " + TABLENAME + " (" +
                        ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        NAME + " TEXT NOT NULL, " +
                        DISTANCE + " TEXT NOT NULL, " +
                        AVGSPEED + " TEXT NOT NULL, " +
                        MAXSPEED + " TEXT NOT NULL, " +
                        TIME + " TEXT NOT NULL, " +
                        DATE + " TEXT NOT NULL, " +
                        UNIT + " TEXT NOT NULL " +
                    ");";
        }

    }

}
