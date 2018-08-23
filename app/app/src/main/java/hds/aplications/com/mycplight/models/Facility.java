package hds.aplications.com.mycp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "facility", id = "_id")
public class Facility extends Model {

    @Column(name = "priceFrom")
    public Float priceFrom;

    @Column(name = "priceTo")
    public Float priceTo;

    @Column(name = "id_accommodation")
    public Accommodation accommodation;

    @Column(name = "id_facility")
    public Facility facility;

    public Facility() {
    }

    public Facility(Float priceFrom, Float priceTo, Accommodation accommodation, Facility facility) {

        this.accommodation = accommodation;
        this.facility = facility;
        this.priceFrom = priceFrom;
        this.priceTo = priceTo;
    }

    public Float getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(Float priceFrom) {
        this.priceFrom = priceFrom;
    }

    public Float getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(Float priceTo) {
        this.priceTo = priceTo;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }
}
