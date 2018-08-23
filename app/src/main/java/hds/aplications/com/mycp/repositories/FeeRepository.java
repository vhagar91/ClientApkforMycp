package hds.aplications.com.mycp.repositories;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Select;

import java.util.List;

import hds.aplications.com.mycp.models.Accommodation;
import hds.aplications.com.mycp.models.ReservationDetail;
import hds.aplications.com.mycp.models.Room;
import hds.aplications.com.mycp.models.ServiceFee;
import mgleon.common.com.repositories.BaseRepository;

public class FeeRepository extends BaseRepository {

    public FeeRepository() {
        this.cls = ServiceFee.class;
    }



    public static void saveFeeFromAcomodation(Accommodation accommodation) {
        ActiveAndroid.beginTransaction();
        try {
            ServiceFee serviceFee = accommodation.getServiceFee();
            FeeRepository feeRepository = new FeeRepository();
            ServiceFee serviceFee1 = (ServiceFee)feeRepository.getByIdRef(serviceFee.getIdRef(), ServiceFee.class);
            if(serviceFee1 != null){
                accommodation.setServiceFee(serviceFee1);
            }
            else {
                serviceFee.save();
                //room.setRoomType((RoomType) BaseRepository.getByName(room.getRoomTypeApiField(), RoomType.class));
                accommodation.setServiceFee(serviceFee);

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
