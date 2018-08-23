package hds.aplications.com.mycp.services;

import android.content.Context;

import java.util.List;

import hds.aplications.com.mycp.helpers.SAppData;
import hds.aplications.com.mycp.models.Answer;
import hds.aplications.com.mycp.models.Language;
import hds.aplications.com.mycp.models.User;
import mgleon.common.com.services.BaseClient;
import retrofit.Callback;

/**
 * Created by Miguel Gomez Leon.
 * mgleonsc@gmail.com
 */
public class LanguageClient extends BaseClient{
    public LanguageClient(Context context) {
        super(LanguageService.class, context);
    }

    public void getAll(Callback<List<Language>> callback) {
        ((LanguageService) this.service).getAll(this.getConfigApi().getmResponseFormat(), this.getConfigApi().getmKey(), callback);
    }

    public void change(String languageId, Callback<Answer> callback){
        User user = SAppData.getInstance().getUser();
        if(user == null){
            return;
        }
        String idUser = String.valueOf(user.getIdRef());
        String token = user.getToken();

        ((LanguageService)this.service).change(this.getConfigApi().getmResponseFormat(), this.getConfigApi().getmKey(), idUser, token, languageId, callback);
    }

}
