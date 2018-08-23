package hds.aplications.com.mycp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;


@Table(name = "country", id = "_id")
public class Country extends Model {

    @Column(name = "id_ref")
    @SerializedName("co_id")
    public Long idRef;

    @Column(name = "name")
    @SerializedName("co_name")
    public String name;

    public Country() {}

    public Country(String name){
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
