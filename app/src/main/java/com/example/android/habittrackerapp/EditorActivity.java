package com.example.android.habittrackerapp;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.habittrackerapp.data.HabitContract.HabitEntry;
import com.example.android.habittrackerapp.data.HabitDbHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditorActivity extends AppCompatActivity {

    /**EditText field to enter workout name*/
    private EditText workoutNameInput;

    /**EditText field to enter date as hint*/
    private EditText dateInput;

    /**EditText field to enter distance*/
    private EditText distanceInput;

    /**EditText field to enter time as hint*/
    private EditText timeInput;

    /** EditText field to enter the habit task completion */
    private Spinner completionSpinner;

    /**
     * Completion of the task. The possible values are:
     * 0 for not Completed the task, 1 for task completed
     */
    private int completion = 0;

    /**
     * Calender value
     */
    private Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor_activity);

        // Find all relevant views that we will need to read user input from
        workoutNameInput = (EditText)findViewById(R.id.edit_workout_name);
        dateInput = (EditText)findViewById(R.id.edit_date_input);
        distanceInput = (EditText)findViewById(R.id.edit_distance);
        timeInput = (EditText)findViewById(R.id.edit_time);
        completionSpinner = (Spinner) findViewById(R.id.spinner_completion);

        setupSpinner();

        //Date picker on date edit text view
        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate();
            }
        };
        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(EditorActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    //format the date into us format
    private void updateDate() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        dateInput.setText(dateFormat.format(myCalendar.getTime()));
    }

    /**
     * Setup the dropdown spinner that allows the user to select the completion of the task.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter completionSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_completion_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        completionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        completionSpinner.setAdapter(completionSpinnerAdapter);

        // Set the integer mSelected to the constant values
        completionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.completion_completed))) {
                        completion = HabitEntry.COMPLETION_COMPLETED; // Task Completed
                    } else {
                        completion = HabitEntry.COMPLETION_NOT_COMPLETED; // Task not Completed
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                completion = 0; // not completed
            }
        });
    }

    //Habit Insertion Method
    private void insertHabit(){

        //Get input from the edit texts
        String name = workoutNameInput.getText().toString().trim();
        String date = dateInput.getText().toString().trim();
        String distanceString = distanceInput.getText().toString().trim();
        int distance = Integer.parseInt(distanceString);
        String time = timeInput.getText().toString().trim();

        //DB helper instance
        HabitDbHelper dbHelper = new HabitDbHelper(this);

        //get the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //Declaring Content value for key value pair
        ContentValues values = new ContentValues();

        values.put(HabitEntry.COLUMN_HABIT_WORKOUT_NAME, name);
        values.put(HabitEntry.COLUMN_HABIT_DATE, date);
        values.put(HabitEntry.COLUMN_HABIT_DISTANCE, distance);
        values.put(HabitEntry.COLUMN_HABIT_TIME, time);
        values.put(HabitEntry.COLUMN_HABIT_COMPLETION, completion);

        long newRowId = db.insert(HabitEntry.TABLE_NAME, null, values);

        if (newRowId == -1){
            Toast.makeText(this, "Error With Saving Habit", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Habit Saved with Row ID: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                insertHabit();
                //Exit Activity
                finish();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
