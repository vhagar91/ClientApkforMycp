package hds.aplications.com.mycp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;



@Table(name = "user_accommodation", id = "_id")
public class UserAccommodation extends Model{

    /*@Column(name = "idRef")
    @SerializedName("own_id")
    public long idRef;*/

    @Column(name = "id_accommodation", onDelete = Column.ForeignKeyAction.CASCADE)
    Accommodation accommodation;

    @Column(name = "id_user", onDelete = Column.ForeignKeyAction.CASCADE)
    User user;

    public UserAccommodation() {
    }

    public UserAccommodation(Accommodation accommodation, User user) {
        this.accommodation = accommodation;
        this.user = user;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
