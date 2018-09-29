package com.iotarch.bingoonline.firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iotarch.bingoonline.entity.User;

import static com.firebase.ui.auth.AuthUI.TAG;

public class DataBaseHelper {

    static DataBaseHelper helper;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userReference = database.getReference().child("Users");

    private DataBaseHelper(){

    }

    public static DataBaseHelper getInstance(){

        if(helper==null)
            helper = new DataBaseHelper();

        return helper;
    }

    public void saveUserData(User user){

        userReference.child(user.getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!dataSnapshot.exists()){
                    Log.d(TAG, "onDataChange: "+"user not exist");
                    userReference.child(user.getUserID()).setValue(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void saveUserImage(User user) {

        userReference.child(user.getUserID()).child("imageId").setValue(user.getImageId());

    }

}
