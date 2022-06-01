package com.finalproject.mvvm;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.finalproject.R;
import com.finalproject.model.MoviesDataModel;
import com.finalproject.model.PostDataModel;
import com.finalproject.model.PostModel;
import com.finalproject.model.ShowDataModel;
import com.finalproject.model.StatusResponse;
import com.finalproject.remote.Api;
import com.finalproject.share.Common;
import com.finalproject.tags.Tags;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FragmentCinemaDataMvvm extends AndroidViewModel {
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<List<PostModel>> onDataSuccess;
    private MutableLiveData<Integer> remove;
    private CompositeDisposable disposable = new CompositeDisposable();

    public FragmentCinemaDataMvvm(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Boolean> getIsLoading() {
        if (isLoading == null) {
            isLoading = new MutableLiveData<>();
        }
        return isLoading;
    }

    public MutableLiveData<List<PostModel>> getOnDataSuccess() {
        if (onDataSuccess == null) {
            onDataSuccess = new MutableLiveData<>();
        }
        return onDataSuccess;
    }

    public MutableLiveData<Integer> getRemove() {
        if (remove == null) {
            remove = new MutableLiveData<>();
        }
        return remove;
    }

    public void getMoviesOrShows(String cinema_id, String type) {
        isLoading.setValue(true);

        Api.getService(Tags.base_url).getCinemaData(cinema_id, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<PostDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<PostDataModel> response) {
                        isLoading.postValue(false);
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getData() != null && response.body().getStatus() == 200) {
                                onDataSuccess.setValue(response.body().getData());
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isLoading.postValue(false);
                        Log.e("error", e.toString());
                    }
                });
    }


    public void removeFromCinema(Context context, String cinema_id, String post_id, int adapterPosition) {
        ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
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
                        dialog.dismiss();
                        Log.e("sttt",response.code()+" "+response.body().getStatus());
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 510) {
                                getRemove().setValue(adapterPosition);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        Log.e("error", e.getMessage());
                    }
                });
    }

}
