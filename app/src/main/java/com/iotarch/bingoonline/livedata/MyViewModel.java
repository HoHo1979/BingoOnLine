package com.iotarch.bingoonline.livedata;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyViewModel extends ViewModel {

    MutableLiveData<String> name;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("/root");

    FirebaseQueryLiveData firebaseData = new FirebaseQueryLiveData(reference);


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
}
