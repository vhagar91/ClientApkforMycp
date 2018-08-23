package hds.aplications.com.mycp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;



@Table(name = "address", id = "_id")
public class Address extends Model {

    @Column(name = "street")
    public String street;

    @Column(name = "number")
    public String number;

    @Column(name = "between_street_one")
    public String betweenStreetOne;

    @Column(name = "between_street_two")
    public String betweenStreetTwo;

    @Column(name = "id_municipality")
    public Municipality municipality;

    public Address() {
    }

    public Address(String street, String number, String betweenStreetOne, String betweenStreetTwo, Municipality municipality) {
        this.street = street;
        this.number = number;
        this.betweenStreetOne = betweenStreetOne;
        this.betweenStreetTwo = betweenStreetTwo;
        this.municipality = municipality;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBetweenStreetOne() {
        return betweenStreetOne;
    }

    public void setBetweenStreetOne(String betweenStreetOne) {
        this.betweenStreetOne = betweenStreetOne;
    }

    public String getBetweenStreetTwo() {
        return betweenStreetTwo;
    }

    public void setBetweenStreetTwo(String betweenStreetTwo) {
        this.betweenStreetTwo = betweenStreetTwo;
    }

    public Municipality getMunicipality() {
        return municipality;
    }

    public void setMunicipality(Municipality municipality) {
        this.municipality = municipality;
    }
}
