package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.TabHost.TabSpec;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView msgBox;
    private EditText editName, editLastName;
    private EditText dataToAddEditText;
    private EditText dataToDeleteEditText;
    private EditText dataToUpdateEditText;
    private EditText dataToSearchEditText;
    private SQLiteDatabase database;
    private ListView listView;
    private String path;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        msgBox = findViewById(R.id.msgBox);
        editName = findViewById(R.id.editName);
        dataToUpdateEditText = findViewById(R.id.dataToUpdateEditText);
        dataToAddEditText = findViewById(R.id.dataToAddEditText);
        dataToDeleteEditText = findViewById(R.id.dataToDeleteEditText);
        dataToSearchEditText = findViewById(R.id.dataToSearchEditText);
        editLastName = findViewById(R.id.editLastName);
        listView = findViewById(R.id.listView);

        File myDbPath = getApplication().getFilesDir();
        path = myDbPath + "/" + "cmpe408"; // name of the database
        msgBox.setText(path);

        dbHelper = new DatabaseHelper(this);
        database = dbHelper.getWritableDatabase();

        // Create tabs
        TabHost tabHost = findViewById(android.R.id.tabhost);
        tabHost.setup();

        TabSpec spec = tabHost.newTabSpec("Administration");
        spec.setContent(R.id.adminLayout);
        spec.setIndicator("Administration");
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("Registration");
        spec.setContent(R.id.generalLayout);
        spec.setIndicator("Registration");
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("Registered Students");
        spec.setContent(R.id.registeredStudentsLayout);
        spec.setIndicator("Registered Students");
        tabHost.addTab(spec);

        // Create our database
        // Create our database
        try {
            if (!databaseExist()) {
                // If we don't have the database
                database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
                Toast.makeText(getApplication(), "Database is created", Toast.LENGTH_LONG).show();

                // Create table for student
                String studentTable = "CREATE TABLE student (recID INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, surname TEXT);";
                database.execSQL(studentTable);
                Toast.makeText(getApplication(), "Student table is created", Toast.LENGTH_LONG).show();

                // Create table for administration
                String adminTable = "CREATE TABLE faculties_departments_table (column1 TEXT, column2 TEXT);";
                database.execSQL(adminTable);
                Toast.makeText(getApplication(), "Administration table is created", Toast.LENGTH_LONG).show();

                // Insert data into the student table
                String studentName = "Güney Berkay";
                String studentLastName = "ATEŞ";
                String studentInsertQuery = "INSERT INTO student (name, surname) VALUES ('" + studentName + "', '" + studentLastName + "')";
                database.execSQL(studentInsertQuery);
                Toast.makeText(getApplication(), "Student data is inserted", Toast.LENGTH_LONG).show();

                // Insert data into the administration table
                String adminData = "Sample Data";
                String adminInsertQuery = "INSERT INTO faculties_departments_table (column1, column2) VALUES ('" + adminData + "', 'Another Data')";
                database.execSQL(adminInsertQuery);
                Toast.makeText(getApplication(), "Administration data is inserted", Toast.LENGTH_LONG).show();

                msgBox.setText("We have data in the tables");
            } else {
                Toast.makeText(getApplication(), "We have a database already", Toast.LENGTH_LONG).show();
            }
        } catch (SQLException e) {
            msgBox.setText(e.getMessage());
        }

    }

    // Helper method to check if we have a database
    private boolean databaseExist() {
        File dbFile = new File(path);
        return dbFile.exists();
    }

    // Read from the database and display it on the ListView
    public void read(View v) {
        database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        String data = "select * from student";
        Cursor cursor = database.rawQuery(data, null);
        ArrayList<String> students = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, students);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String surname = cursor.getString(cursor.getColumnIndexOrThrow("surname"));
            String result = name + "   " + surname;
            students.add(result);
        }
        listView.setAdapter(adapter);
        database.close();
    }

    // Insert into the database
    public void write(View v) {
        try {
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);

            // Check if editName and editLastName are not null
            if (editName != null && editLastName != null) {
                String name = editName.getText().toString();
                String lastName = editLastName.getText().toString();
                String input = "insert into student (name, surname) values ('" + name + "', '" + lastName + "')";
                database.execSQL(input);
                Toast.makeText(getApplication(), "Data inserted", Toast.LENGTH_LONG).show();

                // Reset fields
                editName.setText("");
                editLastName.setText("");
            } else {
                // Handle the case when editName or editLastName is null
                // Display an error message or take appropriate action
            }

            database.close();
        } catch (SQLException e) {
            msgBox.setText(e.getMessage());
        }
    }

    // Delete from the database
    public void delete(View v) {
        try {
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);

            // Check if editLastName is not null
            if (editLastName != null) {
                String lastName = editLastName.getText().toString();
                String remove = "delete from student where surname ='" + lastName + "'";
                database.execSQL(remove);
                Toast.makeText(getApplication(), lastName + " is deleted", Toast.LENGTH_LONG).show();

                // Reset fields
                editLastName.setText("");
            } else {
                // Handle the case when editLastName is null
                // Display an error message or take appropriate action
            }

            database.close();
        } catch (SQLException e) {
            msgBox.setText(e.getMessage());
        }
    }

    // Additional methods for Administration Tab
    // Administration Tab
    public void add(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Data")
                .setMessage("Enter data to add:")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Get user input from the dialog
                        EditText dataToAddEditText = new EditText(MainActivity.this);
                        dataToAddEditText.setHint("Enter data"); // Set hint if needed

                        // Set the EditText view to the builder
                        builder.setView(dataToAddEditText);

                        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Retrieve user input from the dynamically added EditText
                                String dataToAdd = dataToAddEditText.getText().toString();

                                // Check if the input is not empty
                                if (!dataToAdd.isEmpty()) {
                                    // Execute SQL INSERT statement to add data to the administration table
                                    String insertQuery = "INSERT INTO faculties_departments_table (column1) VALUES ('" + dataToAdd + "')";
                                    database.execSQL(insertQuery);

                                    // Display appropriate messages to the user
                                    Toast.makeText(MainActivity.this, "Data added successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Display an error message if the input is empty
                                    Toast.makeText(MainActivity.this, "Please enter valid data", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        builder.setNegativeButton("Cancel", null);

                        // Show the AlertDialog
                        builder.show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    public void deleteAdmin(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Data")
                .setMessage("Enter data to delete:")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Get user input from the dialog
                        EditText dataToDeleteEditText = findViewById(R.id.dataToDeleteEditText);
                        String dataToDelete = dataToDeleteEditText.getText().toString();

                        // Execute SQL DELETE statement to remove data from the administration table
                        String deleteQuery = "DELETE FROM faculties_departments_table WHERE column1 = '" + dataToDelete + "'";
                        database.execSQL(deleteQuery);

                        // Display appropriate messages to the user
                        Toast.makeText(MainActivity.this, "Data deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    public void update(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Data")
                .setMessage("Enter data to update:")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Get user input from the dialog
                        EditText dataToUpdateEditText = findViewById(R.id.dataToUpdateEditText); // Replace with your actual EditText ID
                        String dataToUpdate = dataToUpdateEditText.getText().toString();

                        // Execute SQL UPDATE statement to modify data in the administration table
                        String updateQuery = "UPDATE faculties_departments_table SET column2 = 'new_value' WHERE column1 = '" + dataToUpdate + "'";
                        database.execSQL(updateQuery);

                        // Display appropriate messages to the user
                        Toast.makeText(MainActivity.this, "Data updated successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    public void search(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Search Data")
                .setMessage("Enter data to search:")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Get user input from the dialog
                        EditText dataToSearchEditText = findViewById(R.id.dataToSearchEditText); // Replace with your actual EditText ID
                        String dataToSearch = dataToSearchEditText.getText().toString();

                        // Execute SQL SELECT statement to retrieve data from the administration table
                        String searchQuery = "SELECT * FROM faculties_departments_table WHERE column1 = '" + dataToSearch + "'";
                        Cursor cursor = database.rawQuery(searchQuery, null);

                        // Process the cursor data and display the search results to the user
                        ArrayList<String> searchResults = new ArrayList<>();
                        while (cursor.moveToNext()) {
                            // Process cursor data
                            // Add results to the searchResults ArrayList
                            String result = cursor.getString(cursor.getColumnIndexOrThrow("column1")) + "   " +
                                    cursor.getString(cursor.getColumnIndexOrThrow("column2"));
                            searchResults.add(result);
                        }
                        cursor.close();

                        // Display the search results to the user (e.g., using a ListView)
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, searchResults);
                        listView.setAdapter(adapter);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Additional methods for Registration Tab
    public void register(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Register Student")
                .setMessage("Enter student details:")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Get user input from the dialog
                        EditText nameEditText = findViewById(R.id.nameEditText); // Replace with your actual EditText ID
                        String name = nameEditText.getText().toString();

                        EditText lastNameEditText = findViewById(R.id.lastNameEditText); // Replace with your actual EditText ID
                        String lastName = lastNameEditText.getText().toString();

                        // Generate a 10-digit random Student ID
                        String studentID = generateRandomStudentID();

                        EditText genderEditText = findViewById(R.id.genderEditText); // Replace with your actual EditText ID
                        String gender = genderEditText.getText().toString();

                        EditText facultyEditText = findViewById(R.id.facultyEditText); // Replace with your actual EditText ID
                        String faculty = facultyEditText.getText().toString();

                        EditText departmentEditText = findViewById(R.id.departmentEditText); // Replace with your actual EditText ID
                        String department = departmentEditText.getText().toString();

                        EditText advisorEditText = findViewById(R.id.advisorEditText); // Replace with your actual EditText ID
                        String advisor = advisorEditText.getText().toString();

                        // Execute SQL INSERT statement to add student data to the database
                        String insertQuery = "INSERT INTO student_table (name, lastName, studentID, gender, faculty, department, advisor) " +
                                "VALUES ('" + name + "', '" + lastName + "', '" + studentID + "', '" + gender + "', '" + faculty + "', '" + department + "', '" + advisor + "')";
                        database.execSQL(insertQuery);

                        // Display appropriate messages to the user
                        Toast.makeText(MainActivity.this, "Student registered successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private String generateRandomStudentID() {
        // Implement your logic to generate a random 10-digit student ID
        // For simplicity, you can use a simple random number generator or any other method
        return "1234567890"; // Replace with your actual implementation
    }

    public void cancel(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cancel Registration")
                .setMessage("Are you sure you want to cancel?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Implement cancel logic (e.g., reset registration fields)
                        // Display appropriate messages to the user
                        Toast.makeText(MainActivity.this, "Registration canceled", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Additional methods for Registered Students Tab
    public void displayStudentDetails(View v) {
        // Implement method to display student details in a popup
    }


    // Menu handling
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            // Display a dialog with a short description about your app
            showAboutDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAboutDialog() {
        // Create and show a dialog with your app's description
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About My App")
                .setMessage("Your app's short description goes here.")
                .setPositiveButton("OK", null)
                .show();
    }
}
