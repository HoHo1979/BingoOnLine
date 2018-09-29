package com.iotarch.bingoonline.livedata;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iotarch.bingoonline.entity.User;

public class MyViewModel extends AndroidViewModel {


    DatabaseReference reference;
    FirebaseQueryLiveData firebaseData;

//    final MediatorLiveData<User> liveDataMerger = new MediatorLiveData<>();

//    public MyViewModel() {
//        liveDataMerger.addSource(firebaseData, new Observer<DataSnapshot>() {
//            @Override
//            public void onChanged(@Nullable final DataSnapshot dataSnapshot) {
//
//
//
//            }
//        });
//    }


    LiveData<User> userLiveData;

    public MyViewModel(Application application, String parm) {

        super(application);
        Log.d("ABC", "MyViewModel: "+parm+"999999");
        reference = FirebaseDatabase.getInstance().getReference("/Users").child(parm);
        firebaseData = new FirebaseQueryLiveData(reference);
        userLiveData = Transformations.map(firebaseData, new Function<DataSnapshot, User>() {
            @Override
            public User apply(DataSnapshot dataSnapshot) {
                return dataSnapshot.getValue(User.class);
            }
        });

    }


    @NonNull
    public LiveData<DataSnapshot> getFirebaseData() {
        return firebaseData;
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }
}
