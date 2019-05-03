package com.cs441_app;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import static android.support.constraint.Constraints.TAG;

/**
 * The class that handles all database interactions.
 */
public class Database_Handler {

    /**
     * A reference to the Database
     */
    private FirebaseFirestore db;
    private ArrayList<Task> v;
    /**
     * Default constructor to get said database
     */
    public Database_Handler(){
        db = FirebaseFirestore.getInstance();
        v = new ArrayList();
    }


    /**
     * Writes a task to either a group calendar or an account calendar. If the GroupID parameter is empty, then it will save the task to the users calendar. If a GroupID is provided, then
     * it will write the task to that groups calendar
     * @param AccountID The AccountID that wrote the task, should be saved in task.getUser().getUserID().
     * @param GroupID The GroupID that is going to receive the task in their calendar, if left empty, then the task gets written to the users calendar.
     * @param task The task to be written to the database.
     */
    public void writeTask(String AccountID, String GroupID, Task task){
        String BlockID = generateBlockID(task);

        // GroupID is left blank so write the task to the users calendar
        if(GroupID.isEmpty()) {
            db.collection("Users").document(AccountID).collection("Calendar").document(BlockID).collection("Tasks").document().set(task);
        }
        else{   // The Task is meant for a Group Calendar so write it to the Group
            db.collection("Groups").document(GroupID).collection("Calendar").document(BlockID).collection("Tasks").document().set(task);
        }
    }

    /**
     * Write a message to a groups database. A message is saved as a document in the subcollection called Messages.
     * @param GroupID The group to write the message to
     * @param msg The message itself
     */
    public void writeMessage(String GroupID, Message msg){
        DocumentReference ref = db.collection("Groups").document(GroupID).collection("Messages").document();
        ref.set(msg);
    }
    /**
     * Creates a new group and adds it to the database with a given name and the AccountID of the person who is making it.
     * @param user The user who made the group. This user is automatically added to the group.
     * @param GroupName The name of the group that is being created.
     * @return Returns the group that was just created.
     */
    public Group createGroup(User user, String GroupName){

        DocumentReference groupref = db.collection("Groups").document();

        Group group = new Group(GroupName);
        groupref.set(group);
        group.setGroupID(groupref.getId());
        groupref.collection("Members").document(user.getUserID()).set(user);
        db.collection("Users").document(user.getUserID()).collection("Participates").document(group.getGroupID()).set(group);

        return group;
    }

    /**
     * Adds the user to a group. It does this by adding the users account ID to the groups members list and also adds the group ID to the users participation list
     * @param AccountID The account to be added to the group
     * @param GroupID The group that allows the user to join
     */
    public void joinGroup(String AccountID, String GroupID){
        db.collection("Groups").document(GroupID).collection("Members").document(AccountID);
        db.collection("Users").document(AccountID).collection("Participates").document(GroupID);
    }

    /**
     * Deletes the user from a specified group
     * @param AccountID The user to be deleted from the group
     * @param GroupID The group to be removed from the users group list
     */
    public void leaveGroup(String AccountID, String GroupID){
        db.collection("Groups").document(GroupID).collection("Members").document(AccountID).delete();
        db.collection("Users").document(AccountID).collection("Participates").document(GroupID).delete();
    }

    public void deleteTask(String AccountID, String GroupID, Task task){
        String BlockID = generateBlockID(task);

        if(GroupID == ""){
            db.collection("Users").document(AccountID).collection("Calendar").document(BlockID).collection("Tasks").document(task.getId()).delete();
        }
        else{
            db.collection("Groups").document(GroupID).collection("Calendar").document(BlockID).collection("Tasks").document(task.getId()).delete();
        }
    }
    /**
     * Creates a new user and adds it to the database with a given name. The User is then given a unique ID that is returned in the user class.
     * @param name The name of the user
     * @return Returns the user object that was just created.
     */
    public User createUser(String name){
        DocumentReference userref = db.collection("Users").document();

        User user = new User(name);
        userref.set(user);
        user.setUserID(userref.getId());

        return user;
    }

