package hds.aplications.com.mycp.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hds.aplications.com.mycp.R;
import hds.aplications.com.mycp.helpers.SAppData;
import hds.aplications.com.mycp.models.ReservationDetail;
import hds.aplications.com.mycp.models.RoomType;
import hds.aplications.com.mycp.models.User;
import hds.aplications.com.mycp.view.others.ViewUtils;
import mgleon.common.com.LogUtils;



public class FragmentBookRoom extends Fragment{
    private static final String TAG = LogUtils.makeLogTag(FragmentBookRoom.class);
    private ReservationDetail reservationDetail;
    private ListenerBook listenerBook;
    //private EditText editTextAdults;
    //private EditText editTextChildrens;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_room, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        User user = SAppData.getInstance().getUser();


        String currency = "EUR";
        String lang="EN" ;
        if(user != null){
            currency = user.getCurrency().getCode();
            lang=user.getLangCode();
        }
        ((TextView)view.findViewById(R.id.room_type)).setText(reservationDetail.getRoom().getRoomType().getLocaleName(lang));

        String tc = currency + " ";
        ((TextView) view.findViewById(R.id.from_price)).setText(tc + ViewUtils.getRoundedPrice(reservationDetail.getRoom().getPriceDownTo()));

        if(reservationDetail.getRoom().getUnavailability()){
            return;
        }
        else {
            view.findViewById(R.id.frame_una).setVisibility(View.GONE);
            view.findViewById(R.id.room_guest).setVisibility(View.VISIBLE);
        }

        /******* SPINNER *********/
        List<String> integers = getCapabilities(true);
        Spinner spinner = (Spinner) view.findViewById(R.id.book_adults);
        ArrayAdapter<String> adaptador = new ArrayAdapter<>(getContext(), R.layout.simple_spinner_item_x, integers);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adaptador);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id){
                String val = ((String) parent.getItemAtPosition(position));
                if(!val.isEmpty()){
                    Integer va = Integer.valueOf(val);
                    update(va, true);
                }
            }

            public void onNothingSelected(AdapterView<?> parent){}
        });

        integers = getCapabilities(false);
        spinner = (Spinner) view.findViewById(R.id.book_childrens);
        adaptador = new ArrayAdapter<>(getContext(), R.layout.simple_spinner_item_x, integers);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adaptador);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id){
                String val = ((String) parent.getItemAtPosition(position));
                if(!val.isEmpty()){
                    Integer va = Integer.valueOf(val);
                    update(va, false);
                }
            }

            public void onNothingSelected(AdapterView<?> parent){}
        });

        /*SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, integers);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);*/
    }

    private void update(Integer value, boolean adults){
        if(reservationDetail != null && adults){
            reservationDetail.setAdultsTotal(value);
        }
        else {
            reservationDetail.setKidsTotal(value);
        }
        if(listenerBook != null){
            listenerBook.change();
        }
    }

    public ReservationDetail getReservationDetail(){
        return reservationDetail;
    }

    public void setReservationDetail(ReservationDetail reservationDetail){
        this.reservationDetail = reservationDetail;
    }

    public ListenerBook getListenerBook(){
        return listenerBook;
    }

    public void setListenerBook(ListenerBook listenerBook){
        this.listenerBook = listenerBook;
    }

    public interface ListenerBook{
        void change();
    }

    private List<String> getCapabilities(boolean adults){
        int val = 0;
        List<String> integers = new ArrayList<>();
        integers.add("0");

        RoomType roomType = reservationDetail.getRoom().getRoomType();
        if(roomType.getName().equals(getResources().getString(R.string.ROOM_TYPE_TRIPLE))){
            val = 3;
        }
        else if(roomType.getName().equals(getResources().getString(R.string.ROOM_TYPE_DOBLE_2)) || roomType.getName().equals(getResources().getString(R.string.ROOM_TYPE_DOBLE))){
            val = 2;
            if(!reservationDetail.getRoom().getAccommodation().getModalityComplete() && !adults && reservationDetail.getRoom().getBeds() < 2){
                val = 0;
            }
        }
        else if(roomType.getName().equals(getResources().getString(R.string.ROOM_TYPE_INDIVIDUAL))){
            val = 1;
        }

        for(int i = 1; i <= val; i++){
            String v = String.valueOf(i);
            integers.add(v);
        }

        return integers;
    }
}
