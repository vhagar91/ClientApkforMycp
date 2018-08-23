package hds.aplications.com.mycp.services.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import hds.aplications.com.mycp.R;
import hds.aplications.com.mycp.helpers.SNotification;
import hds.aplications.com.mycp.services.authenticator.AccountGeneral;
import hds.aplications.com.mycp.view.ActivityNoFilesMap;

/**
 * Created by miguel on 24/12/2015.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private final AccountManager accountManager;

    public SyncAdapter(Context context, boolean autoInitialize){
        super(context, autoInitialize);
        accountManager = AccountManager.get(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d("mycplight", "onPerformSync for account[" + account.name + "]");

        try{
            //Obteniendo el token de autenticacion de la cuenta actual. El ultimo parámetro indica si se desea que se levante una notificacion al user si hay algun problema de autenticacion.
            String authToken = accountManager.blockingGetAuthToken(account, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, true);

            //TODO: update local data with remote data

            //Show notification is there is new data
            SNotification.notificate(this.getContext(), ActivityNoFilesMap.class,R.drawable.mcp, null, getContext().getString(R.string.app_name) , "Tiene nuevas reservas confirmadas", null, null);


        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
