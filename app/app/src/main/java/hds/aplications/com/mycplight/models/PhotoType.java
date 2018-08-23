package hds.aplications.com.mycp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by miguel on 26/11/2015.
 */

@Table(name = "photoType", id = "_id")
public class PhotoType extends Model {

    @Column(name = "name")
    public String name;

    public PhotoType() {
    }

    public PhotoType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
