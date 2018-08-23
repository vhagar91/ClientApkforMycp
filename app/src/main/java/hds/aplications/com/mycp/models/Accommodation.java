package hds.aplications.com.mycp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

import java.util.List;



@Table(name = "accommodation", id = "_id")
public class Accommodation extends Model{

    @Column(name = "id_ref")
    @SerializedName("own_id")
    public long idRef;

    @Column(name = "name")
    @SerializedName("own_name")
    String name;

    @Column(name = "destination")
    @SerializedName("destination")
    String destination;

    @Column(name = "inmediate_booking")
    @SerializedName("inmediate_booking")
    Boolean inmediateBooking;

    @Column(name = "stars")
    @SerializedName("stars")
    int stars;

    @Column(name = "code")
    @SerializedName("mcp_code")
    String code;

    @Column(name = "photo")
    @SerializedName("photo")
    String photo;

    @SerializedName("photos")
    List<String> photos;

    @Column(name = "photo_own")
    @SerializedName("photo_own")
    String photoOwn;

    @Column(name = "favorite")
    @SerializedName("in_favorite")
    public Boolean favorite;

    @Column(name = "min_price")
    @SerializedName("min_price")
    public float minPrice;

    @Column(name = "maximun_number_guests")
    @SerializedName("maximun_number_guests")
    public int maximunNumberGuests;

    @Column(name = "lon")
    @SerializedName("lon")
    public double lon;

    @Column(name = "lat")
    @SerializedName("lat")
    public double lat;

    @Column(name = "owner_name")
    @SerializedName("owner_name")
    public String ownerName;

    @Column(name = "id_category")
    @SerializedName("category")
    public AccommodationCategory accommodationCategory;

    @Column(name = "id_type")
    @SerializedName("type")
    public AccommodationType accommodationType;

    @Column(name = "languages")
    @SerializedName("languages")
    public String languages;

    @Column(name = "breakfast")
    @SerializedName("breakfast")
    public Boolean breakfast;

    @Column(name = "breakfast_price")
    @SerializedName("breakfast_price")
    public float breakfastPrice;

    @Column(name = "dinner")
    @SerializedName("dinner")
    public Boolean dinner;

    @Column(name = "dinner_price_from")
    @SerializedName("dinner_price_from")
    public float dinnerPriceFrom;

    @Column(name = "dinner_price_to")
    @SerializedName("dinner_price_to")
    public float dinnerPriceTo;

    @Column(name = "parking")
    @SerializedName("parking")
    public Boolean parking;

    @Column(name = "description")
    @SerializedName("description")
    public String description;

    @SerializedName("rooms")
    List<Room> rooms;

    @SerializedName("commission_percent")
    float commissionPercent;

    /*data to reservation detail*/

    @SerializedName("modality_complete")
    Boolean modalityComplete;

    @SerializedName("room_recharge")
    float roomRecharge;

    @Column(name = "id_service_fee", onDelete = Column.ForeignKeyAction.CASCADE)
    @SerializedName("service_fee")
    ServiceFee serviceFee;

    @Column(name = "cuc_change")
    @SerializedName("cuc_change")
    float cucChange;

    @Column(name = "nights")
    @SerializedName("nights")
    int nights;

    public Accommodation() {
    }

    public Accommodation(String name, String code, Boolean favorite){
        this.name = name;
        this.code = code;
        this.favorite = favorite;
    }

    public Accommodation(long idRef, String name, String code, List<Room> rooms) {
        this.idRef = idRef;
        this.name = name;
        this.rooms = rooms;
        this.code = code;
    }

    public long getIdRef() {
        return idRef;
    }

    public void setIdRef(long idRef) {
        this.idRef = idRef;
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

    public List<Room> getRooms() {
        /*List<Room> roomsDb = getMany(Room.class, "id_accommodation");
        if(roomsDb.size() > 0){
            this.rooms = roomsDb;
        }*/
        return this.rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public Boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public String getPhoto(){
        return photo;
    }

    public void setPhoto(String photo){
        this.photo = photo;
    }

    public String getDestination(){
        return destination;
    }

    public void setDestination(String destination){
        this.destination = destination;
    }

    public float getMinPrice(){
        return minPrice;
    }

    public void setMinPrice(float minPrice){
        this.minPrice = minPrice;
    }

    public int getMaximunNumberGuests(){
        return maximunNumberGuests;
    }

    public void setMaximunNumberGuests(int maximunNumberGuests){
        this.maximunNumberGuests = maximunNumberGuests;
    }

    public Boolean getInmediateBooking(){
        return inmediateBooking;
    }

    public void setInmediateBooking(Boolean inmediateBooking){
        this.inmediateBooking = inmediateBooking;
    }

    public int getStars(){
        return stars;
    }

    public void setStars(int stars){
        this.stars = stars;
    }

    public List<String> getPhotos(){
        return photos;
    }

    public void setPhotos(List<String> photos){
        this.photos = photos;
    }

    public String getPhotoOwn(){
        return photoOwn;
    }

    public void setPhotoOwn(String photoOwn){
        this.photoOwn = photoOwn;
    }

    public Boolean getFavorite(){
        return favorite;
    }

    public String getOwnerName(){
        return ownerName;
    }

    public void setOwnerName(String ownerName){
        this.ownerName = ownerName;
    }

    public AccommodationCategory getAccommodationCategory(){
        return accommodationCategory;
    }

    public void setAccommodationCategory(AccommodationCategory accommodationCategory){
        this.accommodationCategory = accommodationCategory;
    }

    public AccommodationType getAccommodationType(){
        return accommodationType;
    }

    public void setAccommodationType(AccommodationType accommodationType){
        this.accommodationType = accommodationType;
    }

    public String getLanguages(){
        return languages;
    }

    public void setLanguages(String languages){
        this.languages = languages;
    }

    public Boolean getBreakfast(){
        return breakfast;
    }

    public void setBreakfast(Boolean breakfast){
        this.breakfast = breakfast;
    }

    public float getBreakfastPrice(){
        return breakfastPrice;
    }

    public void setBreakfastPrice(float breakfastPrice){
        this.breakfastPrice = breakfastPrice;
    }

    public Boolean getDinner(){
        return dinner;
    }

    public void setDinner(Boolean dinner){
        this.dinner = dinner;
    }

    public float getDinnerPriceFrom(){
        return dinnerPriceFrom;
    }

    public void setDinnerPriceFrom(float dinnerPriceFrom){
        this.dinnerPriceFrom = dinnerPriceFrom;
    }

    public float getDinnerPriceTo(){
        return dinnerPriceTo;
    }

    public void setDinnerPriceTo(float dinnerPriceTo){
        this.dinnerPriceTo = dinnerPriceTo;
    }

    public Boolean getParking(){
        return parking;
    }

    public void setParking(Boolean parking){
        this.parking = parking;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public Boolean getModalityComplete(){
        return modalityComplete;
    }

    public float getRoomRecharge(){
        return roomRecharge;
    }

    public ServiceFee getServiceFee(){
        return serviceFee;
    }
    public void setServiceFee(ServiceFee serviceFee){
        this.serviceFee=serviceFee;
    }
    public int getNights(){
        return nights;
    }

    public void setNights(int nights){
        this.nights = nights;
    }

    public float getCucChange(){
        return cucChange;
    }

    public float getCommissionPercent(){
        return commissionPercent;
    }
}
