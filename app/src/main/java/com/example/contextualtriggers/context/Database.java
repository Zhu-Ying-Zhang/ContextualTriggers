package com.example.contextualtriggers.context;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class Database extends SQLiteOpenHelper {

    public static final String TAG = "DBHelper";

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "ContextualTriggers.db";

    private static Database instance;

    // Geofences table (to store home and work location)
    private static final String GEOFENCE_TABLE_NAME = "Geofences";
    private static final String GEOFENCE_COLUMN_ID = "Id";
    private static final String GEOFENCE_COLUMN_GEOFENCE_NAME = "LocationName";
    private static final String GEOFENCE_COLUMN_LATITUDE = "Latitude";
    private static final String GEOFENCE_COLUMN_LONGITUDE = "Longitude";

    private static final String STEPS_TABLE_NAME = "Steps";
    private static final String STEPS_COLUMN_DATE = "Date";
    private static final String STEPS_COLUMN_STEPS = "Steps";

    private static final String WORK_TABLE_NAME = "Work";
    private static final String WORK_COLUMN_EXIT = "ExitTime";

    private static final String CREATE_GEOFENCE_TABLE_STMT =
            "CREATE TABLE " + GEOFENCE_TABLE_NAME + "( "
                    + GEOFENCE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + GEOFENCE_COLUMN_GEOFENCE_NAME + " TEXT UNIQUE, "
                    + GEOFENCE_COLUMN_LATITUDE + " REAL NOT NULL, "
                    + GEOFENCE_COLUMN_LONGITUDE + " REAL NOT NULL "
                    + ");";

    private static final String CREATE_STEPS_TABLE_STMT =
            "CREATE TABLE " + STEPS_TABLE_NAME + "( "
                    + STEPS_COLUMN_DATE + " INTEGER PRIMARY KEY, "
                    + STEPS_COLUMN_STEPS + " INTEGER DEFAULT 0"
                    + ");";

    private static final String CREATE_WORK_TABLE_STMT =
            "CREATE TABLE " + WORK_TABLE_NAME + " ( "
                    + WORK_COLUMN_EXIT + " INTEGER "
                    + ");";

    private static final String GEOFENCE_DROP_TABLE_STMT =
            "DROP TABLE IF EXISTS " + GEOFENCE_TABLE_NAME;

    private static final String STEPS_DROP_TABLE_STMT =
            "DROP TABLE IF EXISTS " + STEPS_TABLE_NAME;

    private static final String WORK_DROP_TABLE_STMT =
            "DROP TABLE IF EXISTS " + WORK_TABLE_NAME;

    private Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static void init(Context context) {
        if (instance == null)
            instance = new Database(context);
        Log.d("DB", "initialised");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_GEOFENCE_TABLE_STMT);
        db.execSQL(CREATE_STEPS_TABLE_STMT);
        db.execSQL(CREATE_WORK_TABLE_STMT);
        Log.d("DB", "table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(GEOFENCE_DROP_TABLE_STMT);
        db.execSQL(STEPS_DROP_TABLE_STMT);
        db.execSQL(WORK_DROP_TABLE_STMT);
        onCreate(db);
    }

    public static void addGeofence(@NonNull Geofences geofence) {
        ContentValues values = new ContentValues();
        values.put(GEOFENCE_COLUMN_GEOFENCE_NAME, geofence.getName());
        values.put(GEOFENCE_COLUMN_LATITUDE, geofence.getLatitude());
        values.put(GEOFENCE_COLUMN_LONGITUDE, geofence.getLongitude());
        geofence.setId(instance.getWritableDatabase().insert(GEOFENCE_TABLE_NAME, null, values));
    }

    @Nullable
    public static Geofences getGeofence(String geofenceName) {
        String query = "SELECT * FROM " + GEOFENCE_TABLE_NAME
                + " WHERE " + GEOFENCE_COLUMN_GEOFENCE_NAME + " = \"" + geofenceName + "\"";

        Cursor cursor = instance.getWritableDatabase().rawQuery(query, new String[]{});
        if (cursor.moveToNext()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(GEOFENCE_COLUMN_GEOFENCE_NAME));
            @SuppressLint("Range") double latitude = cursor.getDouble(cursor.getColumnIndex(GEOFENCE_COLUMN_LATITUDE));
            @SuppressLint("Range") double longitude = cursor.getDouble(cursor.getColumnIndex(GEOFENCE_COLUMN_LONGITUDE));
            return new Geofences(name, latitude, longitude);
        }
        return null;
    }


    public static void addSteps(long date, int steps) {
        System.out.println("Adding " + steps + " to " + date);
        SQLiteDatabase db = instance.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STEPS_COLUMN_DATE, date);
        values.put(STEPS_COLUMN_STEPS, steps);
        db.replace(STEPS_TABLE_NAME, null, values);
    }

    @SuppressLint("Range")
    public static int getSteps(long date) {
        SQLiteDatabase db = instance.getReadableDatabase();

        String[] projection = {
                STEPS_COLUMN_STEPS
        };
        String selection = STEPS_COLUMN_DATE + " LIKE ?";
        String[] args = new String[] {String.valueOf(date)};

        Cursor c = db.query(STEPS_TABLE_NAME, projection, selection, args, null, null, null);

        int steps = 0;
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            steps = c.getInt(c.getColumnIndex(STEPS_COLUMN_STEPS));
            c.close();
        }
        return steps;
    }

    public static void addWorkExit(long date) {
        ContentValues values = new ContentValues();
        values.put(WORK_COLUMN_EXIT, date);
        instance.getWritableDatabase().insert(WORK_TABLE_NAME, null, values);
    }

    public static long getAvgWorkExitTime() {
        String query = "SELECT AVG(" + WORK_COLUMN_EXIT + ") FROM " + WORK_TABLE_NAME;
        Cursor cursor = instance.getWritableDatabase().rawQuery(query, new String[]{});
        if (cursor.moveToFirst())
            return cursor.getLong(0);

        return Integer.MAX_VALUE;
    }
}

