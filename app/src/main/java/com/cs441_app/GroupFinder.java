package com.cs441_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class GroupFinder extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private UserManager um;

    private SearchView groupsearch;

    private static ListView listview;
    private static ArrayList<Group> itemArray;
    private static ArrayAdapter<Group> adapterArray;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.add_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                Intent intentAddTask = new Intent(GroupFinder.this,
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_layout);

        um = UserManager.getInstance();

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Group Finder");
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setSupportActionBar(myToolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        um.populateNavList(GroupFinder.this, getWindow().getDecorView().getRootView());
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, myToolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        itemArray = new ArrayList();
        listview = findViewById(R.id.GroupList);
        adapterArray = new ArrayAdapter<Group>(GroupFinder.this, android.R.layout.simple_list_item_1, itemArray);
        listview.setAdapter(adapterArray);
        listview.setOnItemClickListener(GroupFinder.this);


        groupsearch = (SearchView) findViewById(R.id.GroupSearch);
        groupsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextChange(String text) {
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String text) {
                // do something when the focus of the query text field changes
                if(!text.isEmpty()){
                    System.out.println(groupsearch.getQuery().toString());
                    MainActivity.dh.readGroups_ByName(groupsearch.getQuery().toString());
                    return true;
                }
                return false;
            }
        });

    }

    public static void getResults(ArrayList<Group> list){
        adapterArray.clear();
        for(int i = 0; i < list.size(); i++){
            adapterArray.add(list.get(i));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        MainActivity.dh.joinGroup(MainActivity.user, itemArray.get(position));
                        Toast.makeText(GroupFinder.this, "Successfully joined " + itemArray.get(position).getName() + "!", Toast.LENGTH_SHORT).show();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Would you like to join this group?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }


}
