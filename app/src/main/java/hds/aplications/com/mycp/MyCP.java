package hds.aplications.com.mycp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;
import com.squareup.picasso.Picasso;

import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.Date;

import hds.aplications.com.mycp.helpers.SAppData;
import hds.aplications.com.mycp.models.Accommodation;
import hds.aplications.com.mycp.models.AccommodationCategory;
import hds.aplications.com.mycp.models.AccommodationType;
import hds.aplications.com.mycp.models.Bathroom;
import hds.aplications.com.mycp.models.ReservationStatus;
import hds.aplications.com.mycp.models.RoomAudiovisual;
import hds.aplications.com.mycp.models.RoomClima;
import hds.aplications.com.mycp.models.RoomType;
import hds.aplications.com.mycp.models.User;
import hds.aplications.com.mycp.repositories.UserRepository;
import hds.aplications.com.mycp.view.activities.ActivityBook;
import hds.aplications.com.mycp.view.activities.ActivityLogin;
import mgleon.common.com.DateUtils;
import mgleon.common.com.repositories.BaseRepository;
import mgleon.common.com.services.Util.Util;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;


public class MyCP extends com.activeandroid.app.Application {
    private Bus mOttoBus;
    private static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;
    private WeakReference<OkHttpClient> mOkHttpClientWeakReference;

    /*public static final String PREFS_NAME = "profile_preference_mycplight";
    private static final String PREF_USERNAME = "username";
    private static final String PREF_PASSWORD = "password";
    getApplicationContext().getResources().getString(R.string.PARAM_AUTHTOKEN_TYPE)*/

    public boolean xlogout = false;
    public boolean initiatedWelcome = false;

    public static String SERVER = "https://www.mycasaparticular.com/uploads/ownershipImages/";
    public static String SERVER2 = "https://www.mycasaparticular.com/uploads/userImages/";
    public static String SERVER3 = "https://www.mycasaparticular.com/avatar/";

    //public static String SERVER = "https://www.mycasaparticular.com/imagine/mycp_thumb_front/uploads/ownershipImages/";
    //public static String SERVER = "http://192.168.43.210";

