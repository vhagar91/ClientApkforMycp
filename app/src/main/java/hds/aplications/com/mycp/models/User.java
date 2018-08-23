package hds.aplications.com.mycp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

import java.util.List;



@Table(name = "user", id = "_id")
public class User extends Model{

    @Column(name = "id_ref")
    @SerializedName("user_id")
    long idRef;

    @Column(name = "name")
    @SerializedName("user_name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "id_currency")
    @SerializedName("currency")
    private Currency currency;

    @Column(name = "first_name")
    @SerializedName("user_user_name")
    private String firstName;

    @Column(name = "last_name")
    @SerializedName("user_last_name")
    private String lastName;

    @Column(name = "token")
    @SerializedName("token")
    private String token;

    @Column(name = "email")
    @SerializedName("user_email")
    private String email;

    @Column(name = "address")
    @SerializedName("user_address")
    private String address;

    @Column(name = "city")
    @SerializedName("user_city")
    private String city;

    @Column(name = "phone")
    @SerializedName("user_phone")
    private String phone;

    @Column(name = "lang_code")
    @SerializedName("lang_code")
    private String langCode;

    @Column(name = "postal_code")
    @SerializedName("postal_code")
    private String postalCode;

    @Column(name = "country_code")
    @SerializedName("co_code")
    private String countryCode;

    @Column(name = "photo")
    @SerializedName("pho_name")
    String photo;


    @Column(name = "country")
    Country country;

    @SerializedName("reservations")
    List<Reservation> reservations;

    public User() {
    }

    public User(String password, String firstName, String lastName, String email, Country country){
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.country = country;
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

    public String getFullName(){
        return this.firstName + " " + this.lastName;
    }

    public List<Reservation> getReservations() {
        /*List<Reservation> reservations = getMany(Reservation.class, "id_user");
        if(reservations.size() > 0){
            this.reservations = reservations;
        }*/
        return this.reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public Currency getCurrency(){
        return currency;
    }

    public String getToken(){
        return token;
    }

    public void setCurrency(Currency currency){
        this.currency = currency;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public void setToken(String token){
        this.token = token;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void setCity(String city){
        this.city = city;
    }

    public void setLangCode(String langCode){
        this.langCode = langCode;
    }

    public void setPostalCode(String postalCode){
        this.postalCode = postalCode;
    }

    public void setCountryCode(String countryCode){
        this.countryCode = countryCode;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getEmail(){
        return email;
    }

    public String getAddress(){
        return address;
    }

    public String getCity(){
        return city;
    }

    public String getLangCode(){
        return langCode;
    }

    public String getPostalCode(){
        return postalCode;
    }

    public String getCountryCode(){
        return countryCode;
    }

    public String getPhoto(){
        return photo;
    }
    public String getPhone(){
        return phone;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    public Country getCountry(){
        return country;
    }

    public void setCountry(Country country){
        this.country = country;
    }
}
