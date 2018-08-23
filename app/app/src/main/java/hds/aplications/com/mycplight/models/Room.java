package hds.aplications.com.mycp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Table(name = "room", id = "_id")
public class Room extends Model {

    @Column(name = "idRef")
    @SerializedName("room_id")
    public long idRef;

    @Column(name = "number")
    @SerializedName("room_num")
    public Integer number;

    @Column(name = "bedsNumber")
    public Integer bedsNumber;

    @Column(name = "priceLowSeason")
    @SerializedName("room_price_down_to")
    public Float priceLowSeason;

    @Column(name = "priceHighSeason")
    @SerializedName("room_price_up_to")
    public Float priceHighSeason;

    @Column(name = "smoker")
    @SerializedName("room_smoker")
    public Boolean smoker;

    @Column(name = "safe")
    @SerializedName("room_safe")
    public Boolean safe;

    @Column(name = "babyFacilities")
    @SerializedName("room_baby")
    public Boolean babyFacilities;

    @Column(name = "terrace")
    @SerializedName("room_terrace")
    public Boolean terrace;

    @Column(name = "yard")
    @SerializedName("room_yard")
    public Boolean yard;

    @Column(name = "stereo")
    @SerializedName("room_stereo")
    public Boolean stereo;

    @Column(name = "windows")
    @SerializedName("room_windows")
    public Integer windows;

    @Column(name = "balcony")
    @SerializedName("room_balcony")
    public Integer balcony;

    @Column(name = "active")
    public Boolean active;

    @Column(name = "id_accommodation", onDelete = Column.ForeignKeyAction.CASCADE)
    public Accommodation accommodation;

    @Column(name = "id_roomType")
    public RoomType roomType;

    @Column(name = "id_bathroomType")
    public BathroomType bathroomType;

    @Column(name = "id_climateType")
    public ClimateType climateType;

    @Column(name = "id_audiovisualType")
    public AudiovisualType audiovisualType;

    //Just for API usage
    @SerializedName("room_type")
    public String roomTypeApiField;

    @SerializedName("room_bathroom")
    public String bathroomTypeApiField;

    @SerializedName("room_climate")
    public String climateTypeApiField;

    @SerializedName("room_audiovisual")
    public String audiovisualTypeApiField;

    @SerializedName("unavailability")
    List<UDetails> uDetailses;

    @SerializedName("reservations")
    List<Reservation> reservations;


    public Room() {
    }

    public Room(long idRef, Integer number, Integer bedsNumber, Float priceLowSeason, Float priceHighSeason, Boolean smoker, Boolean safe,
                Boolean babyFacilities, Boolean terrace, Boolean yard, Boolean stereo, Integer windows, Integer balcony, Boolean active,
                Accommodation accommodation, RoomType roomType, BathroomType bathroomType, ClimateType climateType, AudiovisualType audiovisualType) {
        this.idRef = idRef;
        this.number = number;
        this.bedsNumber = bedsNumber;
        this.priceLowSeason = priceLowSeason;
        this.priceHighSeason = priceHighSeason;
        this.smoker = smoker;
        this.safe = safe;
        this.babyFacilities = babyFacilities;
        this.terrace = terrace;
        this.yard = yard;
        this.stereo = stereo;
        this.windows = windows;
        this.balcony = balcony;
        this.active = active;
        this.accommodation = accommodation;
        this.roomType = roomType;
        this.bathroomType = bathroomType;
        this.climateType = climateType;
        this.audiovisualType = audiovisualType;
    }

    public long getIdRef() {
        return idRef;
    }

    public void setIdRef(long idRef) {
        this.idRef = idRef;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getBedsNumber() {
        return bedsNumber;
    }

    public void setBedsNumber(Integer bedsNumber) {
        this.bedsNumber = bedsNumber;
    }

    public Float getPriceLowSeason() {
        return priceLowSeason;
    }

    public void setPriceLowSeason(Float priceLowSeason) { this.priceLowSeason = priceLowSeason; }

    public Float getPriceHighSeason() {
        return priceHighSeason;
    }

    public void setPriceHighSeason(Float priceHighSeason) { this.priceHighSeason = priceHighSeason; }

    public Boolean getSmoker() {
        return smoker;
    }

    public void setSmoker(Boolean smoker) { this.smoker = smoker; }

    public Boolean getSafe() {
        return safe;
    }

    public void setSafe(Boolean safe) { this.safe = safe; }

    public Boolean getBabyFacilities() {
        return babyFacilities;
    }

    public void setBabyFacilities(Boolean babyFacilities) { this.babyFacilities = babyFacilities; }

    public Boolean getTerrace() {
        return terrace;
    }

    public void setTerrace(Boolean terrace) { this.terrace = terrace; }

    public Boolean getYard() {
        return terrace;
    }

    public void setYard(Boolean terrace) { this.terrace = terrace; }

    public Boolean getStereo() {
        return stereo;
    }

    public void setStereo(Boolean stereo) { this.stereo = stereo; }

    public Integer getWindows() {
        return windows;
    }

    public void setWindows(Integer windows) { this.windows = windows; }

    public Integer getBalcony() {
        return balcony;
    }

    public void setBalcony(Integer balcony) { this.balcony = balcony; }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) { this.active = active; }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) { this.accommodation = accommodation; }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) { this.roomType = roomType; }

    public BathroomType getBathroomType() {
        return bathroomType;
    }

    public void setBathroomType(BathroomType bathroomType) { this.bathroomType = bathroomType; }

    public ClimateType getClimateType() {
        return climateType;
    }

    public void setClimateType(ClimateType climateType) { this.climateType = climateType; }

    public AudiovisualType getAudiovisualType() {
        return audiovisualType;
    }

    public void setAudiovisualType(AudiovisualType audiovisualType) { this.audiovisualType = audiovisualType; }

    public String getRoomTypeApiField() {
        return roomTypeApiField;
    }

    public void setRoomTypeApiField(String roomTypeApiField) {
        this.roomTypeApiField = roomTypeApiField;
    }

    public String getBathroomTypeApiField() {
        return bathroomTypeApiField;
    }

    public void setBathroomTypeApiField(String bathroomTypeApiField) {
        this.bathroomTypeApiField = bathroomTypeApiField;
    }

    public String getClimateTypeApiField() {
        return climateTypeApiField;
    }

    public void setClimateTypeApiField(String climateTypeApiField) {
        this.climateTypeApiField = climateTypeApiField;
    }

    public String getAudiovisualTypeApiField() {
        return audiovisualTypeApiField;
    }

    public void setAudiovisualTypeApiField(String audiovisualTypeApiField) {
        this.audiovisualTypeApiField = audiovisualTypeApiField;
    }

    public List<UDetails> getuDetailses(boolean cached) {
        List<UDetails> uDetailsesDb = getMany(UDetails.class, "id_room");

        if (!cached){
            return uDetailsesDb;
        }

        if(uDetailsesDb.size() > 0){
            this.uDetailses = uDetailsesDb;
        }
        return uDetailses;
    }

    public void setuDetailses(List<UDetails> uDetailses) {
        this.uDetailses = uDetailses;
    }

    public List<Reservation> getReservations(boolean cached) {
        List<Reservation> reservationsDb = getMany(Reservation.class, "id_room");

        if (!cached){
            return reservationsDb;
        }

        if(reservationsDb.size() > 0){
            this.reservations = reservationsDb;
        }
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    @Override
    public String toString() {

        return "Habitación #" +  ((this.number != null) ? this.number.toString() : "0");
    }
}
