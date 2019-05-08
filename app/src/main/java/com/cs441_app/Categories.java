package com.cs441_app;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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


    //NEW
    InternalDatabase myDb;
//

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

    public ArrayList<String> colorArray = new ArrayList<String>();
    int changeMe = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        redText = findViewById(R.id.etRed);
        blueText = findViewById(R.id.etBlue);
        greenText = findViewById(R.id.etGreen);
        yellowText = findViewById(R.id.etYellow);



        //NEW
        myDb = new InternalDatabase(this);

        Cursor data = myDb.getAllData();

       // boolean isInsert = myDb.insertDara(red, blue, green, yellow);


        colorArray = new ArrayList<String>();

        int i = 0;
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



        redText.setText(colorArray.get(0));
        blueText.setText(colorArray.get(1));
        greenText.setText(colorArray.get(2));
        yellowText.setText(colorArray.get(3));


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

                boolean isInsert = myDb.insertDara(red, blue, green, yellow);
                Toast.makeText(Categories.this, "Categories were updated", Toast.LENGTH_SHORT).show();

            }


        });

    }//end of onCreate



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
