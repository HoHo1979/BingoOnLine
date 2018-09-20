package com.iotarch.bingoonline.livedata;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iotarch.bingoonline.entity.User;

public class MyViewModel extends ViewModel {

    MutableLiveData<String> name;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("/root");

    FirebaseQueryLiveData firebaseData = new FirebaseQueryLiveData(reference);

    LiveData<User> userLiveData = Transformations.map(firebaseData, new Function<DataSnapshot, User>() {
        @Override
        public User apply(DataSnapshot input) {
            return input.getValue(User.class);
        }
    });

    public MutableLiveData<String> getName() {

        if(name==null){
           name = new MutableLiveData<>();
        }
        return name;
    }

    @NonNull
    public LiveData<DataSnapshot> getFirebaseData() {
        return firebaseData;
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }
}
