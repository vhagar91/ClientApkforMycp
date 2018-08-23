package hds.aplications.com.mycp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

import java.util.Date;


@Table(name = "uDetails", id = "_id")
public class UDetails extends Model {

    @Column(name = "idRef")
    @SerializedName("ud_id")
    public long idRef;

    @Column(name = "dateFrom")
    @SerializedName("ud_from_date")
    public Date dateFrom;

    @Column(name = "dateTo")
    @SerializedName("ud_to_date")
    public Date dateTo;

    @Column(name = "id_room", onDelete = Column.ForeignKeyAction.CASCADE)
    public Room room;

    @SerializedName("room_id")
    public String roomApiField;

    @SerializedName("reason")
    public String reason;

    public UDetails() {
    }

    public UDetails(long idRef, Date dateFrom, Date dateTo, String roomApiField, Room room, String reason) {
        this.idRef = idRef;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.roomApiField = roomApiField;
        this.room = room;
        this.reason = reason;
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

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getRoomApiField() {
        return roomApiField;
    }

    public void setRoomApiField(String roomApiField) {
        this.roomApiField = roomApiField;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
