package hds.aplications.com.mycp.map.marker;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import hds.aplications.com.mycp.helpers.SAppData;
import hds.aplications.com.mycp.models.Accommodation;
import hds.aplications.com.mycp.models.Reservation;
import hds.aplications.com.mycp.repositories.ReservationRepository;


public class CustomMarkerModel implements Parcelable {

    private final String title;
    private final int drawableRes;
    private final Location location;

    private Accommodation accommodation;
    private List<Reservation> reservations;

    public CustomMarkerModel(@NonNull String title, @DrawableRes int drawableRes, double lati, double longi) {
        this.title = title;
        this.drawableRes = drawableRes;
        this.location = new Location("");
        location.setLatitude(lati);
        location.setLongitude(longi);
    }

    public CustomMarkerModel(@NonNull String title, @DrawableRes int drawableRes, @NonNull Location location) {
        this.title = title;
        this.drawableRes = drawableRes;
        this.location = location;
    }

    public CustomMarkerModel(@NonNull Accommodation accommodation, @DrawableRes int drawableRes) {
        this.accommodation = accommodation;
        this.reservations = new ArrayList<>();
        if(SAppData.getInstance().getUser() != null){
            this.reservations =  ReservationRepository.getReservationsByUserAccomodation(SAppData.getInstance().getUser(), this.accommodation);
        }

        this.title = accommodation.getName();
        this.drawableRes = drawableRes;
        this.location = new Location("");
        location.setLatitude(accommodation.getLat());
        location.setLongitude(accommodation.getLon());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(drawableRes);
        dest.writeParcelable(location, 0);
    }

    public static final Creator<CustomMarkerModel> CREATOR = new Creator<CustomMarkerModel>() {
        public CustomMarkerModel createFromParcel(Parcel in) {
            return new CustomMarkerModel(in);
        }

        public CustomMarkerModel[] newArray(int size) {
            return new CustomMarkerModel[size];
        }
    };

    private CustomMarkerModel(Parcel in) {
        title = in.readString();
        drawableRes = in.readInt();
        location = in.readParcelable(null);
    }

    public String getTitle() {
        return title;
    }

    public int getDrawableRes() {
        return drawableRes;
    }

    public Location getLocation() {
        return location;
    }

    public Drawable getDrawable(@NonNull Resources resources) {
        return resources.getDrawable(drawableRes);
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}
