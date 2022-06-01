package com.finalproject.mvvm;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.finalproject.R;
import com.finalproject.model.AddDayTimeModel;
import com.finalproject.model.CategoryDataModel;
import com.finalproject.model.CategoryModel;
import com.finalproject.model.MoviesDataModel;
import com.finalproject.model.PostModel;
import com.finalproject.model.StatusResponse;
import com.finalproject.remote.Api;
import com.finalproject.share.Common;
import com.finalproject.tags.Tags;
import com.google.gson.Gson;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FragmentMoviesMvvm extends AndroidViewModel {

    private MutableLiveData<Boolean> isLoading;

    private MutableLiveData<List<CategoryModel>> onCategorySuccess;

    private MutableLiveData<List<PostModel>> onMoviesSuccess;

    private MutableLiveData<String> categoryId;

    private MutableLiveData<Integer> selectedCategoryPos;

    public MutableLiveData<Boolean> add = new MutableLiveData<>();
    public MutableLiveData<Boolean> addDayTime = new MutableLiveData<>();

    private CompositeDisposable disposable = new CompositeDisposable();

    public FragmentMoviesMvvm(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Boolean> getIsLoading() {
        if (isLoading == null) {
            isLoading = new MutableLiveData<>();
        }
        return isLoading;
    }

    public MutableLiveData<List<CategoryModel>> getOnCategorySuccess() {
        if (onCategorySuccess == null) {
            onCategorySuccess = new MutableLiveData<>();
        }
        return onCategorySuccess;
    }

    public MutableLiveData<List<PostModel>> getOnMoviesSuccess() {
        if (onMoviesSuccess == null) {
            onMoviesSuccess = new MutableLiveData<>();
        }
        return onMoviesSuccess;
    }

    public MutableLiveData<String> getCategoryId() {
        if (categoryId == null) {
            categoryId = new MutableLiveData<>();
        }
        return categoryId;
    }

    public MutableLiveData<Integer> getSelectedCategoryPos() {
        if (selectedCategoryPos == null) {
            selectedCategoryPos = new MutableLiveData<>(-1);
        }

        return selectedCategoryPos;
    }

    public void setSelectedCategoryPos(int pos) {
        getSelectedCategoryPos().setValue(pos);

    }

    public void getCategory() {
        isLoading.setValue(true);

        Api.getService(Tags.base_url).getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<CategoryDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<CategoryDataModel> response) {
                        isLoading.setValue(false);
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getData() != null && response.body().getStatus() == 200) {
                                onCategorySuccess.setValue(response.body().getData());
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("error", e.toString());
                    }
                });
    }

    public void getMovies(String search,String cinema_id,String user_id) {
        isLoading.setValue(true);

        Api.getService(Tags.base_url).getMovies(getCategoryId().getValue(), search,cinema_id,user_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<MoviesDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<MoviesDataModel> response) {
                        isLoading.setValue(false);
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getData() != null && response.body().getStatus() == 200) {
                                onMoviesSuccess.setValue(response.body().getData());
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("error", e.toString());
                    }
                });
    }

    public void addToCinema(String cinema_id, String post_id) {
//        Log.e("ddd",cinema_id+" "+post_id);
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
//                        Log.e("sttt",response.code()+" "+response.body().getStatus());
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
        Log.e("user_data",user_data);
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
//                        Log.e("sttt22",response.code()+" "+response.body().getStatus());
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