package com.cs441_app;



import android.content.res.Configuration;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
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

    public static Group group;
    public static boolean groupview;

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
    private Button btnMenu;
    //private Button btnGoToTask;
    //private Button btnTodo;
    //-----------------------

    private static int day;
    private static int month;
    private static int year;
    private static boolean calendarcreate;

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_task, menu);
        if(groupview){
            getMenuInflater().inflate(R.menu.view_members, menu);
            getMenuInflater().inflate(R.menu.messenger, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_add_task:
                intent = new Intent(MainActivity.this,
                        AddTask.class);
                startActivity(intent);
                return true;
            case R.id.action_nav_drawer:
                return true;
            case R.id.action_view_members:
                intent = new Intent(MainActivity.this,
                        MemberList.class);
                startActivity(intent);
                return true;
            case R.id.action_messenger:
                intent = new Intent(MainActivity.this,
                        Messenger.class);
                startActivity(intent);
                return true;
                default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        um = UserManager.getInstance();

            Toolbar myToolbar = findViewById(R.id.my_toolbar);
            if(groupview)
                myToolbar.setTitle(group.getName() + "'s Calendar");
            else
                myToolbar.setTitle("My Calendar");
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            setSupportActionBar(myToolbar);


            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            um.populateNavList(MainActivity.this, getWindow().getDecorView().getRootView());
            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, myToolbar, R.string.app_name, R.string.app_name);
            drawerLayout.addDrawerListener(actionBarDrawerToggle);

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
            if(groupview)
                dh.readBlock(user.getUserID(), group.getGroupID(), dh.generateBlockID(new Task(day,month,year)), new Group(), false);
            else
                um.populateCalendar(dh.generateBlockID(new Task(day,month,year)));
        }



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
                if(groupview)
                    dh.readBlock(user.getUserID(), group.getGroupID(), dh.generateBlockID(new Task(day,month,year)), new Group(), false);
                else
                    um.populateCalendar(dh.generateBlockID(new Task(dayOfMonth,month,year)));
            }
        };
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
        adapterArray.notifyDataSetChanged();

    }

    public static void onUserCreation(){
        if(groupview)
            dh.readBlock(user.getUserID(), group.getGroupID(), dh.generateBlockID(new Task(day,month,year)), new Group(), false);
        else
            um.populateCalendar(dh.generateBlockID(new Task(day,month,year)));
        login = true;
        usercreated = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FBuser = FirebaseAuth.getInstance().getCurrentUser();
                um = UserManager.getInstance();
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

    public static boolean isUsercreated() {
        return usercreated;
    }

    public static void setUsercreated(boolean usercreated) {
        MainActivity.usercreated = usercreated;
    }

    public static boolean isLogin() {
        return login;
    }

    public static void setLogin(boolean login) {
        MainActivity.login = login;
    }
}



