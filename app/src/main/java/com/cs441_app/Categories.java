package com.cs441_app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Categories extends AppCompatActivity {

    public static final String NAMEOFFILE = "Colors.dat";


    private Button openMenu;
    private Button updateColors;

    private EditText redText;
    private EditText blueText;
    private EditText greenText;
    private EditText yellowText;

    String red;
    String blue;
    String green;
    String yellow;

    public ArrayList<String> colorArray=new ArrayList<String>();
    int changeMe=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);


        colorArray = getCategories(Categories.this);

        redText = findViewById(R.id.etRed);
        blueText = findViewById(R.id.etBlue);
        greenText = findViewById(R.id.etGreen);
        yellowText = findViewById(R.id.etYellow);


        red = colorArray.get(0);
        blue = colorArray.get(1);
        green = colorArray.get(2);
        yellow = colorArray.get(3);


        redText.setText(red);
        blueText.setText(blue);
        greenText.setText(green);
        yellowText.setText(yellow);


        openMenu = findViewById(R.id.btnMenuCat);
        openMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMenu = new Intent(Categories.this, Menu.class);
                startActivity(intentMenu);
            }
        });//end of onClick
        //--------------------------
        updateColors = findViewById(R.id.btnUpdateCategories);
        updateColors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                red = redText.getText().toString();
                blue = blueText.getText().toString();
                green = greenText.getText().toString();
                yellow = yellowText.getText().toString();

                updateCategories(red, blue, green, yellow, Categories.this);
                Toast.makeText(Categories.this, "Categories were updated", Toast.LENGTH_SHORT).show();

            }


        });

    }//end of onCreate



    public ArrayList<String> getCategories(Context context){
        if(changeMe==0){
            colorArray.add("Work");
            colorArray.add("School");
            colorArray.add("Family");
            colorArray.add("Friends");
        }
        changeMe=1;
        return colorArray;
    }

    public void updateCategories (String s1, String s2, String s3, String s4, Context context){
        colorArray.clear();
        colorArray.add(s1);
        colorArray.add(s2);
        colorArray.add(s3);
        colorArray.add(s4);
    }

    /*
    //Used to update the array list of color categories

    public void updateCategories(String one, String two, String three, String four, Context context) {
        try {
            ArrayList<String> catArray = null;
            FileOutputStream fos = context.openFileOutput(NAMEOFFILE, MODE_PRIVATE);
            ObjectOutput oos = new ObjectOutputStream(fos);
            catArray.add(one);
            catArray.add(two);
            catArray.add(three);
            catArray.add(four);
            oos.writeObject(catArray);
            oos.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public ArrayList<String> getCategories(Context context) {
        ArrayList<String> catArray = null;
        try {
            FileInputStream fis = context.openFileInput(NAMEOFFILE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            if (ois.readObject()==null){
                catArray.add("School");
                catArray.add("Family");
                catArray.add("Work");
                catArray.add("Friends");
            }
            else
            catArray = (ArrayList<String>) ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        return catArray;
    }
    */

}
