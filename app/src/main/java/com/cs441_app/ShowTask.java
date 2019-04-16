package com.cs441_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ShowTask extends AppCompatActivity {

    private Button btnGoToMain;

    private TextView txtTitle;
    private TextView txtDescription;
    private TextView txtLocation;
    private TextView txtTime;
    private TextView txtCategory;
    private TextView txtDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_task);

        txtTitle=findViewById(R.id.txtTitleShowTask);
        txtDescription=findViewById(R.id.txtDescriptionShowTask);
        txtLocation=findViewById(R.id.txtLocationShowTask);
        txtTime=findViewById(R.id.txtTimeShowTask);
        txtCategory=findViewById(R.id.txtCategoryShowTask);
        txtDate=findViewById(R.id.txtDateShowTask);

        //Used for test purposes to fill the object with crap
        //These will be gotten from the object of the selected
        long Day= 7;
        long Month= 10;
        long Year= 2019;
        long Hour= 5;
        long Min= 30;
        int Category= 0;
        String Title= "Test Title";
        String Description= "This is a test description\nHopefully with two lines\nLine 3\nLine 4\nLine 5\nLine 3\nLine 4\nLine 5\nLine 3\nLine 4\nLine 5\nLine 3\nLine 4\nLine 5\nLine 3\nLine 4\nLine 5";
        double IDt= 123456789;
        String Location= "San Marcos, CA";
        boolean Share= true;
        User UserTask=new User();

        Task testTask = new Task( Day, Month, Year, Hour, Min, Category, Title, Description, Location, Share, UserTask);



        //Gets the time
        long startHour=testTask.getHour();
        long startMin=testTask.getMin();

        String TaskTime = (startHour + ":" + startMin);

        //Gets the day
        long dayTask = testTask.getDay();
        long monthTask = testTask.getMonth();
        long yearTask = testTask.getYear();

        String dateTask = (monthTask + "/" + dayTask + "/" +yearTask);

        //For the category
        String cateogoryOutput;
        long category = testTask.getCategory();
        if(category==0) cateogoryOutput="School";
        else cateogoryOutput = "Void";

        //setText functions
        txtDate.setText(dateTask);
        txtTitle.setText(testTask.getTitle());
        txtDescription.setText(testTask.getDescription());
        txtLocation.setText(testTask.getLocation());
        txtTitle.setText(testTask.getTitle());
        txtTime.setText(TaskTime);
        txtCategory.setText(cateogoryOutput);

        //-----------------------------------------------------------
        btnGoToMain=findViewById(R.id.btnBackToMain);
        btnGoToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentHome = new Intent(ShowTask.this,
                        MainActivity.class);
                startActivity(intentHome);
            }
        });

    }
}
