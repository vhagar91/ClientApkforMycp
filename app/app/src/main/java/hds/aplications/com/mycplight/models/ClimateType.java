package hds.aplications.com.mycp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "climateType", id = "_id")
public class ClimateType extends Model {

    @Column(name = "name")
    public String name;

    public ClimateType() {
    }

    public ClimateType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
