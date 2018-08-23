package hds.aplications.com.mycp.repositories;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Select;

import hds.aplications.com.mycp.models.Accommodation;
import hds.aplications.com.mycp.models.User;
import hds.aplications.com.mycp.models.UserAccommodation;

public class AccommodationRepository extends BaseRepository {

    public AccommodationRepository() {
        this.cls = Accommodation.class;
    }

    public Model getByCode(String code) {
        //s.substring(0, 1).toUpperCase();
        return new Select()
                .from((Class<? extends Model>) this.cls).where("LOWER(code) = ?", code.toLowerCase())
                /*.where("code LIKE ?", new String[]{'%' + code + '%'})*/.executeSingle();
    }

    public static void saveAccommodationsFromUser(User user) {
        ActiveAndroid.beginTransaction();
        try {
            for (Accommodation accommodation : user.getReservations()) {
                AccommodationRepository accommodationRepository = new AccommodationRepository();
                Accommodation accommodationAux = (Accommodation)accommodationRepository.getByCode(accommodation.getCode());
                if(accommodationAux != null){
                    accommodationAux.delete();
                }

                accommodation.save();
                RoomRepository.saveRoomsFromAccommodation(accommodation);

                UserAccommodation userAccommodation = new UserAccommodation(accommodation, user);
                userAccommodation.save();
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
}
