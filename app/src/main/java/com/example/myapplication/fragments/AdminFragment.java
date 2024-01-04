package com.example.myapplication.fragments;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

import java.io.File;
import java.util.ArrayList;

public class AdminFragment extends Fragment {
    private EditText editTextFaculty;
    private EditText editTextDepartment;
    private EditText editTextLecturer;
    private Button addButton;
    private Button deleteButton;
    private Button updateButton;
    private Button searchButton;
    private ListView listView;
    private String path;
    private SQLiteDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin, container, false);

        editTextFaculty = view.findViewById(R.id.editTextFaculty);
        editTextDepartment = view.findViewById(R.id.editTextDepartment);
        editTextLecturer = view.findViewById(R.id.editTextLecturer);
        addButton = view.findViewById(R.id.addButton);
        deleteButton = view.findViewById(R.id.deleteButton);
        updateButton = view.findViewById(R.id.updateButton);
        searchButton = view.findViewById(R.id.searchButton);
        listView = view.findViewById(R.id.listView);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                read();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });

        File myDbPath = requireContext().getFilesDir();
        path = myDbPath + "/" + "YourDatabaseName";

        try {
            if (!databaseExist()) {
                database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
                Toast.makeText(requireContext(), "DB Created", Toast.LENGTH_SHORT).show();

                String table = "create table admins (recID integer PRIMARY KEY autoincrement, faculty text, department text, lecturer text);";
                database.execSQL(table);

                Toast.makeText(requireContext(), "Admins Table is created", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(requireContext(), "We have a database already", Toast.LENGTH_LONG).show();
            }
        } catch (SQLException e) {
            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    // Helper method to check if we have a database
    private boolean databaseExist() {
        File dbFile = new File(path);
        return dbFile.exists();
    }

    // Method to add data into the database
    private void add() {
        try {
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);

            String faculty = editTextFaculty.getText().toString();
            String department = editTextDepartment.getText().toString();
            String lecturer = editTextLecturer.getText().toString();

            String sql = "insert into admins (faculty, department, lecturer) values ('" + faculty + "', '" + department + "', '" + lecturer + "');";
            database.execSQL(sql);

            Toast.makeText(requireContext(), "Admin added", Toast.LENGTH_SHORT).show();

            editTextFaculty.setText("");
            editTextDepartment.setText("");
            editTextLecturer.setText("");
        } catch (SQLException e) {
            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Method to read data from the database
    private void read() {
        try {
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);

            String sql = "select * from admins;";
            Cursor cursor = database.rawQuery(sql, null);

            ArrayList<String> admins = new ArrayList<>();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, admins);

            while (cursor.moveToNext()) {
                String faculty = cursor.getString(cursor.getColumnIndexOrThrow("faculty"));
                String department = cursor.getString(cursor.getColumnIndexOrThrow("department"));
                String lecturer = cursor.getString(cursor.getColumnIndexOrThrow("lecturer"));

                admins.add(lecturer + " - " + faculty + " - " + department);
            }

            listView.setAdapter(adapter);
            database.close();
        } catch (SQLException e) {
            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void delete() {
        try {
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
            String lecturer = editTextLecturer.getText().toString();
            String deleteAdmin = "DELETE from admins WHERE lecturer = '" + lecturer + "';";
            database.execSQL(deleteAdmin);
            Toast.makeText(requireContext(), "Admin deleted", Toast.LENGTH_SHORT).show();
            editTextLecturer.setText("");
            database.close();
        } catch (SQLException e) {
            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void update() {
        try {
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
            String faculty = editTextFaculty.getText().toString();
            String department = editTextDepartment.getText().toString();
            String lecturer = editTextLecturer.getText().toString();
            String updateAdmin = "UPDATE admins SET faculty = '" + faculty + "', department = '" + department + "' WHERE lecturer = '" + lecturer + "';";
            database.execSQL(updateAdmin);
            Toast.makeText(requireContext(), "Admin updated", Toast.LENGTH_SHORT).show();
            editTextFaculty.setText("");
            editTextDepartment.setText("");
            editTextLecturer.setText("");
            database.close();
        } catch (SQLException e) {
            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (database != null && database.isOpen()) {
            database.close();
        }
    }
}

