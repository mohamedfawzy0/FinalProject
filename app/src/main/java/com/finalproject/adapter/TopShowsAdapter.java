package com.finalproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.finalproject.R;
import com.finalproject.databinding.TopPostItemRowBinding;
import com.finalproject.model.PostModel;
import com.finalproject.ui.user.activity_home.fragments.FragmentHome;

import java.util.List;

public class TopShowsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PostModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;

    public TopShowsAdapter(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TopPostItemRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.top_post_item_row, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));

        myHolder.itemView.setOnClickListener(view -> {
            FragmentHome fragmentHome = (FragmentHome) fragment;
            fragmentHome.navigateToDetailsActivity(list.get(myHolder.getAbsoluteAdapterPosition()),myHolder.getAdapterPosition());
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
        public TopPostItemRowBinding binding;

        public MyHolder(TopPostItemRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
