package hds.aplications.com.mycp.view.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hds.aplications.com.mycp.R;
import hds.aplications.com.mycp.helpers.SAppData;
import hds.aplications.com.mycp.models.Accommodation;
import hds.aplications.com.mycp.models.Destination;
import hds.aplications.com.mycp.services.AccommodationClient;
import hds.aplications.com.mycp.services.DestinationClient;
import hds.aplications.com.mycp.view.adapters.AdapterAccommodation;
import hds.aplications.com.mycp.view.components.DateComponent;
import hds.aplications.com.mycp.view.others.DirectionScroll;
import hds.aplications.com.mycp.view.others.ViewUtils;
import mgleon.common.com.DateUtils;
import mgleon.common.com.LogUtils;
import mgleon.common.com.MessageToast;
import mgleon.common.com.receivers.ConnectivityReceiver;
import mgleon.common.com.services.Util.ProcessRetrofitError;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActivityHome extends ActivityBase {

    private static final String TAG = LogUtils.makeLogTag(ActivityHome.class);

    LinearLayout linearLayout;

    private boolean locked = false;
    protected boolean loading = false;
    protected Integer beginPage = 0;
    protected RecyclerView recyclerView;

    int indexSelectedDest = -1;
    private List<Destination> destinations;
    protected DateComponent dateComponentCheckIn;
    protected DateComponent dateComponentCheckOut;
    protected Spinner spinnerDestinations;

    @Override
    protected void initialize() {
        super.initialize();

        createShortcutIcon();

        //this.activityTitle = getResources().getString(R.string.app_name);

        View viewItem = View.inflate(ActivityHome.this, R.layout.content_home, null);
        this.addView(viewItem);
    }

    @Override
    protected void bindActions(){
        super.bindActions();
        linearLayout = (LinearLayout) findViewById(R.id.linear_layout_search);

        dateComponentCheckIn = new DateComponent((EditText) findViewById(R.id.check_in), this, null);
        dateComponentCheckOut = new DateComponent((EditText) findViewById(R.id.check_out), this, new DateComponent.Listener() {
            @Override
            public void dateAccept(Date date) {
                Calendar checkInCalendar = DateUtils.getCalendar(dateComponentCheckIn.getDate());
                Calendar checkOutCalendar = DateUtils.getCalendar(date);
                if (checkInCalendar == null || checkOutCalendar.before(checkInCalendar) || checkOutCalendar.equals(checkInCalendar)) {
                    checkOutCalendar.add(Calendar.DATE, -2);
                    dateComponentCheckIn.setDate(checkOutCalendar.getTime());
                }
            }

            @Override
            public void setDate(){
                saveDatesInIntent();
            }
        });
        dateComponentCheckIn.setListener(new DateComponent.Listener() {
            @Override
            public void dateAccept(Date date) {
                Calendar checkInCalendar = DateUtils.getCalendar(date);
                Calendar checkOutCalendar = DateUtils.getCalendar(dateComponentCheckOut.getDate());
                if (checkOutCalendar == null || checkInCalendar.after(checkOutCalendar) || checkInCalendar.equals(checkOutCalendar)) {
                    checkInCalendar.add(Calendar.DATE, 2);
                    dateComponentCheckOut.setDate(checkInCalendar.getTime());
                }
            }

            @Override
            public void setDate(){
                saveDatesInIntent();
            }
        });

        ViewUtils.replaceBackgroundbyImage(context, linearLayout, R.id.image_to_remplace, R.drawable.image_habana);

        final Integer[] offset = {0};
        final DirectionScroll[] directionScroll = {DirectionScroll.down};
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_search);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (offset[0] < verticalOffset && directionScroll[0] != DirectionScroll.down) {
                    directionScroll[0] = DirectionScroll.down;
                    showFabBottom();
                }
                if (offset[0] > verticalOffset && directionScroll[0] != DirectionScroll.up) {
                    directionScroll[0] = DirectionScroll.up;
                    hideFabBottom();
                }

                offset[0] = verticalOffset;
            }
        });

        findViewById(R.id.btn_search).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBtnSearch();
            }
        });

        initFirstDate();

        searchDestinations();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        searchAccommodations(true, new ListenerSearch(){
            @Override
            public void success(List<Accommodation> accommodations){
                initRecyclerView(accommodations);
            }
        });
    }

    private void saveDatesInIntent(){
        final Calendar checkInCalendar = DateUtils.getCalendar(dateComponentCheckIn.getDate());
        final Calendar checkOutCalendar = DateUtils.getCalendar(dateComponentCheckOut.getDate());
        if (checkInCalendar != null && checkOutCalendar != null && checkOutCalendar.after(checkInCalendar)) {
            Bundle bundle = getIntent().getExtras();
            if(bundle == null){
                bundle = new Bundle();
            }
            bundle.putString("check_in", DateUtils.generateStringDate(dateComponentCheckIn.getDate()));
            bundle.putString("check_out", DateUtils.generateStringDate(dateComponentCheckOut.getDate()));
            getIntent().putExtras(bundle);
        }
    }

    protected void onBtnSearch(){
        Intent intent = new Intent(context, ActivitySearch.class);
        Bundle bundle = new Bundle();
        addToBundle(bundle);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void addToBundle(Bundle bundle){
        bundle.putString("check_in", DateUtils.generateStringDate(dateComponentCheckIn.getDate()));
        bundle.putString("check_out", DateUtils.generateStringDate(dateComponentCheckOut.getDate()));
        if(indexSelectedDest != -1){
            bundle.putInt("index_destination", indexSelectedDest);
        }
        SAppData.getInstance().destinations = destinations;

        String sNoGuests = ((EditText)findViewById(R.id.no_guests)).getText().toString();
        String sNoRooms = ((EditText)findViewById(R.id.no_rooms)).getText().toString();

        if(sNoGuests != null && !sNoGuests.isEmpty()){
            bundle.putInt("no_guests", Integer.parseInt(sNoGuests));
        }
        if(sNoRooms != null && !sNoRooms.isEmpty()){
            bundle.putInt("no_rooms", Integer.parseInt(sNoRooms));
        }
    }

    @Override
    protected void addToFavoriteBundle(Bundle bundle){
        addToBundle(bundle);
    }

    private void initRecyclerView(List<Accommodation> accommodations) {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new AdapterAccommodation(accommodations, dateComponentCheckIn.getDate(), dateComponentCheckOut.getDate()));

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(final RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = recyclerView.getAdapter().getItemCount();
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if(!loading && totalItemCount <= (lastVisibleItem + 3)){
                    /*if(onLoadMoreListener != null){
                        onLoadMoreListener.onLoadMore();
                    }*/

                    searchAccommodations(false, new ListenerSearch(){
                        @Override
                        public void success(List<Accommodation> accommodations){
                            AdapterAccommodation adapterAccommodation1 = (AdapterAccommodation)recyclerView.getAdapter();
                            Integer s = adapterAccommodation1.getmItemList().size();
                            adapterAccommodation1.getmItemList().addAll(accommodations);
                            adapterAccommodation1.notifyItemRangeInserted(s, accommodations.size() - 1);
                        }
                    });
                    //MessageToast.showInfo(context, "Cargar mas, ultimo visto" + String.valueOf(lastVisibleItem));
                }
            }
        });

    }

    protected void initFirstDate(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.containsKey("check_in") && bundle.containsKey("check_out")){
            String checkIn = bundle.getString("check_in");
            String checkOut = bundle.getString("check_out");

            dateComponentCheckIn.setDate(DateUtils.generateDayFromString(checkIn));
            dateComponentCheckOut.setDate(DateUtils.generateDayFromString(checkOut));
            return;
        }

        Calendar now = Calendar.getInstance();
        dateComponentCheckIn.setDate(DateUtils.beginningDay(now.getTime()));
        now.add(Calendar.DATE, 2);
        dateComponentCheckOut.setDate(DateUtils.beginningDay(now.getTime()));
    }

    protected void searchDestinations(){
        if(ConnectivityReceiver.isOnline(context)){
            DestinationClient destinationClient = new DestinationClient(context);
            Callback<List<Destination>> callback = new Callback<List<Destination>>(){
                @Override
                public void success(List<Destination> xdestinations, Response response){
                    initSpinnerDestinations(xdestinations, null);
                }

                @Override
                public void failure(RetrofitError error){
                    ProcessRetrofitError.showError(error, context);
                }
            };
            destinationClient.getDestinations(callback);
        }
        else {
            MessageToast.showError(context, context.getString(R.string.no_internet_connection));
        }
    }

    protected void initSpinnerDestinations(List<Destination> xdestinations, Integer destinationSelected){
        destinations = new ArrayList<>();
        destinations.add(new Destination("[None]"));
        for(Destination destination : xdestinations){
            destinations.add(destination);
        }

        spinnerDestinations = (Spinner) findViewById(R.id.spinner_search_destination);
        ArrayAdapter<Destination> adaptador = new ArrayAdapter<Destination>(context, R.layout.simple_spinner_item_y, destinations){
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                TextView textView = (TextView)v.findViewById(android.R.id.text1);
                if (position == 0) {
                    textView.setTextColor(ContextCompat.getColor(context, R.color.hint_loging));
                    textView.setText(R.string.search_destination);
                }
                else {
                    textView.setTextColor(ContextCompat.getColor(context, android.R.color.white));
                }
                return v;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                Destination destinationSelected = (Destination) spinnerDestinations.getSelectedItem();

                View v = super.getDropDownView(position, convertView, parent);
                if (position == 0) {
                    ((CheckedTextView)v.findViewById(android.R.id.text1)).setTextColor(ContextCompat.getColor(context, R.color.hint_loging));
                }
                else if(destinationSelected != null && position == this.getPosition(destinationSelected)){
                    ((CheckedTextView)v.findViewById(android.R.id.text1)).setTextColor(ContextCompat.getColor(context, R.color.bar_background));
                }
                else {
                    ((CheckedTextView)v.findViewById(android.R.id.text1)).setTextColor(ContextCompat.getColor(context, android.R.color.black));
                }
                return v;
            }

            /*@Override
            public int getCount() {
                return super.getCount()-1; // you dont display last item. It is used as hint.
            }*/

        };
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDestinations.setAdapter(adaptador);
        spinnerDestinations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id){
                            /*String val = ((String) parent.getItemAtPosition(position));
                            if(!val.isEmpty()){
                                //Integer va = Integer.valueOf(val);
                            }*/
                indexSelectedDest = position;
            }

            public void onNothingSelected(AdapterView<?> parent){}
        });

        if(destinationSelected != null){
            spinnerDestinations.setSelection(destinationSelected);
        }

        destinations = xdestinations;
    }

    protected void searchAccommodations(final boolean mask, final ListenerSearch listenerSearch){
        if(beginPage >= 30 || locked){
            return;
        }
        if(ConnectivityReceiver.isOnline(context)){
            loading = true;
            if(mask){
                findViewById(R.id.loading_mask_accommodations).setVisibility(View.VISIBLE);
            }
            AccommodationClient accommodationClient = new AccommodationClient(context);
            Callback<List<Accommodation>> callback = new Callback<List<Accommodation>>(){
                @Override
                public void success(List<Accommodation> accommodations, Response response){
                    if(accommodations != null && !accommodations.isEmpty()){
                        loading = false;
                        if(mask){
                            findViewById(R.id.loading_mask_accommodations).setVisibility(View.GONE);
                        }
                        beginPage += accommodations.size();
                        listenerSearch.success(accommodations);
                    }
                }

                @Override
                public void failure(RetrofitError error){
                    loading = false;
                    if(mask){
                        findViewById(R.id.loading_mask_accommodations).setVisibility(View.GONE);
                    }
                    ProcessRetrofitError.showError(error, context);
                }
            };
            accommodationClient.getAccommodationsTop(String.valueOf(beginPage), callback);
        }
        else {
            MessageToast.showError(context, context.getString(R.string.no_internet_connection));
        }
    }

    @SuppressWarnings("deprecation")
    private void createShortcutIcon(){
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LogUtils.LOGI(TAG, "Creado por xml");
            return;
        }*/

        // Checking if ShortCut was already added
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.PREFS_NAME), MODE_PRIVATE);
        boolean shortCutWasAlreadyAdded = sharedPreferences.getBoolean(getString(R.string.PREF_SHORTCUT_ADDED), false);
        if (shortCutWasAlreadyAdded){
            return;
        }

        Intent shortcutIntent = new Intent(getApplicationContext(), ActivityHome.class);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.mipmap.home));
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(addIntent);

        // Remembering that ShortCut was already added
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(getString(R.string.PREF_SHORTCUT_ADDED), true);
        editor.commit();
    }

    @Override
    protected void onResume(){
        super.onResume();
        sTracker.setScreenName("Home");
        sTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    /*@Override
    protected void refreshViewByChangeUser(){
        super.refreshViewByChangeUser();
        beginPage = 0;

        searchAccommodations(true, new ListenerSearch(){
            @Override
            public void success(List<Accommodation> accommodations){
                AdapterAccommodation adapterAccommodatio = new AdapterAccommodation(accommodations, dateComponentCheckIn.getDate(), dateComponentCheckOut.getDate());
                recyclerView.setAdapter(adapterAccommodatio);
            }
        });
    }*/

    public interface ListenerSearch{
        void success(List<Accommodation> accommodations);
    }
}