package com.iotarch.bingoonline.livedata;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class NumbersViewModel extends ViewModel {

    DatabaseReference reference;
    FirebaseQueryLiveData firebaseData;

    LiveData<HashMap<String,Boolean>> number;

    LiveData<Map<String,Boolean>> status;

    public NumbersViewModel(Application application, String roomId){

        reference = FirebaseDatabase.getInstance().getReference();
        Query query=reference.child("Rooms").child(roomId).child("numbers");
        firebaseData = new FirebaseQueryLiveData(query);

    }

    @NonNull
    public LiveData<DataSnapshot> getFirebaseData() {
        return firebaseData;
    }


}
