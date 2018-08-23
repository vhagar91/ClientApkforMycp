package hds.aplications.com.mycp.services;

import java.util.List;

import hds.aplications.com.mycp.models.Answer;
import hds.aplications.com.mycp.models.Reservation;
import hds.aplications.com.mycp.models.User;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;


public interface ReservationService {

    @FormUrlEncoded
    @POST("/api/mcp_v1/adds/tos/carts.{format}")
    void addReservation(@Path("format") String format, @Field("key") String key, @Field("token") String token, @Field("user_id") String userId,
                        @Field("check_dispo") String checkDispo, @Field("from_date") String fromDate, @Field("to_date") String toDate, @Field("ids_rooms") String idsRooms,
                        @Field("adults") String adults, @Field("kids") String kids, @Field("hasCompleteReservation") String hasCompleteReservation, Callback<Answer> callback);

    @FormUrlEncoded
    @POST("/api/mcp_v1/pays/reservations.{format}")
    void payReservation(@Path("format") String format, @Field("key") String key, @Field("token") String token, @Field("user_id") String userId,
                        @Field("reservations_list") String reservations, @Query("currency") String currency, @Query("amount") String amount,
                        @Query("language_code") String languageCode, Callback<Answer> callback);

    @GET("/api/mcp_v1/tourist/reservation/list.{format}")
    void getReservations(@Path("format") String format, @Query("key") String key, @Query("token") String token, @Query("user_id") String userId, @Query("date") String mDate,
                         @Query("currency") String currency, Callback<List<Reservation>> callback);

    @GET("/api/mcp_v1/tourist/reservation/list/booked.{format}")
    void getReservationsBooked(@Path("format") String format, @Query("key") String key, @Query("token") String token, @Query("user_id") String userId,@Query("status") String status, @Query("date") String mDate,
                         @Query("currency") String currency, Callback<List<Reservation>> callback);


    @GET("/api/mcp_v1/tourist/reservation/list.{format}")
    void getReservationsCount(@Path("format") String format, @Query("key") String key, @Query("token") String token, @Query("user_id") String userId, @Query("date") String mDate,
                         @Query("currency") String currency, @Query("count") String count, Callback<Answer> callback);
}
