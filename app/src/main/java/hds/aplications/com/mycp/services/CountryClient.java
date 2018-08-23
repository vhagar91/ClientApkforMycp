package hds.aplications.com.mycp.services;

import android.content.Context;

import java.util.List;

import hds.aplications.com.mycp.models.Country;
import hds.aplications.com.mycp.models.Destination;
import mgleon.common.com.services.BaseClient;
import retrofit.Callback;

/**
 * Created by Miguel Gomez Leon.
 * mgleonsc@gmail.com
 */
public class CountryClient extends BaseClient{
    public CountryClient(Context context) {
        super(CountryService.class, context);
    }

    public void getAll(Callback<List<Country>> callback) {
        ((CountryService) this.service).getAll(this.getConfigApi().getmResponseFormat(), this.getConfigApi().getmKey(), callback);
    }
}
