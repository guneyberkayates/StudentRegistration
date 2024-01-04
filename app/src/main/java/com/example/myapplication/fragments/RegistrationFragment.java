package com.example.myapplication.fragments;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.myapplication.R;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class RegistrationFragment extends Fragment {
    private EditText editTextFirstNameRegistration;
    private EditText editTextLastNameRegistration;
    private EditText editTextFacultyRegistration;
    private EditText editTextDepartmentRegistration;
    private EditText editTextAdvisorRegistration;
    private RadioGroup radioGroupGenderRegistration;
    private Button addButtonRegistration;
    private Button deleteButtonRegistration;
    private Button updateButtonRegistration;
    private Button searchButtonRegistration;
    private ListView listViewRegistration;
    private String path;
    private SQLiteDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        editTextFirstNameRegistration = view.findViewById(R.id.editTextFirstNameRegistration);
        editTextLastNameRegistration = view.findViewById(R.id.editTextLastNameRegistration);
        editTextFacultyRegistration = view.findViewById(R.id.editTextFacultyRegistration);
        editTextDepartmentRegistration = view.findViewById(R.id.editTextDepartmentRegistration);
        editTextAdvisorRegistration = view.findViewById(R.id.editTextAdvisorRegistration);
        radioGroupGenderRegistration = view.findViewById(R.id.genderRadioGroupRegistration);
        addButtonRegistration = view.findViewById(R.id.addButtonRegistration);
        deleteButtonRegistration = view.findViewById(R.id.deleteButtonRegistration);
        updateButtonRegistration = view.findViewById(R.id.updateButtonRegistration);
        searchButtonRegistration = view.findViewById(R.id.searchButtonRegistration);
        listViewRegistration = view.findViewById(R.id.listViewRegistration);

        addButtonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });

        searchButtonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                read();
            }
        });

        deleteButtonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });

        updateButtonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });

        File myDbPath = requireContext().getFilesDir();
        path = myDbPath + "/" + "YourDatabaseName";

        try {
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
            Toast.makeText(requireContext(), "DB Created", Toast.LENGTH_SHORT).show();

            // Create "students" table if not exists
            String studentsTable = "CREATE TABLE IF NOT EXISTS students (recID INTEGER PRIMARY KEY AUTOINCREMENT, firstName TEXT, lastName TEXT, faculty TEXT, department TEXT, advisor TEXT, gender TEXT);";
            database.execSQL(studentsTable);

            Toast.makeText(requireContext(), "Students Table is created", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    public void add() {
        try {
            if (database == null || !database.isOpen()) {
                database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
            }

            String firstName = editTextFirstNameRegistration.getText().toString();
            String lastName = editTextLastNameRegistration.getText().toString();
            String faculty = editTextFacultyRegistration.getText().toString();
            String department = editTextDepartmentRegistration.getText().toString();
            String advisor = editTextAdvisorRegistration.getText().toString();

            // Get the selected gender from the radio group
            int selectedRadioButtonId = radioGroupGenderRegistration.getCheckedRadioButtonId();
            RadioButton selectedRadioButton = getView().findViewById(selectedRadioButtonId);
            String gender = selectedRadioButton != null ? selectedRadioButton.getText().toString() : "";

            String sql = "INSERT INTO students (firstName, lastName, faculty, department, advisor, gender) VALUES ('" + firstName + "', '" + lastName + "', '" + faculty + "', '" + department + "', '" + advisor + "', '" + gender + "');";
            database.execSQL(sql);

            Toast.makeText(requireContext(), "Student added", Toast.LENGTH_SHORT).show();

            editTextFirstNameRegistration.setText("");
            editTextLastNameRegistration.setText("");
            editTextFacultyRegistration.setText("");
            editTextDepartmentRegistration.setText("");
            editTextAdvisorRegistration.setText("");
            radioGroupGenderRegistration.clearCheck();
        } catch (SQLException e) {
            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void read() {
        try {
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);

            String sql = "select * from students;";
            Cursor cursor = database.rawQuery(sql, null);

            ArrayList<String> students = new ArrayList<>();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, students);

            while (cursor.moveToNext()) {
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow("firstName"));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow("lastName"));
                String gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
                String faculty = cursor.getString(cursor.getColumnIndexOrThrow("faculty"));
                String department = cursor.getString(cursor.getColumnIndexOrThrow("department"));
                String advisor = cursor.getString(cursor.getColumnIndexOrThrow("advisor"));

                students.add(firstName + " " + lastName + " - " + gender + " - " + department + " - " + advisor);
            }

            listViewRegistration.setAdapter(adapter);
            database.close();
        } catch (SQLException e) {
            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void delete() {
        try {
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
            String firstName = editTextFirstNameRegistration.getText().toString();
            String deleteStudent = "DELETE from students WHERE firstName = '" + firstName + "';";
            database.execSQL(deleteStudent);
            Toast.makeText(requireContext(), "Student deleted", Toast.LENGTH_SHORT).show();
            editTextFirstNameRegistration.setText("");
            database.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void update() {
        try {
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
            String firstName = editTextFirstNameRegistration.getText().toString();
            String faculty = editTextFacultyRegistration.getText().toString();
            String department = editTextDepartmentRegistration.getText().toString();
            String advisor = editTextAdvisorRegistration.getText().toString();
            String updateAdmin = "UPDATE students SET advisor = '" + advisor + "' WHERE firstName = '" + firstName + "';";
            database.execSQL(updateAdmin);
            Toast.makeText(requireContext(), "Admin updated", Toast.LENGTH_SHORT).show();
            editTextFirstNameRegistration.setText("");
            editTextAdvisorRegistration.setText("");
            database.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}