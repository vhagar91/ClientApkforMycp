package hds.aplications.com.mycp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Miguel Gomez Leon.
 * mgleonsc@gmail.com
 */

@Table(name = "traslation", id = "_id")
public class Traslation extends Model{

    @Column(name = "id_obj")
    public long idObj;

    @Column(name = "type_obj")
    public String typeObj;

    @Column(name = "field")
    public String field;

    @Column(name = "locale")
    public String locale;

    @Column(name = "content")
    public String content;

    public Traslation() {
    }

    public String getContent(){
        return content;
    }
}
