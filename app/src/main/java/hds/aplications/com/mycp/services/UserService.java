package hds.aplications.com.mycp.services;

import hds.aplications.com.mycp.models.Answer;
import hds.aplications.com.mycp.models.User;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;


public interface UserService {

    @FormUrlEncoded
    @POST("/api/mcp_v1/logins/mcps/apps.{format}")
    void login(@Path("format") String format, @Field("key") String key , @Field("user") String user, @Field("password") String password, @Field("start") String start, Callback<User> callback);

    @FormUrlEncoded
    @POST("/api/mcp_v1/favorites.{format}")
    void favorite(@Path("format") String format, @Field("key") String key , @Field("user_id") String userId, @Field("token") String token, @Field("ownership_id") String ownershipId, @Field("action") String action, Callback<Answer> callback);

    @FormUrlEncoded
    @POST("/api/mcp_v1/changes/passes.{format}")
    void changePass(@Path("format") String format, @Field("key") String key , @Field("user_id") String userId, @Field("token") String token, @Field("password") String password, Callback<Answer> callback);
    /***********************/

    @FormUrlEncoded
    @POST("/api/v1/logins.{format}")
    User synclogin(@Path("format") String format, @Field("key") String key , @Field("user") String user, @Field("password") String password, @Field("start") String start);

    @GET("/api/v1/reservationclients/{userid}.{format}")
    void getReservations(@Path("format") String format, @Query("key") String key, @Path("userid") String id, @Query("start") String start, Callback<User> callback);

    @FormUrlEncoded
    @POST("/api/mcp_v1/registers/users.{format}")
    void register(@Path("format") String format, @Field("key") String key , @Field("name") String firstName, @Field("last_name") String lastName, @Field("password") String password,
                  @Field("email") String email, @Field("country_id") String country_id, Callback<User> callback);
    @FormUrlEncoded
    @POST("/api/mcp_v1/profiles/users.{format}")
    void updateprofile(@Path("format") String format, @Field("key") String key , @Field("user_id") String user_id, @Field("phone") String phone, @Field("email") String email,@Field("country_code") String country_code,@Field("city") String city, Callback<User> callback);

}
