package com.finalproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.finalproject.R;
import com.finalproject.databinding.TimeOwnerRowBinding;
import com.finalproject.model.Time;
import com.finalproject.model.TimeModel;
import com.finalproject.ui.owner.activity_home.fragments.FragmentOwnerMovies;
import com.finalproject.ui.owner.activity_home.fragments.FragmentOwnerShows;

import java.util.List;

public class TimeOwnerAdapter extends RecyclerView.Adapter<TimeOwnerAdapter.MyHolder>{

    private List<Time> timeList;
    private Context context;
    private Fragment fragment;
    private LayoutInflater inflater;
    private int mainDayPos;

    public TimeOwnerAdapter(Context context,int mainDayPos,Fragment fragment){
        this.context=context;
        this.fragment=fragment;
        this.mainDayPos=mainDayPos;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TimeOwnerRowBinding binding= DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.time_owner_row,parent,false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        MyHolder myHolder=(MyHolder) holder;
        myHolder.binding.setModel(timeList.get(position));
        if (fragment instanceof FragmentOwnerMovies){
            FragmentOwnerMovies fragmentOwnerMovies=(FragmentOwnerMovies) fragment;
        } else if (fragment instanceof FragmentOwnerShows){
            FragmentOwnerShows fragmentOwnerShows=(FragmentOwnerShows) fragment;
        }
        myHolder.binding.imageDelete.setOnClickListener(view -> {
            if (fragment instanceof FragmentOwnerMovies){
                FragmentOwnerMovies fragmentOwnerMovies=(FragmentOwnerMovies) fragment;
                fragmentOwnerMovies.deleteSelectedTime(myHolder.getAdapterPosition(),mainDayPos);
            } else if (fragment instanceof FragmentOwnerShows){
                FragmentOwnerShows fragmentOwnerShows=(FragmentOwnerShows) fragment;
                fragmentOwnerShows.deleteSelectedTime(myHolder.getAdapterPosition(),mainDayPos);
            }

        });

    }

    @Override
    public int getItemCount() {
        if (timeList == null)
            return 0;
        else
            return timeList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TimeOwnerRowBinding binding;

        public MyHolder(@NonNull TimeOwnerRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public void updateList(List<Time> list) {
        if (list != null) {
            this.timeList = list;
        }
        notifyDataSetChanged();
    }
}
