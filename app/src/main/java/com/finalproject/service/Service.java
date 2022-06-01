package com.finalproject.service;

import com.finalproject.model.AddDayTimeModel;
import com.finalproject.model.CategoryDataModel;
import com.finalproject.model.CinemaDataModel;
import com.finalproject.model.DayDataModel;
import com.finalproject.model.HistoryDataModel;
import com.finalproject.model.HomeDataModel;
import com.finalproject.model.DetailsDataModel;
import com.finalproject.model.MoviesDataModel;
import com.finalproject.model.OwnerHistoryDataModel;
import com.finalproject.model.PostDataModel;
import com.finalproject.model.SeatsModel;
import com.finalproject.model.ShowDataModel;
import com.finalproject.model.SingleCinemaModel;
import com.finalproject.model.SliderDataModel;
import com.finalproject.model.StatusResponse;
import com.finalproject.model.TimeDataModel;
import com.finalproject.model.UserModel;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Service {

    @Multipart
    @POST("api/register")
    Single<Response<UserModel>> signUp(@Part("name") RequestBody name,
                                       @Part("user_name") RequestBody user_name,
                                       @Part("password") RequestBody password,
                                       @Part("national_id") RequestBody national_id,
                                       @Part("email") RequestBody email,
                                       @Part("gender") RequestBody gender,
                                       @Part("type") RequestBody type,
                                       @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("api/login")
    Single<Response<UserModel>> login(
            @Field("user_name") String user_name,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("api/logoutOrDelete")
    Single<Response<StatusResponse>> logOut(@Field("user_id") String user_id,
                                            @Field("delete") String delete);


    @Multipart
    @POST("api/edit_profile")
    Single<Response<UserModel>> update(@Part("user_id") RequestBody user_id,
                                       @Part("name") RequestBody name,
                                       @Part("user_name") RequestBody user_name,
                                       @Part("national_id") RequestBody national_id,
                                       @Part("gender") RequestBody gender,
                                       @Part("email") RequestBody email,
                                       @Part("password") RequestBody password,
                                       @Part MultipartBody.Part image);


    @GET("api/home")
    Single<Response<SliderDataModel>> getSlider();

    @GET("api/home")
    Single<Response<HomeDataModel>> getHomeData();

    @GET("api/posts/show")
    Single<Response<ShowDataModel>> getShow(@Query("title") String title,
                                            @Query("cinema_id") String cinema_id,
                                            @Query("user_id") String user_id);


    @GET("api/search")
    Single<Response<PostDataModel>> getSearch(@Query("text") String text);

    @GET("api/posts/move")
    Single<Response<MoviesDataModel>> getMovies(@Query("category_id") String category_id,
                                                @Query("title") String title,
                                                @Query("cinema_id") String cinema_id,
                                                @Query("user_id") String user_id);

    @GET("api/categories")
    Single<Response<CategoryDataModel>> getCategories();

    @GET("api/onePost")
    Single<Response<DetailsDataModel>> getDetails(@Query("post_id") String post_id);


    @FormUrlEncoded
    @POST("api/contact_us")
    Single<Response<StatusResponse>> ContactUs(
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("message") String message
    );

    @GET("api/cinemas")
    Single<Response<CinemaDataModel>> getCinemas(@Query("post_id") String post_id);

    @GET("api/postDays")
    Single<Response<DayDataModel>> getDays(@Query("cinema_id") String cinema_id,
                                           @Query("post_id") String post_id);

    @GET("api/hoursOfDay")
    Single<Response<TimeDataModel>> getTimes(@Query("day_id") String day_id);

    @GET("api/chairStatus")
    Single<Response<SeatsModel>> getSeats(@Query("cinema_id") String cinema_id,
                                          @Query("day_id") String day_id,
                                          @Query("hour_id") String hour_id);

    @FormUrlEncoded
    @POST("api/makeReservation")
    Single<Response<StatusResponse>> book(
            @Field("user_id") String user_id,
            @Field("cinema_id") String cinema_id,
            @Field("post_id") String post_id,
            @Field("day_id") String day_id,
            @Field("hour_id") String hour_id,
            @Field("number_of_seats") String number_of_seats,
            @Field("total_price") String total_price,
            @Field("ticket_type") String ticket_type);

    @GET("api/myReservations")
    Single<Response<HistoryDataModel>> getHistory(@Query("user_id") String user_id);

    @FormUrlEncoded
    @POST("api/cancelReservation")
    Single<Response<StatusResponse>> cancelBooking(@Field("reservation_id") String reservation_id);

    @FormUrlEncoded
    @POST("api/createCinema")
    Single<Response<SingleCinemaModel>> createCinema(
            @Field("user_id") String user_id,
            @Field("title") String title,
            @Field("location") String location,
            @Field("chairs_count") String chairs_count,
            @Field("price") String price);

    @FormUrlEncoded
    @POST("api/addToMyCinema")
    Single<Response<StatusResponse>> addRemoveFromCinema(@Field("cinema_id") String cinema_id,
                                                         @Field("post_id") String post_id);

    @POST("api/addPostDaysAndTimes")
    Single<Response<StatusResponse>> addDayAndTime(@Body AddDayTimeModel addDayTimeModel);


    @GET("api/myCinema")
    Single<Response<PostDataModel>> getCinemaData(@Query("cinema_id") String cinema_id,
                                                  @Query("type") String type);

    @GET("api/daysOfMovies")
    Single<Response<DayDataModel>> getOwnerDays(@Query("cinema_id") String cinema_id,
                                                @Query("post_id") String post_id);

    @GET("api/bookingDetails")
    Single<Response<OwnerHistoryDataModel>> getOwnerBookingsDetails(@Query("cinema_id") String cinema_id,
                                                                    @Query("post_id") String post_id,
                                                                    @Query("day_id") String day_id);


}
