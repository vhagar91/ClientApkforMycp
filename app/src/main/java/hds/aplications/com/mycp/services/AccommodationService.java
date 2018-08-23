package hds.aplications.com.mycp.services;

import java.util.List;

import hds.aplications.com.mycp.models.Accommodation;
import hds.aplications.com.mycp.models.Destination;
import hds.aplications.com.mycp.models.Room;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Miguel Gomez Leon on 11/24/15.
 * mgleonsc@gmail.com
 */
public interface AccommodationService{
    @GET("/api/mcp_v1/accomodation.{format}")
    void getAccommodation(@Path("format") String format, @Query("key") String key, @Query("language") String language, @Query("user_id") String userId,
                          @Query("currency") String currency, @Query("own_id") String ownId, Callback<Accommodation> callback);

    @GET("/api/mcp_v1/accomodations.{format}")
    void getAccommodationsTop(@Path("format") String format, @Query("key") String key, @Query("language") String language, @Query("start") String start,
                              @Query("user_id") String userId, @Query("currency") String currency, @Query("top") String top,
                              Callback<List<Accommodation>> callback);

    @GET("/api/mcp_v1/accomodations.{format}")
    void getAccommodations(@Path("format") String format, @Query("key") String key, @Query("language") String language, @Query("start") String start,
                           @Query("user_id") String userId, @Query("currency") String currency, @Query("destination_id") String destinationId,
                           @Query("guests") String guests, @Query("rooms") String rooms, @Query("from") String from, @Query("to") String to, @Query("favorite") String favorite,
                           Callback<List<Accommodation>> callback);

    @GET("/api/mcp_v1/available/rooms.{format}")
    void getAccommodationRooms(@Path("format") String format, @Query("key") String key, @Query("currency") String currency, @Query("own_id") String ownId,
                               @Query("from") String from, @Query("to") String to, Callback<Accommodation> callback);

    /*old functions*/

    @GET("/api/v1/accommodations/{code}.{format}")
    void getAccommodation(@Path("code") String code, @Path("format") String format, @Query("key") String key, Callback<Accommodation> callback);
}
