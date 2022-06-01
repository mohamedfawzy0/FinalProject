package com.finalproject.ui.user.activity_home.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.finalproject.R;
import com.finalproject.adapter.SearchHomeAdapter;
import com.finalproject.databinding.FragmentSearchBinding;
import com.finalproject.model.PostModel;
import com.finalproject.mvvm.FragmentSearchMvvm;
import com.finalproject.share.Common;
import com.finalproject.ui.common_uis.activity_base.BaseFragment;
import com.finalproject.ui.common_uis.activity_login.LoginActivity;
import com.finalproject.ui.user.activity_home.HomeActivity;
import com.finalproject.ui.user.activity_details.DetailsActivity;

import io.reactivex.disposables.CompositeDisposable;


public class FragmentHomeSearch extends BaseFragment {
    private static final String TAG = FragmentHomeSearch.class.getName();
    private HomeActivity activity;
    private FragmentSearchBinding binding;
    private FragmentSearchMvvm mvvm;
    private SearchHomeAdapter adapter;
    private PostModel postModel;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView() {
        postModel = new PostModel();
        mvvm = ViewModelProviders.of(this).get(FragmentSearchMvvm.class);

        mvvm.getIsLoading().observe(activity, isLoading -> {
            binding.swipeRef.setRefreshing(isLoading);
            if (isLoading) {
                adapter.updateList(null);
            }
        });
        binding.swipeRef.setColorSchemeResources(R.color.primary_dark2);

        mvvm.getOnPostSuccess().observe(activity, postModels -> {
            if (postModels.size() > 0) {
                binding.cardNoData.setVisibility(View.GONE);
            } else {
                binding.cardNoData.setVisibility(View.VISIBLE);
            }
            if (adapter != null) {
                adapter.updateList(postModels);
            }
        });

        adapter = new SearchHomeAdapter(activity, this);
        binding.recyclerMovies.setLayoutManager(new GridLayoutManager(activity, 2));
        binding.recyclerMovies.setAdapter(adapter);

        binding.swipeRef.setOnRefreshListener(() -> mvvm.getSearch(binding.edtSearch.getText().toString()));

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mvvm.getSearch(s.toString());
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Common.CloseKeyBoard(activity, binding.edtSearch);
        disposable.clear();
    }


    public void navigateToDetails(PostModel postModel, int adapterPosition) {
        if (getUserModel() != null) {
            Intent intent = new Intent(activity, DetailsActivity.class);
            intent.putExtra("post_id", postModel.getId());
            startActivity(intent);
        } else {
            navigateToLoginActivity();
        }

    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(activity, LoginActivity.class);
        startActivity(intent);
    }
}