package com.cs441_app;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private UserManager um;


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                Intent intentAddTask = new Intent(ToDoActivity.this,
                        AddTask.class);
                startActivity(intentAddTask);
                return true;
            case R.id.action_nav_drawer:
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
        setContentView(R.layout.activity_to_do);

        um = UserManager.getInstance();

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle("ToDo List");
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setSupportActionBar(myToolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        um.populateNavList(ToDoActivity.this,getWindow().getDecorView().getRootView());
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, myToolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        etItem = findViewById(R.id.etxtToDoAdd);
        lvItem = findViewById(R.id.listToDo);
        btnToDoAdd = findViewById(R.id.btnToDoAddTask);

        itemArray = ToDoWrite.readData(ToDoActivity.this);
        adapterToDo = new ArrayAdapter<String>(ToDoActivity.this, android.R.layout.simple_list_item_1, itemArray);
        lvItem.setAdapter(adapterToDo);

        lvItem.setOnItemClickListener(ToDoActivity.this);



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