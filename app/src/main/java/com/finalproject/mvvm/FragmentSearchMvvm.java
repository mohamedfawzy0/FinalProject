package com.finalproject.mvvm;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.finalproject.model.MoviesDataModel;
import com.finalproject.model.PostDataModel;
import com.finalproject.model.PostModel;
import com.finalproject.remote.Api;
import com.finalproject.tags.Tags;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FragmentSearchMvvm extends AndroidViewModel {
    private static final String TAG = "FragmentSearchMvvm";
    private Context context;

    private MutableLiveData<List<PostModel>> onPostSuccess;
    private MutableLiveData<Boolean> isLoadingLivData;

    private CompositeDisposable disposable = new CompositeDisposable();


    public FragmentSearchMvvm(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }


    public MutableLiveData<Boolean> getIsLoading() {
        if (isLoadingLivData == null) {
            isLoadingLivData = new MutableLiveData<>();
        }
        return isLoadingLivData;
    }

    public MutableLiveData<List<PostModel>> getOnPostSuccess() {
        if (onPostSuccess==null){
            onPostSuccess=new MutableLiveData<>();
        }
        return onPostSuccess;
    }


    //_________________________hitting api_________________________________


    public void getSearch(String search){
        isLoadingLivData.setValue(true);
        Api.getService(Tags.base_url).getSearch(search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<PostDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<PostDataModel> response) {
                        isLoadingLivData.setValue(false);

                        if (response.isSuccessful() && response.body()!=null){
                            if (response.body().getData()!=null && response.body().getStatus()==200){
                                onPostSuccess.postValue(response.body().getData());
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }
    public void getMovies(String search) {
        isLoadingLivData.setValue(true);

        Api.getService(Tags.base_url).getMovies(null,search,null,null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<MoviesDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<MoviesDataModel> response) {
                        isLoadingLivData.setValue(false);
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getData() != null && response.body().getStatus() == 200) {
                                onPostSuccess.postValue(response.body().getData());
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("error", e.toString());
                    }
                });
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();

    }

}
