package hds.aplications.com.mycp.view.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;

import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;

import hds.aplications.com.mycp.MyCP;
import hds.aplications.com.mycp.R;
import hds.aplications.com.mycp.helpers.LocaleHelper;
import hds.aplications.com.mycp.helpers.PDFTools;
import hds.aplications.com.mycp.helpers.PermissionsAdapter;
import hds.aplications.com.mycp.helpers.SAppData;
import hds.aplications.com.mycp.models.Answer;
import hds.aplications.com.mycp.models.Currency;
import hds.aplications.com.mycp.models.Language;
import hds.aplications.com.mycp.models.User;
import hds.aplications.com.mycp.services.CurrencyClient;
import hds.aplications.com.mycp.services.LanguageClient;
import hds.aplications.com.mycp.services.ReservationClient;
import hds.aplications.com.mycp.services.UserClient;
import hds.aplications.com.mycp.view.components.LoadMask;
import hds.aplications.com.mycp.view.others.AnimatorUtils;
import mgleon.common.com.MessageToast;
import mgleon.common.com.receivers.ConnectivityReceiver;
import mgleon.common.com.services.Util.ProcessRetrofitError;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityBase extends AppCompatActivity{
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    /*private static final String TAG = LogUtils.makeLogTag(ActivityBase.class);*/
    protected Context context;
    protected DrawerLayout drawer;
    protected RelativeLayout content;
    protected LoadMask loadingMask;
    /*private LoadMask loadingMaskNotifications;*/
    protected AppBarLayout appBarLayout;
    protected RelativeLayout relativeLayout;

    protected MyCP myCP;
    protected User user;

    private View layoutSnackbar;

    protected String activityTitle;
    protected AppCompatTextView textViewBarTitle;

    protected FloatingActionButton fabMenu;
    protected ImageButton menuOptions;
    protected ImageButton menuNotifications;
    private ScrollView menuAllOptions;
    private boolean optionsVisible = false;

    private ConnectivityReceiver.Listener wifiReceiverListener;

    private TextView badge;

    private AlertDialog.Builder alertCoins;
    private AlertDialog alertDialogCoins;
    private ListView listViewCoins;

    private AlertDialog.Builder alertPass;
    private AlertDialog alertDialogPass;

    private AlertDialog.Builder alertLanguages;
    private AlertDialog alertDialogLanguages;
    private ListView listViewLanguages;
    protected Tracker sTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        MyCP application = (MyCP) getApplication();
        sTracker = application.getDefaultTracker();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appBarLayout =  findViewById(R.id.app_bar_layout);
        relativeLayout = findViewById(R.id.base_relative_layout);

        loadingMask = new LoadMask(this);
        /*loadingMaskNotifications = new LoadMask(this, (RelativeLayout) findViewById(R.id.load_mask_notifications));*/
        content = findViewById(R.id.content_activity);

        layoutSnackbar = findViewById(R.id.relative_layout_snackbar);

        badge = findViewById(R.id.badge);

        PermissionsAdapter.checkPermissions(this);

        this.initOptins();
        this.initialize();
        this.bindActions();
    }

    private User getUser(){
        myCP = MyCP.getApplication(context);
        return myCP.loadUser();
    }

    private void initOptins(){
        menuAllOptions = findViewById(R.id.menu_all_options);
        fabMenu = findViewById(R.id.fab_menu);
        fabMenu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(optionsVisible){
                    hideOptions();
                }
                else {
                    showOptions();
                }
            }
        });

        findViewById(R.id.option_login).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                hideOptions();
                Intent i1 = new Intent(context, ActivityLogin.class);
                startActivity(i1);
            }
        });

        findViewById(R.id.option_logout).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                hideOptions();
                MyCP.getApplication(context).goLogout();
            }
        });

        findViewById(R.id.option_register).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                hideOptions();
                Intent i1 = new Intent(context, ActivityRegister.class);
                startActivity(i1);
            }
        });

        findViewById(R.id.option_basket).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                hideOptions();
                Intent i1 = new Intent(context, ActivityPaymentAllReservations.class);
                startActivity(i1);
            }
        });
        findViewById(R.id.option_reservations).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                hideOptions();
                Intent i1 = new Intent(context, MycasatripActivity.class);
                startActivity(i1);
            }
        });

        findViewById(R.id.option_coins).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                hideOptions();
                findCoins();
            }
        });

        findViewById(R.id.option_pass).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                hideOptions();
                changePass();
            }
        });


        findViewById(R.id.option_favorite).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                hideOptions();

                Intent intent = new Intent(context, ActivitySearch.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("favorites", true);
                addToFavoriteBundle(bundle);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        findViewById(R.id.option_lan).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                hideOptions();
                findLanguages();
            }
        });

        findViewById(R.id.l_option_notifications).setVisibility(View.GONE);

        findViewById(R.id.l_option_security).setVisibility(View.GONE);
        findViewById(R.id.l_option_profile).setVisibility(View.GONE);
    }

    protected void addToFavoriteBundle(Bundle bundle){}

    private void showOptions(){
        fabMenu.setRotation(0f);
        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                fabMenu,
                AnimatorUtils.rotation(0f, 90f)
        );
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(400);
        animSet.setInterpolator(new OvershootInterpolator());
        animSet.playTogether(anim);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //fabMenu.setImageResource(R.drawable.vector_minimizar);
            }
        });
        animSet.start();

        menuAllOptions.setVisibility(View.VISIBLE);

        optionsVisible = true;
    }

    private void hideOptions(){
        fabMenu.setRotation(0f);
        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                fabMenu,
                AnimatorUtils.rotation(90f, 0f)
        );
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(400);
        animSet.setInterpolator(new OvershootInterpolator());
        animSet.playTogether(anim);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //fabMenu.setImageResource(R.drawable.vector_menu_burger);
            }
        });
        animSet.start();

        menuAllOptions.setVisibility(View.GONE);

        optionsVisible = false;
    }

    protected void initialize(){
        activityTitle = getResources().getString(R.string.app_name_together);
        textViewBarTitle = ((AppCompatTextView) findViewById(R.id.main_bar_title));
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        menuOptions = (ImageButton) findViewById(R.id.menu_options);
        menuNotifications = (ImageButton) findViewById(R.id.menu_notifications);
        wifiReceiverListener = new ConnectivityReceiver.Listener(){
            @Override
            public boolean Online(){
                layoutSnackbar.setVisibility(View.GONE);
                online();
                return true;
            }

            @Override
            public boolean OffOnline(){
                layoutSnackbar.setVisibility(View.VISIBLE);
                offOnline();
                return true;
            }
        };
    }

    private void findCoins(){
        if(ConnectivityReceiver.isOnline(context)){
            loadingMask.show();
            CurrencyClient currencyClient = new CurrencyClient(context);
            Callback<List<Currency>> callback = new Callback<List<Currency>>(){
                @Override
                public void success(List<Currency> currencies, Response response){
                    loadingMask.hide();
                    initializeChangeCoins(currencies);
                }

                @Override
                public void failure(RetrofitError error){
                    loadingMask.hide();
                    ProcessRetrofitError.showError(error, context);
                }
            };
            currencyClient.getAll(callback);
        }
        else {
            MessageToast.showError(context, context.getString(R.string.no_internet_connection));
        }
    }

    private void initializeChangeCoins(final List<Currency> currencies){
        if(alertCoins == null){
            alertCoins = new AlertDialog.Builder(this);
            final View view = View.inflate(this, R.layout.alert_change_coins, null);
            listViewCoins = view.findViewById(R.id.coins_list);
            ArrayAdapter<Currency> dataAdapter = new ArrayAdapter<Currency>(this, android.R.layout.simple_list_item_single_choice, currencies){
                @NonNull
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    CheckedTextView checkedTextViewLast = v.findViewById(android.R.id.text1);
                    Currency currency = ((ArrayAdapter<Currency>) listViewCoins.getAdapter()).getItem(position);

                    if(user.getCurrency().getIdRef() == currency.getIdRef()){
                        checkedTextViewLast.setChecked(true);
                    }
                    else{
                        ((CheckedTextView)v.findViewById(android.R.id.text1)).setChecked(false);
                    }
                    return v;
                }
            };
            listViewCoins.setAdapter(dataAdapter);
            listViewCoins.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    final Currency currency = ((ArrayAdapter<Currency>) listViewCoins.getAdapter()).getItem(position);
                    final Currency oldCurrency = user.getCurrency();
                    if(currency.getIdRef() == oldCurrency.getIdRef()){
                        loadingMask.hide();
                        return;
                    }
                    else {
                        alertDialogCoins.dismiss();
                        loadingMask.show();

                        CurrencyClient currencyClient = new CurrencyClient(context);
                        Callback<Answer> callback = new Callback<Answer>(){
                            @Override
                            public void success(Answer answer, Response response){
                                loadingMask.hide();
                                if(answer.isSuccess()){
                                    currency.save();
                                    user.setCurrency(currency);
                                    user.save();
                                    oldCurrency.delete();
                                    MessageToast.showInfo(context, String.valueOf(currency.getIdRef()) + " - " + currency.getName() + "-" + currency.getCode());
                                    restartActivity();
                                }
                            }

                            @Override
                            public void failure(RetrofitError error){
                                loadingMask.hide();
                                ProcessRetrofitError.showError(error, context);
                            }
                        };
                        currencyClient.change(String.valueOf(currency.getIdRef()), callback);
                    }
                }
            });
            view.findViewById(R.id.btn_closex).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    alertDialogCoins.dismiss();
                }
            });

            alertCoins.setView(view);
            alertDialogCoins = alertCoins.show();
        }
        else {
            ArrayAdapter<Currency> dataAdapter = (ArrayAdapter<Currency>) listViewCoins.getAdapter();
            dataAdapter.clear();
            dataAdapter.addAll(currencies);
            dataAdapter.notifyDataSetChanged();
            alertDialogCoins.show();
        }
    }

    private void findLanguages(){
        if(ConnectivityReceiver.isOnline(context)){
            loadingMask.show();
            LanguageClient languageClient = new LanguageClient(context);
            Callback<List<Language>> callback = new Callback<List<Language>>(){
                @Override
                public void success(List<Language> languages, Response response){
                    loadingMask.hide();
                    initializeChangeLanguages(languages);
                }

                @Override
                public void failure(RetrofitError error){
                    loadingMask.hide();
                    ProcessRetrofitError.showError(error, context);
                }
            };
            languageClient.getAll(callback);
        }
        else {
            MessageToast.showError(context, context.getString(R.string.no_internet_connection));
        }
    }

    private void initializeChangeLanguages(final List<Language> languages){
        if(alertLanguages == null){
            alertLanguages = new AlertDialog.Builder(this);
            final View view = View.inflate(this, R.layout.alert_change_languages, null);
            listViewLanguages = view.findViewById(R.id.languages_list);
            ArrayAdapter<Language> dataAdapter = new ArrayAdapter<Language>(this, android.R.layout.simple_list_item_single_choice, languages){
                @NonNull
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    CheckedTextView checkedTextViewLast = v.findViewById(android.R.id.text1);
                    Language language = ((ArrayAdapter<Language>) listViewLanguages.getAdapter()).getItem(position);

                    if(user.getLangCode().equals(language.getCode())){
                        checkedTextViewLast.setChecked(true);
                    }
                    else{
                        ((CheckedTextView)v.findViewById(android.R.id.text1)).setChecked(false);
                    }
                    return v;
                }
            };
            listViewLanguages.setAdapter(dataAdapter);
            listViewLanguages.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    final Language language = ((ArrayAdapter<Language>) listViewLanguages.getAdapter()).getItem(position);
                    final String oldLanguage = user.getLangCode();
                    if(language.getCode().equals(oldLanguage)){
                        loadingMask.hide();
                        return;
                    }
                    else {
                        alertDialogLanguages.dismiss();
                        loadingMask.show();

                        LanguageClient languageClient = new LanguageClient(context);
                        Callback<Answer> callback = new Callback<Answer>(){
                            @Override
                            public void success(Answer answer, Response response){
                                loadingMask.hide();
                                if(answer.isSuccess()){
                                    user.setLangCode(language.getCode());
                                    user.save();
                                    MessageToast.showInfo(context, String.valueOf(language.getIdRef()) + " - " + language.getName() + "-" + language.getCode());
                                    restartActivity();
                                }
                            }

                            @Override
                            public void failure(RetrofitError error){
                                loadingMask.hide();
                                ProcessRetrofitError.showError(error, context);
                            }
                        };
                        languageClient.change(String.valueOf(language.getIdRef()), callback);
                    }
                }
            });
            view.findViewById(R.id.btn_closex).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    alertDialogLanguages.dismiss();
                }
            });

            alertLanguages.setView(view);
            alertDialogLanguages = alertLanguages.show();
        }
        else {
            ArrayAdapter<Language> dataAdapter = (ArrayAdapter<Language>) listViewLanguages.getAdapter();
            dataAdapter.clear();
            dataAdapter.addAll(languages);
            dataAdapter.notifyDataSetChanged();
            alertDialogLanguages.show();
        }
    }

    private void changePass(){
        if(alertPass == null){
            alertPass = new AlertDialog.Builder(this);
            final View view = View.inflate(this, R.layout.alert_change_pass, null);
            view.findViewById(R.id.btn_closex).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    alertDialogPass.dismiss();
                }
            });

            view.findViewById(R.id.button_change_pass).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    String currentPass = ((AppCompatEditText)view.findViewById(R.id.current_password)).getText().toString();
                    String newPass = ((AppCompatEditText)view.findViewById(R.id.password)).getText().toString();
                    String newrPass = ((AppCompatEditText)view.findViewById(R.id.r_password)).getText().toString();
                    if(newPass.isEmpty() || newrPass.isEmpty()){
                        MessageToast.showError(context, "Required fields");
                        return;
                    }
                    else if(!newPass.equals(newrPass)){
                        MessageToast.showError(context, "Passwords must be equal");
                        return;
                    }
                    alertDialogPass.dismiss();

                    loadingMask.show();
                    UserClient userClient = new UserClient(context);
                    Callback<Answer> callback = new Callback<Answer>(){
                        @Override
                        public void success(Answer answer, Response response){
                            loadingMask.hide();
                            if(answer.isSuccess()){
                                MessageToast.showInfo(context, "Successfully changed");
                                return;
                            }
                            MessageToast.showError(context, "An error has occurred");
                        }

                        @Override
                        public void failure(RetrofitError error){
                            loadingMask.hide();
                            ProcessRetrofitError.showError(error, context);
                        }
                    };
                    userClient.changePass(newPass, callback);
                }
            });

            alertPass.setView(view);
            alertDialogPass = alertPass.show();
        }
        else {
            alertDialogPass.show();
        }
    }

    protected void offOnline(){

    }

    protected void online(){

    }

    protected void bindActions(){
        /*Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Myriad_Pro_Regular.ttf");*/

        textViewBarTitle.setText(this.activityTitle);
        //textViewBarTitle.setTypeface(typeface);

        menuOptions.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onMenuOptions();
            }
        });

        menuNotifications.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent i1 = new Intent(context, ActivityPaymentAllReservations.class);
                startActivity(i1);
                /*if(user == null){
                    return;
                }
                if(drawer.isDrawerOpen(GravityCompat.END)){
                    drawer.closeDrawer(GravityCompat.END);
                }
                else{
                    drawer.openDrawer(GravityCompat.END);
                }*/
            }
        });

        NavigationView navigationViewOptions = (NavigationView) findViewById(R.id.nav_view_options);
        NavigationView navigationViewNotifications = (NavigationView) findViewById(R.id.nav_view_notifications);

        int width = getResources().getDisplayMetrics().widthPixels - 40;
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) navigationViewNotifications.getLayoutParams();
        params.width = width;
        navigationViewNotifications.setLayoutParams(params);

        params = (DrawerLayout.LayoutParams) navigationViewOptions.getLayoutParams();
        params.width = width;
        navigationViewOptions.setLayoutParams(params);
    }

    protected boolean isShowReservationsCount(){
        return true;
    }

    private void findReservationsCount(){
        if(isShowReservationsCount() && user != null){
            ReservationClient reservationClient = new ReservationClient(context);
            Callback<Answer> callback = new Callback<Answer>(){
                @Override
                public void success(Answer answer, Response response){
                    String v = String.valueOf(answer.getMsg());
                    badge.setText(v);

                    if(v.equals("0")){
                        badge.setVisibility(View.INVISIBLE);
                    }
                    else {
                        badge.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void failure(RetrofitError error){}
            };

            reservationClient.getReservationsCount(callback);
        }
    }

    protected void addListeners(){
        ConnectivityReceiver connectivityReceiver = ConnectivityReceiver.getInstance();
        connectivityReceiver.addListener(wifiReceiverListener);
    }

    protected void removeListeners(){
        ConnectivityReceiver connectivityReceiver = ConnectivityReceiver.getInstance();
        connectivityReceiver.removeListener(wifiReceiverListener);
    }

    protected void onMenuOptions(){
        if(user == null){
            finish();
            return;
        }

        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            drawer.openDrawer(GravityCompat.START);
        }
    }

    public void addView(View child) {
        this.content.addView(child, 0);
    }

    protected void hideFabBottom() {
        /*LinearLayout linearLayout = (LinearLayout) findViewById(R.id.table_content_home);
        linearLayout.animate().translationY(-linearLayout.getHeight()).setInterpolator(new AccelerateInterpolator(2));*/

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) fabMenu.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        fabMenu.animate().translationY(fabMenu.getHeight() + fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    protected void showFabBottom() {
        /*LinearLayout linearLayout = (LinearLayout) findViewById(R.id.table_content_home);
        linearLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));*/

        fabMenu.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }

    /************
     * Overrides
     ***********/

    protected void restartActivity(){
        Intent intent = this.getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    protected void onResume(){
        super.onResume();


        if(user == null && SAppData.getInstance().getUser() != null){
            user = SAppData.getInstance().getUser();

            restartActivity();
            return;
        }

        if(user != null){
            findViewById(R.id.l_option_login).setVisibility(View.GONE);
            findViewById(R.id.l_option_logout).setVisibility(View.VISIBLE);
            findViewById(R.id.l_option_coins).setVisibility(View.VISIBLE);
            findViewById(R.id.l_option_register).setVisibility(View.GONE);
            findViewById(R.id.l_option_reservations).setVisibility(View.VISIBLE);
            findViewById(R.id.l_option_basket).setVisibility(View.VISIBLE);
            findViewById(R.id.l_option_pass).setVisibility(View.VISIBLE);
            findViewById(R.id.l_option_favorite).setVisibility(View.VISIBLE);
            findViewById(R.id.l_option_lan).setVisibility(View.VISIBLE);

            menuOptions.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.vector_menu_burger));
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            updateUserInfo();
            findReservationsCount();
        }
        else {
            findViewById(R.id.l_option_login).setVisibility(View.VISIBLE);
            findViewById(R.id.l_option_logout).setVisibility(View.GONE);
            findViewById(R.id.l_option_coins).setVisibility(View.VISIBLE);
            findViewById(R.id.l_option_register).setVisibility(View.VISIBLE);
            findViewById(R.id.l_option_reservations).setVisibility(View.GONE);
            findViewById(R.id.l_option_pass).setVisibility(View.GONE);
            findViewById(R.id.l_option_favorite).setVisibility(View.GONE);
            findViewById(R.id.l_option_lan).setVisibility(View.VISIBLE);
            findViewById(R.id.l_option_basket).setVisibility(View.GONE);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            menuOptions.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.vector_arrow_prev));

            badge.setVisibility(View.INVISIBLE);
            menuNotifications.setVisibility(View.INVISIBLE);
        }

        addListeners();

        if(ConnectivityReceiver.isOnline(getApplicationContext())){
            layoutSnackbar.setVisibility(View.GONE);
        }
        else{
            layoutSnackbar.setVisibility(View.VISIBLE);
        }
    }

    private void updateUserInfo(){
        if(user.getPhoto() != null && !user.getPhoto().isEmpty()){
            ImageView imageView = (ImageView) findViewById(R.id.circle_image_view_profile_user_image);
            MyCP.getPicassoBuilder(context).build().load(MyCP.SERVER2 + user.getPhoto())/*.placeholder(R.mipmap.user_white)*/.fit().centerCrop().into(imageView);
        }

//        TextView textView = (TextView) findViewById(R.id.user_name);
//        textView.setText(user.getName());
        TextView textView = (TextView) findViewById(R.id.user_full_name);
        textView.setText(user.getFullName());

    }

    @Override
    public void onBackPressed(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(drawer.isDrawerOpen(GravityCompat.END)){
            drawer.closeDrawer(GravityCompat.END);
        }
        else if(optionsVisible){
            hideOptions();
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop(){
        super.onStop();

        removeListeners();
    }

    @Override
    protected void attachBaseContext(Context newBase){
        Context c = CalligraphyContextWrapper.wrap(newBase);

        context = c;
        user = getUser();
        if(user != null && !user.getLangCode().toLowerCase().equals(Locale.getDefault().getLanguage().toLowerCase()) && LocaleHelper.supportLocale(user.getLangCode())){
            c = LocaleHelper.onAttach(c, user.getLangCode());
            //MessageToast.showInfo(c, "Lenguague " + user.getLangCode());
        }
        context = c;
        super.attachBaseContext(c);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsAdapter.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
    public void onClickProfile(View v){

        Intent i1 = new Intent(context, ActivityProfile.class);
        startActivity(i1);
    }
    public void onClickReservations(View v){

        Intent i1 = new Intent(context, MycasatripActivity.class);
        startActivity(i1);
    }
    public void onClickSettings(View v){

        Intent i1 = new Intent(context, SettingsActivity.class);
        startActivity(i1);
    }
    public void LogOut(View v){
        hideOptions();
        MyCP.getApplication(context).goLogout();
    }
    public void TermsConditions(View v){
        if(PDFTools.isPDFSupported(context)){
            AssetManager assetManager = getAssets();
            String lang =SAppData.getInstance().getUser().getLangCode();
            if(lang==""||lang==null) {
                lang="EN";
            }
            InputStream in = null;
            OutputStream out = null;
            String filename="terms_"+lang+".pdf";
            String fileuri="pdf"+File.separator+filename;

            String strDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)+"";
            final File tempFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), filename);
            if (tempFile.exists()) {
                // If we have downloaded the file before, just go ahead and show it.

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setDataAndType(Uri.fromFile(tempFile), "application/pdf");
                startActivity(i);


            }
            else {
                File fileDir = new File(strDir);
                fileDir.mkdirs();   // crear la ruta si no existe

                File file = new File(fileDir, filename);

                try {

                    in = assetManager.open(fileuri);

                    //leer el archivo de assets
                    out = new BufferedOutputStream(new FileOutputStream(file)); //crear el archivo


                    PDFTools.copyFile(in, out);
                    in.close();
                    in = null;
                    out.flush();
                    out.close();
                    out = null;
                } catch (Exception e) {
                    Log.e("tag", e.getMessage());
                }
                final File tempFile1 = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), filename);


                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setDataAndType(Uri.fromFile(tempFile1), "application/pdf");
                startActivity(i);
            }
        }
        else{

        }

    }
    public void Security(View v){
        if(PDFTools.isPDFSupported(context)){
            AssetManager assetManager = getAssets();
            String lang =SAppData.getInstance().getUser().getLangCode();
            if(lang==""||lang==null) {
                lang="EN";
            }
            InputStream in = null;
            OutputStream out = null;
            String filename="security_"+lang+".pdf";
            String fileuri="pdf"+File.separator+filename;

            String strDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)+"";
            final File tempFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), filename);
            if (tempFile.exists()) {
                // If we have downloaded the file before, just go ahead and show it.

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setDataAndType(Uri.fromFile(tempFile), "application/pdf");
                startActivity(i);


            }
            else {
                File fileDir = new File(strDir);
                fileDir.mkdirs();   // crear la ruta si no existe

                File file = new File(fileDir, filename);

                try {

                    in = assetManager.open(fileuri);

                    //leer el archivo de assets
                    out = new BufferedOutputStream(new FileOutputStream(file)); //crear el archivo


                    PDFTools.copyFile(in, out);
                    in.close();
                    in = null;
                    out.flush();
                    out.close();
                    out = null;
                } catch (Exception e) {
                    Log.e("tag", e.getMessage());
                }
                final File tempFile1 = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), filename);

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setDataAndType(Uri.fromFile(tempFile1), "application/pdf");
                startActivity(i);
            }
        }
        else{

        }


    }
    public void onFavorites(View v){
        hideOptions();

        Intent intent = new Intent(context, ActivitySearch.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("favorites", true);
        addToFavoriteBundle(bundle);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}