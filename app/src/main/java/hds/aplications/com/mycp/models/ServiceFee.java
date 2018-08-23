package hds.aplications.com.mycp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

import java.util.List;



@Table(name = "service_fee", id = "_id")
public class ServiceFee extends Model {
    public long getIdRef() {
        return idRef;
    }

    public void setIdRef(long idRef) {
        this.idRef = idRef;
    }

    public void setFixedFee(float fixedFee) {
        this.fixedFee = fixedFee;
    }

    public void setOneNrUntil20Percent(float oneNrUntil20Percent) {
        this.oneNrUntil20Percent = oneNrUntil20Percent;
    }

    public void setOneNrFrom20To25Percent(float oneNrFrom20To25Percent) {
        this.oneNrFrom20To25Percent = oneNrFrom20To25Percent;
    }

    public void setOneNrFromMore25Percent(float oneNrFromMore25Percent) {
        this.oneNrFromMore25Percent = oneNrFromMore25Percent;
    }

    public void setOneNightSeveralRoomsPercent(float oneNightSeveralRoomsPercent) {
        this.oneNightSeveralRoomsPercent = oneNightSeveralRoomsPercent;
    }

    public void setOne2NightsPercent(float one2NightsPercent) {
        this.one2NightsPercent = one2NightsPercent;
    }

    public void setOne3NightsPercent(float one3NightsPercent) {
        this.one3NightsPercent = one3NightsPercent;
    }

    public void setOne4NightsPercent(float one4NightsPercent) {
        this.one4NightsPercent = one4NightsPercent;
    }

    public void setOne5NightsPercent(float one5NightsPercent) {
        this.one5NightsPercent = one5NightsPercent;
    }

    @Column(name = "id_ref")
    @SerializedName("id")
    private long idRef;

    @Column(name = "fixedFee")
    @SerializedName("fixedFee")
    private float fixedFee;

    @Column(name = "one_nr_until_20_percent")
    @SerializedName("one_nr_until_20_percent")
    private float oneNrUntil20Percent;

    @Column(name = "one_nr_from_20_to_25_percent")
    @SerializedName("one_nr_from_20_to_25_percent")
    private float oneNrFrom20To25Percent;

    @Column(name = "one_nr_from_more_25_percent")
    @SerializedName("one_nr_from_more_25_percent")
    private float oneNrFromMore25Percent;

    @Column(name = "one_night_several_rooms_percent")
    @SerializedName("one_night_several_rooms_percent")
    private float oneNightSeveralRoomsPercent;

    @Column(name = "one_2_nights_percent")
    @SerializedName("one_2_nights_percent")
    private float one2NightsPercent;

    @Column(name = "one_3_nights_percent")
    @SerializedName("one_3_nights_percent")
    private float one3NightsPercent;

    @Column(name = "one_4_nights_percent")
    @SerializedName("one_4_nights_percent")
    private float one4NightsPercent;

    @Column(name = "one_5_nights_percent")
    @SerializedName("one_5_nights_percent")
    private float one5NightsPercent;

    public ServiceFee(){
    }

    public float getFixedFee(){
        return fixedFee;
    }

    public float getOneNrUntil20Percent(){
        return oneNrUntil20Percent;
    }

    public float getOneNrFrom20To25Percent(){
        return oneNrFrom20To25Percent;
    }

    public float getOneNrFromMore25Percent(){
        return oneNrFromMore25Percent;
    }

    public float getOneNightSeveralRoomsPercent(){
        return oneNightSeveralRoomsPercent;
    }

    public float getOne2NightsPercent(){
        return one2NightsPercent;
    }

    public float getOne3NightsPercent(){
        return one3NightsPercent;
    }

    public float getOne4NightsPercent(){
        return one4NightsPercent;
    }

    public float getOne5NightsPercent(){
        return one5NightsPercent;
    }
}
