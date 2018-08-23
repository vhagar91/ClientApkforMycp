package hds.aplications.com.mycp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

import java.util.List;



@Table(name = "accommodation", id = "_id")
public class Accommodation extends Model{

    @Column(name = "idRef")
    @SerializedName("own_id")
    public long idRef;

    @Column(name = "name")
    @SerializedName("own_name")
    String name;

    @Column(name = "licenseNumber")
    String licenseNumber;

    @Column(name = "code")
    @SerializedName("own_mcp_code")
    String code;

    @Column(name = "id_address")
    public Address address;

    @Column(name = "mobile")
    @SerializedName("own_mobile_number")
    String mobile;

    @Column(name = "phone")
    @SerializedName("own_phone_number")
    String phone;

    @Column(name = "email_one")
    @SerializedName("own_email_1")
    String emailOne;

    @Column(name = "email_two")
    @SerializedName("own_email_2")
    String emailTwo;

    @Column(name = "pool")
    Boolean pool;

    @Column(name = "jacuzee")
    Boolean jacuzee;

    @Column(name = "sauna")
    Boolean sauna;

    @SerializedName("room")
    List<Room> rooms;

    List<Photo> photos;

    List<SpokenLanguage> spokenLanguages;

    List<Facility> facilities;

    public Accommodation() {
    }

    public Accommodation(long idRef, String name, String licenseNumber, String code, Address address, String mobile, String phone, String emailOne, String emailTwo, Boolean pool, Boolean jacuzee, Boolean sauna, List<Room> rooms, List<Photo> photos, List<SpokenLanguage> spokenLanguages, List<Facility> facilities) {
        this.idRef = idRef;
        this.name = name;
        this.address = address;
        this.mobile = mobile;
        this.phone = phone;
        this.emailOne = emailOne;
        this.emailTwo = emailTwo;
        this.rooms = rooms;
        this.photos = photos;
        this.spokenLanguages = spokenLanguages;
        this.facilities = facilities;
        this.licenseNumber = licenseNumber;
        this.code = code;
        this.sauna = sauna;
        this.jacuzee = jacuzee;
        this.pool = pool;
    }

    public long getIdRef() {
        return idRef;
    }

    public void setIdRef(long idRef) {
        this.idRef = idRef;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmailOne() {
        return emailOne;
    }

    public void setEmailOne(String emailOne) {
        this.emailOne = emailOne;
    }

    public String getEmailTwo() {
        return emailTwo;
    }

    public void setEmailTwo(String emailTwo) {
        this.emailTwo = emailTwo;
    }

    public Boolean getPool() {
        return pool;
    }

    public void setPool(Boolean pool) {
        this.pool = pool;
    }

    public Boolean getJacuzee() {
        return jacuzee;
    }

    public void setJacuzee(Boolean jacuzee) {
        this.jacuzee = jacuzee;
    }

    public Boolean getSauna() {
        return sauna;
    }

    public void setSauna(Boolean sauna) {
        this.sauna = sauna;
    }

    public List<Room> getRooms() {
        List<Room> roomsDb = getMany(Room.class, "id_accommodation");
        if(roomsDb.size() > 0){
            this.rooms = roomsDb;
        }
        return this.rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Photo> getPhotos() {
        return this.photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public List<SpokenLanguage> getSpokenLanguages() {
        return this.spokenLanguages;
    }

    public void setSpokenLanguages(List<SpokenLanguage> spokenLanguages) {
        this.spokenLanguages = spokenLanguages;
    }

    public List<Facility> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<Facility> facilities) {
        this.facilities = facilities;
    }
}
