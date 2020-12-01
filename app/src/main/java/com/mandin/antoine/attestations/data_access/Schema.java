package com.mandin.antoine.attestations.data_access;

import android.provider.BaseColumns;

public class Schema {
    public static class Profile implements BaseColumns {
        public static final String LAST_NAME = "last_name";
        public static final String FIRST_NAME = "first_name";
        public static final String BIRTHDAY = "birthday";
        public static final String PLACE_OF_BIRTH = "place_of_birth";
        public static final String TABLE_NAME = "profile";

        public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                LAST_NAME + " TEXT," +
                FIRST_NAME + " TEXT," +
                BIRTHDAY + " INTEGER," +
                PLACE_OF_BIRTH + " TEXT" +
                ")";
        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class Place implements BaseColumns {
        public static final String CITY = "city";
        public static final String ADDRESS = "address";
        public static final String ZIP_CODE = "zip_code";
        public static final String TABLE_NAME = "place";

        public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                CITY + " TEXT," +
                ADDRESS + " TEXT," +
                ZIP_CODE + " INTEGER"+
                ")";
        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class Attestation implements BaseColumns {
        public static final String CREATION_DATE = "creation_date";
        public static final String USING_DATE = "using_date";
        public static final String REAL_CREATION_DATE = "real_creation_date";
        public static final String PROFILE = "profile_id";
        public static final String PLACE = "place_id";
        public static final String REASON = "reason_id";
        public static final String TABLE_NAME = "attestation";

        public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                CREATION_DATE + " INTEGER," +
                USING_DATE + " INTEGER," +
                REAL_CREATION_DATE + " INTEGER,"+
                PROFILE + " INTEGER," +
                PLACE + " INTEGER," +
                REASON + " INTEGER" +
                ")";
        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}
