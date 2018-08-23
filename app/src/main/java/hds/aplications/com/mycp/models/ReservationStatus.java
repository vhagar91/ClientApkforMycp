package hds.aplications.com.mycp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;


@Table(name = "reservation_status", id = "_id")
public class ReservationStatus extends TranslatableModel {

    @Column(name = "id_ref")
    @SerializedName("own_res_status")
    public long idRef;

    @Column(name = "name")
    public String name;

    public ReservationStatus() {
    }

    public ReservationStatus(String name) {
        this.name = name;
    }

    public String getLocaleName(String langcode) {
        Traslation traslation = getTraslation("name",langcode);
        return (traslation != null) ? (traslation.getContent()) : name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
