package hds.aplications.com.mycp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;



@Table(name = "province", id = "_id")
public class Province extends Model{

    @Column(name = "idRef")
    public Long idRef;

    @Column(name = "name")
    public String name;

    @Column(name = "phone_code")
    public String phoneCode;

    public Province() {}

    public Province(Long idRef, String name) {
        this.idRef = idRef;
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

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public List<Municipality> municipalitis() {
        return getMany(Municipality.class, "id_province");
    }

    @Override
    public String toString() {
        return this.name;
    }
}
