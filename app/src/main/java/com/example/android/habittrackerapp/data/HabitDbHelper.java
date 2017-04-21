package com.example.android.habittrackerapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.habittrackerapp.data.HabitContract.HabitEntry;
/**
 * Created by sksho on 21-Apr-17.
 */

public class HabitDbHelper extends SQLiteOpenHelper {

    //LOG message
    public static final String LOG_TAG = HabitDbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "habits.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    public HabitDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_HABIT_TABLE = "CREATE TABLE " + HabitEntry.TABLE_NAME + " ("
                + HabitEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + HabitEntry.COLUMN_HABIT_WORKOUT_NAME + " TEXT NOT NULL,"
                + HabitEntry.COLUMN_HABIT_DATE + " TEXT,"
                + HabitEntry.COLUMN_HABIT_DISTANCE + " INTEGER NOT NULL,"
                + HabitEntry.COLUMN_HABIT_TIME + " TEXT NOT NULL,"
                + HabitEntry.COLUMN_HABIT_COMPLETION + " INTEGER NOT NULL DEFAULT 0);";

        Log.v(LOG_TAG,SQL_CREATE_HABIT_TABLE);

        //executes the SQL
        db.execSQL(SQL_CREATE_HABIT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
