package com.finalproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.finalproject.R;
import com.finalproject.databinding.OwnerHistoryRowBinding;
import com.finalproject.model.PostModel;
import com.finalproject.ui.owner.activity_home.fragments.FragmentOwnerMovies;
import com.finalproject.ui.owner.activity_home.fragments.cinema_module.FragmentOwnerCinema;
import com.finalproject.ui.owner.activity_home.fragments.cinema_module.fragments.FragmentCinemaMovies;
import com.finalproject.ui.owner.activity_home.fragments.cinema_module.fragments.FragmentCinemaShows;

import java.util.List;

public class OwnerHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PostModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;

    public OwnerHistoryAdapter(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OwnerHistoryRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.owner_history_row, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.itemView.setOnClickListener(view -> {
            if (fragment instanceof FragmentCinemaMovies) {
                FragmentCinemaMovies fragmentCinemaMovies = (FragmentCinemaMovies) fragment;
                fragmentCinemaMovies.navigateToDetails(myHolder.getAdapterPosition(), list.get(position));
            } else if (fragment instanceof FragmentCinemaShows) {
                FragmentCinemaShows fragmentCinemaShows = (FragmentCinemaShows) fragment;
                fragmentCinemaShows.navigateToDetails(myHolder.getAdapterPosition(), list.get(position));
            }
        });

        myHolder.binding.flDelete.setOnClickListener(view -> {
            if (fragment instanceof FragmentCinemaMovies){
                FragmentCinemaMovies fragmentCinemaMovies=(FragmentCinemaMovies) fragment;
                fragmentCinemaMovies.delete(myHolder.getAdapterPosition(),list.get(myHolder.getAdapterPosition()));
            }
            if (fragment instanceof FragmentCinemaShows) {
                FragmentCinemaShows fragmentCinemaShows = (FragmentCinemaShows) fragment;
                fragmentCinemaShows.delete(myHolder.getAdapterPosition(),list.get(myHolder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }

    public void updateList(List<PostModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public OwnerHistoryRowBinding binding;

        public MyHolder(OwnerHistoryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}

