package hds.aplications.com.mycp.repositories;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import hds.aplications.com.mycp.models.Accommodation;
import hds.aplications.com.mycp.models.Reservation;
import hds.aplications.com.mycp.models.User;
import mgleon.common.com.repositories.BaseRepository;

public class ReservationRepository extends BaseRepository {

    public ReservationRepository() {
        this.cls = Reservation.class;
    }

    public static void saveReservationFromUser(User user) {
        ActiveAndroid.beginTransaction();
        try {
            for(Reservation reservation : user.getReservations())
            {
                reservation.setUser(user);
                AccommodationRepository.saveAccommodationsFromReservation(reservation);
                reservation.save();

                ReservationDetailRepository.saveReservationDetailsFromReservation(reservation);
            }
            ActiveAndroid.setTransactionSuccessful();
        }
        catch(Exception ex){
            throw  ex;
        }
        finally {
            ActiveAndroid.endTransaction();
        }
    }

    public List<Reservation> getReservationsByUser(User user){
        return new Select().from(Reservation.class).where("id_user = ?", user.getId()).orderBy("date_from ASC").execute();
    }
    public List<Reservation> getReservationsByStatus(User user,int status){
        return new Select().from(Reservation.class).where("id_user = ?", user.getId()).and("id_status = ?", status).orderBy("date_from DESC").execute();
    }

    public static List<Reservation> getReservationsByUserAccomodation(User user, Accommodation accommodation){
        if(accommodation.getId() == null){
            return new ArrayList<>();
        }
        return new Select().from(Reservation.class).where("id_user = ?", user.getId()).and("id_accommodation = ?", accommodation.getId()).orderBy("date_from ASC").execute();
    }
}
