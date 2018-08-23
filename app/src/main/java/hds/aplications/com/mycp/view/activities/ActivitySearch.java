package hds.aplications.com.mycp.view.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;

import java.util.List;

import hds.aplications.com.mycp.R;
import hds.aplications.com.mycp.helpers.SAppData;
import hds.aplications.com.mycp.models.Accommodation;
import hds.aplications.com.mycp.models.Destination;
import hds.aplications.com.mycp.services.AccommodationClient;
import hds.aplications.com.mycp.view.adapters.AdapterAccommodation;
import mgleon.common.com.DateUtils;
import mgleon.common.com.LogUtils;
import mgleon.common.com.MessageToast;
import mgleon.common.com.receivers.ConnectivityReceiver;
import mgleon.common.com.services.Util.ProcessRetrofitError;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActivitySearch extends ActivityHome {
    String destinationId = "";
    String from = "";
    String to = "";
    String guests = "";
    String rooms = "";
    boolean favorites = false;


    private static final String TAG = LogUtils.makeLogTag(ActivitySearch.class);

    @Override
    protected void initialize(){
        super.initialize();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.containsKey("favorites") && bundle.getBoolean("favorites")){
            favorites = true;
            ((TextView)findViewById(R.id.title_list_acomodations)).setText(getString(R.string.title_search_results_favorites));
        }
        else{
            ((TextView)findViewById(R.id.title_list_acomodations)).setText(getString(R.string.title_search_results));
        }
    }

    @Override
    protected void initFirstDate(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.containsKey("check_in") && bundle.containsKey("check_out")){
            String checkIn = bundle.getString("check_in");
            String checkOut = bundle.getString("check_out");

            dateComponentCheckIn.setDate(DateUtils.generateDayFromString(checkIn));
            dateComponentCheckOut.setDate(DateUtils.generateDayFromString(checkOut));
        }

        if(bundle != null && bundle.containsKey("no_guests")){
            int noGuests = bundle.getInt("no_guests");
            ((EditText)findViewById(R.id.no_guests)).setText(String.valueOf(noGuests));
        }

        if(bundle != null && bundle.containsKey("no_rooms")){
            int noRooms = bundle.getInt("no_rooms");
            ((EditText)findViewById(R.id.no_rooms)).setText(String.valueOf(noRooms));
        }
    }

    @Override
    protected void searchDestinations(){
        if(SAppData.getInstance().destinations == null){
            super.searchDestinations();
            return;
        }

        Integer index = null;

        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.containsKey("check_in")){
            index = bundle.getInt("index_destination");
        }

        initSpinnerDestinations(SAppData.getInstance().destinations, index);
        SAppData.getInstance().destinations = null;
    }

    private void captureValues(){
        if(spinnerDestinations != null && spinnerDestinations.getSelectedItem() != null && ((Destination)spinnerDestinations.getSelectedItem()).getIdRef() != null){
            destinationId = String.valueOf(((Destination)spinnerDestinations.getSelectedItem()).getIdRef());
        }
        else {
            destinationId = "";
        }

        from = DateUtils.generateStringDate(dateComponentCheckIn.getDate());
        to = DateUtils.generateStringDate(dateComponentCheckOut.getDate());

        guests = ((EditText)findViewById(R.id.no_guests)).getText().toString();
        rooms = ((EditText)findViewById(R.id.no_rooms)).getText().toString();
    }

    @Override
    protected void searchAccommodations(final boolean mask, final ActivityHome.ListenerSearch listenerSearch){
        if(ConnectivityReceiver.isOnline(context)){
            captureValues();
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
            accommodationClient.getAccommodations(String.valueOf(beginPage), destinationId, guests, rooms, from, to, favorites ? "1" : "0", callback);
        }
        else {
            MessageToast.showError(context, context.getString(R.string.no_internet_connection));
        }
    }

    @Override
    protected void onBtnSearch(){
        beginPage = 0;
        searchAccommodations(true, new ListenerSearch(){
            @Override
            public void success(List<Accommodation> accommodations){
                AdapterAccommodation adapterAccommodatio = new AdapterAccommodation(accommodations, dateComponentCheckIn.getDate(), dateComponentCheckOut.getDate());
                recyclerView.setAdapter(adapterAccommodatio);
            }
        });

    }
}