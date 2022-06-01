package com.finalproject.ui.user.activity_home.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finalproject.R;
import com.finalproject.adapter.MoviesAdapter;
import com.finalproject.adapter.MoviesFilterAdapter;
import com.finalproject.databinding.FragmentMoviesBinding;
import com.finalproject.model.CategoryModel;
import com.finalproject.model.PostModel;
import com.finalproject.mvvm.FragmentMoviesMvvm;
import com.finalproject.ui.common_uis.activity_base.BaseFragment;
import com.finalproject.ui.common_uis.activity_login.LoginActivity;
import com.finalproject.ui.user.activity_home.HomeActivity;
import com.finalproject.ui.user.activity_details.DetailsActivity;

import java.util.ArrayList;
import java.util.List;


public class FragmentMovies extends BaseFragment {
    private HomeActivity activity;
    private FragmentMoviesBinding binding;
    private MoviesFilterAdapter moviesFilterAdapter;
    private MoviesAdapter moviesAdapter;
    private FragmentMoviesMvvm mvvm;
    private List<PostModel> movieModelList;
    private int req;
    private ActivityResultLauncher<Intent> launcher;

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1 && result.getResultCode() == Activity.RESULT_OK) {
                activity.navigateToHistory();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movies, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        movieModelList = new ArrayList<>();
        mvvm = ViewModelProviders.of(this).get(FragmentMoviesMvvm.class);

        mvvm.getIsLoading().observe(activity, isLoading -> {
            binding.swipeRef.setRefreshing(isLoading);
        });
        mvvm.getOnCategorySuccess().observe(activity, categoryModels -> {
            if (categoryModels.size() > 0) {
                if (moviesFilterAdapter != null) {
                    moviesFilterAdapter.updateList(categoryModels);
                }
            }
        });
        mvvm.getCategory();
        mvvm.getOnMoviesSuccess().observe(activity, movieModels -> {
            if (mvvm.getCategoryId()!=null){
                if (movieModels.size() > 0) {
                    binding.cardNoData.setVisibility(View.GONE);
                    if (moviesAdapter != null) {
                        movieModelList.clear();
                        movieModelList.addAll(movieModels);
                        moviesAdapter.updateList(movieModels);
                    }
                } else {
                    binding.cardNoData.setVisibility(View.VISIBLE);
                    moviesAdapter.updateList(new ArrayList<>());
                }
            }


        });
        binding.swipeRef.setOnRefreshListener(() -> {
            mvvm.getMovies(null,null,getUserModel().getData().getId());
        });
        binding.swipeRef.setColorSchemeResources(R.color.primary_dark2);
        moviesFilterAdapter = new MoviesFilterAdapter(activity, this);
        binding.recyclerFilter.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        binding.recyclerFilter.setAdapter(moviesFilterAdapter);


        moviesAdapter = new MoviesAdapter(activity, this);
        binding.recyclerMovies.setLayoutManager(new GridLayoutManager(activity, 2));
        binding.recyclerMovies.setAdapter(moviesAdapter);

        binding.llSearch.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.fragmentSearch);
        });
    }

    public void setItemCategory(CategoryModel categoryModel, int currentPos) {
        mvvm.getCategoryId().setValue(categoryModel.getId());
//        mvvm.setSelectedCategoryPos(currentPos);
        if (getUserModel()!=null){
            mvvm.getMovies(null,null,getUserModel().getData().getId());
        }else {
            mvvm.getMovies(null,null,null);
        }

    }

    public void navigateToMovieDetails(PostModel postModel) {
        req = 1;
        if (getUserModel()!=null){
            Intent intent = new Intent(activity, DetailsActivity.class);
            intent.putExtra("post_id", postModel.getId());
            launcher.launch(intent);
        }else {
            navigateToLoginActivity();

        }

    }

    private void navigateToLoginActivity() {
        Intent intent=new Intent(activity, LoginActivity.class);
        startActivity(intent);
    }
}