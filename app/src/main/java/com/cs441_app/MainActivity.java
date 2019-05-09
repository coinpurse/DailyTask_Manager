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

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    public static Database_Handler dh;
    public static UserManager um;
    public static User user;
    private static FirebaseUser FBuser;
    private static boolean usercreated;
    private static boolean login;

    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 500;

    // Github Test
    // Task view stuff
    private static ListView listview;
    private static ArrayList<Task> itemArray;
    private static ArrayAdapter<Task> adapterArray;
    private static boolean itemarraycreated;

    //For getting the date
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    //-------------------

    //Buttons
    private Button btnAddTask;
    private Button btnMenu;
    //private Button btnGoToTask;
    //private Button btnTodo;
    //-----------------------

    private static int day;
    private static int month;
    private static int year;
    private static boolean calendarcreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnMenu=findViewById(R.id.btnMenuMain);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMenu = new Intent(MainActivity.this,Menu.class);
                startActivity(intentMenu);
            }
        });


        //------------------------------------------------------MAKE THIS INTO A FUNCTION
        mDisplayDate = (TextView) findViewById(R.id.tvDate);

        // First Time calendar creation
        if(!calendarcreate) {
            Calendar cal = Calendar.getInstance();
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);
            day = cal.get(Calendar.DAY_OF_MONTH);
            month = month + 1;
            calendarcreate = true;
        }


        String date = month + "/" + day + "/" + year;
        mDisplayDate.setText(date);

        // Task List
        if(!itemarraycreated){
            itemArray = new ArrayList();
            itemarraycreated = true;
        }
        listview = findViewById(R.id.TaskList);
        adapterArray = new ArrayAdapter<Task>(MainActivity.this, android.R.layout.simple_list_item_1, itemArray);
        listview.setAdapter(adapterArray);
        listview.setOnItemClickListener(MainActivity.this);

        // User sign-in
        if(!usercreated) {
            //user = dh.createUser("Tyler");
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build());
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
            usercreated = true;
        }

        if(login){
            um.populateCalendar(dh.generateBlockID(new Task(day,month,year)));
        }


        // Get task list for the current date
        //dh.readBlock(user.getUserID(),"", dh.generateBlockID(new Task(day,month,year)));

        //um.populateCalendar(dh.generateBlockID(new Task(day,month,year)));
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


                DatePickerDialog dialog = new DatePickerDialog(
                        MainActivity.this,
                        android.R.style.Theme_DeviceDefault_Dialog,//not sure if this is the correct theme
                        mDateSetListener,
                        year, month - 1, day);

                //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int Year, int Month, int dayOfMonth) {
                //This is where it would call the server to get the data for the day here
                day = dayOfMonth;
                month = Month + 1;
                year = Year;
                String date = month + "/" + dayOfMonth + "/" + year;
                mDisplayDate.setText(date);
                //dh.readBlock(user.getUserID(),"", dh.generateBlockID(new Task(dayOfMonth,month,year)));
                um.populateCalendar(dh.generateBlockID(new Task(dayOfMonth,month,year)));
            }
        };
        //-----------------------------------------------------------------------------

        /*
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
        */
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ShowTask.getTaskFromMain(itemArray.get(position));
        Intent intentGoToTask = new Intent(MainActivity.this,
                ShowTask.class);
        startActivity(intentGoToTask);
    }

    public static void updateTaskList(ArrayList<Task> list){
        adapterArray.clear();
        for(int i = 0; i < list.size(); i++){
            adapterArray.add(list.get(i));
        }

    }

    public static void onUserCreation(){
        um.populateCalendar(dh.generateBlockID(new Task(day,month,year)));
        login = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FBuser = FirebaseAuth.getInstance().getCurrentUser();
                um = new UserManager();
                dh = new Database_Handler();
                user = new User(FBuser.getUid(),FBuser.getDisplayName());
                dh.createUser(user);
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                List<AuthUI.IdpConfig> providers = Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build(),
                        new AuthUI.IdpConfig.GoogleBuilder().build());
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build(),
                        RC_SIGN_IN);
                usercreated = true;
            }
        }
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


