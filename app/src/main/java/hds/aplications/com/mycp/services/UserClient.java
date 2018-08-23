package hds.aplications.com.mycp.services;

import android.content.Context;

import hds.aplications.com.mycp.helpers.SAppData;
import hds.aplications.com.mycp.models.Answer;
import hds.aplications.com.mycp.models.User;
import mgleon.common.com.services.BaseClient;
import retrofit.Callback;


public class UserClient extends BaseClient{
    //public AccommodationService service;

    public UserClient(Context context) {
        super(UserService.class, context);
    }

    public void login(String user, String password, String start, Callback<User> callback){
        ((UserService)this.service).login(this.getConfigApi().getmResponseFormat(), this.getConfigApi().getmKey(), user, password, start, callback);
    }

    public void favorite(String idUser, String token, String idOwn, String action, Callback<Answer> callback){
        ((UserService)this.service).favorite(this.getConfigApi().getmResponseFormat(), this.getConfigApi().getmKey(), idUser, token, idOwn, action, callback);
    }

    public User login(String user, String password, String start){
        return ((UserService)this.service).synclogin(this.getConfigApi().getmResponseFormat(), this.getConfigApi().getmKey(), user, password, start);
    }

    public void getReservations(String id, String start, Callback<User> callback){
        ((UserService)this.service).getReservations(this.getConfigApi().getmResponseFormat(), this.getConfigApi().getmKey(), id, start, callback);
    }

    public void register(User user, Callback<User> callback){
        ((UserService)this.service).register(this.getConfigApi().getmResponseFormat(), this.getConfigApi().getmKey(), user.getFirstName(), user.getLastName(), user.getPassword(),
                user.getEmail(), String.valueOf(user.getCountry().getIdRef()), callback);
    }

    public void changePass(String password, Callback<Answer> callback){
        User user = SAppData.getInstance().getUser();
        if(user == null){
            return;
        }
        String idUser = String.valueOf(user.getIdRef());
        String token = user.getToken();

        ((UserService)this.service).changePass(this.getConfigApi().getmResponseFormat(), this.getConfigApi().getmKey(), idUser, token, password, callback);
    }
    public void editProfile(String email,String countr_code,String phone,String city, Callback<User> callback){
        User user = SAppData.getInstance().getUser();
        if(user == null){
            return;
        }
        String idUser = String.valueOf(user.getIdRef());


        ((UserService)this.service).updateprofile(this.getConfigApi().getmResponseFormat(), this.getConfigApi().getmKey(), idUser, phone,email,countr_code,city, callback);
    }
}
