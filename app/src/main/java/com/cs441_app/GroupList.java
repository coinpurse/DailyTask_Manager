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
import android.widget.ListView;

import java.util.ArrayList;

public class GroupList extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private UserManager um;

    private static ListView listview;
    private static ArrayList<Group> itemArray;
    private static GroupAdapter adapterArray;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                Intent intentAddTask = new Intent(GroupList.this,
                        CreateGroup.class);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grouplist_layout);

        um = UserManager.getInstance();

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle("My Groups");
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setSupportActionBar(myToolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        um.populateNavList(GroupList.this, getWindow().getDecorView().getRootView());
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, myToolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        itemArray = new ArrayList();

        adapterArray = new GroupAdapter(itemArray, this);
        listview = findViewById(R.id.GroupList);
        listview.setAdapter(adapterArray);
        listview.setOnItemClickListener(GroupList.this);

        MainActivity.dh.readGroups_ByParticipation(MainActivity.user.getUserID());
    }

    public static void getResults(ArrayList<Group> list){
        adapterArray.clear();
        for(int i = 0; i < list.size(); i++){
            adapterArray.add(list.get(i));
        }
        adapterArray.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
