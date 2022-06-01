package com.finalproject.ui.user.activity_booking_seats;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.finalproject.R;
import com.finalproject.adapter.DayAdapter;
import com.finalproject.adapter.SpinnerTicketTypeAdapter;
import com.finalproject.adapter.TimeAdapter;
import com.finalproject.databinding.ActivityBookingSeatsBinding;
import com.finalproject.databinding.DialogSeatsBinding;
import com.finalproject.model.BookCinemaModel;
import com.finalproject.model.CinemaModel;
import com.finalproject.model.DayModel;
import com.finalproject.model.PostModel;
import com.finalproject.model.TicketTypeModel;
import com.finalproject.model.TimeModel;
import com.finalproject.model.UserModel;
import com.finalproject.mvvm.ActivityBookingSeatsMvvm;
import com.finalproject.ui.common_uis.activity_base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class BookingSeatsActivity extends BaseActivity {
    private String lang;
    private ActivityBookingSeatsBinding binding;
    private BookCinemaModel bookCinemaModel;
    private UserModel userModel;
    private DialogSeatsBinding seatsBinding;
    private PostModel model;
    private CinemaModel cinemaModel;
    private DayModel dayModel;
    private TimeModel timeModel;
    private List<DayModel> dayModelList;
    private List<TimeModel> timeModelList;
    private DayAdapter dayAdapter;
    private TimeAdapter timeAdapter;
    private SpinnerTicketTypeAdapter spinnerTicketTypeAdapter;
    private List<TicketTypeModel> ticketTypeModelList;
    private AlertDialog dialog;
    private ActivityBookingSeatsMvvm mvvm;
    private String id;
    private String available_seats = "";
    private String selected_seats;
    private int total_price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_booking_seats);
        getDataFromIntent();
        initView();


    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        model = (PostModel) intent.getSerializableExtra("postModel");
        cinemaModel = (CinemaModel) intent.getSerializableExtra("cinemaModel");

    }

    private void initView() {
        bookCinemaModel = new BookCinemaModel();
        binding.setModel(bookCinemaModel);
        userModel = getUserModel();
        bookCinemaModel.setUser_id(userModel.getData().getId());
        setUpToolbar(binding.toolbar, getString(R.string.booking_seats), R.color.color2, R.color.white);
        binding.toolbar.llBack.setOnClickListener(view -> {
            finish();
        });
        ticketTypeModelList = new ArrayList<>();
        ticketTypeModelList.add(0, new TicketTypeModel(getResources().getString(R.string.ticket_type)));
        ticketTypeModelList.add(1, new TicketTypeModel("normal"));
        ticketTypeModelList.add(2, new TicketTypeModel("VIP"));
        dayModel = new DayModel();
        timeModel = new TimeModel();
        dayModelList = new ArrayList<>();
        timeModelList = new ArrayList<>();
        binding.setPostModel(model);
        binding.setCinemaModel(cinemaModel);

        bookCinemaModel.setCinema_id(cinemaModel.getModel().getId());
        bookCinemaModel.setPost_id(model.getId());


        mvvm = ViewModelProviders.of(this).get(ActivityBookingSeatsMvvm.class);

        mvvm.getOnDaySuccess().observe(this, dayModels -> {
            if (dayModels.size() > 0) {
                binding.tvNoDays.setVisibility(View.GONE);
                if (dayAdapter != null) {
                    dayModelList.clear();
                    dayModelList.addAll(dayModels);
                    dayAdapter.updateList(dayModels);
                }
            } else {
                binding.tvNoDays.setVisibility(View.VISIBLE);
                dayModelList = new ArrayList<>();
            }
        });
        mvvm.getOnTimeSuccess().observe(this, timeModels -> {
            if (timeModels.size() > 0) {
                binding.tvNoTimes.setVisibility(View.GONE);
                if (timeAdapter != null) {
                    timeModelList.clear();
                    timeModelList.addAll(timeModels);
                    timeAdapter.updateList(timeModels);
                }
            } else {
                binding.tvNoTimes.setVisibility(View.VISIBLE);
                timeModelList = new ArrayList<>();
            }
        });
        mvvm.getDays(cinemaModel.getModel().getId(), model.getId());
        mvvm.getOnSeatsSuccess().observe(this, seatsModel -> {
            if (seatsModel.getData() != null) {
                seatsBinding.setSeatsModel(seatsModel);
                available_seats = seatsModel.getData().getChairs_available();
            }
        });

        spinnerTicketTypeAdapter = new SpinnerTicketTypeAdapter(ticketTypeModelList, this);
        binding.spinnerTicketType.setAdapter(spinnerTicketTypeAdapter);
        binding.spinnerTicketType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    bookCinemaModel.setTicket_type("0");
                } else {
                    bookCinemaModel.setTicket_type(ticketTypeModelList.get(i).getTitle());

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dayAdapter = new DayAdapter(dayModelList, this);
        timeAdapter = new TimeAdapter(timeModelList, this);
        binding.recViewDay.setLayoutManager(new GridLayoutManager(this, 3));
        binding.recViewTime.setLayoutManager(new GridLayoutManager(this, 3));
        binding.recViewDay.setAdapter(dayAdapter);
        binding.recViewTime.setAdapter(timeAdapter);

        binding.llTime.setEnabled(false);
        binding.llChooseSeats.setEnabled(false);
        binding.llDay.setOnClickListener(view -> openDays());
        binding.llTime.setOnClickListener(view -> openTimes());
        binding.closeDay.setOnClickListener(view -> binding.flDay.setVisibility(View.GONE));
        binding.closeTime.setOnClickListener(view -> binding.flTime.setVisibility(View.GONE));
        binding.tvDone1.setOnClickListener(view -> {
            if (dayModel.getDay() != null) {
                binding.flDay.setVisibility(View.GONE);
                binding.tvDay.setText(dayModel.getDay());
                bookCinemaModel.setDay_id(dayModel.getId());
                binding.llTime.setEnabled(true);
                binding.tvTime.setText(getResources().getString(R.string.time));
                timeModel = new TimeModel();
                mvvm.getTimes(dayModel);
            }

        });
        binding.tvDone2.setOnClickListener(view -> {
            if (timeModel.getHour() != null) {
                binding.flTime.setVisibility(View.GONE);
                binding.tvTime.setText(timeModel.getHour());
                bookCinemaModel.setHour_id(timeModel.getId());
                binding.llChooseSeats.setEnabled(true);
                mvvm.getSeats(this, cinemaModel.getModel(), dayModel, timeModel);
            }

        });

        binding.llChooseSeats.setOnClickListener(view -> {
            if (!available_seats.isEmpty()) {
                dialog.show();
            } else {
                Toast.makeText(this, R.string.no_seats_available, Toast.LENGTH_SHORT).show();
            }

        });

        createSeatsDialog();

        mvvm.book.observe(this, aBoolean -> {
            if (aBoolean) {
                Toast.makeText(this, getResources().getString(R.string.booked_sucess), Toast.LENGTH_LONG).show();
                setResult(RESULT_OK);
                finish();
            }
        });

        binding.btnBook.setOnClickListener(view -> {
            if (bookCinemaModel.isDataValid(BookingSeatsActivity.this)) {
                mvvm.book(bookCinemaModel, userModel, this);
            }
        });
    }


    private void openDays() {
        binding.flDay.setVisibility(View.VISIBLE);
    }

    private void openTimes() {
        binding.flTime.setVisibility(View.VISIBLE);
    }

    private void createSeatsDialog() {

        dialog = new AlertDialog.Builder(this)
                .create();
        seatsBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_seats, null, false);

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(seatsBinding.getRoot());

        seatsBinding.btnConfirm.setOnClickListener(view -> {

            selected_seats = seatsBinding.number.getText().toString();
            if ((Integer.parseInt(selected_seats) <= Integer.parseInt(available_seats))) {
                bookCinemaModel.setNumber_of_seats(selected_seats);
                total_price = Integer.parseInt(selected_seats) * Integer.parseInt(cinemaModel.getModel().getPrice());
                binding.totalPrice.setText(total_price + " ");
                bookCinemaModel.setTotal_price(String.valueOf(total_price));
                binding.numberBooked.setText(seatsBinding.number.getText().toString());
                dialog.dismiss();
            } else {
                Toast.makeText(this, R.string.selected_is_greater_than_available, Toast.LENGTH_SHORT).show();
            }

        });
        seatsBinding.btnCancel.setOnClickListener(view -> dialog.dismiss());


    }

    public void setDayItem(DayModel model) {
        binding.tvDone1.setVisibility(View.VISIBLE);
        this.dayModel = model;
//        Log.e("kkkkk", "hhhhh");

    }

    public void setTimeItem(TimeModel model) {
        binding.tvDone2.setVisibility(View.VISIBLE);
        this.timeModel = model;
    }

}
