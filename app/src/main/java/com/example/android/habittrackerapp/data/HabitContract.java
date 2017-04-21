package com.example.android.habittrackerapp.data;

import android.provider.BaseColumns;

/**
 * Created by sksho on 21-Apr-17.
 */

public class HabitContract {
    public HabitContract() {
    }

    public static final class HabitEntry implements BaseColumns {

        //Table name in DB
        public final static String TABLE_NAME = "habit_tracker";

        //Declaring Column into DB
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_HABIT_WORKOUT_NAME = "workout_name";
        public final static String COLUMN_HABIT_DATE = "date";
        public final static String COLUMN_HABIT_DISTANCE = "distance";
        public final static String COLUMN_HABIT_TIME = "time";
        public final static String COLUMN_HABIT_COMPLETION = "completion";

        //Task Completed or not Constant
        public static final int COMPLETION_NOT_COMPLETED = 0;
        public static final int COMPLETION_COMPLETED = 1;

    }
}
