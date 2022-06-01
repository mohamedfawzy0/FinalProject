package com.finalproject.ui.owner.activity_home.fragments.cinema_module;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finalproject.R;
import com.finalproject.adapter.MyPagerAdapter;
import com.finalproject.databinding.FragmentOwnerCinemaBinding;
import com.finalproject.ui.common_uis.activity_base.BaseFragment;
import com.finalproject.ui.owner.activity_home.OwnerHomeActivity;
import com.finalproject.ui.owner.activity_home.fragments.cinema_module.fragments.FragmentCinemaMovies;
import com.finalproject.ui.owner.activity_home.fragments.cinema_module.fragments.FragmentCinemaShows;

import java.util.ArrayList;
import java.util.List;


public class FragmentOwnerCinema extends BaseFragment {
    private OwnerHomeActivity activity;
    private FragmentOwnerCinemaBinding binding;
    private List<Fragment> fragmentList;
    private List<String> titles;
    private MyPagerAdapter pagerAdapter;


    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (OwnerHomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_owner_cinema, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }
    private void initView() {


        titles = new ArrayList<>();
        fragmentList = new ArrayList<>();
        titles.add(getString(R.string.movies));
        titles.add(getString(R.string.shows));
        binding.tab.setupWithViewPager(binding.pager);

        fragmentList.add(FragmentCinemaMovies.newInstance());
        fragmentList.add(FragmentCinemaShows.newInstance());

        pagerAdapter = new MyPagerAdapter(getChildFragmentManager(), PagerAdapter.POSITION_UNCHANGED, fragmentList, titles);

        binding.pager.setAdapter(pagerAdapter);
        binding.pager.setOffscreenPageLimit(fragmentList.size());
        for (int i = 0; i < binding.tab.getTabCount(); i++) {
            Log.e("i", i + "");
            View view = ((ViewGroup) binding.tab.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            params.setMargins(16, 0, 16, 0);
        }
    }





//    public void navigateToDetails() {
//        Intent intent=new Intent(activity, OwnerBookingDetailsActivity.class);
//        startActivity(intent);
//    }

//    public void delete(Object o) {
////        mvvm.delete(buffetModel.getId());
//    }



}