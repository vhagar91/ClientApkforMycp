package hds.aplications.com.mycp.models;

import com.activeandroid.Model;
import com.activeandroid.TableInfo;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.Locale;



public class TranslatableModel extends Model{
    protected Traslation getTraslation(String field,String lang){
        TableInfo tableInfo = new TableInfo(this.getClass());
//        String locale = Locale.getDefault().getLanguage();
        String tableName = tableInfo.getTableName();

        return new Select().from(Traslation.class).where("id_obj = ?", this.getId()).
                and("type_obj = ?", tableName).and("field = ?", field).and("locale = ?", lang).executeSingle();
    }
}