    static Picasso.Builder builder;

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidGraphicFactory.createInstance(this);
        mOttoBus = new Bus(ThreadEnforcer.ANY);
        sAnalytics = GoogleAnalytics.getInstance(this);
        loadDefaultTypeAdapterObjs();
    }
    /**
     * Gets the default {@link Tracker} for this {@link MyCP}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (sTracker == null) {
            sTracker = sAnalytics.newTracker(R.xml.global_tracker);
            //Track all activities and Errors
            sTracker.enableExceptionReporting(true);
//            sTracker.enableAutoActivityTracking(true);
        }

        return sTracker;
    }

    public static MyCP getApplication(Context context) {
        if (context instanceof MyCP) {
            return (MyCP) context;
        }
        return (MyCP) context.getApplicationContext();
    }

    public void sendOttoEvent(Object event) {
        mOttoBus.post(event);
    }

    public void unregisterOttoBus(Object object) {
        mOttoBus.unregister(object);
    }

    public void registerOttoBus(Object object) {
        mOttoBus.register(object);
    }

    public OkHttpClient getOkHttpClient() {
        if (mOkHttpClientWeakReference == null || mOkHttpClientWeakReference.get() == null) {
            mOkHttpClientWeakReference = new WeakReference<>(new OkHttpClient());
        }
        return mOkHttpClientWeakReference.get();
    }

    public void bookNow(Bundle bundle, Accommodation accommodation, Date checkIn, Date checkOut){
        //Nota: Si se especifica un bundle, es porque no hace falta crearlo y se asume los otros parametros como nulos.
        if(bundle == null){
            bundle = new Bundle();
            bundle.putString("code_own", accommodation.getCode());
            bundle.putLong("id_own", accommodation.getIdRef());
            if(accommodation.getInmediateBooking()){
                bundle.putBoolean("inmediate_booking", true);
            }
            if(checkIn != null && checkOut != null){
                bundle.putString("check_in", DateUtils.generateStringDate(checkIn));
                bundle.putString("check_out", DateUtils.generateStringDate(checkOut));
            }
        }

        User user = SAppData.getInstance().getUser();
        if(user == null){ //este if pude quitarse e implementarse cuando ya se ha incertado la reserva y despues se va a pagar.
            bundle.putString("action", ActivityBook.TAG);
            goLogin(false, bundle);
        }
        else {
            Intent i1 = new Intent(getApplicationContext(), ActivityBook.class);
            i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i1.putExtras(bundle);
            startActivity(i1);
        }
    }

    /*public void finishAplication(*//*Activity activity*//*) {
        finish = true;
        goLogin();
    }*/

    public void goLogin(boolean top, Bundle bundle) {
        Intent intent = new Intent(getApplicationContext(), ActivityLogin.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(top){
            xlogout = true;
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        if(bundle != null){
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void goLogout() {
        rememberCredentials(null);
        SAppData.getInstance().setUser(null);
        //finishAplication();
        goLogin(true, null);
    }

    public boolean isRemember() {
        SharedPreferences pref = getSharedPreferences(getApplicationContext().getResources().getString(R.string.PREFS_NAME), MODE_PRIVATE);
        String username = pref.getString(getApplicationContext().getResources().getString(R.string.PREFS_NAME), null);
        //String password = pref.getString(PREF_PASSWORD, null);

        if (username != null /*&& password != null*/) {
            return true;
        }
        return false;
    }

    public void rememberCredentials(String user/*, String password*/) {
        if(user != null && !TextUtils.isEmpty(user.toLowerCase())){
            user = user.toLowerCase();
        }

        getSharedPreferences(getApplicationContext().getResources().getString(R.string.PREFS_NAME), MODE_PRIVATE).edit().putString(getApplicationContext().getResources().getString(R.string.PREFS_NAME), user)/*.putString(PREF_PASSWORD, password)*/.commit();
    }

    public String getUserRemember() {
        SharedPreferences pref = getSharedPreferences(getApplicationContext().getResources().getString(R.string.PREFS_NAME), MODE_PRIVATE);
        return pref.getString(getApplicationContext().getResources().getString(R.string.PREFS_NAME), null);
    }

    /*public String getPasswordRemember() {
        SharedPreferences pref = getSharedPreferences(getApplicationContext().getResources().getString(R.string.PREFS_NAME), MODE_PRIVATE);
        return pref.getString(PREF_PASSWORD, null);
    }*/

    public User loadUser(){
        AccountManager mAccountManager = AccountManager.get(this);
        User user = SAppData.getInstance().getUser();
        if (user == null && this.isRemember()) {
            String username = this.getUserRemember();
            Account account = new Account(username, this.getResources().getString(R.string.ACCOUNT_TYPE));

            String password = mAccountManager.getPassword(account);

            if(password != null && !password.isEmpty()){
                UserRepository userRepository = new UserRepository();
                user = (User)userRepository.getByUser(username);
                if(user != null){
                    SAppData.getInstance().setUser(user);
                }
            }
        }
        return user;
    }

    /*@SuppressWarnings("deprecation")
    public static void updateLocale(Context context, String sLocale){
        Locale locale = new Locale(sLocale.toLowerCase(), sLocale.toUpperCase());
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            configuration.setLocale(locale);
            context.createConfigurationContext(configuration);
        }
        else{
            configuration.locale=locale;
            resources.updateConfiguration(configuration,displayMetrics);
        }
    }*/

    private void loadDefaultTypeAdapterObjs(){
        final Gson gson = Util.GSON_BUILDER.serializeNulls().create();

        Util.addTypeAdapterObjs(Boolean.class, new JsonDeserializer<Boolean>() {
            @Override
            public Boolean deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException{
                Boolean value;
                try {
                    if (json instanceof JsonObject){
                        value = true;
                    }
                    else if (json instanceof JsonPrimitive){
                        int v = json.getAsInt();
                        value = (v == 0) ? (false) : (true);
                    }
                    else {
                        value = false;
                    }
                }
                catch (Exception e){
                    return null;
                }
                return value;
            }
        });

        Util.addTypeAdapterObjs(AccommodationCategory.class, new JsonDeserializer<AccommodationCategory>() {
            @Override
            public AccommodationCategory deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                AccommodationCategory obj;
                try {
                    if (json instanceof JsonObject){
                        obj = gson.fromJson(json, typeOfT);
                    }
                    else if (json instanceof JsonPrimitive){
                        String acronym = json.getAsString();
                        obj = (AccommodationCategory) BaseRepository.getByName(acronym, AccommodationCategory.class);
                    }
                    else {
                        obj = null;
                    }
                }
                catch (Exception e){
                    return null;
                }
                return obj;
            }
        });

        Util.addTypeAdapterObjs(AccommodationType.class, new JsonDeserializer<AccommodationType>() {
            @Override
            public AccommodationType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                AccommodationType obj;
                try {
                    if (json instanceof JsonObject){
                        obj = gson.fromJson(json, typeOfT);
                    }
                    else if (json instanceof JsonPrimitive){
                        String acronym = json.getAsString();
                        obj = (AccommodationType) BaseRepository.getByName(acronym, AccommodationType.class);
                    }
                    else {
                        obj = null;
                    }
                }
                catch (Exception e){
                    return null;
                }
                return obj;
            }
        });

        Util.addTypeAdapterObjs(RoomType.class, new JsonDeserializer<RoomType>() {
            @Override
            public RoomType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                RoomType obj;
                try {
                    if (json instanceof JsonObject){
                        obj = gson.fromJson(json, typeOfT);
                    }
                    else if (json instanceof JsonPrimitive){
                        String acronym = json.getAsString();
                        obj = (RoomType) BaseRepository.getByName(acronym, RoomType.class);
                    }
                    else {
                        obj = null;
                    }
                }
                catch (Exception e){
                    return null;
                }
                return obj;
            }
        });

        Util.addTypeAdapterObjs(Bathroom.class, new JsonDeserializer<Bathroom>() {
            @Override
            public Bathroom deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                Bathroom obj;
                try {
                    if (json instanceof JsonObject){
                        obj = gson.fromJson(json, typeOfT);
                    }
                    else if (json instanceof JsonPrimitive){
                        String acronym = json.getAsString();
                        obj = (Bathroom) BaseRepository.getByName(acronym, Bathroom.class);
                    }
                    else {
                        obj = null;
                    }
                }
                catch (Exception e){
                    return null;
                }
                return obj;
            }
        });

        Util.addTypeAdapterObjs(RoomClima.class, new JsonDeserializer<RoomClima>() {
            @Override
            public RoomClima deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                RoomClima obj;
                try {
                    if (json instanceof JsonObject){
                        obj = gson.fromJson(json, typeOfT);
                    }
                    else if (json instanceof JsonPrimitive){
                        String acronym = json.getAsString();
                        obj = (RoomClima) BaseRepository.getByName(acronym, RoomClima.class);
                    }
                    else {
                        obj = null;
                    }
                }
                catch (Exception e){
                    return null;
                }
                return obj;
            }
        });

        Util.addTypeAdapterObjs(RoomAudiovisual.class, new JsonDeserializer<RoomAudiovisual>() {
            @Override
            public RoomAudiovisual deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                RoomAudiovisual obj;
                try {
                    if (json instanceof JsonObject){
                        obj = gson.fromJson(json, typeOfT);
                    }
                    else if (json instanceof JsonPrimitive){
                        String acronym = json.getAsString();
                        obj = (RoomAudiovisual) BaseRepository.getByName(acronym, RoomAudiovisual.class);
                    }
                    else {
                        obj = null;
                    }
                }
                catch (Exception e){
                    return null;
                }
                return obj;
            }
        });

        Util.addTypeAdapterObjs(ReservationStatus.class, new JsonDeserializer<ReservationStatus>() {
            @Override
            public ReservationStatus deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                ReservationStatus obj;
                try {
                    if (json instanceof JsonObject){
                        obj = gson.fromJson(json, typeOfT);
                    }
                    else if (json instanceof JsonPrimitive){
                        long idRef = json.getAsLong();
                        obj = (ReservationStatus) BaseRepository.getByIdRef(idRef, ReservationStatus.class);
                    }
                    else {
                        obj = null;
                    }
                }
                catch (Exception e){
                    return null;
                }
                return obj;
            }
        });
    }

    public static Picasso.Builder getPicassoBuilder(Context context){
        if(builder == null){
            builder = new Picasso.Builder(context);
            builder.listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    exception.printStackTrace();
                }
            });
        }

        return builder;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
