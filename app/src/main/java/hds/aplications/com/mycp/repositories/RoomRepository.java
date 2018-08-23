package hds.aplications.com.mycp.repositories;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Select;

import java.util.List;

import hds.aplications.com.mycp.models.Accommodation;
import hds.aplications.com.mycp.models.ReservationDetail;
import hds.aplications.com.mycp.models.Room;
import hds.aplications.com.mycp.models.RoomType;
import mgleon.common.com.repositories.BaseRepository;

public class RoomRepository extends BaseRepository {

    public RoomRepository() {
        this.cls = Room.class;
    }

    public List<Model> getByAccommodation(Accommodation accommodation) {
        return new Select()
                .from((Class<? extends Model>) this.cls)
                .where("id_accommodation = ?", accommodation.getId()).orderBy("number ASC").execute();
    }

    public static void saveRoomFromReservationDetail(ReservationDetail reservationDetail) {
        ActiveAndroid.beginTransaction();
        try {
            Room room = reservationDetail.getRoom();
            RoomRepository roomRepository = new RoomRepository();
            Room roomAux = (Room)roomRepository.getByIdRef(room.getIdRef(), Room.class);
            if(roomAux != null){
                reservationDetail.setRoom(roomAux);
                roomAux.setAccommodation(reservationDetail.getReservation().getAccommodation());
            }
            else {
                room.setAccommodation(reservationDetail.getReservation().getAccommodation());
                //room.setRoomType((RoomType) BaseRepository.getByName(room.getRoomTypeApiField(), RoomType.class));

                room.save();
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
