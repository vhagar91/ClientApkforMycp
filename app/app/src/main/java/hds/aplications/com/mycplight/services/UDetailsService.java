package hds.aplications.com.mycp.services;

import java.util.List;

import hds.aplications.com.mycp.models.UDetails;
import retrofit.Callback;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;


public interface UDetailsService {

    @GET("/api/v1/availables/{code}.{format}")
    void getAll(@Path("code") long code, @Path("format") String format, @Query("key") String key, Callback<List<UDetails>> callback);

    @FormUrlEncoded
    @POST("/api/v1/addavailablerooms/{code}.{format}")
    void add(@Path("code") long code, @Field("from_date") String fromDate, @Field("to_date") String toDate, @Field("reason") String reason, @Path("format") String format, @Field("key") String key, Callback<UDetails> callback);

    @FormUrlEncoded
    @POST("/api/v1/addavailableroombyranges/{code}.{format}")
    void addByRanges(@Path("code") long code, @Path("format") String format, @Field("key") String key, @Field("start") String start, @Field("end") String end, @Field("date_range") String dateRange, @Field("reason") String reason, Callback<UDetails> callback);

    @DELETE("/api/v1/delavailablerooms/{code}.{format}")
    void delete(@Path("code") long code, @Field("from_date") String fromDate, @Field("to_date") String toDate, @Field("ud_id") String id, @Path("format") String format, @Field("key") String key, Callback<UDetails> callback);

}
