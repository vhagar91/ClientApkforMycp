package hds.aplications.com.mycp.sync;

import android.content.Context;

import java.util.Date;

import hds.aplications.com.mycp.R;
import hds.aplications.com.mycp.helpers.SAppData;
import hds.aplications.com.mycp.models.User;
import hds.aplications.com.mycp.repositories.UserRepository;
import hds.aplications.com.mycp.services.UserClient;
import hds.aplications.com.mycp.view.components.LoadMask;
import mgleon.common.com.DateUtils;
import mgleon.common.com.LogUtils;
import mgleon.common.com.MessageToast;
import mgleon.common.com.receivers.ConnectivityReceiver;
import mgleon.common.com.services.Util.ProcessRetrofitError;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class DataSynchronizer {

    LoadMask loadingMask;
    String initialText;
    String startDate;
    private static final String TAG = LogUtils.makeLogTag(DataSynchronizer.class);

    public DataSynchronizer() {
        loadingMask = null;
        Date today = DateUtils.beginningDay(new Date());
        this.startDate = DateUtils.DATE_FORMAT.format(today);
    }

    public DataSynchronizer(LoadMask loadingMask)
    {
        this.loadingMask = loadingMask;

        Date today = DateUtils.beginningDay(new Date());
        this.startDate = DateUtils.DATE_FORMAT.format(today);
        //this.startDate = "2014-01-01";
    }

    public DataSynchronizer(LoadMask loadingMask, String initialText)
    {
        this.loadingMask = loadingMask;
        this.initialText = initialText;
        Date today = DateUtils.beginningDay(new Date());
        this.startDate = DateUtils.DATE_FORMAT.format(today);
        //this.startDate = "2014-01-01";
    }

    public void synchronizeReservationsData(final Context context, final Callback<User> callBackSync) {
        if(ConnectivityReceiver.isOnline(context)){
            UserClient userClient = new UserClient(context);
            showLoadingMask();

            Callback<User> callback = getCallback(context, callBackSync, null);

            /*Callback<User> callback = new Callback<User>() {
                @Override
                public void success(User user, Response response) {
                    try {
                        UserRepository.saveUserSync(user);
                        callBackSync.success(user, response);
                    }
                    catch(Exception ex){
                        MessageToast.showError(context, ex.getLocalizedMessage());
                    }
                    finally {
                        loadingMask.hide();
                    }
                }
                @Override
                public void failure(RetrofitError retrofitError) {
                    callBackSync.failure(retrofitError);
                    loadingMask.hide();
                    MessageToast.showError(context, context.getString(R.string.no_internet_connection));
                }
            };*/


            userClient.getReservations(Long.toString(SAppData.getInstance().getUser().getIdRef()), this.startDate, callback);
        }
        else {
            hideLoadingMask();
            MessageToast.showError(context, context.getString(R.string.no_internet_connection));
        }
    }

    public void synchronizeUserData(String userName, final String pass, final Context context, final Callback<User> callBackSync) {
        if(ConnectivityReceiver.isOnline(context)){
            UserClient userClient = new UserClient(context);
            showLoadingMask();
            Callback<User> callback = getCallback(context, callBackSync, pass);
            userClient.login(userName, pass, this.startDate, callback);
        }
        else {
            MessageToast.showError(context, context.getString(R.string.no_internet_connection));
        }
    }

    public User synchronizeUserData(String userName, final String pass, final Context context) {
        if(ConnectivityReceiver.isOnline(context)){
            UserClient userClient = new UserClient(context);
            showLoadingMask();
            return userClient.login(userName, pass, this.startDate);
        }
        else {
            MessageToast.showError(context, context.getString(R.string.no_internet_connection));
            return null;
        }
    }

    private void showLoadingMask()
    {
        if (loadingMask == null){
            return;
        }
        if(this.initialText != null && this.initialText != "")
            loadingMask.show(this.initialText);
        else
            loadingMask.show();
    }

    private void hideLoadingMask()
    {
        if (loadingMask == null){
            return;
        }
        loadingMask.hide();
    }

    private Callback<User> getCallback(final Context context,final Callback<User> callBackSync, final String password)
    {
        Callback<User> callback = new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                try {
                    if(password != null && password != ""){
//                        user.setPassword(password);
                    }
                    else
                    {
//                        String currentPassword = SAppData.getInstance().getUser().getPassword();
//
//                        user.setPassword(currentPassword);
                    }
                    UserRepository.saveUserSync(user);

                    callBackSync.success(user, response);
                    hideLoadingMask();
//                    MessageToast.showSuccess(context, context.getString(R.string.sync_data_title));

                }
                catch(Exception ex){
                    MessageToast.showError(context, ex.getLocalizedMessage());
                    hideLoadingMask();
                }
                finally {
                    hideLoadingMask();
                }
            }
            @Override
            public void failure(RetrofitError retrofitError) {
                callBackSync.failure(retrofitError);
                hideLoadingMask();
                ProcessRetrofitError.showError(retrofitError, context);

                LogUtils.LOGE(TAG, retrofitError.getMessage(), retrofitError);
            }
        };
        return callback;
    }
}
