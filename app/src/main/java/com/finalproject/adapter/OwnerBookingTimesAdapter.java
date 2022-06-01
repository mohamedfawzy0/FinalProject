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
import com.finalproject.databinding.BookingDetailsRowBinding;
import com.finalproject.model.OwnerHistoryModel;
import com.finalproject.ui.owner.activity_owner_booking_details.OwnerBookingDetailsActivity;

import java.util.List;

public class OwnerBookingTimesAdapter extends RecyclerView.Adapter<OwnerBookingTimesAdapter.MyHolder>{
    private List<OwnerHistoryModel> timeList;
    private Context context;
    private Fragment fragment;
    private int currentPos = 0;
    private int oldPos = currentPos;
    private RecyclerView.ViewHolder oldHolder;
    private LayoutInflater inflater;
    private String lang;

    public OwnerBookingTimesAdapter(List<OwnerHistoryModel> timeList, Context context,String lang) {
        this.timeList = timeList;
        this.context = context;
        this.lang=lang;
        this.fragment = fragment;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BookingDetailsRowBinding rowBinding= DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.booking_details_row,parent,false);
        return new MyHolder(rowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        MyHolder myHolder=(MyHolder) holder;
        myHolder.rowBinding.setModel(timeList.get(position));
        myHolder.rowBinding.setLang(lang);

        if (context instanceof OwnerBookingDetailsActivity){
            OwnerBookingDetailsActivity activity=(OwnerBookingDetailsActivity) context;
        }

        if (oldHolder==null){
            oldHolder=myHolder;
        }
        myHolder.rowBinding.llItem.setOnClickListener(view -> {
            if (oldHolder!=null){
                OwnerHistoryModel oldTime=timeList.get(oldPos);
                oldTime.setSelected(false);
                timeList.set(oldPos,oldTime);

                MyHolder oHolder=(MyHolder) oldHolder;
                oHolder.rowBinding.setModel(oldTime);
            }
            currentPos=myHolder.getAdapterPosition();
            OwnerHistoryModel model=timeList.get(currentPos);
            model.setSelected(true);
            timeList.set(currentPos,model);
            myHolder.rowBinding.setModel(model);

            oldHolder=myHolder;
            oldPos=currentPos;

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
        private BookingDetailsRowBinding rowBinding;

        public MyHolder(@NonNull BookingDetailsRowBinding rowBinding) {
            super(rowBinding.getRoot());
            this.rowBinding = rowBinding;
        }
    }
    public void updateList(List<OwnerHistoryModel> list) {
        if (list != null) {
            this.timeList = list;
        }
        notifyDataSetChanged();
    }
}

