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
import com.finalproject.databinding.MovieRowBinding;
import com.finalproject.model.PostModel;

import com.finalproject.ui.user.activity_home.fragments.FragmentSearch;

import java.util.List;

public class SearchItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<PostModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;

    public SearchItemAdapter(Context context, Fragment fragment) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MovieRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.movie_row, parent, false);
        return new MoviesAdapter.MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MoviesAdapter.MyHolder myHolder = (MoviesAdapter.MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.itemView.setOnClickListener(view -> {
            if (fragment instanceof FragmentSearch){
                FragmentSearch fragmentSearch = (FragmentSearch) fragment;
                fragmentSearch.navigateToMovieDetails(list.get(position));
            }

        });
        if (fragment instanceof FragmentSearch){
            FragmentSearch fragmentSearch=(FragmentSearch) fragment;
            myHolder.binding.flAddToCinema.setVisibility(View.GONE);
        }

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
        public MovieRowBinding binding;

        public MyHolder(MovieRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
