package hds.aplications.com.mycp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;


@Table(name = "destination", id = "_id")
public class Destination extends Model {

    @Column(name = "id_ref")
    @SerializedName("des_id")
    public Long idRef;

    @Column(name = "name")
    @SerializedName("des_name")
    public String name;

    public Destination() {}

    public Destination(String name){
        this.name = name;
    }

    public Long getIdRef() {
        return idRef;
    }

    public void setIdRef(Long idRef) {
        this.idRef = idRef;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
