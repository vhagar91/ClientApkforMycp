package hds.aplications.com.mycp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;



@Table(name = "photo", id = "_id")
public class Photo extends Model {

    @Column(name = "idRef")
    public long idRef;

    @Column(name = "name")
    public String name;

    @Column(name = "description")
    public String description;

    @Column(name = "id_accommodation")
    public Accommodation accommodation;

    @Column(name = "id_photoType")
    public PhotoType photoType;

    public Photo() {
    }

    public Photo(long idRef, String name, String description, Accommodation accommodation, PhotoType photoType) {
        this.idRef = idRef;
        this.name = name;
        this.accommodation = accommodation;
        this.photoType = photoType;
        this.description = description;
    }

    public long getIdRef() { return idRef; }

    public void setIdRef(long id) {
        this.idRef = idRef;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    public PhotoType getPhotoType() {
        return photoType;
    }

    public void setPhotoType(PhotoType photoType) {
        this.photoType = photoType;
    }
}
