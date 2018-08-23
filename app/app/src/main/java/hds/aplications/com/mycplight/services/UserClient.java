package hds.aplications.com.mycp.services;

import android.content.Context;

import hds.aplications.com.mycp.models.User;
import retrofit.Callback;

/**
 * Created by Miguel Gomez Leon on 11/24/15.
 * mgleonsc@gmail.com
 */
public class UserClient extends BaseClient {
    //public AccommodationService service;

    public UserClient(Context context) {
        super(UserService.class, context);
    }

    public void login(String user, String password, String start, String end, Callback<User> callback){
        ((UserService)this.service).login(BaseClient.API_RESPONSE_FORMAT, BaseClient.API_KEY, user, password, start, end, callback);
    }
}
