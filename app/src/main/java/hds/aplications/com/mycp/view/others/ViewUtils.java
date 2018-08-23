package hds.aplications.com.mycp.view.others;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import hds.aplications.com.mycp.R;
import hds.aplications.com.mycp.models.Accommodation;
import hds.aplications.com.mycp.models.ReservationDetail;
import hds.aplications.com.mycp.models.ServiceFee;
import hds.aplications.com.mycp.models.User;
import hds.aplications.com.mycp.view.activities.ActivityGalery;
import mgleon.common.com.LogUtils;

/**
 * Created by Miguel Gomez Leon.
 * mgleonsc@gmail.com
 */

public class ViewUtils{
    private static final String TAG = LogUtils.makeLogTag(ViewUtils.class);

    public static void replaceBackgroundbyImage(final Context context, final ViewGroup viewGroup, final int toRemplace, final int image){
        viewGroup.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int availableHeight = viewGroup.getMeasuredHeight();
                if (availableHeight > 0) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        //noinspection deprecation
                        viewGroup.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        //LogUtils.LOGI(TAG, "Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN");
                    } else {
                        viewGroup.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }

                    try {
                        int availableWidth = viewGroup.getMeasuredWidth();
                        LayerDrawable background = (LayerDrawable) viewGroup.getBackground();
                        BitmapDrawable d = (BitmapDrawable) ContextCompat.getDrawable(context, image);
                        Bitmap bitmap = d.getBitmap();
                        Drawable bitmapDrawable = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, availableWidth, availableHeight, true));
                        background.setDrawableByLayerId(toRemplace, bitmapDrawable);
                    }
                    catch (Exception e) {
                        LogUtils.LOGE(TAG, "Error in replaceBackgroundbyImage");
                    }
                }
            }
        });
    }

    public static String getRoundedPrice(float price){


        return String.format( Locale.US, "%.2f", price );
    }

    public static Map<String, Float> getPrices(User user, List<ReservationDetail> reservationsDetail, Context context){
        Map<String, Float> prices = new HashMap<>();

        /**************************/
        String currency = "EUR";
        if(user != null){
            currency = user.getCurrency().getCode();
        }

        float lodgingPrice = 0;
        int roomsTotal = 0;
        for(ReservationDetail reservationDetail : reservationsDetail){
            if (reservationDetail.isActive()){
                float t = reservationDetail.getTotalPrice(context);
                lodgingPrice += t;
                if(t > 0){
                    roomsTotal++;
                }
            }
        }

        if(lodgingPrice == 0){
            prices.put("totalPrice", 0f);
            prices.put("payInAccommodation", 0f);
            prices.put("lodgingPrice", 0f);
            prices.put("tourist_service", 0f);
            prices.put("fixedFee", 0f);
            prices.put("onlinePrepayment", 0f);
            prices.put("payInAccommodationCuc", 0f);
            return  prices;
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

        String tc = currency + " ";

        prices.put("totalPrice", totalPrice* user.getCurrency().getChange());
        prices.put("payInAccommodation", payInAccommodation*user.getCurrency().getChange());
        prices.put("lodgingPrice", lodgingPrice*user.getCurrency().getChange());
        prices.put("tourist_service", tourist_service*user.getCurrency().getChange());
        prices.put("fixedFee", fixedFee*user.getCurrency().getChange());
        prices.put("onlinePrepayment", onlinePrepayment*user.getCurrency().getChange());
        prices.put("payInAccommodationCuc", payInAccommodationCuc);
        /**************************/

        return prices;
    }

    public static Map<String, Float> getPricesOne(User user, ReservationDetail reservationsDetail, Context context){
        Map<String, Float> prices = new HashMap<>();

        /**************************/
        String currency = "EUR";
        if(user != null){
            currency = user.getCurrency().getCode();
        }

        float lodgingPrice = 0;
        int roomsTotal = 0;


        float t = reservationsDetail.getTotalPrice(context);
        lodgingPrice += t;
        if(t > 0){
        roomsTotal++;
        }



        if(lodgingPrice == 0){
            prices.put("totalPrice", 0f);
            prices.put("payInAccommodation", 0f);
            prices.put("lodgingPrice", 0f);
            prices.put("tourist_service", 0f);
            prices.put("fixedFee", 0f);
            prices.put("onlinePrepayment", 0f);
            prices.put("payInAccommodationCuc", 0f);
            return  prices;
        }

        /**/
        Accommodation accommodation = reservationsDetail.getRoom().getAccommodation();
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
 ///Aqui dando bateo con los precios y las monedas
        prices.put("totalPrice", totalPrice * user.getCurrency().getChange());
        prices.put("payInAccommodation", payInAccommodation);
        prices.put("lodgingPrice", lodgingPrice);
        prices.put("tourist_service", tourist_service);
        prices.put("fixedFee", fixedFee);
        prices.put("onlinePrepayment", onlinePrepayment);
        prices.put("payInAccommodationCuc", payInAccommodationCuc);
        /**************************/

        return prices;
    }
}
