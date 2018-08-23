package hds.aplications.com.mycp.view.activities;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hds.aplications.com.mycp.R;
import hds.aplications.com.mycp.helpers.Request;
import hds.aplications.com.mycp.models.Answer;
import hds.aplications.com.mycp.models.Reservation;
import hds.aplications.com.mycp.models.ReservationDetail;
import hds.aplications.com.mycp.services.ReservationClient;
import hds.aplications.com.mycp.view.fragments.FragmentPayment;
import hds.aplications.com.mycp.view.fragments.FragmentPaymentRoom;
import hds.aplications.com.mycp.view.others.OnScrollViewChangedListener;
import hds.aplications.com.mycp.view.others.ViewUtils;
import mgleon.common.com.LogUtils;
import mgleon.common.com.MessageToast;
import mgleon.common.com.services.Util.ProcessRetrofitError;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActivityPaymentAllReservations extends ActivityBase{

    private static final String TAG = LogUtils.makeLogTag(ActivityHome.class);
    private FragmentPaymentRoom.ListenerPaymentRoom listenerPaymentRoom;
    private FragmentPayment fragmentPayment;
    private FragmentPayment.ListenerFragmentPayment listenerFragmentPayment;
    private List<ReservationDetail> reservationsDetail;
    private List<Reservation> reservations;
    private String amount = "0";

    @Override
    protected void initialize(){
        super.initialize();

        this.activityTitle = getString(R.string.reservation_details);

        textViewBarTitle.setCompoundDrawables(null, null, null, null);

        View viewItem = View.inflate(ActivityPaymentAllReservations.this, R.layout.content_payment_all_reservations, null);
        this.addView(viewItem);

        fabMenu.setVisibility(View.GONE);
        ScrollView scrollView = (ScrollView) findViewById(R.id.scroll_view_book);
        OnScrollViewChangedListener onScrollViewChangedListener = new OnScrollViewChangedListener(scrollView){
            @Override
            public void onHide(){
                //hideFabBottom();
            }

            @Override
            public void onShow(){
                //showFabBottom();
            }
        };
        scrollView.getViewTreeObserver().addOnScrollChangedListener(onScrollViewChangedListener);

        listenerPaymentRoom = new FragmentPaymentRoom.ListenerPaymentRoom(){
            @Override
            public void change(){
                updatePrices();
            }
        };

        loadingMask.show(getString(R.string.loading_reservations));
        ReservationClient reservationClient = new ReservationClient(context);
        Callback<List<Reservation>> callback = new Callback<List<Reservation>>(){
            @Override
            public void success(List<Reservation> xreservations, Response response){
                loadingMask.hide();

                reservations = xreservations;

                reservationsDetail = new ArrayList<>();
                for(Reservation reservation : xreservations){
                    List<ReservationDetail> reservationDetails = reservation.getReservationDetails();
                    for(ReservationDetail reservationDetail : reservationDetails){
                        reservationDetail.getRoom().setAccommodation(reservation.getAccommodation());
                        reservationsDetail.add(reservationDetail);
                    }
                }
                createViewReservationDetail();
                updatePrices();
            }

            @Override
            public void failure(RetrofitError error){
                loadingMask.hide();
            }
        };

        reservationClient.getReservations(callback);


        /*createViewReservationDetail();
        updatePrices();*/
    }
    @Override
    protected void onResume(){
        super.onResume();
        sTracker.setScreenName("Pay All Reservations");
        sTracker.send(new HitBuilders.ScreenViewBuilder().build());
        menuOptions.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.vector_arrow_prev));

    }
    @Override
    protected void bindActions(){
        super.bindActions();

        findViewById(R.id.view_details).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showDetailPayment();
            }
        });

        findViewById(R.id.btn_payment).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                payment();
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

    @Override
    protected void onMenuOptions(){
        //super.onMenuOptions();
        finish();
    }

    private void showDetailPayment(){
        findViewById(R.id.frame_detail).setVisibility(View.VISIBLE);
    }

    private void hideDetailPayment(){
        findViewById(R.id.frame_detail).setVisibility(View.GONE);
    }

    private void createViewReservationDetail(){
        for(ReservationDetail reservationDetail : reservationsDetail){
            reservationDetail.setActive(true);
            FragmentPaymentRoom fragmentPaymentRoom = new FragmentPaymentRoom();
            fragmentPaymentRoom.setReservationDetail(reservationDetail);
            fragmentPaymentRoom.setListenerBook(listenerPaymentRoom);

            getSupportFragmentManager().beginTransaction().add(R.id.content_book_room, fragmentPaymentRoom).commit();
        }
    }

    private void updatePrices(){
        //MessageToast.showInfo(context, "Actualizar totales");

        String currency = "EUR";
        if(user != null){
            currency = user.getCurrency().getCode();
        }

        float totalPrice = 0;
        float onlinePrepayment = 0;
        float payInAccommodation = 0;
        float lodgingPrice = 0;
        float tourist_service = 0;
        float fixedFee = 0;
        float payInAccommodationCuc = 0;

        for(Reservation reservation : reservations){
            Map<String, Float> prices = ViewUtils.getPrices(user, reservation.getReservationDetails(), context);

            totalPrice += prices.get("totalPrice");
            onlinePrepayment += prices.get("onlinePrepayment");
            payInAccommodation += prices.get("payInAccommodation");
            lodgingPrice += prices.get("lodgingPrice");
            tourist_service += prices.get("tourist_service");
            fixedFee += prices.get("fixedFee");
            payInAccommodationCuc += prices.get("payInAccommodationCuc");
        }
        /**/

        amount = ViewUtils.getRoundedPrice(onlinePrepayment);

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

    private void payment(){
        CheckBox checkBox = findViewById(R.id.term_condi);
        if (!checkBox.isChecked()){
            MessageToast.showInfo(context, getString(R.string.acep_t_c));
            return;
        }

        String reservations = "";
        String s = "";
        for(ReservationDetail reservationDetail : reservationsDetail){
            if (reservationDetail.isActive()){
                reservations += s + reservationDetail.getIdRef();
            }
        }

        loadingMask.show(getString(R.string.gen_booking));
        Callback<Answer> callback = new Callback<Answer>(){
            @Override
            public void success(Answer answer, Response response){
                if(answer.getUrl() == null || answer.getUrl().isEmpty()){
                    MessageToast.showError(context, answer.getMsg());
                    return;
                }
                sTracker.send(new HitBuilders.EventBuilder().setCategory("Pay Acction").setAction("Click").build());
                paymentInSkrill(answer.getUrl());
            }

            @Override
            public void failure(RetrofitError error){
                loadingMask.hide();
                ProcessRetrofitError.showError(error, context);
            }
        };
        ReservationClient reservationClient = new ReservationClient(context);
        reservationClient.payReservation(reservations, amount,callback);
    }

    private void paymentInSkrill(String url){
        loadingMask.show(getString(R.string.load_skrill));

        if(listenerFragmentPayment == null){
            listenerFragmentPayment = new FragmentPayment.ListenerFragmentPayment(){
                @Override
                public void endPostUrl(){
                    //
                }

                @Override
                public void close(){
                    if(fragmentPayment != null){
                        getSupportFragmentManager().beginTransaction().remove(fragmentPayment).commit();
                        fragmentPayment = null;
                    }
                }
            };
        }

        fragmentPayment = new FragmentPayment();
        fragmentPayment.setUrl(url);
        fragmentPayment.setLoadingMask(loadingMask);
        fragmentPayment.setListenerFragmentPayment(listenerFragmentPayment);

        /*RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.content_all_payment);
        relativeLayout.removeAllViews();*/

        getSupportFragmentManager().beginTransaction().replace(R.id.content_all_payment, fragmentPayment).commit();
    }

}