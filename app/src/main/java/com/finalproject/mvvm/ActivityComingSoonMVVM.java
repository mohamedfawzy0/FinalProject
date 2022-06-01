package com.finalproject.mvvm;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.finalproject.model.ComingSoonModel;
import com.finalproject.model.HomeDataModel;
import com.finalproject.remote.Api;
import com.finalproject.tags.Tags;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ActivityComingSoonMVVM extends AndroidViewModel {
    private Context context;
    private MutableLiveData<List<ComingSoonModel>> comingSoonMutableLiveData;
    private MutableLiveData<Boolean> isLoadingLivData;
    private CompositeDisposable disposable = new CompositeDisposable();

    public ActivityComingSoonMVVM(@NonNull @NotNull Application application) {
        super(application);
        context = application.getApplicationContext();

    }

    public LiveData<List<ComingSoonModel>> getComingSoon() {
        if (comingSoonMutableLiveData == null) {
            comingSoonMutableLiveData = new MutableLiveData<>();

        }
        return comingSoonMutableLiveData;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        if (isLoadingLivData == null) {
            isLoadingLivData = new MutableLiveData<>();
        }
        return isLoadingLivData;
    }

    public void getComingSoonData(Context context) {
        isLoadingLivData.setValue(true);
        Api.getService(Tags.base_url).getHomeData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<HomeDataModel>>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NotNull Response<HomeDataModel> response) {
                        isLoadingLivData.postValue(false);
                        if (response.isSuccessful()&&response.body()!=null){
                            if (response.body().getStatus()==200){
                                comingSoonMutableLiveData.postValue(response.body().getSoon());
                            }
                        }
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        Log.e("error",e.getMessage());
                    }
                });
    }
}
