package hds.aplications.com.mycp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;



public class Answer{

    @SerializedName("success")
    Boolean success;

    @SerializedName("msg")
    String msg;

    @SerializedName("reservations")
    List<ReservationDetail> reservationDetails;

    @SerializedName("url")
    String url;

    public Answer() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean isSuccess(){
        return success;
    }

    public void setReservationDetails(List<ReservationDetail> reservationDetails){
        this.reservationDetails = reservationDetails;
    }

    public List<ReservationDetail> getReservationDetails(){
        return reservationDetails;
    }

    public String getUrl(){
        return url;
    }
}
