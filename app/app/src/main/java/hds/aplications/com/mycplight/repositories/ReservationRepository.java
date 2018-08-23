package hds.aplications.com.mycp.repositories;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.util.List;

import hds.aplications.com.mycp.models.Reservation;
import hds.aplications.com.mycp.models.ReservationStatus;
import hds.aplications.com.mycp.models.Room;

public class ReservationRepository extends BaseRepository {

    public ReservationRepository() {
        this.cls = Reservation.class;
    }

    public static void saveReservationsFromRoom(Room room) {
        ActiveAndroid.beginTransaction();
        try {
            for(Reservation reservation : room.getReservations(true))
            {
                reservation.setRoom(room);

                //Cargar el estado
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

    public List<Reservation> getReservedReservations(Room room){
        ReservationStatus status = (ReservationStatus) BaseRepository.getByName(ReservationStatus.STATUS_RESERVED, ReservationStatus.class);
        return this.getReservationsByStatus(room, status);
    }

    public List<Reservation> getCancelledReservations(Room room){
        ReservationStatus status = (ReservationStatus) BaseRepository.getByName(ReservationStatus.STATUS_CANCELED, ReservationStatus.class);
        return this.getReservationsByStatus(room, status);
    }

    public List<Reservation> getReservationsByStatus(Room room, ReservationStatus status){
        return new Select()
                .from(Reservation.class)
                .where("id_room = ?", room.getId())
                .where("id_status = ?", status.getId())
                .execute();
    }

}
