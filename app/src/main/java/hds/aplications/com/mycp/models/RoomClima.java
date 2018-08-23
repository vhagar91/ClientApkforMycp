package hds.aplications.com.mycp.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;



@Table(name = "room_clima", id = "_id")
public class RoomClima extends TranslatableModel{

    @Column(name = "name")
    public String name;

    public RoomClima() {
    }

    public String getLocaleName(String langcode) {
        Traslation traslation = getTraslation("name",langcode);
        return (traslation != null) ? (traslation.getContent()) : name;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
