package com.iotarch.bingoonline.livedata;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class MyViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Application application;
    private String parm;

    public MyViewModelFactory(Application application, String parm){
        this.application = application;
        this.parm = parm;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MyViewModel(application,parm);
    }
}
