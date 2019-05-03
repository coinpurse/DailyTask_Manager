package com.cs441_app;

public class Group {
    private String groupID;
    private String name;

    public Group(){

    }

    public Group(String Name){
        name = Name;
    }

    public Group(String GroupID, String Name){
        groupID = GroupID;
        name = Name;
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


