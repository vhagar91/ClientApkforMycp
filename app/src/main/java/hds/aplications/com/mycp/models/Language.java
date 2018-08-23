package hds.aplications.com.mycp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

/**
 * Created by miguel on 26/11/2015.
 */

@Table(name = "language", id = "_id")
public class Language extends Model {


    @SerializedName("lang_id")
    @Column(name = "id_ref")
    public long idRef;

    @SerializedName("lang_name")
    @Column(name = "name")
    public String name;

    @SerializedName("lang_code")
    @Column(name = "code")
    public String code;

    public Language() {
    }

    public Language(long idRef, String name, String code) {

        this.idRef = idRef;
        this.name = name;
        this.code = code;
    }

    public long getIdRef() {
        return idRef;
    }

    public void setIdRef(long idRef) {
        this.idRef = idRef;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String name) {
        this.code = code;
    }

    @Override
    public String toString(){
        return this.name + " (" + this.code + ")";
    }
}
