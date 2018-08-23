package hds.aplications.com.mycp.models;

import com.activeandroid.Model;
import com.activeandroid.TableInfo;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.Locale;



@Table(name = "accommodation_type", id = "_id")
public class AccommodationType extends TranslatableModel{

    @Column(name = "name")
    public String name;

    public AccommodationType() {
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
