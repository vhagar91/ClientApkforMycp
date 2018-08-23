package hds.aplications.com.mycp.services;

import java.util.List;

import hds.aplications.com.mycp.models.Country;
import hds.aplications.com.mycp.models.Destination;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Miguel Gomez Leon.
 * mgleonsc@gmail.com
 */
public interface CountryService{
    @GET("/api/mcp_v1/counties.{format}")
    void getAll(@Path("format") String format, @Query("key") String key, Callback<List<Country>> callback);
}
