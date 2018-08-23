package hds.aplications.com.mycp.services;

import android.content.Context;

import java.util.Date;
import java.util.List;

import hds.aplications.com.mycp.helpers.SAppData;
import hds.aplications.com.mycp.models.Answer;
import hds.aplications.com.mycp.models.Reservation;
import hds.aplications.com.mycp.models.User;
import mgleon.common.com.DateUtils;
import mgleon.common.com.services.BaseClient;
import retrofit.Callback;
import retrofit.http.Field;


public class ReservationClient extends BaseClient{

    public ReservationClient(Context context) {
        super(ReservationService.class, context);
    }

    /*public void getReservationsByUser(String idUser, String start, Callback<User> callback){
        ((ReservationService)this.service).getReservations(this.getConfigApi().getmResponseFormat(), this.getConfigApi().getmKey(), idUser, start, callback);
    }*/

    public void addReservation(String checkDispo, String fromDate, String toDate, String idsRooms, String adults, String kids, String hasCompleteReservation, Callback<Answer> callback){
        User user = SAppData.getInstance().getUser();
        if(user == null){
            return;
        }

        ((ReservationService)this.service).addReservation(this.getConfigApi().getmResponseFormat(), this.getConfigApi().getmKey(), user.getToken(), String.valueOf(user.getIdRef()),
                checkDispo, fromDate, toDate, idsRooms, adults, kids, hasCompleteReservation, callback);
    }

    public void payReservation(String reservations, String amount, Callback<Answer> callback){
        User user = SAppData.getInstance().getUser();
        if(user == null){
            return;
        }

        ((ReservationService)this.service).payReservation(this.getConfigApi().getmResponseFormat(), this.getConfigApi().getmKey(), user.getToken(), String.valueOf(user.getIdRef()),
                reservations, user.getCurrency().getCode(), amount, user.getLangCode(), callback);
    }

    public void getReservations(Callback<List<Reservation>> callback){
        User user = SAppData.getInstance().getUser();
        if(user == null){
            return;
        }

        Date date = DateUtils.beginningDay(new Date());
        ((ReservationService)this.service).getReservations(this.getConfigApi().getmResponseFormat(), this.getConfigApi().getmKey(), user.getToken(), String.valueOf(user.getIdRef()),
                DateUtils.generateStringDate(date), user.getCurrency().getCode(), callback);
    }
    public void getReservationsBooked(Callback<List<Reservation>> callback){
        User user = SAppData.getInstance().getUser();
        if(user == null){
            return;
        }

        Date date = DateUtils.beginningDay(new Date());
        ((ReservationService)this.service).getReservationsBooked(this.getConfigApi().getmResponseFormat(), this.getConfigApi().getmKey(), user.getToken(), String.valueOf(user.getIdRef()),
                "5",null, user.getCurrency().getCode(), callback);
    }
    public void getReservationsPending(Callback<List<Reservation>> callback){
        User user = SAppData.getInstance().getUser();
        if(user == null){
            return;
        }

        Date date = DateUtils.beginningDay(new Date());
        ((ReservationService)this.service).getReservationsBooked(this.getConfigApi().getmResponseFormat(), this.getConfigApi().getmKey(), user.getToken(), String.valueOf(user.getIdRef()),
                "0",null, user.getCurrency().getCode(), callback);
    }
    public void getReservationsAvailable(Callback<List<Reservation>> callback){
        User user = SAppData.getInstance().getUser();
        if(user == null){
            return;
        }

        Date date = DateUtils.beginningDay(new Date());
        ((ReservationService)this.service).getReservationsBooked(this.getConfigApi().getmResponseFormat(), this.getConfigApi().getmKey(), user.getToken(), String.valueOf(user.getIdRef()),
                "1",null, user.getCurrency().getCode(), callback);
    }

    public void getReservationsCount(Callback<Answer> callback){
        User user = SAppData.getInstance().getUser();
        if(user == null){
            return;
        }

        Date date = DateUtils.beginningDay(new Date());
        ((ReservationService)this.service).getReservationsCount(this.getConfigApi().getmResponseFormat(), this.getConfigApi().getmKey(), user.getToken(), String.valueOf(user.getIdRef()),
                DateUtils.generateStringDate(date), user.getCurrency().getCode(), "1", callback);
    }
}
