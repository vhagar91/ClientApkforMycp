package hds.aplications.com.mycp.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import hds.aplications.com.mycp.R;
import hds.aplications.com.mycp.helpers.PDFTools;
import hds.aplications.com.mycp.helpers.SAppData;
import hds.aplications.com.mycp.models.Accommodation;
import hds.aplications.com.mycp.models.Reservation;
import hds.aplications.com.mycp.models.ReservationDetail;
import hds.aplications.com.mycp.models.User;
import hds.aplications.com.mycp.repositories.ReservationRepository;
import hds.aplications.com.mycp.view.activities.ActivityDetail;
import hds.aplications.com.mycp.view.activities.ActivityMap;
import hds.aplications.com.mycp.view.components.LoadMask;
import hds.aplications.com.mycp.view.others.ViewUtils;
import mgleon.common.com.DateUtils;
import mgleon.common.com.MessageToast;


public class ReservationAdapter extends BaseAdapter {
    private Context context;
    private List<Reservation> items;
    private ListView list;

    public ReservationAdapter(Context context, List<Reservation> items, ListView list){
        this.context= context;
        this.items = items;
        this.list = list;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return  (((Reservation)this.getItem(position)).getId() != null) ?((Reservation)this.getItem(position)).getId(): ((Reservation)this.getItem(position)).getIdRef();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (convertView == null) {
            // Create a new view into the list.
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.item_reservation, parent, false);
        }

        // Set data into the view.
        final User user = SAppData.getInstance().getUser();

        final Reservation item = this.items.get(position);
        ((TextView)rowView.findViewById(R.id.title_accommodation)).setText(item.getAccommodation().getName());
        ((TextView)rowView.findViewById(R.id.room_type)).setText(item.getReservationDetails1().get(0).getRoom().getRoomType().getLocaleName(user.getLangCode()));

        String c = "";
        if(item.getReservationDetails1().get(0).getAdultsTotal() > 0){
            c = context.getString(R.string.adults) + " " + String.valueOf(item.getReservationDetails1().get(0).getAdultsTotal());
        }
        if(item.getReservationDetails1().get(0).getKidsTotal() > 0){
            c += !c.isEmpty() ? ", " : "";
            c += context.getString(R.string.children) + " " + String.valueOf(item.getReservationDetails1().get(0).getKidsTotal());
        }
        ((TextView)rowView.findViewById(R.id.cap_reservation)).setText(c);
        ((TextView)rowView.findViewById(R.id.status)).setText(item.getReservationDetails1().get(0).getStatus().getLocaleName(user.getLangCode()));
        if(item.getReservationDetails1().get(0).getStatus().getId()==1){
            rowView.findViewById(R.id.action_download).setVisibility(rowView.GONE);
            ((RelativeLayout)rowView.findViewById(R.id.btn_status)).setBackgroundResource(R.color.calendar_text_inactive);
        }
        else if(item.getReservationDetails1().get(0).getStatus().getId()==5){

            ((RelativeLayout)rowView.findViewById(R.id.btn_status)).setBackgroundResource(R.color.color_btn_blue);

        }
        else{
             rowView.findViewById(R.id.action_download).setVisibility(rowView.GONE);
            ((RelativeLayout)rowView.findViewById(R.id.btn_status)).setBackgroundResource(R.color.color_btn_green);
        }


        ((TextView)rowView.findViewById(R.id.destination_accommodation)).setText(item.getReservationDetails1().get(0).getRoom().getAccommodation().getDestination());

        c = DateUtils.generateStringDate(item.getReservationDetails1().get(0).getDateFrom(), new SimpleDateFormat("dd/MM/yyyy")) + " - " + DateUtils.generateStringDate(item.getReservationDetails1().get(0).getDateTo(), new SimpleDateFormat("dd/MM/yyyy"));
        ((TextView)rowView.findViewById(R.id.date_reservation)).setText(c);
        //Price calculation
        float totalPrice = 0;

        Map<String, Float> prices = ViewUtils.getPricesOne(user, item.getReservationDetails1().get(0), context);

        totalPrice += prices.get("totalPrice");

        String currency = "EUR";

        if(user != null){
            currency = user.getCurrency().getCode();
        }
        String tc = currency + " ";
        ((TextView) rowView.findViewById(R.id.total_price_f)).setText(tc + ViewUtils.getRoundedPrice(totalPrice));



        ((TextView)rowView.findViewById(R.id.cas_e)).setText("CAS." + item.getIdRef());

        rowView.findViewById(R.id.action_map).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                goToMap(item.getReservationDetails1().get(0));
            }
        });


        rowView.findViewById(R.id.voucher).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                goDetail(item.getReservationDetails1().get(0).getRoom().getAccommodation());
            }
        });

        rowView.findViewById(R.id.action_download).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                downloadVoucher(item.getIdRef(),user.getIdRef(),item.getBooking());

            }
        });

        return rowView;
    }
    private void goToMap(ReservationDetail detail){

        SAppData.getInstance().accommodation = detail.getRoom().getAccommodation();
        Intent intent = new Intent(context, ActivityMap.class);
        Bundle bundle = new Bundle();
        bundle.putLong("id_own", detail.getIdRef());
        bundle.putString("code_own", detail.getRoom().getAccommodation().getCode());
        intent.putExtras(bundle);

        context.startActivity(intent);
    }
    private void downloadVoucher(Long cas,Long user,String booking){
        try{

            String name = "voucher" + user + '_' + booking + ".pdf";

            String SERVER4 = "http://192.168.137.1/MyCasaParticular/web/app_dev.php/download/";
            PDFTools.showPDFUrl(context,SERVER4+cas,name);


        }catch (Exception e){
            e.printStackTrace();

            MessageToast.showError(context, e.getMessage());
        }
    }

    private void goDetail(Accommodation accommodation){

        Intent intent = new Intent(context, ActivityDetail.class);
        Bundle bundle = new Bundle();
        bundle.putString("code_own", accommodation.getCode());
        bundle.putString("id_own", String.valueOf(accommodation.getIdRef()));



        intent.putExtras(bundle);

        context.startActivity(intent);
    }



}
