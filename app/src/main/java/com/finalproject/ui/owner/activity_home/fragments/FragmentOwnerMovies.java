package com.finalproject.ui.owner.activity_home.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.finalproject.R;
import com.finalproject.adapter.DayOwnerAdapter;
import com.finalproject.adapter.MoviesAdapter;
import com.finalproject.adapter.MoviesFilterAdapter;
import com.finalproject.adapter.TimeOwnerAdapter;
import com.finalproject.databinding.FragmentOwnerMoviesBinding;
import com.finalproject.model.AddDayTimeModel;
import com.finalproject.model.CategoryModel;
import com.finalproject.model.Day;
import com.finalproject.model.PostModel;
import com.finalproject.model.Time;
import com.finalproject.mvvm.FragmentMoviesMvvm;
import com.finalproject.ui.common_uis.activity_base.BaseFragment;
import com.finalproject.ui.owner.activity_home.OwnerHomeActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class FragmentOwnerMovies extends BaseFragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private OwnerHomeActivity activity;
    private FragmentOwnerMoviesBinding binding;
    private MoviesFilterAdapter moviesFilterAdapter;
    private MoviesAdapter moviesAdapter;
    private FragmentMoviesMvvm mvvm;
    private BottomSheetBehavior behavior;
    private DayOwnerAdapter dayAdapter;
    private List<Day> dayModelList;
    private TimeOwnerAdapter timeOwnerAdapter;
    private List<PostModel> movieModelList;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private String date = null;
    private List<Time> list;
    private int pos;
    private AddDayTimeModel addDayTimeModel;
    private Day dayModel;
    private String added="";

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (OwnerHomeActivity) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_owner_movies, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {

        movieModelList = new ArrayList<>();
        dayModelList = new ArrayList<>();
        list = new ArrayList<>();
        behavior = BottomSheetBehavior.from(binding.sheet.root);
        binding.sheet.setLang(getLang());
        mvvm = ViewModelProviders.of(this).get(FragmentMoviesMvvm.class);
        mvvm.getIsLoading().observe(activity, isLoading -> {
            binding.swipeRef.setRefreshing(isLoading);
        });

        mvvm.getOnCategorySuccess().observe(activity, categoryModels -> {
            if (categoryModels.size() > 0) {
                if (moviesFilterAdapter != null) {
                    moviesFilterAdapter.updateList(categoryModels);
                    binding.recyclerMovies.setVisibility(View.VISIBLE);
                }
            }

        });
        mvvm.getCategory();
        mvvm.getOnMoviesSuccess().observe(activity, movieModels -> {
            if (mvvm.getCategoryId() != null) {
                if (movieModels.size() > 0) {
                    binding.cardNoData.setVisibility(View.GONE);
                    if (moviesAdapter != null) {
                        movieModelList.clear();
                        movieModelList.addAll(movieModels);
                        moviesAdapter.updateList(movieModels);
                    }
                } else {
                    binding.cardNoData.setVisibility(View.VISIBLE);
                    moviesAdapter.updateList(new ArrayList<>());
                }
            }

        });

        binding.sheet.llChooseDay.setOnClickListener(view -> datePickerDialog.show(activity.getFragmentManager(), ""));

        createDateDialog();

        createTimeDialog();

        dayAdapter = new DayOwnerAdapter(dayModelList, this, activity);
        binding.sheet.recViewDays.setLayoutManager(new LinearLayoutManager(activity));
        binding.sheet.recViewDays.setAdapter(dayAdapter);
        binding.swipeRef.setColorSchemeResources(R.color.colorPrimary);

        binding.swipeRef.setOnRefreshListener(() -> {
            if (mvvm.getCategoryId().getValue() != null) {
                mvvm.getMovies(null,getUserModel().getData().getCinema().getId(),getUserModel().getData().getId());
            } else {
                binding.swipeRef.setRefreshing(false);
            }
        });
        moviesFilterAdapter = new MoviesFilterAdapter(activity, this);
        binding.recyclerFilter.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        binding.recyclerFilter.setAdapter(moviesFilterAdapter);


        moviesAdapter = new MoviesAdapter(activity, this);
        binding.recyclerMovies.setLayoutManager(new GridLayoutManager(activity, 2));
        binding.recyclerMovies.setAdapter(moviesAdapter);


        mvvm.addDayTime.observe(activity, addedDayTime -> {
            if (addedDayTime) {
                Toast.makeText(activity, R.string.added_success, Toast.LENGTH_SHORT).show();
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        mvvm.add.observe(activity, added -> {
            if (added) {
                mvvm.AddDayAndTime(activity, addDayTimeModel);
                mvvm.getMovies(null,getUserModel().getData().getCinema().getId(),getUserModel().getData().getId());
            }
        });

    }

    public void setItemCategory(CategoryModel categoryModel, int currentPos) {
        mvvm.getCategoryId().setValue(categoryModel.getId());
        mvvm.getMovies(null,getUserModel().getData().getCinema().getId(),getUserModel().getData().getId());
    }

    public void setItemChecked(PostModel postModel, int position) {
        dayModelList.clear();
        list.clear();
        dayAdapter.updateList(dayModelList);
        binding.sheet.tvDate.setText("");
        openSheet(postModel, position);
    }


    private void openSheet(PostModel postModel, int position) {
        addDayTimeModel = new AddDayTimeModel();
        addDayTimeModel.setCinema_id(getUserModel().getData().getCinema().getId());
        addDayTimeModel.setPost_id(postModel.getId());
        binding.sheet.btnConfirm.setOnClickListener(view -> {
            if (addDayTimeModel != null) {
                if (dayModelList.size()>0){
                    mvvm.addToCinema(getUserModel().getData().getCinema().getId(), postModel.getId());
                }else {
                    Toast.makeText(activity, R.string.choose_at_least_one_time, Toast.LENGTH_SHORT).show();
                }
            }
            moviesAdapter.notifyItemChanged(position);

        });
        binding.sheet.btnCancel.setOnClickListener(view -> {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            dayModelList.clear();
            list.clear();
            dayAdapter.updateList(dayModelList);
            binding.sheet.tvDate.setText("");
            postModel.setAdded("0");
            moviesAdapter.notifyItemChanged(position);
        });

        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setDraggable(false);
    }

    private void createDateDialog() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
        datePickerDialog.dismissOnPause(true);
        datePickerDialog.setAccentColor(ActivityCompat.getColor(activity, R.color.colorPrimary));
        datePickerDialog.setCancelColor(ActivityCompat.getColor(activity, R.color.gray12));
        datePickerDialog.setOkColor(ActivityCompat.getColor(activity, R.color.colorPrimary));
        datePickerDialog.setMinDate(calendar);
        datePickerDialog.setOkText(getString(R.string.select));
        datePickerDialog.setCancelText(getString(R.string.cancel));
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);

    }

    private void createTimeDialog() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), false);
        timePickerDialog.dismissOnPause(true);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        try {
            if(dayModel !=null&&!dateFormat.parse(dayModel.getDay()).after(dateFormat.parse(dateFormat.format(calendar.getTimeInMillis())))){
                timePickerDialog.setMinTime(calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),calendar.get(Calendar.SECOND));
            }
        } catch (Exception e) {
//            Log.e("ffff",e.toString());
          //  e.printStackTrace();
        }
        timePickerDialog.setAccentColor(ActivityCompat.getColor(activity, R.color.colorPrimary));
        timePickerDialog.setCancelColor(ActivityCompat.getColor(activity, R.color.gray4));
        timePickerDialog.setOkColor(ActivityCompat.getColor(activity, R.color.colorPrimary));
        timePickerDialog.setVersion(TimePickerDialog.Version.VERSION_2);


    }


    @Override
    public void onDateSet(DatePickerDialog datePicker, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        date = dateFormat.format(new Date(calendar.getTimeInMillis()));
        binding.sheet.tvDate.setText(date);
        Day dayModel = new Day(date);

        boolean inList = isItemInDayList(dayModel);
        if (!inList) {
            dayModel.setTimeModelList(new ArrayList<>());
            dayModelList.add(0, dayModel);
            dayAdapter.updateList(dayModelList);
            addDayTimeModel.setDays(dayModelList);

        } else {
            Toast.makeText(activity, R.string.day_added_before, Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        String time = new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(calendar.getTime());
        Time timeModel = new Time(time);
        boolean inList = isItemInTimeList(timeModel);
        if (!inList) {
            list.add(timeModel);
            timeOwnerAdapter.updateList(list);
        } else {
            Toast.makeText(activity, R.string.time_added_before, Toast.LENGTH_SHORT).show();
        }
    }

    public void addNewTime(Day dayModel, int adapterPosition, List<Time> timeModels, TimeOwnerAdapter adapter) {
        // list.clear();
        Calendar calendar = Calendar.getInstance();
        this.list = timeModels;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);

        // dayModel.setTimeModelList(list);
        this.timeOwnerAdapter = adapter;
        this.dayModel =dayModel;
        // adapter.notifyDataSetChanged();
        try {
            if(this.dayModel !=null&&!dateFormat.parse(this.dayModel.getDay()).after(dateFormat.parse(dateFormat.format(calendar.getTimeInMillis())))){
                calendar.setTimeInMillis(System.currentTimeMillis());

                timePickerDialog.setMinTime(calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),calendar.get(Calendar.SECOND));
            }
            else{
               timePickerDialog.setMinTime(0,0,0);
            }
        } catch (Exception e) {
            Log.e("ffff",e.toString());
            //  e.printStackTrace();
        }
        timePickerDialog.show(activity.getFragmentManager(), "");

    }

    public void deleteSelectedDay(int adapterPosition) {
        dayModelList.remove(adapterPosition);
        dayAdapter.notifyItemRemoved(adapterPosition);

    }

    public void deleteSelectedTime(int position, int adapterPosition) {
        dayAdapter.remove(position, adapterPosition);
//        dayModelList.get(position).getTimeModelList().remove(adapterPosition);
//        timeOwnerAdapter.notifyItemRemoved(adapterPosition);
    }

    private boolean isItemInDayList(Day dayModel) {
        for (Day model : dayModelList) {
            if (dayModel.getDay().equals(model.getDay())) {
                return true;
            }
        }
        return false;
    }

    private boolean isItemInTimeList(Time timeModel) {
        for (Time model : list) {
            if (timeModel.getHour().equals(model.getHour())) {
                return true;
            }
        }
        return false;
    }

}