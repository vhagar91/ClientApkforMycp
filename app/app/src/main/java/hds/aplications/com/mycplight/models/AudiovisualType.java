package hds.aplications.com.mycp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "audiovisualType", id = "_id")
public class AudiovisualType extends Model {

    @Column(name = "name")
    public String name;

    public AudiovisualType() {
    }

    public AudiovisualType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
