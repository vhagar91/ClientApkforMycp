package hds.aplications.com.mycp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

import java.util.List;



@Table(name = "currency", id = "_id")
public class Currency extends Model {

    @Column(name = "id_ref")
    @SerializedName("curr_id")
    private long idRef;

    @Column(name = "name")
    @SerializedName("curr_name")
    private String name;

    @Column(name = "code")
    @SerializedName("curr_code")
    private String code;

    @Column(name = "change")
    @SerializedName("curr_cuc_change")
    private Float change;

    @Column(name = "symbol")
    @SerializedName("curr_symbol")
    private String symbol;

    public Currency(){
    }

    public Currency(String name, String code){
        this.name = name;
        this.code = code;
    }

    public long getIdRef(){
        return idRef;
    }

    public String getName(){
        return name;
    }

    public String getCode(){
        return code;
    }

    public Float getChange(){
        return change;
    }

    public String getSymbol(){
        return symbol;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setCode(String code){
        this.code = code;
    }

    public void setChange(Float change){
        this.change = change;
    }

    public void setSymbol(String symbol){
        this.symbol = symbol;
    }

    @Override
    public String toString(){
        return this.name + " (" + this.code + ")";
    }
}
