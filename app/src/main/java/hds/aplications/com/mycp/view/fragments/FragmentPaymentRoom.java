package hds.aplications.com.mycp.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Map;

import hds.aplications.com.mycp.R;
import hds.aplications.com.mycp.helpers.SAppData;
import hds.aplications.com.mycp.models.ReservationDetail;
import hds.aplications.com.mycp.models.User;
import hds.aplications.com.mycp.view.others.ViewUtils;
import mgleon.common.com.DateUtils;
import mgleon.common.com.LogUtils;

/**
 * Created by Miguel Gomez Leon.
 * mgleonsc@gmail.com
 */

public class FragmentPaymentRoom extends Fragment{
    private static final String TAG = LogUtils.makeLogTag(FragmentPaymentRoom.class);
    private ReservationDetail reservationDetail;
    private ListenerPaymentRoom listenerPaymentRoom;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment_room, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         User user = SAppData.getInstance().getUser();

        String c = "";
        if(reservationDetail.getAdultsTotal() > 0){
            c = getString(R.string.adults) + " " + String.valueOf(reservationDetail.getAdultsTotal());
        }
        if(reservationDetail.getKidsTotal() > 0){
            c += !c.isEmpty() ? ", " : "";
            c += getString(R.string.children) + " " + String.valueOf(reservationDetail.getKidsTotal());
        }
        ((TextView)view.findViewById(R.id.cap_reservation)).setText(c);

        ((TextView)view.findViewById(R.id.destination_accommodation)).setText(reservationDetail.getRoom().getAccommodation().getDestination());

        c = DateUtils.generateStringDate(reservationDetail.getDateFrom(), new SimpleDateFormat("dd/MM/yyyy")) + " - " + DateUtils.generateStringDate(reservationDetail.getDateTo(), new SimpleDateFormat("dd/MM/yyyy"));
        ((TextView)view.findViewById(R.id.date_reservation)).setText(c);



        String currency = "EUR";
        String lang="EN" ;
        //Price calculation
        float totalPrice = 0;

        Map<String, Float> prices = ViewUtils.getPricesOne(user, reservationDetail, getContext());

        totalPrice += prices.get("totalPrice");
        if(user != null){
            currency = user.getCurrency().getCode();
            lang=user.getLangCode();
        }
        ((TextView)view.findViewById(R.id.title_accommodation)).setText(reservationDetail.getRoom().getAccommodation().getName());
        ((TextView)view.findViewById(R.id.room_type)).setText(reservationDetail.getRoom().getRoomType().getLocaleName(lang));


          String tc = currency + " ";
        ((TextView) view.findViewById(R.id.total_price_f)).setText(tc + ViewUtils.getRoundedPrice(totalPrice));


        ((TextView)view.findViewById(R.id.cas_e)).setText("CAS." + reservationDetail.getCas());

        final CheckBox checkBox = ((CheckBox)view.findViewById(R.id.check_box));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                reservationDetail.setActive(isChecked);
                listenerPaymentRoom.change();
            }
        });

        view.findViewById(R.id.content_alll).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkBox.setChecked(!checkBox.isChecked());
            }
        });

    }

    public void setReservationDetail(ReservationDetail reservationDetail){
        this.reservationDetail = reservationDetail;
    }

    public void setListenerBook(ListenerPaymentRoom listenerPaymentRoom){
        this.listenerPaymentRoom = listenerPaymentRoom;
    }

    public interface ListenerPaymentRoom{
        void change();
    }
}
