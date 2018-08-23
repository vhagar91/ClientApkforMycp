package hds.aplications.com.mycp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;



@Table(name = "roomType", id = "_id")
public class RoomType extends Model{

    @Column(name = "name")
    public String name;

    public RoomType() {
    }

    public RoomType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
