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
import com.finalproject.databinding.PostRowBinding;
import com.finalproject.model.PostModel;
import com.finalproject.ui.user.activity_home.fragments.FragmentHome;
import com.finalproject.ui.user.activity_home.fragments.FragmentHomeSearch;

import java.util.List;

public class SearchHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<PostModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;

    public SearchHomeAdapter(Context context, Fragment fragment) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PostRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.post_row, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.itemView.setOnClickListener(view -> {
            if (fragment instanceof FragmentHomeSearch){
                FragmentHomeSearch fragmentSearch = (FragmentHomeSearch) fragment;
                fragmentSearch.navigateToDetails(list.get(position),myHolder.getAdapterPosition());
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
        public PostRowBinding binding;

        public MyHolder(PostRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
