package com.cs441_app;

public class Group {
    private String groupID;
    private String name;
    private boolean sync;

    public Group(){
        sync = false;
    }

    public Group(String Name){
        name = Name;
        sync = false;
    }

    public Group(String GroupID, String Name){
        groupID = GroupID;
        name = Name;
        sync = false;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


