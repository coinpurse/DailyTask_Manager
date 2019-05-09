package com.cs441_app;

import java.util.ArrayList;

public class UserManager {


    public UserManager(){
    }

    public void populateCalendar(String BlockID){
        MainActivity.dh.readBlock(MainActivity.user.getUserID(), "", BlockID, new Group(), false);
        MainActivity.dh.readGroups_BySync(MainActivity.user.getUserID(), BlockID);
    }

    public static void updateTaskList(ArrayList<Task> t_list){
        MainActivity.updateTaskList(t_list);
    }

    public static void updateGroupList(ArrayList<Group> g_list, String BlockID){
        for(int i = 0; i < g_list.size(); i++){
            MainActivity.dh.readBlock(MainActivity.user.getUserID(), g_list.get(i).getGroupID(), BlockID, g_list.get(i), true);
        }
    }
}
