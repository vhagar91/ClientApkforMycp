package hds.aplications.com.mycp.view.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.gms.analytics.HitBuilders;

import hds.aplications.com.mycp.MyCP;
import hds.aplications.com.mycp.R;
import hds.aplications.com.mycp.helpers.SAppData;
import hds.aplications.com.mycp.models.User;
import hds.aplications.com.mycp.repositories.UserRepository;
import hds.aplications.com.mycp.sync.DataSynchronizer;
import hds.aplications.com.mycp.sync.adapter.SyncAdapter;
import mgleon.common.com.LogUtils;
import mgleon.common.com.MessageToast;
import mgleon.common.com.receivers.ConnectivityReceiver;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActivityLogin extends ActivityBase {

    public static final String TAG = LogUtils.makeLogTag(ActivityLogin.class);
    private AccountManager mAccountManager;
    private Account account;
    public static boolean NOT_SYNC = false;
    protected boolean mNewAccount = true;

    @Override
    protected void initialize() {
        super.initialize();

        menuOptions.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.vector_arrow_prev));

        View viewItem = View.inflate(ActivityLogin.this, R.layout.content_login, null);
        this.addView(viewItem);
    }

    @Override
    protected void bindActions(){
        super.bindActions();
        this.init();

        this.fabMenu.setVisibility(View.GONE);
        //this.appBarLayout.setVisibility(View.GONE);
        textViewBarTitle.setCompoundDrawables(null, null, null, null);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        /*CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)relativeLayout.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        relativeLayout.setLayoutParams(params);*/
    }

    @Override
    protected void onMenuOptions(){
        //super.onMenuOptions();
        finish();
    }

    public void init(){
        mAccountManager = AccountManager.get(this);

        myCP = MyCP.getApplication(getApplicationContext());

        final Intent intent = getIntent();
        if (intent.getExtras() != null && intent.getExtras().getBoolean(this.getResources().getString(R.string.KEY_ACCOUNT_ADD_AUTHENTICATOR))){
            myCP.rememberCredentials(null);
        }

        if (myCP.isRemember()) {
            String username = myCP.getUserRemember();
            account = new Account(username, this.getResources().getString(R.string.ACCOUNT_TYPE));
            String password = mAccountManager.getPassword(account);

            if(password != null){
                ((CheckBox)findViewById(R.id.remember_pass)).setChecked(true);
                ((EditText) findViewById(R.id.edit_text_user)).setText(username);
                ((EditText)findViewById(R.id.edit_text_user_pass)).setText(password);
                login(username, password);
            }
        }

        SyncAdapter.SyncAdapterListener syncAdapterListener = new SyncAdapter.SyncAdapterListener() {
            @Override
            public boolean beforeSync() {
                if (NOT_SYNC){
                    NOT_SYNC = false;
                    return false;
                }
                return true;
            }
        };
        SyncAdapter.addSyncAdapterListener(syncAdapterListener);

        //quitar el foco de el primer input
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void actionLogin(View view) {
        EditText editTextUser = (EditText)findViewById(R.id.edit_text_user);
        String user = editTextUser.getText().toString();

        EditText editTextPass = (EditText)findViewById(R.id.edit_text_user_pass);
        String pass = editTextPass.getText().toString();

        if (!TextUtils.isEmpty(user) && !TextUtils.isEmpty(pass)){
            login(user, pass);
        }
        else {
            MessageToast.showError(getApplicationContext(), getString(R.string.info_alert_user));
        }

    }
    public void onClickRegister(View v){

        Intent i1 = new Intent(context, ActivityRegister.class);
        startActivity(i1);
    }

    private void login(final String name, final String pass){
        UserRepository userRepository = new UserRepository();
        final User user = (User)userRepository.getByUser(name);
        final DataSynchronizer synchronizer = new DataSynchronizer(loadingMask, getString(R.string.login_user));

        final Callback<User> callBackSync = new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                LogUtils.LOGI(TAG, "Sync successfully through ActivityLogin synchronizeUserData");
                ActivityLogin.this.loginSuccess(user, pass);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                LogUtils.LOGE(TAG, "Sync failure through ActivityLogin synchronizeUserData", retrofitError);
            }
        };

        mNewAccount = (user == null);
        if(!mNewAccount){
            String username = user.getEmail();
            account = new Account(username, this.getResources().getString(R.string.ACCOUNT_TYPE));
        }

        /*if(user != null && ConnectivityReceiver.isOnline(getApplicationContext())){
            FragmentManager fragmentManager = getSupportFragmentManager();
            Confirmation dialogo = new Confirmation();
            dialogo.setMessage(getString(R.string.sync_info_alert_message_to_local)).setTitle(getString(R.string.sync_info_alert_title)).setTitleNotOkBnt(getString(R.string.option_no)).setTitleOKBnt(getString(R.string.option_yes));

            dialogo.clickOk = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id){
                    dialog.cancel();
                    //ActivityLogin.this.synchronizeUserData(name, pass, callBackSync);
                    synchronizer.synchronizeUserData(name, pass, ActivityLogin.this, callBackSync);
                }
            };
            dialogo.clickNotOk = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id){
                    dialog.cancel();
                    ckeckUserLocal(user, pass);
                }
            };
            dialogo.show(fragmentManager, "tagConfirmacion");
            return;
        }
        else */if(ConnectivityReceiver.isOnline(getApplicationContext())){
            synchronizer.synchronizeUserData(name, pass, ActivityLogin.this, callBackSync);
            return;
        }

        ckeckUserLocal(user, pass);
    }

    private void ckeckUserLocal(User user, String pass){
        if(account == null){
            MessageToast.showError(ActivityLogin.this.getApplicationContext(), getString(R.string.icorrect_credential));
            return;
        }

        String rpass = mAccountManager.getPassword(account);

        if(user != null && rpass != null && rpass.equals(pass)){
            loginSuccess(user, rpass);
        }
        else {
            MessageToast.showError(ActivityLogin.this.getApplicationContext(), getString(R.string.icorrect_credential));
        }
    }

    private void loginSuccess(User user, String pass) {
        MyCP.getApplication(getApplicationContext()).initiatedWelcome = true;
        if (ContentResolver.getSyncAutomatically(account, this.getResources().getString(R.string.CONTENT_AUTHORITY))){
            NOT_SYNC = true;
        }
        if (mNewAccount || mAccountManager.getPassword(account) == null) {
            account = new Account(user.getEmail(), this.getResources().getString(R.string.ACCOUNT_TYPE));
            mAccountManager.addAccountExplicitly(account, pass, null);
        } else {
            mAccountManager.setPassword(account, pass);
        }

        if(((CheckBox)findViewById(R.id.remember_pass)).isChecked()){
            myCP.rememberCredentials(user.getEmail());
        }
        else {
            myCP.rememberCredentials(null);
        }

        //Creamos la información a pasar entre activities
        SAppData.getInstance().setUser(user);
        if(MyCP.getApplication(getApplicationContext()).xlogout){
            startActivity(new Intent(ActivityLogin.this, ActivityHome.class));
            MyCP.getApplication(getApplicationContext()).xlogout = false;
        }
        else {
            finish();
            Bundle bundle = getIntent().getExtras();
            if(bundle != null && bundle.containsKey("action")){//si bundle != null es porque se le seteo el bundle para despues ir a bookNow, (podria ir a otro lugar segun parametros)
                switch (bundle.getString("action")){
                    case ActivityBook.TAG:
                        MyCP.getApplication(context).bookNow(bundle, null, null, null);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        sTracker.setScreenName("Log In");
        sTracker.send(new HitBuilders.ScreenViewBuilder().build());

        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.containsKey("action") && bundle.getString("action").equals(ActivityLogin.TAG)){
            ((EditText)findViewById(R.id.edit_text_user)).setText(bundle.getString("user"));
            ((EditText)findViewById(R.id.edit_text_user_pass)).setText(bundle.getString("password"));

            actionLogin(null);
        }
        /*if(MyCP.getApplication(getApplicationContext()).finish){
            MyCP.getApplication(getApplicationContext()).finish = false;
            MyCP.getApplication(getApplicationContext()).initiatedWelcome = false;
            this.finish();
            return;
        }*/
    }
}
