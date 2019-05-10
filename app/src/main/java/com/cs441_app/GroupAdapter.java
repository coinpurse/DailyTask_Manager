package com.cs441_app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class GroupAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<Group> list;
    private Context context;

    public GroupAdapter(ArrayList<Group> List, Context context){
        this.context = context;
        list = List;
    }

    @Override
    public int getCount(){
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    public void clear(){
        list.clear();
    }

    public void add(Group group){
        list.add(group);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_grouplist_adapter, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position).toString());

        //Handle buttons and add onClickListeners
        Switch Sync = (Switch)view.findViewById(R.id.sync_switch);
        Sync.setChecked(list.get(position).isSync());

        Sync.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    MainActivity.dh.setGroupSync(MainActivity.user.getUserID(), list.get(position), true);
                } else {
                    // The toggle is disabled
                    MainActivity.dh.setGroupSync(MainActivity.user.getUserID(), list.get(position), false);
                }
            }
        });

        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MainActivity.groupview = true;
                MainActivity.group = list.get(position);
                Intent intentHome = new Intent(context,
                        MainActivity.class);
                context.startActivity(intentHome);
            }
        });
        return view;
    }
}
