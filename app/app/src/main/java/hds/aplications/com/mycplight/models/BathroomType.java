package hds.aplications.com.mycp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;



@Table(name = "bathroomType", id = "_id")
public class BathroomType extends Model {

    @Column(name = "name")
    public String name;

    public BathroomType() {
    }

    public BathroomType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
