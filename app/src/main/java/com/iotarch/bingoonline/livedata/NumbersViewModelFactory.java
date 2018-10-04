package com.iotarch.bingoonline.livedata;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class NumbersViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Application application;
    private String parm;

    public NumbersViewModelFactory(Application application, String parm){
        this.application = application;
        this.parm = parm;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new NumbersViewModel(application,parm);
    }



}
