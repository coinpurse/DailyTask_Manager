package com.cs441_app;



import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.view.*;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.TimePicker;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    public static Database_Handler dh;
    public static User user;
    private static boolean usercreated;

    private static final String TAG = "MainActivity";

    //For getting the date
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    //-------------------

    //Buttons
    private Button btnAddTask;
    private Button btnGoToTask;
    private Button btnTodo;
    //-----------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //------------------------------------------------------MAKE THIS INTO A FUNCTION
        mDisplayDate = (TextView) findViewById(R.id.tvDate);
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        month = month + 1;
        String date = month + "/" + day + "/" + year;
        mDisplayDate.setText(date);

        dh = new Database_Handler();

        if(!usercreated) {
            user = dh.createUser("Tyler");
            usercreated = true;
        }

        //----------------------------------------------------------

        btnAddTask = findViewById(R.id.btnAddObject);

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddTask = new Intent(MainActivity.this,
                        AddTask.class);
                startActivity(intentAddTask);
            }
        });

        //-----------------------------------------------------


        mDisplayDate = (TextView) findViewById(R.id.tvDate);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        MainActivity.this,
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
                mDisplayDate.setText(date);
            }
        };
        //-----------------------------------------------------------------------------
        btnGoToTask = findViewById(R.id.btnGoToTask);
        btnGoToTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGoToTask = new Intent(MainActivity.this,
                        ShowTask.class);
                startActivity(intentGoToTask);
            }
        });

        //-----------------------------------------------------------------------
        btnTodo=findViewById(R.id.btnToDoList);
        btnTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToDo = new Intent(MainActivity.this,ToDoActivity.class);
                startActivity(intentToDo);
            }
        });
    }
}
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dh = new Database_Handler();
        user = dh.createUser("Tyler");

        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_SHORT);
                Task t = new Task(16,4,2019,20,30,"This is the Title", "This is the Description","San Marcos", true, user);
                Task a = new Task(16,4,2019,22,00,"This is another Title", "This is the Description","San Marcos", true, user);
                Task b = new Task(16,4,2019,15,30,"This is a different Title", "This is the Description","San Marcos", true, user);
                Task c = new Task(16,4,2019,5,00,"This is the last Title", "This is the Description","San Marcos", true, user);
                dh.writeTask(user.getUserID(),"",t);
                dh.writeTask(user.getUserID(),"",a);
                dh.writeTask(user.getUserID(),"",b);
                dh.writeTask(user.getUserID(),"",c);

                dh.readDay(user.getUserID(),"","04162019");

                System.out.println("hello");
            }
        });


    }*/


