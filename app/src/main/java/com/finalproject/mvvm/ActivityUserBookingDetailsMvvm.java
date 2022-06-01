package com.finalproject.mvvm;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.finalproject.R;
import com.finalproject.model.StatusResponse;
import com.finalproject.remote.Api;
import com.finalproject.share.Common;
import com.finalproject.tags.Tags;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ActivityUserBookingDetailsMvvm extends AndroidViewModel {
    public MutableLiveData<Boolean> cancelBooking=new MutableLiveData<>();

    private CompositeDisposable disposable = new CompositeDisposable();
    public ActivityUserBookingDetailsMvvm(@NonNull Application application) {
        super(application);
    }

    public void cancelBooking(Context context,String reservation_id){
        ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        Api.getService(Tags.base_url)
                .cancelBooking(reservation_id)
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
                        if (response.isSuccessful() && response.body()!=null){
                            if (response.body().getStatus()==200){
                                cancelBooking.setValue(true);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        Log.e("error",e.getMessage());
                    }
                });
    }
}
