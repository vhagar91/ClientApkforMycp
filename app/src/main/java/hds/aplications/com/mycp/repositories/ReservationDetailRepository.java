package hds.aplications.com.mycp.repositories;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.util.List;

import hds.aplications.com.mycp.models.Reservation;
import hds.aplications.com.mycp.models.ReservationDetail;
import hds.aplications.com.mycp.models.ReservationStatus;
import hds.aplications.com.mycp.models.Room;
import hds.aplications.com.mycp.models.User;
import mgleon.common.com.repositories.BaseRepository;

public class ReservationDetailRepository extends BaseRepository {

    public ReservationDetailRepository() {
        this.cls = ReservationDetail.class;
    }

    public static void saveReservationDetailsFromReservation(Reservation reservation) {
        ActiveAndroid.beginTransaction();
        try {
            for(ReservationDetail reservationDetail : reservation.getReservationDetails())
            {
                reservationDetail.setReservation(reservation);
                RoomRepository.saveRoomFromReservationDetail(reservationDetail);
                reservationDetail.save();
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

    public List<Reservation> getReservationsByStatus(Room room, ReservationStatus status){
        return new Select().from(Reservation.class).where("id_room = ?", room.getId()).where("id_status = ?", status.getId()).execute();
    }

    public List<Reservation> getReservationsByUser(User user){
        return new Select().from(Reservation.class).where("id_room = ?", user.getId()).execute();
    }

}
