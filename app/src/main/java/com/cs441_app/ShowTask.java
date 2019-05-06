package com.cs441_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ShowTask extends AppCompatActivity {

    private Button btnGoToMain;

    private TextView txtTitle;
    private TextView txtDescription;
    private TextView txtLocation;
    private TextView txtTime;
    private TextView txtCategory;
    private TextView txtDate;

    private static Task task;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_task);

        txtTitle = findViewById(R.id.txtTitleShowTask);
        txtDescription = findViewById(R.id.txtDescriptionShowTask);
        txtLocation = findViewById(R.id.txtLocationShowTask);
        txtTime = findViewById(R.id.txtTimeShowTask);
        txtCategory = findViewById(R.id.txtCategoryShowTask);
        txtDate = findViewById(R.id.txtDateShowTask);


        //Gets the time
        long startHour = task.getHour();
        long startMin = task.getMin();

        String TaskTime = (startHour + ":" + startMin);

        //Gets the day
        long dayTask = task.getDay();
        long monthTask = task.getMonth();
        long yearTask = task.getYear();

        String dateTask = (monthTask + "/" + dayTask + "/" + yearTask);

        //For the category
        String categoryOutput;
        long category = task.getCategory();
        
        if (category == 0) {
            categoryOutput = "School";
            txtTitle.setTextColor(getResources().getColor(R.color.red));
        } else if (category == 1) {
            categoryOutput = "Work";
            txtTitle.setTextColor(getResources().getColor(R.color.blue));
        }
        else if (category == 2) {
            categoryOutput = "Family";
            txtTitle.setTextColor(getResources().getColor(R.color.green));
        }
        else {
            categoryOutput = "Friends";
            txtTitle.setTextColor(getResources().getColor(R.color.yellow));

        }


        //setText functions
        txtDate.setText(dateTask);
        txtTitle.setText(task.getTitle());
        txtDescription.setText(task.getDescription());
        txtLocation.setText(task.getLocation());
        txtTitle.setText(task.getTitle());
        txtTime.setText(TaskTime);
        txtCategory.setText(categoryOutput);

        //-----------------------------------------------------------
        btnGoToMain = findViewById(R.id.btnBackToMain);
        btnGoToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentHome = new Intent(ShowTask.this,
                        MainActivity.class);
                startActivity(intentHome);
            }
        });

    }

    public static void getTaskFromMain(Task t) {
        task = t;
    }
}
