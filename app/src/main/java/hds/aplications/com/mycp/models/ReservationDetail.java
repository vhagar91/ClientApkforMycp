package hds.aplications.com.mycp.models;

import android.content.Context;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import hds.aplications.com.mycp.R;



@Table(name = "reservation_detail", id = "_id")
public class ReservationDetail extends Model {

    @Column(name = "id_ref")
    @SerializedName("own_res_id")
    public long idRef;

    @Column(name = "date_from")
    @SerializedName("own_res_reservation_from_date")
    public Date dateFrom;

    @Column(name = "date_to")
    @SerializedName("own_res_reservation_to_date")
    public Date dateTo;

    @Column(name = "adults_total")
    @SerializedName("own_res_count_adults")
    public int adultsTotal;

    @Column(name = "kids_total")
    @SerializedName("own_res_count_childrens")
    public int kidsTotal;

    @Column(name = "id_status")
    @SerializedName("own_res_status")
    private ReservationStatus status;

    @Column(name = "id_reservation", onDelete = Column.ForeignKeyAction.CASCADE)
    public Reservation reservation;

    @Column(name = "id_room", onDelete = Column.ForeignKeyAction.CASCADE)
    @SerializedName("own_res_selected_room_id")
    public Room room;

    private List<Float> pricesByNights;

    @Column(name = "totalPrice")
    @SerializedName("own_res_total_in_site")
    private Float totalPrice;

    private boolean active;

    @Column(name = "cas")
    @SerializedName("own_res_gen_res_id")
    private String cas;

    /*private Accommodation accommodation;*/

    public ReservationDetail() {
    }

    public ReservationDetail(Date dateFrom, Date dateTo, Room room){
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.room = room;
        this.kidsTotal = 0;
        this.adultsTotal = 0;
    }

    public ReservationDetail(long idRef,Float totalPrice ,Date dateFrom, Date dateTo, int adultsTotal, int kidsTotal, ReservationStatus status, Reservation reservation, Room room) {
        this.idRef = idRef;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.adultsTotal = adultsTotal;
        this.kidsTotal = kidsTotal;
        this.status = status;
        this.reservation = reservation;
        this.room = room;
        this.totalPrice=totalPrice;
    }

    public long getIdRef() {
        return idRef;
    }

    public void setIdRef(long idRef) {
        this.idRef = idRef;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public int getAdultsTotal() {
        return adultsTotal;
    }

    public void setAdultsTotal(int adultsTotal) {
        this.adultsTotal = adultsTotal;
    }

    public int getKidsTotal() {
        return kidsTotal;
    }

    public void setKidsTotal(int kidsTotal) {
        this.kidsTotal = kidsTotal;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public List<Float> getPricesByNights(){
        return pricesByNights;
    }

    public void setPricesByNights(List<Float> pricesByNights){
        this.pricesByNights = pricesByNights;
    }

    /*public Accommodation getAccommodation(){
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation){
        this.accommodation = accommodation;
    }*/

    private float getPrice(){
        float price = 0;
        if(this.pricesByNights == null || this.pricesByNights.size() == 0){
            return price;
        }

        for(Float p : this.pricesByNights){
            price += p;
        }
        return price;
    }

    public float getTotalPrice(Context context){
        if(this.totalPrice != null){
            return this.totalPrice;
        }

        float price = this.getPrice();
        float totalPrice = 0;
        if(price == 0 || (this.adultsTotal == 0 && this.kidsTotal == 0)){
            return totalPrice;
        }

        totalPrice = price;
        int persons = this.adultsTotal + this.kidsTotal;
        RoomType roomType = room.getRoomType();
        if(persons >= 3 && (roomType.getName().equals(context.getString(R.string.ROOM_TYPE_TRIPLE)) ||
                roomType.getName().equals(context.getString(R.string.ROOM_TYPE_DOBLE_2)) ||
                roomType.getName().equals(context.getString(R.string.ROOM_TYPE_DOBLE)))){
            totalPrice += this.room.getAccommodation().getRoomRecharge() * this.pricesByNights.size();
        }

        this.totalPrice = totalPrice;
        return  this.totalPrice;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCas(){
        return cas;
    }

    public void setCas(String cas){
        this.cas = cas;
    }
}
