package hds.aplications.com.mycp.services;

import android.content.Context;

import com.activeandroid.ActiveAndroid;

import hds.aplications.com.mycp.helpers.MessageToast;
import hds.aplications.com.mycp.models.Accommodation;
import hds.aplications.com.mycp.models.AudiovisualType;
import hds.aplications.com.mycp.repositories.BaseRepository;
import hds.aplications.com.mycp.models.BathroomType;
import hds.aplications.com.mycp.models.ClimateType;
import hds.aplications.com.mycp.models.Room;
import hds.aplications.com.mycp.models.RoomType;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Miguel Gomez Leon on 11/24/15.
 * mgleonsc@gmail.com
 */
public class AccommodationClient extends BaseClient {
    //public AccommodationService service;

    public AccommodationClient(Context context) {
        super(AccommodationService.class, context);
    }

    public void getAccommodation(String code){
        Callback<Accommodation> callback = new Callback<Accommodation>() {
            @Override
            public void success(Accommodation accommodation, Response response) {

                ActiveAndroid.beginTransaction();
                try {
                    accommodation.save();
                    for(Room room : accommodation.getRooms() )
                    {
                        room.setAccommodation(accommodation);

                        //Buscar tipo de habitación y si no existe guardarlo
                        room.setRoomType((RoomType)BaseRepository.getByName(room.getRoomTypeApiField(), RoomType.class));

                        //Buscar tipo de climatizacion y si no existe guardarlo
                        room.setClimateType((ClimateType) BaseRepository.getByName(room.getClimateTypeApiField(), ClimateType.class));

                        //Buscar tipo de audiovisual y si no existe guardarlo
                        room.setAudiovisualType((AudiovisualType)BaseRepository.getByName(room.getAudiovisualTypeApiField(), AudiovisualType.class));

                        //Buscar tipo de baño y si no existe guardarlo
                        room.setBathroomType((BathroomType)BaseRepository.getByName(room.getBathroomTypeApiField(), BathroomType.class));

                        room.save();
                    }
                    ActiveAndroid.setTransactionSuccessful();
            }
            catch(Exception ex){
                MessageToast.showError(AccommodationClient.this.context, ex.getLocalizedMessage());
            }
            finally {
                ActiveAndroid.endTransaction();
            }

            }

            @Override
            public void failure(RetrofitError retrofitError) {
                MessageToast.showError(AccommodationClient.this.context, retrofitError.getLocalizedMessage());
            }
        };

        ((AccommodationService)this.service).getAccommodation(code, BaseClient.API_RESPONSE_FORMAT, BaseClient.API_KEY, callback);
    }

    public void getAccommodation(String code, Callback<Accommodation> callback){
        ((AccommodationService)this.service).getAccommodation(code, BaseClient.API_RESPONSE_FORMAT, BaseClient.API_KEY, callback);
    }
}