    /**
     * Creates an array list of messages from a groups message collection and gives it to a static function
     * @param GroupID The group the message list comes from
     */
    public void readMessageList(String GroupID){
        db.collection("Groups").document(GroupID).collection("Messages").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Message> list = new ArrayList();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        Map info = document.getData();
                        Message m = new Message((String) info.get("content"), (String) info.get("authorName"), (String) info.get("authorID"), (Long) info.get("day"), (Long) info.get("month"), (Long) info.get("year"), (Long) info.get("hour"), (Long) info.get("min"));
                        list.add(m);
                    }
                    // full array list is here, put function call to update view here.
                    System.out.println("Size of list: " + list.size());
                    // STATIC FUNCTION GOES HERE
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    /**
     * Creates an array list of users from a groups members collection and gives it to a static function
     * @param GroupID The group to get the members list from
     */
    public void readGroupMembers(String GroupID){
        db.collection("Groups").document(GroupID).collection("Members").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<User> list = new ArrayList();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        Map info = document.getData();
                        User u = new User((String) info.get("UserID"), (String) info.get("Name"));
                        list.add(u);
                    }
                    // full array list is here, put function call to update view here.
                    System.out.println("Size of list: " + list.size());
                    // STATIC FUNCTION GOES HERE
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }
    /**
     * Creates an array list of groups from a users participation collection and gives it to static function
     * @param AccountID The user to get the participation list from
     */
    public void readGroups_ByParticipation(String AccountID){
        db.collection("Users").document(AccountID).collection("Participates").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Group> list = new ArrayList();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        Map info = document.getData();
                        Group g = new Group((String) info.get("groupID"), (String) info.get("name"));
                        list.add(g);
                    }
                    // full array list is here, put function call to update view here.
                    System.out.println("Size of list: " + list.size());
                    // STATIC FUNCTION GOES HERE
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    /**
     * Creates an array list of groups from a given name and gives it to a static function
     * @param Name The name of the group to search for
     */
    public void readGroups_ByName(String Name){
        char i = Name.charAt(Name.length() - 1);
        String Skey = Name.substring(0,Name.length() - 1) + (i+1);

        Query q = db.collection("Groups").whereGreaterThanOrEqualTo("name", Name).whereLessThan("name", Skey);

        q.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Group> list = new ArrayList();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        Map info = document.getData();
                        Group g = new Group((String) info.get("groupID"), (String) info.get("name"));
                        list.add(g);
                    }
                    // full array list is here, put function call to update view here.
                    System.out.println("Size of list: " + list.size());
                    // STATIC FUNCTION GOES HERE
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }
    /**
     * Reads a block of tasks from the database and returns an array list of tasks to a static function.
     * @param AccountID The account that is accessing the database
     * @param GroupID The group that is accessing the database, if there is no group, leave this blank
     * @param BlockID The block id used to find the specified block to read from
     */
    public void readBlock(String AccountID, String GroupID, String BlockID){
        if(GroupID == "") {
            db.collection("Users").document(AccountID).collection("Calendar").document(BlockID).collection("Tasks").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        ArrayList<Task> list = new ArrayList();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            Map info = document.getData();
                            Task t = new Task((long) info.get("day"), (long) info.get("month"), (long) info.get("year"), (long) info.get("hour"), (long) info.get("min"), (long) info.get("category"), (String) info.get("title"), (String) info.get("description"), (String) info.get("location"), (boolean) info.get("share")
                                    , new User((String) ((Map) info.get("user")).get("userID"), (String) ((Map) info.get("user")).get("Name")), document.getId());
                            list.add(t);
                        }
                        // full array list is here, put function call to update view here.
                        System.out.println("Size of list: " + list.size());
                        MainActivity.updateTaskList(list);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
        }
        else{
            db.collection("Groups").document(GroupID).collection("Calendar").document(BlockID).collection("Tasks").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        ArrayList<Task> list = new ArrayList();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            Map info = document.getData();
                            Task t = new Task((long) info.get("day"), (long) info.get("month"), (long) info.get("year"), (long) info.get("hour"), (long) info.get("min"), (long) info.get("category"), (String) info.get("title"), (String) info.get("description"), (String) info.get("location"), (boolean) info.get("share")
                                    , new User((String) ((Map) info.get("user")).get("userID"), (String) ((Map) info.get("user")).get("Name")));
                            list.add(t);
                        }
                        // full array list is here, put function call to update view here.
                        System.out.println("Size of list: " + list.size());
                        // STATIC FUNCTION GOES HERE
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
        }
        return;
    }
    /**
     * Private function that creates a BlockID given a task. The blockID if formatted as a string that represents the date and time of the task. For example, if the date was April 10, 2019
     * at 15:30 then the BlockID would be 041020191530 (04-10-2019-15-30). This function is used when writing a new task to the database.
     * @param task The task that will be used to generate the BlockID.
     * @return returns the newly made BlockID
     */
    public String generateBlockID(Task task){
        String BlockID = "";

        // Generates the BlockID
        // Add Month to BlockID
        if(task.getMonth()>=10)
            BlockID = BlockID + Long.toString(task.getMonth());
        else
            BlockID = BlockID + "0" + Long.toString(task.getMonth());

        // Add Day to BlockID
        if(task.getDay()>=10)
            BlockID = BlockID + Long.toString(task.getDay());
        else
            BlockID = BlockID + "0" + Long.toString(task.getDay());

        // Add Year to BlockID
        BlockID = BlockID + Long.toString(task.getYear());


        return BlockID;
    }

}
