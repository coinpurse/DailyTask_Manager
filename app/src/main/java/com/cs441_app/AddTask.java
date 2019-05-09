package com.cs441_app;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class AddTask extends AppCompatActivity {

    private Button btnExit;
    private Button btnSave;

    private EditText txtTitle;
    private EditText txtDescription;
    private EditText txtLocation;



    //For setting the date
    public int year;
    public int month;
    public int day;

    private int yearPass = 0;
    private int monthPass = 0;
    private int dayPass = 0;

    private Button chooseStartTime;
    private Button chooseEndTime;
    private TimePickerDialog timePickerDialog;
    private TimePickerDialog timePickerDialogEnd;
    private Calendar calendarTime;

    private int currentHour;
    private int currentMinute;
    private String amPm;

    private int startHour = 0;
    private int startMinute = 0;
    private String startAmPm;
    private int endHour = 0;
    private int endMinute = 0;
    private String endAmPm;

    private String titleTask = null;
    private String descriptionTask = null;
    private String locationTask = null;
    private int categoryTask; //change this later on. Going to stay 0 for now
    private boolean isShareable=true;

    InternalDatabase myDB;
    ArrayList<String> colorArray;


    private Button mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        myDB = new InternalDatabase(this);
        colorArray = new ArrayList<>();

        int i = 0;
        Cursor data = myDB.getAllData();
        while (data.moveToNext()) {
            i++;
            colorArray = new ArrayList<>();
            String c0 = (data.getString(0));
            String c1 = (data.getString(1));
            String c2 = (data.getString(2));
            String c3 = (data.getString(3));

            colorArray.add(c0);
            colorArray.add(c1);
            colorArray.add(c2);
            colorArray.add(c3);

        }

        if (i==0) {
            colorArray.add("Work");
            colorArray.add("School");
            colorArray.add("Friends");
            colorArray.add("Family");
        }

        txtTitle = findViewById(R.id.txtTitleTask);
        txtDescription = findViewById(R.id.txtDescriptionTask);
        txtLocation = findViewById(R.id.txtLocationTask);

        btnExit = findViewById(R.id.btnReturnHome);
        btnSave = findViewById(R.id.btnSaveTask);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentHome = new Intent(AddTask.this,
                        MainActivity.class);
                startActivity(intentHome);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //These get the user inputs and stores them into variables to pass to the server
                titleTask = txtTitle.getText().toString();
                descriptionTask = txtDescription.getText().toString();
                locationTask = txtLocation.getText().toString();


                //Also set the code to add the object to the server,
                //It should call getInfo() which will store all of the inputs into strings or ints


                //This checks to make sure the input is valid, if not will not allow the user to save to database
                if (dayPass == 0 || monthPass == 0 || yearPass == 0 || titleTask == null || descriptionTask == null || startMinute == 0 || startHour == 0)
                    Toast.makeText(AddTask.this, "Incorrect input, please try again", Toast.LENGTH_SHORT).show();
                else {
                    MainActivity.dh.writeTask(MainActivity.user.getUserID(), "", new Task(dayPass, monthPass, yearPass, startHour, startMinute, categoryTask, titleTask, descriptionTask, locationTask, false, MainActivity.user));
                    //MainActivity.dh.readBlock(MainActivity.user.getUserID(), "", "04202019");
                    Toast.makeText(AddTask.this, "Task saved to your calendar", Toast.LENGTH_LONG).show();
                    Intent intentHome = new Intent(AddTask.this,
                            MainActivity.class);
                    startActivity(intentHome);
                }

            }
        });

        //--------------------------------------------------------
        mDisplayDate = (Button) findViewById(R.id.btnDateTaskObject);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddTask.this,
                        android.R.style.Theme_DeviceDefault_Dialog,//not sure if this is the correct theme
                        mDateSetListener,
                        year, month, day);

                //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //This is where it would call the server to get the data for the day here
                month = month + 1;
                String date = month + "/" + dayOfMonth + "/" + year;
                dayPass = dayOfMonth;
                yearPass = year;
                monthPass = month;
                mDisplayDate.setText(date);
            }
        };
        //--------------------------------------------------
        chooseStartTime = findViewById(R.id.btnTimePickStart);
        chooseStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarTime = Calendar.getInstance();
                currentHour = calendarTime.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendarTime.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(AddTask.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {

                        startHour = hourOfDay;
                        startMinute = minute;

                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        if (hourOfDay == 0) {
                            hourOfDay = 12;
                        }

                        if (hourOfDay > 12) {
                            hourOfDay -= 12;
                        }
                        chooseStartTime.setText(String.format(hourOfDay + ":" + minute + amPm));
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });



//Used for the spinner
        Spinner spinner = (Spinner) findViewById(R.id.spnrAddCat);
        ArrayList<String> thisArray =new ArrayList<String>();
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,colorArray);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryTask=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                categoryTask=0;
            }
        });

        //-------------------------------------------------
        /*chooseEndTime = findViewById(R.id.btnTimePickEnd);
        chooseEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarTime = Calendar.getInstance();
                currentHour = 1 + (calendarTime.get(Calendar.HOUR_OF_DAY));
                currentMinute = calendarTime.get(Calendar.MINUTE);

                timePickerDialogEnd = new TimePickerDialog(AddTask.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {

                        endHour = hourOfDay;
                        endMinute = minute;

                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        if (hourOfDay == 0) {
                            hourOfDay = 12;
                        }
                        ;
                        if (hourOfDay > 12) {
                            hourOfDay -= 12;
                        }

                        chooseEndTime.setText(String.format(hourOfDay + ":" + minute + amPm));
                    }
                }, currentHour, currentMinute, false);
                //Add here to check to make sure this time is after the second time
                timePickerDialogEnd.show();
            }
        });
        */
    }

}

