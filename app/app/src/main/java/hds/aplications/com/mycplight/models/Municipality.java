package hds.aplications.com.mycp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;


@Table(name = "municipality", id = "_id")
public class Municipality extends Model {

    @Column(name = "idRef")
    public Long idRef;

    @Column(name = "name")
    public String name;

    @Column(name = "id_province")
    public Province province;

    public Municipality() {}

    public Municipality(Long idRef, String name, Province province) {
        this.idRef = idRef;
        this.name = name;
        this.province = province;
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

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
