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

    public FirebaseFirestore getDb(){
        return db;
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
     * Creates a new group and adds it to the database with a given name and the AccountID of the person who is making it.
     * @param AccountID The user who made the group. This user is automatically added to the group.
     * @param GroupName The name of the group that is being created.
     * @return Returns the group that was just created.
     */
    public Group createGroup(String AccountID, String GroupName){

        DocumentReference groupref = db.collection("Groups").document();

        groupref.set(GroupName);
        groupref.collection("Members").document(AccountID);
        db.collection("Users").document(AccountID).collection("Participates").document(groupref.getId());

        return new Group(groupref.getId(), GroupName);
    }

    /**
     * Creates a new user and adds it to the database with a given name. The User is then given a unique ID that is returned in the user class.
     * @param name The name of the user
     * @return Returns the user object that was just created.
     */
    public User createUser(String name){
        DocumentReference userref = db.collection("Users").document();

        Map<String, Object> data = new HashMap<>();
        data.put("name", name);

        User user = new User(userref.getId(),name);
        userref.set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

        return new User(userref.getId(), name);
    }

    public void readBlock(String AccountID, String GroupID, String BlockID){
        db.collection("Users").document(AccountID).collection("Calendar").document(BlockID).collection("Tasks").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Task> list = new ArrayList();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        Map info = document.getData();
                        Task t = new Task((long) info.get("day"), (long) info.get("month"), (long) info.get("year"), (long) info.get("hour"), (long) info.get("min"), (long) info.get("category"), (String) info.get("title"), (String) info.get("description"), (String) info.get("location"), (boolean) info.get("share")
                                , new User((String) ((Map) info.get("user")).get("userID"), (String) ((Map) info.get("user")).get("name")));
                        list.add(t);
                    }
                    // full array list is here, put function call to update view here.
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
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
