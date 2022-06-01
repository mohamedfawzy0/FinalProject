package com.finalproject.mvvm;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.finalproject.R;
import com.finalproject.model.CinemaModel;
import com.finalproject.model.CreateCinemaModel;
import com.finalproject.model.SingleCinemaModel;
import com.finalproject.model.UserModel;
import com.finalproject.remote.Api;
import com.finalproject.share.Common;
import com.finalproject.tags.Tags;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ActivityCreateCinemaMvvm extends AndroidViewModel {

    private MutableLiveData<CinemaModel.Model> onCinemaSuccess;

    private CompositeDisposable disposable = new CompositeDisposable();

    public ActivityCreateCinemaMvvm(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<CinemaModel.Model> getOnCinemaSuccess() {
        if (onCinemaSuccess==null){
            onCinemaSuccess=new MutableLiveData<>();
        }
        return onCinemaSuccess;
    }

    public void CreateCinema(UserModel userModel, CreateCinemaModel model, Context context){
        ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Log.e("dddd",userModel.getData().getId()+" "+model.getTitle()+" "+model.getLocation()+" "+model.getChairs_count()+" "+model.getPrice());
        Api.getService(Tags.base_url).createCinema(userModel.getData().getId(),model.getTitle(), model.getLocation(), model.getChairs_count(), model.getPrice())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SingleCinemaModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<SingleCinemaModel> response) {
                        dialog.dismiss();
                        Log.e("responsee",response.code()+" "+response.body().getStatus()+" "+response.body().getData());
                        if (response.isSuccessful() && response.body()!=null){
                            if (response.body().getStatus()==200){
                                onCinemaSuccess.postValue(response.body().getData());
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
