package com.finalproject.mvvm;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.finalproject.model.DayDataModel;
import com.finalproject.model.DayModel;
import com.finalproject.model.OwnerHistoryDataModel;
import com.finalproject.model.OwnerHistoryModel;
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

public class ActivityOwnerBookingDetailsMvvm extends AndroidViewModel {
    private MutableLiveData<Boolean> isLoadingLivData;
    private MutableLiveData<List<DayModel>> onDaysSuccess;
    private MutableLiveData<String> dayId;
    private MutableLiveData<List<OwnerHistoryModel>> onHoursSuccess;
    private CompositeDisposable disposable = new CompositeDisposable();

    public ActivityOwnerBookingDetailsMvvm(@NonNull Application application) {
        super(application);
    }


    public MutableLiveData<Boolean> getIsLoadingLivData() {
        if (isLoadingLivData == null) {
            isLoadingLivData = new MutableLiveData<>();
        }
        return isLoadingLivData;
    }

    public MutableLiveData<List<DayModel>> getOnDaysSuccess() {
        if (onDaysSuccess == null) {
            onDaysSuccess = new MutableLiveData<>();
        }
        return onDaysSuccess;
    }

    public MutableLiveData<List<OwnerHistoryModel>> getOnHoursSuccess() {
        if (onHoursSuccess == null) {
            onHoursSuccess = new MutableLiveData<>();
        }
        return onHoursSuccess;
    }

    public MutableLiveData<String> getDayId() {
        if (dayId == null) {
            dayId = new MutableLiveData<>();
        }
        return dayId;
    }

    public void getDays(UserModel userModel, String post_id) {
        isLoadingLivData.setValue(true);
//        Log.e("reservings1",userModel.getData().getCinema().getId()+" "+post_id);
        Api.getService(Tags.base_url)
                .getOwnerDays(userModel.getData().getCinema().getId(), post_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<DayDataModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(Response<DayDataModel> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getData() != null && response.body().getStatus() == 200) {
                                getOnDaysSuccess().setValue(response.body().getData());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public void getOwnerBookings(UserModel userModel, String post_id) {
        isLoadingLivData.setValue(true);
        Log.e("reservings3", userModel.getData().getCinema().getId() + " " + post_id + " " + getDayId().getValue());
        Api.getService(Tags.base_url).getOwnerBookingsDetails(userModel.getData().getCinema().getId(), post_id, getDayId().getValue())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<OwnerHistoryDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<OwnerHistoryDataModel> response) {
                        isLoadingLivData.postValue(false);
                        Log.e("ddddata", response.code() + " " + response.body().getStatus());
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getData() != null && response.body().getStatus() == 200) {
                                onHoursSuccess.postValue(response.body().getData());
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("error", e.toString());
                    }
                });
    }
}
