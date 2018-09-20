package com.iotarch.bingoonline.livedata;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.MainThread;

public class MyViewModel extends ViewModel {

    MutableLiveData<String> name;



    public MutableLiveData<String> getName() {

        if(name==null){
           name = new MutableLiveData<>();
        }
        return name;
    }

}
