package hds.aplications.com.mycp.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ScrollView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;

import hds.aplications.com.mycp.R;
import hds.aplications.com.mycp.helpers.Request;
import hds.aplications.com.mycp.helpers.SAppData;
import hds.aplications.com.mycp.models.Accommodation;
import hds.aplications.com.mycp.models.Answer;
import hds.aplications.com.mycp.models.ReservationDetail;
import hds.aplications.com.mycp.models.ServiceFee;
import hds.aplications.com.mycp.models.User;
import hds.aplications.com.mycp.services.ReservationClient;
import hds.aplications.com.mycp.view.fragments.FragmentPayment;
import hds.aplications.com.mycp.view.others.OnScrollViewChangedListener;
import hds.aplications.com.mycp.view.fragments.FragmentBookRoom;
import hds.aplications.com.mycp.view.fragments.FragmentPaymentRoom;
import hds.aplications.com.mycp.view.others.ViewUtils;
import mgleon.common.com.LogUtils;
import mgleon.common.com.MessageToast;
import mgleon.common.com.services.Util.ProcessRetrofitError;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActivityPaymentReservations extends ActivityBase{

    private static final String TAG = LogUtils.makeLogTag(ActivityHome.class);
    private FragmentPaymentRoom.ListenerPaymentRoom listenerPaymentRoom;
    private FragmentPayment fragmentPayment;
    private FragmentPayment.ListenerFragmentPayment listenerFragmentPayment;
    private List<ReservationDetail> reservationsDetail;
    private String amount = "0";

    @Override
    protected void initialize(){
        super.initialize();

        this.activityTitle = getString(R.string.reservation_details);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.containsKey("code_own")){
            String code = bundle.getString("code_own");
            //this.activityTitle += accommodation.getCode();
            reservationsDetail = SAppData.getInstance().reservation.getReservationDetails();
            SAppData.getInstance().reservation = null;
        }
        textViewBarTitle.setCompoundDrawables(null, null, null, null);

        View viewItem = View.inflate(ActivityPaymentReservations.this, R.layout.content_payment_reservations, null);
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
        createViewReservationDetail();
        updatePrices();
    }
    @Override
    protected void onResume(){
        super.onResume();
        sTracker.setScreenName("Pay Reservations");
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

        float lodgingPrice = 0;
        int roomsTotal = 0;
        for(ReservationDetail reservationDetail : reservationsDetail){
            if (reservationDetail.isActive()){
                float t = reservationDetail.getTotalPrice(this);
                lodgingPrice += t;
                if(t > 0){
                    roomsTotal++;
                }
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
        Accommodation accommodation = reservationsDetail.get(0).getRoom().getAccommodation();
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
            MessageToast.showInfo(context, "Accept the terms and conditions");
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
                sTracker.send(new HitBuilders.EventBuilder().setCategory("Pay Acction")
                .setAction("Click").build());
                paymentInSkrill(answer.getUrl());
            }

            @Override
            public void failure(RetrofitError error){
                loadingMask.hide();
                ProcessRetrofitError.showError(error, context);
            }
        };
        ReservationClient reservationClient = new ReservationClient(context);
        reservationClient.payReservation(reservations, amount, callback);
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
    private String hashMapToUrl(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}