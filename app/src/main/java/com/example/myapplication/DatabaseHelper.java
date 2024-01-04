package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "YourDatabaseName";
    private static final int DATABASE_VERSION = 1;

    // Define your table and column names
    private static final String TABLE_NAME = "admins";
    private static final String COLUMN_NAME = "faculty";
    private static final String COLUMN_SURNAME = "surname";
    private static final String COLUMN_LECTURER = "lecturer";
    // Add other columns as needed

    // SQL statement to create the table
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    "recID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_SURNAME + " TEXT," +
                    COLUMN_LECTURER + " TEXT);";
    // Add other columns to the CREATE_TABLE statement

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the table when the database is first created
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades if needed
    }

    public long insertStudent(String lecturer) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_LECTURER, lecturer);

        long result = db.insert(TABLE_NAME, null, values);
        db.close();

        return result;
    }
}