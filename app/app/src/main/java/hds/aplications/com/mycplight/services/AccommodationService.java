package hds.aplications.com.mycp.services;

import hds.aplications.com.mycp.models.Accommodation;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Miguel Gomez Leon on 11/24/15.
 * mgleonsc@gmail.com
 */
public interface AccommodationService{

    @GET("/api/v1/accommodations/{code}.{format}")
    void getAccommodation(@Path("code") String code, @Path("format") String format, @Query("key") String key, Callback<Accommodation> callback);
}
