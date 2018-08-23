package hds.aplications.com.mycp.services;

import java.util.List;

import hds.aplications.com.mycp.models.Accommodation;
import hds.aplications.com.mycp.models.Destination;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Miguel Gomez Leon on 11/24/15.
 * mgleonsc@gmail.com
 */
public interface DestinationService{
    @GET("/api/mcp_v1/destinations.{format}")
    void getDestinations(@Path("format") String format, @Query("key") String key, Callback<List<Destination>> callback);
}
