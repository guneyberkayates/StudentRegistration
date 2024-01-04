package com.example.myapplication;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "YourDatabaseName";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create your tables here
        String adminsTable = "CREATE TABLE admins (recID INTEGER PRIMARY KEY AUTOINCREMENT, faculty TEXT, department TEXT, lecturer TEXT);";
        db.execSQL(adminsTable);

        String studentsTable = "CREATE TABLE students (recID INTEGER PRIMARY KEY AUTOINCREMENT, firstName TEXT, lastName TEXT, faculty TEXT, department TEXT, advisor TEXT);";
        db.execSQL(studentsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades if needed
    }
}
