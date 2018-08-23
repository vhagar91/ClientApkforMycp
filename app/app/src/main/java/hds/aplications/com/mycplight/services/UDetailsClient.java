package hds.aplications.com.mycp.services;

import android.content.Context;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;

import java.util.List;

import hds.aplications.com.mycp.helpers.MessageToast;
import hds.aplications.com.mycp.models.UDetails;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UDetailsClient extends BaseClient {
    //public AccommodationService service;

    public UDetailsClient(Context context) {
        super(UDetailsService.class, context);
    }

    public void getAll(long code){
        Callback<List<UDetails>> callback = new Callback<List<UDetails>>() {
            @Override
            public void success(List<UDetails> uDetailses, Response response) {

                ActiveAndroid.beginTransaction();
                try {
                    for(UDetails udetail : uDetailses ) {
                        udetail.save();
                    }
                    ActiveAndroid.setTransactionSuccessful();
            }
            catch(Exception ex){
                Toast toast = Toast.makeText(UDetailsClient.this.context, ex.getLocalizedMessage(), Toast.LENGTH_LONG);
                toast.show();
            }
            finally {
                ActiveAndroid.endTransaction();
            }

            }

            @Override
            public void failure(RetrofitError retrofitError) {
                //Showing errors
                Toast toast = Toast.makeText(UDetailsClient.this.context, retrofitError.getLocalizedMessage(), Toast.LENGTH_LONG);
                toast.show();
            }
        };

        ((UDetailsService)this.service).getAll(code, BaseClient.API_RESPONSE_FORMAT, BaseClient.API_KEY, callback);

    }

    public void getAll(long code, Callback<List<UDetails>> callback){

        ((UDetailsService)this.service).getAll(code, BaseClient.API_RESPONSE_FORMAT, BaseClient.API_KEY, callback);

    }

    public void add(long code,String fromDate, String toDate, String reason){
        Callback<UDetails> callback = new Callback<UDetails>() {
            @Override
            public void success(UDetails udetail, Response response) {

                ActiveAndroid.beginTransaction();
                try {
                    udetail.save();
                    ActiveAndroid.setTransactionSuccessful();
                }
                catch(Exception ex){
                    Toast toast = Toast.makeText(UDetailsClient.this.context, ex.getLocalizedMessage(), Toast.LENGTH_LONG);
                    toast.show();
                }
                finally {
                    ActiveAndroid.endTransaction();
                }

            }

            @Override
            public void failure(RetrofitError retrofitError) {
                //Showing errors
                Toast toast = Toast.makeText(UDetailsClient.this.context, retrofitError.getLocalizedMessage(), Toast.LENGTH_LONG);
                toast.show();
            }
        };

        ((UDetailsService)this.service).add(code, fromDate, toDate, reason, BaseClient.API_RESPONSE_FORMAT, BaseClient.API_KEY, callback);

    }

    public void add(long code,String fromDate, String toDate, String reason, Callback<UDetails> callback){
        ((UDetailsService)this.service).add(code, fromDate, toDate, reason, BaseClient.API_RESPONSE_FORMAT, BaseClient.API_KEY, callback);

    }

    public void addByRange(long code, String start, String end, String dateRange, String reason, Callback<UDetails> callback){
        ((UDetailsService)this.service).addByRanges(code, BaseClient.API_RESPONSE_FORMAT, BaseClient.API_KEY, start, end, dateRange, reason, callback);
    }

    public void delete(long code,String fromDate, String toDate, String id){
        Callback<UDetails> callback = new Callback<UDetails>() {
            @Override
            public void success(UDetails udetail, Response response) {

                ActiveAndroid.beginTransaction();
                try {
                    udetail.delete();
                    ActiveAndroid.setTransactionSuccessful();
                }
                catch(Exception ex){
                    MessageToast.showError(UDetailsClient.this.context, ex.getLocalizedMessage());
                }
                finally {
                    ActiveAndroid.endTransaction();
                }

            }

            @Override
            public void failure(RetrofitError retrofitError) {
                MessageToast.showError(UDetailsClient.this.context, retrofitError.getLocalizedMessage());
            }
        };

        ((UDetailsService)this.service).delete(code, fromDate, toDate, id, BaseClient.API_RESPONSE_FORMAT, BaseClient.API_KEY, callback);

    }
}
