package hds.aplications.com.mycp.view;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import hds.aplications.com.mycp.R;
import hds.aplications.com.mycp.helpers.DateUtils;
import hds.aplications.com.mycp.helpers.MessageToast;
import hds.aplications.com.mycp.helpers.WifiReceiver;
import hds.aplications.com.mycp.models.User;
import hds.aplications.com.mycp.repositories.UserRepository;
import hds.aplications.com.mycp.services.UserClient;
import hds.aplications.com.mycp.services.authenticator.AccountGeneral;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * The Authenticator activity.
 *
 * Called by the Authenticator and in charge of identifing the user.
 *
 * It sends back to the Authenticator the result.
 */
public class LoginActivity extends AccountAuthenticatorActivity {

    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";

    public static final String KEY_ERROR_MESSAGE = "ERR_MSG";

    public final static String PARAM_USER_PASS = "USER_PASS";

    private final int REQ_SIGNUP = 1;

    private final String TAG = this.getClass().getSimpleName();

    private AccountManager mAccountManager;
    private String mAuthTokenType;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAccountManager = AccountManager.get(getBaseContext());

        String accountName = getIntent().getStringExtra(ARG_ACCOUNT_NAME);
        mAuthTokenType = getIntent().getStringExtra(ARG_AUTH_TYPE);
        if (mAuthTokenType == null)
            mAuthTokenType = AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS;

        if (accountName != null) {
            ((TextView)findViewById(R.id.username_edit)).setText(accountName);
        }

        findViewById(R.id.ok_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        /*findViewById(R.id.signUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Since there can only be one AuthenticatorActivity, we call the sign up activity, get his results,
                // and return them in setAccountAuthenticatorResult(). See finishLogin().
                Intent signup = new Intent(getBaseContext(), SignUpActivity.class);
                signup.putExtras(getIntent().getExtras());
                startActivityForResult(signup, REQ_SIGNUP);
            }
        });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // The sign up activity returned that the user has successfully created an account
        if (requestCode == REQ_SIGNUP && resultCode == RESULT_OK) {
            finishLogin(data);
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    public void submit() {

        final String userName = ((TextView) findViewById(R.id.username_edit)).getText().toString();
        final String userPass = ((TextView) findViewById(R.id.password_edit)).getText().toString();

        final String accountType = getIntent().getStringExtra(ARG_ACCOUNT_TYPE);

        new AsyncTask<String, Void, Intent>() {

            @Override
            protected Intent doInBackground(String... params) {

                Bundle data = new Bundle();
                if(WifiReceiver.isOnline(getApplicationContext())) {
                    Log.d("MyCasaParticular", TAG + "> Started authenticating");
                    final String authtoken = null;

                    try {
                        Date start = DateUtils.beginningDay(new Date());
                        Date end = DateUtils.beginningDay(new Date());
                        Callback<User> callback = new Callback<User>() {
                            @Override
                            public void success(User user, Response response) {
                                try {
                                    user.setPassword(userPass);
                                    UserRepository.saveUserSync(user);

                                    LoginActivity.this.startActivityWelcome(user);
                                }
                                catch(Exception ex){
                                    MessageToast.showError(LoginActivity.this.getApplicationContext(), ex.getLocalizedMessage());
                                }
                                finally {
                                    //loadingMask.hide();
                                }
                            }
                            @Override
                            public void failure(RetrofitError retrofitError) {
                                //loadingMask.hide();
                                MessageToast.showError(LoginActivity.this.getApplicationContext(), getString(R.string.no_internet_connection));
                            }
                        };
                        //authtoken = sServerAuthenticate.userSignIn(userName, userPass, mAuthTokenType);

                        UserClient userClient = new UserClient(getApplicationContext());
                        userClient.login(userName, userPass, DateUtils.DATE_FORMAT.format(start), DateUtils.DATE_FORMAT.format(end), callback);

                        data.putString(AccountManager.KEY_ACCOUNT_NAME, userName);
                        data.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType);
                        data.putString(AccountManager.KEY_AUTHTOKEN, authtoken);
                        data.putString(PARAM_USER_PASS, userPass);

                    } catch (Exception e) {
                        data.putString(KEY_ERROR_MESSAGE, e.getMessage());
                    }

                }
                else
                    data.putString(KEY_ERROR_MESSAGE, getString(R.string.no_internet_connection));

                final Intent res = new Intent();
                res.putExtras(data);
                return res;
            }

            @Override
            protected void onPostExecute(Intent intent) {
                if (intent.hasExtra(KEY_ERROR_MESSAGE)) {
                    Toast.makeText(getBaseContext(), intent.getStringExtra(KEY_ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
                } else {
                    finishLogin(intent);
                }
            }
        }.execute();
    }

    private void finishLogin(Intent intent) {
        Log.d("MyCasaParticular", TAG + "> finishLogin");

        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String accountPassword = intent.getStringExtra(PARAM_USER_PASS);
        final Account account = new Account(accountName, AccountGeneral.ACCOUNT_TYPE);

        /*
        if (getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {*/
            Log.d("MyCasaParticular", TAG + "> finishLogin > addAccountExplicitly");
            String authtoken = "sdafdsadfsdfdf"; //intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
            String authtokenType = mAuthTokenType;

            // Creating the account on the device and setting the auth token we got
            // (Not setting the auth token will cause another call to the server to authenticate the user)
            mAccountManager.addAccountExplicitly(account, accountPassword, null);
            mAccountManager.setAuthToken(account, authtokenType, authtoken);
       /* } else {
            Log.d("MyCasaParticular", TAG + "> finishLogin > setPassword");
            mAccountManager.setPassword(account, accountPassword);
        }
     */
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void startActivityWelcome(User user){
        //Creamos la información a pasar entre activities
        //SAppData.getInstance().setUser(user);
        //LoginActivity.this.startActivity(new Intent(LoginActivity.this, ActivityWelcome.class));
    }

}
