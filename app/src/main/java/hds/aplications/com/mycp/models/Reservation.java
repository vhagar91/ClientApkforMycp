package hds.aplications.com.mycp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;



@Table(name = "reservation", id = "_id")
public class Reservation extends Model {

    @Column(name = "id_ref")
    @SerializedName("gen_res_id")
    public long idRef;

    @Column(name = "date_from")
    @SerializedName("gen_res_from_date")
    public Date dateFrom;

    @Column(name = "date_to")
    @SerializedName("gen_res_to_date")
    public Date dateTo;

    @Column(name = "id_status")
    @SerializedName("own_res_status")
    public ReservationStatus status;

    public String getBooking() {
        return booking;
    }

    public void setBooking(String booking) {
        this.booking = booking;
    }

    @Column(name = "id_booking")
    @SerializedName("booking")
    public String booking;

    @Column(name = "id_user", onDelete = Column.ForeignKeyAction.CASCADE)
    public User user;

    @Column(name = "id_accommodation", onDelete = Column.ForeignKeyAction.CASCADE)
    public Accommodation accommodation;

    //Just for API usage
    @SerializedName("reservation_status")
    public String reservationStatusApiField;

    @SerializedName("details")
    List<ReservationDetail> reservationDetails;


    public Reservation() {
    }

    public Reservation(long idRef, Date dateFrom, Date dateTo, ReservationStatus status, User user, Accommodation accommodation) {
        this.idRef = idRef;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.status = status;
        this.user = user;
        this.accommodation = accommodation;
    }

    public long getIdRef() {
        return idRef;
    }

    public void setIdRef(long idRef) {
        this.idRef = idRef;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getReservationStatusApiField() {
        return reservationStatusApiField;
    }

    public void setReservationStatusApiField(String reservationStatusApiField) {
        this.reservationStatusApiField = reservationStatusApiField;
    }

    public List<ReservationDetail> getReservationDetails() {
//        List<ReservationDetail> reservationDetails = getMany(ReservationDetail.class, "id_reservation");
//        if(reservationDetails.size() > 0){
//            this.reservationDetails = reservationDetails;
//        }
        return this.reservationDetails;
    }
    public List<ReservationDetail> getReservationDetails1() {
        List<ReservationDetail> reservationDetails = getMany(ReservationDetail.class, "id_reservation");
        if(reservationDetails.size() > 0){
            this.reservationDetails = reservationDetails;
        }
        return this.reservationDetails;
    }
    public void setReservationDetails(List<ReservationDetail> reservationDetails){
        this.reservationDetails = reservationDetails;
    }
}
