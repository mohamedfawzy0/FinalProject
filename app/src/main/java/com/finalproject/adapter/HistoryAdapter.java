package com.finalproject.adapter;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.finalproject.R;
import com.finalproject.databinding.HistoryItemRowBinding;
import com.finalproject.model.HistoryModel;
import com.finalproject.ui.user.activity_home.fragments.FragmentHistory;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<HistoryModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;
    private String lang;


    public HistoryAdapter(Context context, Fragment fragment,String lang) {
        this.context = context;
        this.fragment = fragment;
        this.lang=lang;
        inflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HistoryItemRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.history_item_row, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.binding.setLang(lang);
        if (fragment instanceof FragmentHistory){
            FragmentHistory fragmentHistory=(FragmentHistory) fragment;
        }
        myHolder.binding.llHeader.setOnClickListener(view -> {
            if (!myHolder.binding.expand.isExpanded()) {
                TransitionManager.beginDelayedTransition(myHolder.binding.cardItem, new AutoTransition());
                myHolder.binding.arrowClicked.setImageResource(R.drawable.ic_top);
                myHolder.binding.expand.setExpanded(true);
            } else {
                TransitionManager.beginDelayedTransition(myHolder.binding.cardItem, new AutoTransition());
                myHolder.binding.arrowClicked.setImageResource(R.drawable.ic_down);
                myHolder.binding.expand.collapse(true);
            }
        });
        myHolder.binding.llDetails.setOnClickListener(view -> {
            if (fragment instanceof FragmentHistory){
                FragmentHistory fragmentHistory=(FragmentHistory) fragment;
                fragmentHistory.navigateToDetails(list.get(position),myHolder.getAdapterPosition());
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

    public void updateList(List<HistoryModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public HistoryItemRowBinding binding;

        public MyHolder(HistoryItemRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
