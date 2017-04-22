package com.example.android.habittrackerapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.habittrackerapp.data.HabitContract.HabitEntry;
import com.example.android.habittrackerapp.data.HabitDbHelper;


public class CatalogActivity extends AppCompatActivity {

    private HabitDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalog_activity);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);

            }
        });

        dbHelper = new HabitDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the habits database.
     *
     */
    private void displayDatabaseInfo() {
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        HabitDbHelper mDbHelper = new HabitDbHelper(this);

        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        //String Projection for query
        String[] projection = {
                HabitEntry._ID,
                HabitEntry.COLUMN_HABIT_WORKOUT_NAME,
                HabitEntry.COLUMN_HABIT_DATE,
                HabitEntry.COLUMN_HABIT_DISTANCE,
                HabitEntry.COLUMN_HABIT_TIME,
                HabitEntry.COLUMN_HABIT_COMPLETION
        };

        //cursor query
        Cursor cursor = db.query(
                HabitEntry.TABLE_NAME,
                projection,
                null, null, null, null, null, null);

        //find view id
        TextView displayView = (TextView) findViewById(R.id.text_view_habit);

        try {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).
            displayView.setText("The habit_tracker table contains: " + cursor.getCount() + " habits.\n\n");
            displayView.append(HabitEntry._ID + " - " +
                    HabitEntry.COLUMN_HABIT_WORKOUT_NAME + " - " +
                    HabitEntry.COLUMN_HABIT_DATE + " - " +
                    HabitEntry.COLUMN_HABIT_DISTANCE + " - " +
                    HabitEntry.COLUMN_HABIT_TIME + " - " +
                    HabitEntry.COLUMN_HABIT_COMPLETION + "\n");

            int idColumnIndex = cursor.getColumnIndex(HabitEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_WORKOUT_NAME);
            int dateColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_DATE);
            int distanceColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_DISTANCE);
            int timeColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_TIME);
            int completionColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_COMPLETION);

            //goes through each row to check
            while (cursor.moveToNext()) {
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentDate = cursor.getString(dateColumnIndex);
                int currentDistance = cursor.getInt(distanceColumnIndex);
                String currentTime = cursor.getString(timeColumnIndex);
                int currentCompletion = cursor.getInt(completionColumnIndex);

                //display data in textview
                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentDate + " - " +
                        currentDistance + " - " +
                        currentTime + " - " +
                        currentCompletion));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    /**
     * Dummy data insert method
     */
    private void insertHabit() {

        //get the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(HabitEntry.COLUMN_HABIT_WORKOUT_NAME, "Day Workout");
        values.put(HabitEntry.COLUMN_HABIT_DATE, "04/21/17");
        values.put(HabitEntry.COLUMN_HABIT_DISTANCE, 10);
        values.put(HabitEntry.COLUMN_HABIT_TIME, "10m");
        values.put(HabitEntry.COLUMN_HABIT_COMPLETION, HabitEntry.COMPLETION_COMPLETED);

        long newRowId = db.insert(HabitEntry.TABLE_NAME, null, values);

        Log.v("CatalogActivity", "New Row Id " + newRowId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (item.getItemId() == R.id.action_insert_dummy_data) {
            insertHabit();
            displayDatabaseInfo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
