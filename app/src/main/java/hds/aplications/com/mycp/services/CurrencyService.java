package hds.aplications.com.mycp.services;

import java.util.List;

import hds.aplications.com.mycp.models.Answer;
import hds.aplications.com.mycp.models.Country;
import hds.aplications.com.mycp.models.Currency;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Miguel Gomez Leon.
 * mgleonsc@gmail.com
 */
public interface CurrencyService{
    @GET("/api/mcp_v1/currencies.{format}")
    void getAll(@Path("format") String format, @Query("key") String key, Callback<List<Currency>> callback);

    @FormUrlEncoded
    @POST("/api/mcp_v1/changes/currencies.{format}")
    void change(@Path("format") String format, @Field("key") String key , @Field("user_id") String userId, @Field("token") String token, @Field("currency_id") String currencyId, Callback<Answer> callback);
}
