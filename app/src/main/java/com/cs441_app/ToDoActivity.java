package com.cs441_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ToDoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private EditText etItem;
    private Button btnToDoAdd;
    private ListView lvItem;

    private ArrayList<String> itemArray;
    private ArrayAdapter<String> adapterToDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        etItem = findViewById(R.id.etxtToDoAdd);
        lvItem = findViewById(R.id.listToDo);
        btnToDoAdd = findViewById(R.id.btnToDoAddTask);

        itemArray = ToDoWrite.readData(ToDoActivity.this);
        adapterToDo = new ArrayAdapter<String>(ToDoActivity.this, android.R.layout.simple_list_item_1, itemArray);
        lvItem.setAdapter(adapterToDo);

        lvItem.setOnItemClickListener(ToDoActivity.this);

        //This will be removed once the navigation bar is implemented
        Button returnHome;
        returnHome = findViewById(R.id.btnToDoGoHome);
        returnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentHome = new Intent(ToDoActivity.this,MainActivity.class);
                startActivity(intentHome);
            }
        });


        btnToDoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnToDoAddTask:
                        String itemNew = etItem.getText().toString();
                        if (itemNew.length()==0);
                        else{
                            adapterToDo.add(itemNew);
                            etItem.setText("");
                            ToDoWrite.writeList(itemArray, ToDoActivity.this);
                            Toast.makeText(ToDoActivity.this, "Item Added to the List", Toast.LENGTH_SHORT).show();
                            break;
                        }
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        itemArray.remove(position);
        adapterToDo.notifyDataSetChanged();
        Toast.makeText(this, "Item was Completed!", Toast.LENGTH_SHORT).show();
    }
}