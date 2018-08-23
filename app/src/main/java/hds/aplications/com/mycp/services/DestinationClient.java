package hds.aplications.com.mycp.services;

import android.content.Context;

import com.activeandroid.ActiveAndroid;

import java.util.List;

import hds.aplications.com.mycp.models.Accommodation;
import hds.aplications.com.mycp.models.Destination;
import hds.aplications.com.mycp.models.Room;
import hds.aplications.com.mycp.models.RoomType;
import mgleon.common.com.MessageToast;
import mgleon.common.com.repositories.BaseRepository;
import mgleon.common.com.services.BaseClient;
import mgleon.common.com.services.Util.ProcessRetrofitError;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Miguel Gomez Leon on 11/24/15.
 * mgleonsc@gmail.com
 */
public class DestinationClient extends BaseClient{
    public DestinationClient(Context context) {
        super(DestinationService.class, context);
    }

    public void getDestinations(Callback<List<Destination>> callback) {
        ((DestinationService) this.service).getDestinations(this.getConfigApi().getmResponseFormat(), this.getConfigApi().getmKey(), callback);
    }
}
