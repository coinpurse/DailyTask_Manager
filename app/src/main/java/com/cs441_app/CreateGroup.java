package com.cs441_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class CreateGroup extends AppCompatActivity{

    private String titleTask = null;
    private EditText txtTitle;
    private Button btnExit;
    private Button btnSave;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creategroup_layout);

        txtTitle = findViewById(R.id.txtTitleTask);

        btnExit = findViewById(R.id.btnReturnHome);
        btnSave = findViewById(R.id.btnSaveTask);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentHome = new Intent(CreateGroup.this,
                        GroupList.class);
                startActivity(intentHome);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //These get the user inputs and stores them into variables to pass to the server
                titleTask = txtTitle.getText().toString();

                //This checks to make sure the input is valid, if not will not allow the user to save to database
                if (titleTask == "" || titleTask == null)
                    Toast.makeText(CreateGroup.this, "Incorrect input, please try again", Toast.LENGTH_SHORT).show();
                else {
                    MainActivity.dh.createGroup(MainActivity.user, titleTask);
                    Toast.makeText(CreateGroup.this, "Group has been created", Toast.LENGTH_LONG).show();
                    Intent intentHome = new Intent(CreateGroup.this,
                            GroupList.class);
                    startActivity(intentHome);
                }
            }
        });
    }


}
