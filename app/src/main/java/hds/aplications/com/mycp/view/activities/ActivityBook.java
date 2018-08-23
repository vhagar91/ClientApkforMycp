package hds.aplications.com.mycp.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hds.aplications.com.mycp.MyCP;
import hds.aplications.com.mycp.R;
import hds.aplications.com.mycp.helpers.SAppData;
import hds.aplications.com.mycp.models.Accommodation;
import hds.aplications.com.mycp.models.Answer;
import hds.aplications.com.mycp.models.Reservation;
import hds.aplications.com.mycp.models.ReservationDetail;
import hds.aplications.com.mycp.models.Room;
import hds.aplications.com.mycp.models.ServiceFee;
import hds.aplications.com.mycp.services.AccommodationClient;
import hds.aplications.com.mycp.services.ReservationClient;
import hds.aplications.com.mycp.view.components.DateComponent;
import hds.aplications.com.mycp.view.others.OnScrollViewChangedListener;
import hds.aplications.com.mycp.view.fragments.FragmentBookRoom;
import hds.aplications.com.mycp.view.others.ViewUtils;
import mgleon.common.com.DateUtils;
import mgleon.common.com.MessageToast;
import mgleon.common.com.services.Util.ProcessRetrofitError;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActivityBook extends ActivityBase{

    public static final String TAG = "tac_activity_book";

    LinearLayout linearLayout;
    //private List<Room> rooms;
    private Reservation reservation;
    private Accommodation accommodation;
    String last;
    private FragmentBookRoom.ListenerBook listenerBook;
    boolean loading = false;
    List<ReservationDetail> reservationsDetail;
    private DateComponent dateComponentCheckIn;
    private DateComponent dateComponentCheckOut;

    @Override
    protected void initialize() {
        super.initialize();

        this.activityTitle = "BOOKING ";
        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.containsKey("code_own") && bundle.containsKey("id_own")){
            String code = bundle.getString("code_own");
            accommodation = new Accommodation("Casa " + code, code, false);//buscar la casa q es.
            accommodation.setIdRef(bundle.getLong("id_own"));
            this.activityTitle += accommodation.getCode();
            reservation = new Reservation();
        }
        textViewBarTitle.setCompoundDrawables(null, null, null, null);

        View viewItem = View.inflate(this, R.layout.content_book, null);
        this.addView(viewItem);

        ScrollView scrollView = (ScrollView) findViewById(R.id.scroll_view_book);
        OnScrollViewChangedListener onScrollViewChangedListener = new OnScrollViewChangedListener(scrollView){
            @Override
            public void onHide(){
                hideFabBottom();
            }

            @Override
            public void onShow(){
                showFabBottom();
            }
        };
        scrollView.getViewTreeObserver().addOnScrollChangedListener(onScrollViewChangedListener);

        listenerBook = new FragmentBookRoom.ListenerBook(){
            @Override
            public void change(){
                updatePrices();
            }
        };

        if(bundle.containsKey("inmediate_booking")){
            ((Button)findViewById(R.id.book_now)).setText(getString(R.string.payy));
        }
    }

    @Override
    protected void onResume(){
    super.onResume();
    sTracker.setScreenName("Booking");
    sTracker.send(new HitBuilders.ScreenViewBuilder().build());
    menuOptions.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.vector_arrow_prev));
    }
    @Override
    protected void onMenuOptions(){
        //super.onMenuOptions();
        finish();
    }
    @Override
    protected void bindActions(){
        super.bindActions();
        linearLayout = (LinearLayout) findViewById(R.id.linear_layout_search);

        dateComponentCheckIn = new DateComponent((EditText) findViewById(R.id.check_in), this, null);
        dateComponentCheckOut = new DateComponent((EditText) findViewById(R.id.check_out), this, null);
        dateComponentCheckOut.setListener(new DateComponent.Listener() {
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
                findRooms(dateComponentCheckIn, dateComponentCheckOut);
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
                findRooms(dateComponentCheckIn, dateComponentCheckOut);
            }
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.containsKey("check_in") && bundle.containsKey("check_out")){
            String checkIn = bundle.getString("check_in");
            String checkOut = bundle.getString("check_out");

            dateComponentCheckIn.setDate(DateUtils.generateDayFromString(checkIn));
            dateComponentCheckOut.setDate(DateUtils.generateDayFromString(checkOut));
        }
        else {
            Calendar now = Calendar.getInstance();
            dateComponentCheckIn.setDate(now.getTime());
            now.add(Calendar.DATE, 2);
            dateComponentCheckOut.setDate(now.getTime());
        }

        ViewUtils.replaceBackgroundbyImage(context, linearLayout, R.id.image_to_remplace, R.drawable.image_habana_2);

        findViewById(R.id.book_now).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                bookNow();
            }
        });

        findViewById(R.id.view_details).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showDetailPayment();
            }
        });

        findViewById(R.id.btn_closex).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                hideDetailPayment();
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void findRooms(DateComponent dateComponentCheckIn, DateComponent dateComponentCheckOut){
        if(loading){
            return;
        }

        final Calendar checkInCalendar = DateUtils.getCalendar(dateComponentCheckIn.getDate());
        final Calendar checkOutCalendar = DateUtils.getCalendar(dateComponentCheckOut.getDate());

        if (checkInCalendar != null && checkOutCalendar != null && checkOutCalendar.after(checkInCalendar)) {
            /*****************************************************/
            Bundle bundle = getIntent().getExtras();
            if(bundle == null){
                bundle = new Bundle();
            }
            bundle.putString("check_in", DateUtils.generateStringDate(dateComponentCheckIn.getDate()));
            bundle.putString("check_out", DateUtils.generateStringDate(dateComponentCheckOut.getDate()));
            getIntent().putExtras(bundle);
            /*********************/

            final String s = DateUtils.generateStringDate(checkInCalendar.getTime()) + " to " + DateUtils.generateStringDate(checkOutCalendar.getTime());
            if(last == null || !s.equals(last)){
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.content_book_room);
                linearLayout.removeAllViews();

                String from = DateUtils.generateStringDate(dateComponentCheckIn.getDate());
                String to = DateUtils.generateStringDate(dateComponentCheckOut.getDate());
                loadingMask.show("Consultando disponibilidad de la casa");

                AccommodationClient accommodationClient = new AccommodationClient(context);
                loading = true;
                Callback<Accommodation> callback = new Callback<Accommodation>(){
                    @Override
                    public void success(Accommodation accommodationx, Response response){
                        accommodation = accommodationx;
                        List<Room> rooms = accommodation.getRooms();
                        if(rooms != null && !rooms.isEmpty()){
                            updateListReservationDetail(checkInCalendar.getTime(), checkOutCalendar.getTime(), rooms);
                            last = s;
                        }
                        loading = false;
                        loadingMask.hide();
                    }

                    @Override
                    public void failure(RetrofitError error){
                        loadingMask.hide();
                        ProcessRetrofitError.showError(error, context);
                        loading = false;
                    }
                };
                accommodationClient.getAccommodationRooms(String.valueOf(accommodation.getIdRef()), from, to, callback);
            }
        }
    }

    private void bookNow(){
        String from = DateUtils.generateStringDate(dateComponentCheckIn.getDate());
        String to = DateUtils.generateStringDate(dateComponentCheckOut.getDate());
        String checkDispo = accommodation.getInmediateBooking() ? "2" : "1";
        String hasCompleteReservation = accommodation.getModalityComplete() ? "1" : "0";

        String idsRooms = "";
        String adults = "";
        String kids = "";

        String s = "";

        List<ReservationDetail> reservationDetails = reservation.getReservationDetails();
        final List<ReservationDetail> lastReservationDetails = new ArrayList<>();
        for(ReservationDetail reservationDetail : reservationDetails){
            if(reservationDetail != null && (reservationDetail.getAdultsTotal() > 0 || reservationDetail.getKidsTotal() > 0)){
                String idRoom = String.valueOf(reservationDetail.getRoom().getIdRef());
                idsRooms += s + idRoom;

                String adult = String.valueOf(reservationDetail.getAdultsTotal());
                adults += s + adult;

                String kid = String.valueOf(reservationDetail.getKidsTotal());
                kids += s + kid;

                s = "&";
                lastReservationDetails.add(reservationDetail);
            }
        }

        if(idsRooms.isEmpty()){
            MessageToast.showInfo(context, getString(R.string.no_res_hab));
            return;
        }

        Callback<Answer> callback = new Callback<Answer>(){
            @Override
            public void success(Answer answer, Response response){
                List<ReservationDetail> reservationDetailsAux = new ArrayList<>();
                if(answer.getReservationDetails() == null || answer.getReservationDetails().isEmpty()){
                    MessageToast.showError(context, answer.getMsg());
                    return;
                }
                for(ReservationDetail reservationDetail : answer.getReservationDetails()){
                    ReservationDetail reservationDetailx = getReservation(lastReservationDetails, reservationDetail.getRoom().getIdRef());
                    if(reservationDetailx != null){
                        reservationDetailx.setCas(reservationDetail.getCas());
                        reservationDetailsAux.add(reservationDetailx);
                    }
                }


                if(reservationDetailsAux.size() <= 0){
                    MessageToast.showError(context, answer.getMsg());
                    return;
                }
                reservation.setReservationDetails(reservationDetailsAux);

                SAppData.getInstance().reservation = reservation;
                Intent intent = new Intent(context, ActivityPaymentReservations.class);
                Bundle bundle = new Bundle();
                bundle.putString("code_own", accommodation.getCode());
                intent.putExtras(bundle);
                startActivity(intent);

                finish();
            }

            @Override
            public void failure(RetrofitError error){
                loadingMask.hide();
                ProcessRetrofitError.showError(error, context);
            }
        };
        ReservationClient reservationClient = new ReservationClient(context);
        reservationClient.addReservation(checkDispo, from, to, idsRooms, adults, kids, hasCompleteReservation, callback);
    }

    private ReservationDetail getReservation(List<ReservationDetail> reservationDetails, long id){
        for(ReservationDetail reservationDetail : reservationDetails){
            if(reservationDetail.getRoom().getIdRef() == id){
                return reservationDetail;
            }
        }
        return null;
    }

    /*@Override
    protected void refreshViewByChangeUser(){
        super.refreshViewByChangeUser();
        findRooms(dateComponentCheckIn, dateComponentCheckOut);
    }*/

    private void updateListReservationDetail(Date from, Date to, List<Room> rooms){
        reservation.setDateFrom(from);
        reservation.setDateTo(to);
        List<ReservationDetail> reservationDetails = new ArrayList<>();

        this.reservationsDetail = new ArrayList<>();
        for(Room room : rooms){
            room.setAccommodation(accommodation);
            ReservationDetail reservationDetail = new ReservationDetail(from, to, room);
            reservationDetails.add(reservationDetail);
            reservationDetail.setPricesByNights(room.getPricesByNights());

            FragmentBookRoom fragmentBookRoom = new FragmentBookRoom();
            fragmentBookRoom.setReservationDetail(reservationDetail);
            fragmentBookRoom.setListenerBook(listenerBook);
            this.reservationsDetail.add(reservationDetail);
            getSupportFragmentManager().beginTransaction().add(R.id.content_book_room, fragmentBookRoom).commit();
        }
        reservation.setReservationDetails(reservationDetails);
    }

    private void updatePrices(){
        //MessageToast.showInfo(context, "Actualizar totales");

        String currency = "EUR";
        if(user != null){
            currency = user.getCurrency().getCode();
        }

        float lodgingPrice = 0;
        int roomsTotal = 0;
        for(ReservationDetail reservationDetail : reservationsDetail){
            float t = reservationDetail.getTotalPrice(this);
            lodgingPrice += t;
            if(t > 0){
                roomsTotal++;
            }
        }

        if(lodgingPrice == 0){
            findViewById(R.id.layout_details).setVisibility(View.GONE);
            return;
        }
        else {
            findViewById(R.id.layout_details).setVisibility(View.VISIBLE);
        }

        /**/
        ServiceFee serviceFee = accommodation.getServiceFee();
        float fixedFee = serviceFee.getFixedFee()*accommodation.getCucChange();
        float curr = accommodation.getCucChange();
        int nights = accommodation.getNights();
        float avgPrice = lodgingPrice / nights;
        float tourist_fee_percent = 0;

        if(nights == 1)
        {
            if(roomsTotal == 1)
            {
                if(avgPrice < 20*curr )
                    tourist_fee_percent = serviceFee.getOneNrUntil20Percent();
                else if(avgPrice >= 20*curr && avgPrice < 25*curr)
                    tourist_fee_percent = serviceFee.getOneNrFrom20To25Percent();
                else if(avgPrice >= 25*curr)
                    tourist_fee_percent = serviceFee.getOneNrFromMore25Percent();
            }
            else
                tourist_fee_percent = serviceFee.getOneNightSeveralRoomsPercent();
        }
        else if(nights == 2)
            tourist_fee_percent = serviceFee.getOne2NightsPercent();
        else if(nights == 3)
            tourist_fee_percent = serviceFee.getOne3NightsPercent();
        else if(nights == 4)
            tourist_fee_percent = serviceFee.getOne4NightsPercent();
        else if(nights >= 5)
            tourist_fee_percent = serviceFee.getOne5NightsPercent();

        float tourist_service = lodgingPrice*tourist_fee_percent;
        float totalPrice = lodgingPrice + tourist_service + fixedFee;

        float onlinePrepayment = ((accommodation.getCommissionPercent() * lodgingPrice) / 100) + tourist_service + fixedFee;
        float payInAccommodation = totalPrice - onlinePrepayment;
        float payInAccommodationCuc = payInAccommodation / curr;
        /**/

        String tc = currency + " ";
        ((TextView) findViewById(R.id.book_total_price)).setText(tc + ViewUtils.getRoundedPrice(totalPrice));
        ((TextView) findViewById(R.id.book_online_prepayment)).setText(tc + ViewUtils.getRoundedPrice(onlinePrepayment));
        ((TextView) findViewById(R.id.book_pay_in_accommodation)).setText(tc + ViewUtils.getRoundedPrice(payInAccommodation));
        ((TextView) findViewById(R.id.lodging_price)).setText(tc + ViewUtils.getRoundedPrice(lodgingPrice));
        ((TextView) findViewById(R.id.tourist_service)).setText(tc + ViewUtils.getRoundedPrice(tourist_service));
        ((TextView) findViewById(R.id.fixed_fee)).setText(tc + ViewUtils.getRoundedPrice(fixedFee));
        ((TextView) findViewById(R.id.total_price)).setText(tc + ViewUtils.getRoundedPrice(totalPrice));
        ((TextView) findViewById(R.id.online_prepayment)).setText(tc + ViewUtils.getRoundedPrice(onlinePrepayment));
        ((TextView) findViewById(R.id.pay_in_accommodation)).setText(tc + ViewUtils.getRoundedPrice(payInAccommodation));
        ((TextView) findViewById(R.id.pay_in_accommodation_cuc)).setText(" CUC " + ViewUtils.getRoundedPrice(payInAccommodationCuc));
    }

    private void showDetailPayment(){
        findViewById(R.id.frame_detail).setVisibility(View.VISIBLE);
    }

    private void hideDetailPayment(){
        findViewById(R.id.frame_detail).setVisibility(View.GONE);
    }

}