package edu.bluejack19_1.eassum.Model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.InstanceIdResult;

public class User {
    private static User user;

    String name , key;
    int semester, type;

    public User(String name , int semester, int type , String key) {
        this.name = name;
        this.semester = semester;
        this.type = type;
        this.key = key;
    }

    public static void updateFCMToken(String token){
        Log.d("yb", "updateFCMToken: " + token);
        FirebaseDatabase.getInstance().getReference().child("User").child(user.key).child("fcm_token").setValue(token);
    }

    public static void addFCMTokenFromService() {
        com.google.android.gms.tasks.OnCompleteListener<InstanceIdResult> listener = new com.google.android.gms.tasks.OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if(task.isSuccessful()) {
                    Log.d("yb", task.getResult().getToken());
                    updateFCMToken(task.getResult().getToken());
                }
            }
        };
        MyFirebaseMessagingService.getToken(listener);
    }
    public static void setUser(String name,int semester, int type , String key) {
        user = new User(name ,semester, type , key);
        Log.d("HAHAA", "setUser: " + "daasda");
    }

    public static int getInstanceUserType() {
        Log.d("HAHAA", "setUser: " + user);
        return user.type;
    }
    public static int getInstanceUserSemester() {
        return user.semester;
    }

    public static String getInstanceUserName() {
        return user.name;
    }

    public static String getInstanceUserKey(){
        return user.key;
    }
}
