package com.finalproject.mvvm;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.finalproject.model.HistoryDataModel;
import com.finalproject.model.HistoryModel;
import com.finalproject.model.UserModel;
import com.finalproject.remote.Api;
import com.finalproject.tags.Tags;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FragmentHistoryMvvm extends AndroidViewModel {

    private MutableLiveData<List<HistoryModel>> onHistorySuccess;
    private MutableLiveData<Boolean> isLoadingLivData;
    private CompositeDisposable disposable = new CompositeDisposable();

    public FragmentHistoryMvvm(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Boolean> getIsLoadingLivData() {
        if (isLoadingLivData==null){
            isLoadingLivData=new MutableLiveData<>();
        }
        return isLoadingLivData;
    }

    public MutableLiveData<List<HistoryModel>> getOnHistorySuccess() {
        if (onHistorySuccess==null){
            onHistorySuccess=new MutableLiveData<>();
        }
        return onHistorySuccess;
    }

    public void getHistory(UserModel userModel){
        isLoadingLivData.setValue(true);
//        Log.e("idd",userModel.getData().getId());
        Api.getService(Tags.base_url).getHistory(userModel.getData().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<HistoryDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<HistoryDataModel> response) {
                        isLoadingLivData.postValue(false);

                        if (response.isSuccessful() && response.body()!=null){
                            if (response.body().getData()!=null && response.body().getStatus()==200){
                                onHistorySuccess.postValue(response.body().getData());
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }
}
