package hds.aplications.com.mycp.repositories;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Select;

import hds.aplications.com.mycp.models.User;

public class UserRepository extends BaseRepository {

    public UserRepository() {
        this.cls = User.class;
    }

    public Model getByUser(String user) {
        //s.substring(0, 1).toUpperCase();
        return new Select().from((Class<? extends Model>) this.cls).where("LOWER(name) = ?", user.toLowerCase()).executeSingle();
    }

    public static void saveUser(User user) {
        ActiveAndroid.beginTransaction();
        try {
            UserRepository userRepository = new UserRepository();
            User userAux = (User)userRepository.getByUser(user.getName());
            if(userAux != null){
                userAux.delete();
            }

            user.save();
            AccommodationRepository.saveAccommodationsFromUser(user);
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
