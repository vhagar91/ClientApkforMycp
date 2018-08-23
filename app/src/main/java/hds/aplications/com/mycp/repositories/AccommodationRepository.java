package hds.aplications.com.mycp.repositories;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Select;

import java.util.List;

import hds.aplications.com.mycp.models.Accommodation;
import hds.aplications.com.mycp.models.Reservation;
import hds.aplications.com.mycp.models.User;
import mgleon.common.com.repositories.BaseRepository;

public class AccommodationRepository extends BaseRepository{

    public AccommodationRepository() {
        this.cls = Accommodation.class;
    }

    public static void saveAccommodationsFromReservation (Reservation reservation) {
        ActiveAndroid.beginTransaction();
        try {

            Accommodation accommodation = reservation.getAccommodation();
            AccommodationRepository accommodationRepository = new AccommodationRepository();
            Accommodation accommodationAux = (Accommodation)accommodationRepository.getByIdRef(accommodation.getIdRef(), Accommodation.class);

            FeeRepository.saveFeeFromAcomodation(accommodation);

            if(accommodationAux != null){
                accommodationAux=accommodation;
                FeeRepository.saveFeeFromAcomodation(accommodationAux);
                reservation.setAccommodation(accommodationAux);
                accommodationAux.save();
            }
            else {

                accommodation.save();
            }

            ActiveAndroid.setTransactionSuccessful();
        }
        catch(Exception ex){
            throw ex;
        }
        finally {
            ActiveAndroid.endTransaction();
        }
    }

    public Model getByCode(String code) {
        //s.substring(0, 1).toUpperCase();
        return new Select()
                .from((Class<? extends Model>) this.cls).where("LOWER(code) = ?", code.toLowerCase())
                /*.where("code LIKE ?", new String[]{'%' + code + '%'})*/.executeSingle();
    }

    public static List<Model> getAcomodationReservedUser(User user){
        return new Select().from(Accommodation.class).innerJoin(Reservation.class).on("accommodation._id = reservation.id_accommodation").where("reservation.id_user = ?", user.getId()).groupBy("accommodation.name").execute();
    }

    /*
    SELECT
    ownership.own_name
            FROM
    ownership
    INNER JOIN generalreservation ON (ownership.own_id = generalreservation.gen_res_own_id)
    WHERE
    generalreservation.gen_res_user_id = 2100
    GROUP BY
    ownership.own_name
    */
}
