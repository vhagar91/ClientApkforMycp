package hds.aplications.com.mycp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Miguel Gomez Leon on 11/21/15.
 * mgleonsc@gmail.com
 */

@Table(name = "room", id = "_id")
public class Room extends Model {

    @Column(name = "id_ref")
    @SerializedName("room_id")
    public long idRef;

    @Column(name = "id_roomType")
    @SerializedName("type")
    public RoomType roomType;

    @Column(name = "id_accommodation", onDelete = Column.ForeignKeyAction.CASCADE)
    @SerializedName("accommodation")
    public Accommodation accommodation;

    @SerializedName("reservations")
    List<Reservation> reservations;

    @Column(name = "number")
    @SerializedName("room_num")
    public int number;

    /*services*/
    @Column(name = "beds")
    @SerializedName("beds")
    public int beds;

    @Column(name = "price_up_from")
    @SerializedName("price_up_from")
    public float priceUpFrom;

    @Column(name = "price_up_to")
    @SerializedName("price_up_to")
    public float priceUpTo;

    @Column(name = "price_down_from")
    @SerializedName("price_down_from")
    public float priceDownFrom;

    @Column(name = "price_down_to")
    @SerializedName("price_down_to")
    public float priceDownTo;

    @Column(name = "id_clima")
    @SerializedName("climate")
    public RoomClima clima;

    @Column(name = "id_audiovisual")
    @SerializedName("audiovisual")
    public RoomAudiovisual roomAudiovisual;

    @Column(name = "smoker")
    @SerializedName("smoker")
    public Boolean smoke;

    @Column(name = "safe")
    @SerializedName("safe")
    public Boolean safe;

    @Column(name = "cradle")
    @SerializedName("baby")
    public Boolean cradle;

    @Column(name = "id_bathroom")
    @SerializedName("bathroom")
    public Bathroom bathroom;

    @Column(name = "stereo")
    @SerializedName("stereo")
    public Boolean stereo;

    @Column(name = "windows")
    @SerializedName("windows")
    public Integer windows;

    @Column(name = "balcony")
    @SerializedName("balcony")
    public Integer balcony;

    @Column(name = "terrace")
    @SerializedName("terrace")
    public Boolean terrace;

    @Column(name = "yard")
    @SerializedName("yard")
    public Boolean yard;

    /*data to reservation detail*/

    @SerializedName("unavailability")
    public Boolean unavailability;

    @SerializedName("prices")
    public List<Float> pricesByNights;

    public Room() {}

    public Room(long idRef, Accommodation accommodation, RoomType roomType) {
        this.idRef = idRef;
        this.accommodation = accommodation;
        this.roomType = roomType;
    }

    public long getIdRef() {
        return idRef;
    }

    public void setIdRef(long idRef) {
        this.idRef = idRef;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) { this.accommodation = accommodation; }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) { this.roomType = roomType; }

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

    public boolean isStereo(){
        return stereo;
    }

    public void setStereo(boolean stereo){
        this.stereo = stereo;
    }

    public boolean isSmoke(){
        return smoke;
    }

    public void setSmoke(boolean smoke){
        this.smoke = smoke;
    }

    public boolean isSafe(){
        return safe;
    }

    public void setSafe(boolean safe){
        this.safe = safe;
    }

    public boolean isCradle(){
        return cradle;
    }

    public void setCradle(boolean cradle){
        this.cradle = cradle;
    }

    public boolean isTerrace(){
        return terrace;
    }

    public void setTerrace(boolean terrace){
        this.terrace = terrace;
    }

    public boolean isYard(){
        return yard;
    }

    public void setYard(boolean yard){
        this.yard = yard;
    }

    public Integer getBeds(){
        return beds;
    }

    public void setBeds(Integer beds){
        this.beds = beds;
    }

    public Integer getBalcony(){
        return balcony;
    }

    public void setBalcony(Integer balcony){
        this.balcony = balcony;
    }

    public Integer getWindows(){
        return windows;
    }

    public void setWindows(Integer windows){
        this.windows = windows;
    }

    public List<Reservation> getReservations(){
        return reservations;
    }

    public int getNumber(){
        return number;
    }

    public void setNumber(int number){
        this.number = number;
    }

    public void setBeds(int beds){
        this.beds = beds;
    }

    public float getPriceUpFrom(){
        return priceUpFrom;
    }

    public void setPriceUpFrom(float priceUpFrom){
        this.priceUpFrom = priceUpFrom;
    }

    public float getPriceUpTo(){
        return priceUpTo;
    }

    public void setPriceUpTo(float priceUpTo){
        this.priceUpTo = priceUpTo;
    }

    public float getPriceDownFrom(){
        return priceDownFrom;
    }

    public void setPriceDownFrom(float priceDownFrom){
        this.priceDownFrom = priceDownFrom;
    }

    public float getPriceDownTo(){
        return priceDownTo;
    }

    public void setPriceDownTo(float priceDownTo){
        this.priceDownTo = priceDownTo;
    }

    public RoomClima getClima(){
        return clima;
    }

    public void setClima(RoomClima clima){
        this.clima = clima;
    }

    public RoomAudiovisual getRoomAudiovisual(){
        return roomAudiovisual;
    }

    public void setRoomAudiovisual(RoomAudiovisual roomAudiovisual){
        this.roomAudiovisual = roomAudiovisual;
    }

    public Boolean getSmoke(){
        return smoke;
    }

    public void setSmoke(Boolean smoke){
        this.smoke = smoke;
    }

    public Boolean getSafe(){
        return safe;
    }

    public void setSafe(Boolean safe){
        this.safe = safe;
    }

    public Boolean getCradle(){
        return cradle;
    }

    public void setCradle(Boolean cradle){
        this.cradle = cradle;
    }

    public Bathroom getBathroom(){
        return bathroom;
    }

    public void setBathroom(Bathroom bathroom){
        this.bathroom = bathroom;
    }

    public Boolean getStereo(){
        return stereo;
    }

    public void setStereo(Boolean stereo){
        this.stereo = stereo;
    }

    public Boolean getTerrace(){
        return terrace;
    }

    public void setTerrace(Boolean terrace){
        this.terrace = terrace;
    }

    public Boolean getYard(){
        return yard;
    }

    public void setYard(Boolean yard){
        this.yard = yard;
    }

    public Boolean getUnavailability(){
        return unavailability;
    }

    public List<Float> getPricesByNights(){
        return pricesByNights;
    }

    @Override
    public String toString() {

        return "Habitación #" +  String.valueOf(this.number);
    }
}
