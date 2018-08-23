package hds.aplications.com.mycp.services;

import android.content.Context;

import java.util.List;

import hds.aplications.com.mycp.helpers.SAppData;
import hds.aplications.com.mycp.models.Answer;
import hds.aplications.com.mycp.models.Country;
import hds.aplications.com.mycp.models.Currency;
import hds.aplications.com.mycp.models.User;
import mgleon.common.com.services.BaseClient;
import retrofit.Callback;

/**
 * Created by Miguel Gomez Leon.
 * mgleonsc@gmail.com
 */
public class CurrencyClient extends BaseClient{
    public CurrencyClient(Context context) {
        super(CurrencyService.class, context);
    }

    public void getAll(Callback<List<Currency>> callback) {
        ((CurrencyService) this.service).getAll(this.getConfigApi().getmResponseFormat(), this.getConfigApi().getmKey(), callback);
    }

    public void change(String currencyId, Callback<Answer> callback){
        User user = SAppData.getInstance().getUser();
        if(user == null){
            return;
        }
        String idUser = String.valueOf(user.getIdRef());
        String token = user.getToken();

        ((CurrencyService)this.service).change(this.getConfigApi().getmResponseFormat(), this.getConfigApi().getmKey(), idUser, token, currencyId, callback);
    }

}
