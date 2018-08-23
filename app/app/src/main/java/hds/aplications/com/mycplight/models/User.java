package hds.aplications.com.mycp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


@Table(name = "user", id = "_id")
public class User extends Model{

    @Column(name = "idRef")
    @SerializedName("user_id")
    public long idRef;

    @Column(name = "name")
    @SerializedName("user_name")
    String name;

    @Column(name = "password")
    String password;

    @Column(name = "first_name")
    @SerializedName("user_user_name")
    String firstName;

    @Column(name = "last_name")
    @SerializedName("user_last_name")
    String lastName;

    @Column(name = "email")
    @SerializedName("user_email")
    String email;

    @Column(name = "phone")
    @SerializedName("user_phone")
    String phone;

    @Column(name = "enabled")
    @SerializedName("user_enabled")
    String enabled;

    @SerializedName("property")
    List<Accommodation> accommodations;

    String userToken;

    public User() {
    }

    public User(String name, String firstName, String lastName, String email, String phone, String enabled) {
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.enabled = enabled;
    }

    public long getIdRef() {
        return idRef;
    }

    public void setIdRef(long idRef) {
        this.idRef = idRef;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getFullName(){
        return this.firstName + " " + this.lastName;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public List<Accommodation> getAccommodations() {
        if(this.accommodations == null){
            List<UserAccommodation> userAccommodations = this.getUserAccommodations();
            if (userAccommodations != null){
                this.accommodations = new ArrayList<>();
                for(UserAccommodation userAccommodation : userAccommodations)
                {
                    Accommodation accommodation = userAccommodation.getAccommodation();
                    this.accommodations.add(accommodation);
                }
            }
        }
        return this.accommodations;
    }

    public List<UserAccommodation> getUserAccommodations() {
        return getMany(UserAccommodation.class, "id_user");
    }
}

//ch048 ---  123456
//ch002 --- 123456
