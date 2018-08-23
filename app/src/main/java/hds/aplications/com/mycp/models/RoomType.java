package hds.aplications.com.mycp.models;

import com.activeandroid.Model;
import com.activeandroid.TableInfo;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;
import java.util.Locale;



@Table(name = "room_type", id = "_id")
public class RoomType extends TranslatableModel{

    @Column(name = "name")
    public String name;

    public RoomType() {
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

    /*private Traslation getTraslation(){
        String locale = Locale.getDefault().getLanguage();

        TableInfo tableInfo = new TableInfo(this.getClass());

        return new Select().from(Traslation.class).where("id_obj = ?", this.getId()).
                and("type_obj = ?", "room_type").and("field = ?", "name").and("locale = ?", locale).executeSingle();
    }*/

    /*
    private List<RoomTypeTraslation> roomTypeTraslations;
    public List<RoomTypeTraslation> getRoomTypeTraslations() {
        roomTypeTraslations = getMany(RoomTypeTraslation.class, "id_room_type");
        return roomTypeTraslations;
    }

    public void setRoomTypeTraslations(List<RoomTypeTraslation> roomTypeTraslations) {
        this.roomTypeTraslations = roomTypeTraslations;
    }
    */
}
