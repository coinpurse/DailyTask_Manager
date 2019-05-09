package com.cs441_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {

    Button menuYourCalendar;// = findViewById(R.id.btnYourCalendar);
    Button menuGroup;// = findViewById(R.id.btnGroup);
    Button menuChat;// = findViewById(R.id.btnChat);
    Button menuCategories;// = findViewById(R.id.btnCategories);
    Button menuAccount;// = findViewById(R.id.btnAccount);
    Button menuToDo;// = findViewById(R.id.btnToDo);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //---------
        menuYourCalendar = findViewById(R.id.btnYourCalendar);
        menuGroup = findViewById(R.id.btnGroup);
        menuChat = findViewById(R.id.btnChat);
        menuCategories = findViewById(R.id.btnCategories);
        menuAccount = findViewById(R.id.btnAccount);
        menuToDo = findViewById(R.id.btnToDo);
        //---------

     menuYourCalendar.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent intentMain = new Intent(Menu.this,MainActivity.class);
             startActivity(intentMain);
         }
     });
     //----------------------------
        //FOR THE GROUP ACTIVITY
        /*
        menuGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGroup = new Intent(Menu.this,Group.class);
                startActivity(intentGroup);
            }
        });
        */
      //--------------------------
        //FOR THE CHAT ACTIVITY
        /*
        menuChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentChat = new Intent(Menu.this,Chat.class);
                startActivity(intentChat);
            }
        });
        */
    //-------------------------------
        menuCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCategories = new Intent(Menu.this,Categories.class);
                startActivity(intentCategories);
            }
        });
    //-----------------------------------
        menuToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToDo = new Intent(Menu.this,ToDoActivity.class);
                startActivity(intentToDo);
            }
        });
     //--------------------------------------
        //FOR THE ACCOUNT ACTIVITY
        /*
        menuAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAccount = new Intent(Menu.this,Account.class);
                startActivity(intentAccount);
            }
        });
        */

    }//end of onClick
}
