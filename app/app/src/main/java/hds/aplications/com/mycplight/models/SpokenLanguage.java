package hds.aplications.com.mycp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;



@Table(name = "spokenLanguage", id = "_id")
public class SpokenLanguage extends Model {

    @Column(name = "id_accommodation")
    public Accommodation accommodation;

    /*@Column(name = "id_language")
    public Language language;*/

    public SpokenLanguage() {
    }

    public SpokenLanguage(Accommodation accommodation/*, Language language*/) {

        this.accommodation = accommodation;
        //this.language = language;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    /*public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }*/
}
