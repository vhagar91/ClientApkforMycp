package hds.aplications.com.mycp.repositories;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Select;

import hds.aplications.com.mycp.models.Currency;
import hds.aplications.com.mycp.models.Reservation;
import hds.aplications.com.mycp.models.ReservationStatus;
import hds.aplications.com.mycp.models.User;
import mgleon.common.com.repositories.BaseRepository;

public class UserRepository extends BaseRepository {

    public UserRepository() {
        this.cls = User.class;
    }

    public Model getByUser(String user) {
        //s.substring(0, 1).toUpperCase();
        return new Select().from((Class<? extends Model>) this.cls).where("LOWER(name) = ?", user.toLowerCase()).or("email = ?", user).executeSingle();
    }

    public static void saveUserSync(User user) {
        ActiveAndroid.beginTransaction();
        try {
            UserRepository userRepository = new UserRepository();
            User userAux = (User)userRepository.getByUser(user.getEmail());
            if(userAux != null){
                userAux.delete();
            }

            if(user.getCurrency() != null){
                Currency xCurrency = (Currency) BaseRepository.getByIdRef(user.getCurrency().getIdRef(), Currency.class);
                Currency nCurrency = user.getCurrency();

                if(xCurrency != null){
                    xCurrency.setName(nCurrency.getName());
                    xCurrency.setCode(nCurrency.getCode());
                    xCurrency.setChange(nCurrency.getChange());
                    xCurrency.setSymbol(nCurrency.getSymbol());
                    xCurrency.save();
                    user.setCurrency(xCurrency);
                }
                else{
                    nCurrency.save();
                }
            }
            user.save();

            if(user.getReservations() != null && !user.getReservations().isEmpty()){
                ReservationRepository.saveReservationFromUser(user);
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

    public static void saveReservationsFromUser(User user) {
        ActiveAndroid.beginTransaction();
        try {

            user.save();

            for(Reservation reservation : user.getReservations())
            {
                reservation.setUser(user);
                reservation.setStatus((ReservationStatus) BaseRepository.getByName(reservation.getReservationStatusApiField(), ReservationStatus.class));
                reservation.save();
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
}
