package com.cs441_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MemberList extends AppCompatActivity{
    private static ListView listview;
    private static ArrayList<User> itemArray;
    private static ArrayAdapter<User> adapterArray;

    private Button exitBtn;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memberlist_layout);

        exitBtn = findViewById(R.id.btnReturnHome);

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentHome = new Intent(MemberList.this,
                        MainActivity.class);
                startActivity(intentHome);
            }
        });

        itemArray = new ArrayList();
        listview = findViewById(R.id.memberList);
        adapterArray = new ArrayAdapter<User>(MemberList.this, android.R.layout.simple_list_item_1, itemArray);
        listview.setAdapter(adapterArray);

        MainActivity.dh.readGroupMembers(MainActivity.group.getGroupID());
    }

    public static void updateMembers(ArrayList<User> list){
        adapterArray.clear();
        for(int i = 0; i < list.size(); i++){
            adapterArray.add(list.get(i));
        }
        adapterArray.notifyDataSetChanged();
    }

}
