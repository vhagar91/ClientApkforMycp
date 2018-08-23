package hds.aplications.com.mycp.repositories;

import com.activeandroid.Model;
import com.activeandroid.query.Select;

import java.util.List;


public class BaseRepository {

    public Class<?> cls;

    public BaseRepository() {
    }

    public Model getById(String id) {
        return new Select().from((Class<? extends Model>) cls).where(cls.getSimpleName() + " = ?", id).executeSingle();
    }

    public static Model getByName(String searchName, Class<? extends Model> model){
        return new Select().from(model).where("name = ?", searchName).executeSingle();
    }

    public Model getByName(String searchName){
        return new Select().from((Class<? extends Model>) cls).where("name = ?", searchName).executeSingle();
    }

    public List<Model> getAll() {
        return new Select().from((Class<? extends Model>) cls).orderBy("name ASC").execute();
    }
}
