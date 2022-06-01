package com.finalproject.mvvm;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.finalproject.R;
import com.finalproject.model.AddDayTimeModel;
import com.finalproject.model.PostModel;
import com.finalproject.model.ShowDataModel;
import com.finalproject.model.StatusResponse;
import com.finalproject.remote.Api;
import com.finalproject.share.Common;
import com.finalproject.tags.Tags;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FragmentShowMVVM extends AndroidViewModel {
    private Context context;
    private MutableLiveData<List<PostModel>> onShowsSuccess;
    private MutableLiveData<Boolean> isLoadingLivData;

    public MutableLiveData<Boolean> add = new MutableLiveData<>();
    public MutableLiveData<Boolean> addDayTime = new MutableLiveData<>();

    private CompositeDisposable disposable = new CompositeDisposable();



    public FragmentShowMVVM(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }
    public LiveData<List<PostModel>> getShowsSuccess() {
        if (onShowsSuccess == null) {
            onShowsSuccess = new MutableLiveData<>();

        }
        return onShowsSuccess;
    }
    public MutableLiveData<Boolean> getIsLoading() {
        if (isLoadingLivData == null) {
            isLoadingLivData = new MutableLiveData<>();
        }
        return isLoadingLivData;
    }

    public void getShowData(Context context,String search,String cinema_id,String user_id) {
        isLoadingLivData.setValue(true);
        Api.getService(Tags.base_url).getShow(search,cinema_id,user_id)
        .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<ShowDataModel>>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NotNull Response<ShowDataModel> response) {
                        isLoadingLivData.postValue(false);
                        if (response.isSuccessful()&&response.body()!=null){
                            if (response.body().getStatus()==200){

                                onShowsSuccess.postValue(response.body().getData());

                            }
                        }
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        Log.e("error",e.getMessage());
                    }
                });
    }

    public void addToCinema(String cinema_id, String post_id) {
        Log.e("ddd",cinema_id+" "+post_id);
        Api.getService(Tags.base_url)
                .addRemoveFromCinema(cinema_id, post_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<StatusResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(Response<StatusResponse> response) {
                        Log.e("sttt",response.code()+" "+response.body().getStatus());
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                add.setValue(true);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("error", e.getMessage());
                    }
                });
    }

    public void AddDayAndTime(Context context, AddDayTimeModel addDayTimeModel) {
        Gson gson = new Gson();
        String user_data = gson.toJson(addDayTimeModel);
//        Log.e("user_data",user_data);
        ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        Api.getService(Tags.base_url)
                .addDayAndTime(addDayTimeModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<StatusResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                addDayTime.setValue(true);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("error", e.getMessage());
                    }
                });
    }

}
