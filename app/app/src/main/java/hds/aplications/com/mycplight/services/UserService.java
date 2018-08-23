package hds.aplications.com.mycp.services;

import hds.aplications.com.mycp.models.User;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Miguel Gomez Leon on 11/24/15.
 * mgleonsc@gmail.com
 */
public interface UserService {

    @FormUrlEncoded
    @POST("/api/v1/logins.{format}")
    void login(@Path("format") String format, @Field("key") String key, @Field("user") String user, @Field("password") String password, @Field("start") String start, @Field("end") String end, Callback<User> callback);

}
