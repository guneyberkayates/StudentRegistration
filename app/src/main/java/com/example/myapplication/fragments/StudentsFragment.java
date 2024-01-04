package com.example.myapplication.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

public class StudentsFragment extends Fragment {

    private TextView studentsListTextView;
    private Button showStudentsButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_students, container, false);

        studentsListTextView = view.findViewById(R.id.studentsListTextView);
        showStudentsButton = view.findViewById(R.id.showStudentsButton);

        showStudentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call a method to fetch and display student data
                fetchAndDisplayStudents();
            }
        });

        return view;
    }

    private void fetchAndDisplayStudents() {
        try {
            // Get a reference to the database
            SQLiteDatabase database = SQLiteDatabase.openDatabase(
                    getActivity().getFilesDir().getAbsolutePath() + "/YourDatabaseName",
                    null,
                    SQLiteDatabase.OPEN_READONLY
            );

            // Query the database
            Cursor cursor = database.rawQuery("SELECT * FROM students", null);

            // Loop through the results and append them to the TextView
            StringBuilder stringBuilder = new StringBuilder();
            while (cursor.moveToNext()) {
                stringBuilder.append(cursor.getString(0));
                stringBuilder.append(" ");
                stringBuilder.append(cursor.getString(1));
                stringBuilder.append(" ");
                stringBuilder.append(cursor.getString(2));
                stringBuilder.append(" ");
                stringBuilder.append(cursor.getString(3));
                stringBuilder.append(" ");
                stringBuilder.append(cursor.getString(4));
                stringBuilder.append(" ");
                stringBuilder.append(cursor.getString(5));
                stringBuilder.append("\n");
            }

            // Display the results
            studentsListTextView.setText(stringBuilder.toString());

            // Close the database
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}