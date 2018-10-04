package com.iotarch.bingoonline.firebase;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iotarch.bingoonline.R;
import com.iotarch.bingoonline.entity.GameRoom;
import com.iotarch.bingoonline.entity.User;

import static com.firebase.ui.auth.AuthUI.TAG;

public class DataBaseHelper {

    static DataBaseHelper helper;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userReference = database.getReference().child("Users");
    DatabaseReference roomReference = database.getReference().child("Rooms");


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


    public void saveRoom(GameRoom room){

        String roomKey=roomReference.push().getKey();
        room.setRoomId(roomKey);
        roomReference.child(roomKey).setValue(room);

        for (int i = 1; i <= 25; i++) {
            roomReference.child(roomKey).child("numbers/"+(i)).setValue(false);
        }


    }

    public void updateRoomUser(String ownerId, TextView tvOwner, ImageView ivRoomAvatar, int[] avatars) {


        userReference.child(ownerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                tvOwner.setText(user.getDisplayName());
                ivRoomAvatar.setBackgroundResource(avatars[user.getImageId()]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void updateRoomStatus(String roomId, int number, boolean b) {

        roomReference.child(roomId).child("numbers").child(number+"").setValue(b);

    }
}
