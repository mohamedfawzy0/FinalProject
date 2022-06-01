package com.finalproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.finalproject.R;
import com.finalproject.databinding.DayOwnerRowBinding;
import com.finalproject.model.Day;
import com.finalproject.model.DayModel;
import com.finalproject.ui.owner.activity_home.fragments.FragmentOwnerMovies;
import com.finalproject.ui.owner.activity_home.fragments.FragmentOwnerShows;


import java.util.ArrayList;
import java.util.List;

public class DayOwnerAdapter extends RecyclerView.Adapter<DayOwnerAdapter.MyHolder> {
    private List<Day> dayList;
    private Context context;
    private Fragment fragment;
    private LayoutInflater inflater;

    public DayOwnerAdapter(List<Day> dayList, Fragment fragment, Context context) {
        this.dayList = dayList;
        this.context = context;
        this.fragment = fragment;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DayOwnerRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.day_owner_row, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(dayList.get(position));
        myHolder.binding.recViewTime.setLayoutManager(new GridLayoutManager(context, 2));

        TimeOwnerAdapter adapter = new TimeOwnerAdapter(context, myHolder.getAdapterPosition(),fragment);
        adapter.updateList(dayList.get(position).getTimeModelList());
        myHolder.binding.recViewTime.setAdapter(adapter);

        if (fragment instanceof FragmentOwnerMovies) {
            FragmentOwnerMovies fragmentOwnerMovies = (FragmentOwnerMovies) fragment;
        } else if (fragment instanceof FragmentOwnerShows) {
            FragmentOwnerShows fragmentOwnerShows = (FragmentOwnerShows) fragment;
        }

        myHolder.binding.llAddNew.setOnClickListener(view -> {
            if (fragment instanceof FragmentOwnerMovies){
                FragmentOwnerMovies fragmentOwnerMovies = (FragmentOwnerMovies) fragment;
                fragmentOwnerMovies.addNewTime(dayList.get(myHolder.getAdapterPosition()), myHolder.getAdapterPosition(),dayList.get(myHolder.getAdapterPosition()).getTimeModelList(),adapter);

            } else if (fragment instanceof FragmentOwnerShows){
                FragmentOwnerShows fragmentOwnerShows=(FragmentOwnerShows) fragment;
                fragmentOwnerShows.addNewTime(dayList.get(myHolder.getAdapterPosition()), myHolder.getAdapterPosition(),dayList.get(myHolder.getAdapterPosition()).getTimeModelList(),adapter);
            }

        });

        myHolder.binding.imageClose.setOnClickListener(view -> {
            if (fragment instanceof FragmentOwnerMovies) {
                FragmentOwnerMovies fragmentOwnerMovies = (FragmentOwnerMovies) fragment;
                fragmentOwnerMovies.deleteSelectedDay(myHolder.getAdapterPosition());
            } else if (fragment instanceof FragmentOwnerShows) {
                FragmentOwnerShows fragmentOwnerShows = (FragmentOwnerShows) fragment;
                fragmentOwnerShows.deleteSelectedDay(myHolder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        if (dayList == null)
            return 0;
        else
            return dayList.size();
    }

    public void remove(int position, int adapterPosition) {
        dayList.get(adapterPosition).getTimeModelList().remove(position);
        notifyItemChanged(adapterPosition);
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private DayOwnerRowBinding binding;

        public MyHolder(@NonNull DayOwnerRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void updateList(List<Day> list) {
        if (list != null) {
            this.dayList = list;
        }
        notifyDataSetChanged();
    }
}
