package hds.aplications.com.mycp.repositories;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Select;

import java.util.List;

import hds.aplications.com.mycp.models.Accommodation;
import hds.aplications.com.mycp.models.AudiovisualType;
import hds.aplications.com.mycp.models.BathroomType;
import hds.aplications.com.mycp.models.ClimateType;
import hds.aplications.com.mycp.models.Room;
import hds.aplications.com.mycp.models.RoomType;

public class RoomRepository extends BaseRepository {

    public RoomRepository() {
        this.cls = Room.class;
    }

    public List<Model> getByAccommodation(Accommodation accommodation) {
        return new Select()
                .from((Class<? extends Model>) this.cls)
                .where("id_accommodation = ?", accommodation.getId()).orderBy("number ASC").execute();
    }

    public Model getByIdRef(long idRef) {
        return new Select().from((Class<? extends Model>) this.cls).where("idRef = ?", idRef).orderBy("number ASC").executeSingle();
    }

    public static void saveRoomsFromAccommodation(Accommodation accommodation) {
        ActiveAndroid.beginTransaction();
        try {
            for(Room room : accommodation.getRooms())
            {
                //no hace falta por: onDelete = Column.ForeignKeyAction.CASCADE
                /*RoomRepository roomRepository = new RoomRepository();
                Room roomAux = (Room)roomRepository.getByIdRef(room.getIdRef());
                if(roomAux != null){
                    roomAux.delete();
                }*/

                room.setAccommodation(accommodation);
                room.setRoomType((RoomType) BaseRepository.getByName(room.getRoomTypeApiField(), RoomType.class));
                room.setClimateType((ClimateType) BaseRepository.getByName(room.getClimateTypeApiField(), ClimateType.class));
                room.setAudiovisualType((AudiovisualType) BaseRepository.getByName(room.getAudiovisualTypeApiField(), AudiovisualType.class));
                room.setBathroomType((BathroomType) BaseRepository.getByName(room.getBathroomTypeApiField(), BathroomType.class));
                room.save();

                UDetailsRepository.saveUDetailsFromRoom(room);
                ReservationRepository.saveReservationsFromRoom(room);
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
