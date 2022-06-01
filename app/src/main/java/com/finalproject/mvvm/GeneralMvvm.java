package com.finalproject.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class GeneralMvvm extends AndroidViewModel {
    private MutableLiveData<Integer> postPage;
    private MutableLiveData<Boolean> onPostRefreshed;


    public GeneralMvvm(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Integer> getPostPage() {
        if (postPage == null) {
            postPage = new MutableLiveData<>();
        }
        return postPage;
    }
    public MutableLiveData<Boolean> getOnPostRefreshed() {
        if (onPostRefreshed == null) {
            onPostRefreshed = new MutableLiveData<>();
        }
        return onPostRefreshed;
    }

}
