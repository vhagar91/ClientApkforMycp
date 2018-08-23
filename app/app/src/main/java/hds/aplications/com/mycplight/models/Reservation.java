package hds.aplications.com.mycp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

import java.util.Date;



@Table(name = "reservation", id = "_id")
public class Reservation extends Model {

    @Column(name = "idRef")
    @SerializedName("own_res_id")
    public long idRef;

    @Column(name = "date_from")
    @SerializedName("own_res_reservation_from_date")
    public Date dateFrom;

    @Column(name = "date_to")
    @SerializedName("own_res_reservation_to_date")
    public Date dateTo;

    @Column(name = "description")
    public String description;

    @Column(name = "id_status")
    public ReservationStatus status;

    @Column(name = "id_room", onDelete = Column.ForeignKeyAction.CASCADE)
    public Room room;

    //Just for API usage
    @SerializedName("own_res_status")
    public String reservationStatusApiField;

    public Reservation() {
    }

    public Reservation(long idRef, Date dateFrom, Date dateTo, String description, ReservationStatus status, Room room) {
        this.idRef = idRef;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.description = description;
        this.status = status;
        this.room = room;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReservationStatusApiField() {
        return reservationStatusApiField;
    }

    public void setReservationStatusApiField(String reservationStatusApiField) {
        this.reservationStatusApiField = reservationStatusApiField;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
