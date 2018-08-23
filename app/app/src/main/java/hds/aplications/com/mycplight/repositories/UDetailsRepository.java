package hds.aplications.com.mycp.repositories;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Update;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import hds.aplications.com.mycp.helpers.DateUtils;
import hds.aplications.com.mycp.models.Room;
import hds.aplications.com.mycp.models.UDetails;

public class UDetailsRepository extends BaseRepository {

    public UDetailsRepository() {
        this.cls = UDetails.class;
    }

    public static void saveUDetailsFromRoom(Room room) {
        ActiveAndroid.beginTransaction();
        try {
            for(UDetails uDetails : room.getuDetailses(true))
            {
                uDetails.setRoom(room);
                uDetails.save();
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

    public void saveUDetailsFromRanges(Room room, Date startDate, Date endDate, JSONArray ranges) throws JSONException {
        Calendar calendarStartDate = Calendar.getInstance();
        calendarStartDate.setTime(startDate);
        calendarStartDate.add(Calendar.DATE, -1);

        new Update((Class<? extends Model>)this.cls).set("dateTo = " + calendarStartDate.getTime().getTime())
                .where("id_room = " + room.getId() + " AND " + "dateFrom < " + startDate.getTime() + " AND " + "dateTo >= " + startDate.getTime())
                .execute();

        new Delete().from((Class<? extends Model>)this.cls)
                .where("id_room = ?", room.getId())
                .and("dateFrom >= ?", startDate.getTime())
                .and("dateTo <= ?", endDate.getTime())
                .execute();

        ActiveAndroid.beginTransaction();
        try {
            for (int i = 0; i < ranges.length(); i++){
                JSONObject jsonObject = ranges.getJSONObject(i);
                Date dateFrom = DateUtils.generateDayFromString(jsonObject.getString("start"));
                Date dateTo = DateUtils.generateDayFromString(jsonObject.getString("end"));

                UDetails uDetails = new UDetails(-1, dateFrom, dateTo, null, room, "sdf");
                uDetails.save();
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
