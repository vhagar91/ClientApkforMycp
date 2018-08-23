package hds.aplications.com.mycp.services;

import android.content.Context;

import com.activeandroid.ActiveAndroid;

import java.util.List;
import java.util.Locale;

import hds.aplications.com.mycp.helpers.SAppData;
import hds.aplications.com.mycp.models.Accommodation;
import hds.aplications.com.mycp.models.Room;
import hds.aplications.com.mycp.models.User;
import mgleon.common.com.MessageToast;
import mgleon.common.com.services.BaseClient;
import mgleon.common.com.services.Util.ProcessRetrofitError;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Miguel Gomez Leon on 11/24/15.
 * mgleonsc@gmail.com
 */
public class AccommodationClient extends BaseClient{
    //public AccommodationService service;

    public AccommodationClient(Context context) {
        super(AccommodationService.class, context);
    }

    public void getAccommodation(String ownId, Callback<Accommodation> callback) {
        String language = Locale.getDefault().getLanguage().toUpperCase();

        String idUser = "";
        String currency = "";
        User user = SAppData.getInstance().getUser();
        if(user != null){
            idUser = String.valueOf(user.getIdRef());
            currency = user.getCurrency().getCode();
        }
        ((AccommodationService) this.service).getAccommodation(this.getConfigApi().getmResponseFormat(), this.getConfigApi().getmKey(), language, idUser, currency, ownId, callback);
    }

    public void getAccommodationsTop(String startPage, Callback<List<Accommodation>> callback) {
        String language = Locale.getDefault().getLanguage().toUpperCase();

        String idUser = "";
        String currency = "";
        User user = SAppData.getInstance().getUser();
        if(user != null){
            idUser = String.valueOf(user.getIdRef());
            currency = user.getCurrency().getCode();
        }


        ((AccommodationService) this.service).getAccommodationsTop(this.getConfigApi().getmResponseFormat(), this.getConfigApi().getmKey(), language, startPage, idUser, currency, "1", callback);
    }

    public void getAccommodations(String startPage, String destinationId, String guests, String rooms, String from, String to, String favorite, Callback<List<Accommodation>> callback) {
        String language = Locale.getDefault().getLanguage().toUpperCase();

        String idUser = "";
        String currency = "";
        User user = SAppData.getInstance().getUser();
        if(user != null){
            idUser = String.valueOf(user.getIdRef());
            currency = user.getCurrency().getCode();
        }

        ((AccommodationService) this.service).getAccommodations(this.getConfigApi().getmResponseFormat(), this.getConfigApi().getmKey(), language, startPage, idUser, currency, destinationId, guests, rooms, from, to, favorite, callback);
    }

    public void getAccommodationRooms(String ownId, String from, String to, Callback<Accommodation> callback) {
        String language = Locale.getDefault().getLanguage().toUpperCase();

        String currency = "";
        User user = SAppData.getInstance().getUser();
        if(user != null){
            currency = user.getCurrency().getCode();
        }

        ((AccommodationService) this.service).getAccommodationRooms(this.getConfigApi().getmResponseFormat(), this.getConfigApi().getmKey(), currency, ownId, from, to, callback);
    }

    /*Old functions*/

    public void getAccommodation(String code) {
        Callback<Accommodation> callback = new Callback<Accommodation>() {
            @Override
            public void success(Accommodation accommodation, Response response) {

                ActiveAndroid.beginTransaction();
                try {
                    accommodation.save();
                    for (Room room : accommodation.getRooms()) {
                        room.setAccommodation(accommodation);

                        //Buscar tipo de habitación y si no existe guardarlo
                        /*room.setRoomType((RoomType) BaseRepository.getByName(room.getRoomTypeApiField(), RoomType.class));*/

                        room.save();
                    }
                    ActiveAndroid.setTransactionSuccessful();
                } catch (Exception ex) {
                    MessageToast.showError(AccommodationClient.this.context, ex.getLocalizedMessage());
                } finally {
                    ActiveAndroid.endTransaction();
                }

            }

            @Override
            public void failure(RetrofitError retrofitError) {
                ProcessRetrofitError.showError(retrofitError, AccommodationClient.this.context);
            }
        };

        ((AccommodationService) this.service).getAccommodation(code, this.getConfigApi().getmResponseFormat(), this.getConfigApi().getmKey(), callback);
    }

    public void getAccommodationx(String code, Callback<Accommodation> callback) {
        ((AccommodationService) this.service).getAccommodation(code, this.getConfigApi().getmResponseFormat(), this.getConfigApi().getmKey(), callback);
    }
}
