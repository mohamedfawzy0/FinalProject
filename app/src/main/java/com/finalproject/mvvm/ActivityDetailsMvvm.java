package com.finalproject.mvvm;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.finalproject.model.DetailsDataModel;
import com.finalproject.model.PostModel;
import com.finalproject.remote.Api;
import com.finalproject.tags.Tags;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ActivityDetailsMvvm extends AndroidViewModel {

    private MutableLiveData<Boolean> isLoading;

    private MutableLiveData<PostModel> onDataSuccess;

    private CompositeDisposable disposable = new CompositeDisposable();

    public ActivityDetailsMvvm(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Boolean> getIsLoading() {
        if (isLoading==null){
            isLoading=new MutableLiveData<>();
        }
        return isLoading;
    }

    public MutableLiveData<PostModel> getOnDataSuccess() {
        if (onDataSuccess==null){
            onDataSuccess=new MutableLiveData<>();
        }
        return onDataSuccess;
    }



    public void getDetails(String id){
        isLoading.setValue(true);

        Api.getService(Tags.base_url).getDetails(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<DetailsDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<DetailsDataModel> response) {
                        isLoading.setValue(false);
                        if (response.isSuccessful() && response.body()!=null){
                            if (response.body().getData()!=null && response.body().getStatus()==200){
                                onDataSuccess.postValue(response.body().getData());
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("error",e.toString());
                    }
                });
    }


}
