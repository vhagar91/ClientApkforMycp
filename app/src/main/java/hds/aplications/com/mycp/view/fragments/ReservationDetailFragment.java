package hds.aplications.com.mycp.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import hds.aplications.com.mycp.R;
import hds.aplications.com.mycp.helpers.SAppData;
import hds.aplications.com.mycp.models.ReservationDetail;
import hds.aplications.com.mycp.models.RoomType;
import hds.aplications.com.mycp.models.User;
import hds.aplications.com.mycp.repositories.ReservationDetailRepository;
import mgleon.common.com.DateUtils;

/**
 * Created by miguel on 16/01/2016.
 */
public class ReservationDetailFragment extends Fragment{

    public static final String SINGLE_ROOM_TYPE_TEXT = "Habitación individual";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final User user = SAppData.getInstance().getUser();
        View V = inflater.inflate(R.layout.reservation_detail_tab, container, false);

        //Obtener el ReservationDetail por el id del bundle
        Long reservationDetailId = getArguments().getLong("reservation.details.id");

        ReservationDetailRepository reservationDetailRepository = new ReservationDetailRepository();
        ReservationDetail detail = (ReservationDetail)reservationDetailRepository.getById(reservationDetailId);

        //Binding
        TextView accommodationName = (TextView) V.findViewById(R.id.accommodationName);
        accommodationName.setText(detail.getRoom().getAccommodation().getName());

        TextView accommodationCode = (TextView) V.findViewById(R.id.accommodationCode);
        accommodationCode.setText(detail.getRoom().getAccommodation().getCode());

        TextView textCheckout = (TextView) V.findViewById(R.id.textCheckout);
        TextView textCheckin = (TextView) V.findViewById(R.id.textCheckin);
        textCheckout.setText(DateUtils.getLargeDate(detail.getDateFrom(), true));
        textCheckin.setText(DateUtils.getLargeDate(detail.getDateTo(), true));

        TextView roomTypeText = (TextView) V.findViewById(R.id.tv_room_type);
        ImageView roomTypeImage = (ImageView) V.findViewById(R.id.iv_room_type);

        RoomType roomType = detail.getRoom().getRoomType();
        roomTypeText.setText(roomType.getLocaleName(user.getLangCode()));

        if(roomType.getName() == SINGLE_ROOM_TYPE_TEXT)
            roomTypeImage.setImageResource(R.mipmap.ic_single_room_type_);
        else
            roomTypeImage.setImageResource(R.mipmap.ic_double_room_type_);

        TextView adultsTotal = (TextView) V.findViewById(R.id.tv_adults_total);
        adultsTotal.setText(Integer.toString(detail.getAdultsTotal()));

        TextView childrenTotal = (TextView) V.findViewById(R.id.tv_children_total);
        childrenTotal.setText(Integer.toString(detail.getKidsTotal()));

        /*TextView destination = (TextView) V.findViewById(R.id.tv_destination);
        destination.setText((detail.getRoom().getAccommodation().getAddress() != null &&
                            detail.getRoom().getAccommodation().getAddress().getDestination() != null) ? detail.getRoom().getAccommodation().getAddress().getDestination().getName() : getString(R.string.no_data));

        TextView province = (TextView) V.findViewById(R.id.tv_province);
        province.setText((detail.getRoom().getAccommodation().getAddress() != null &&
                        detail.getRoom().getAccommodation().getAddress().getMunicipality() != null &&
                        detail.getRoom().getAccommodation().getAddress().getDestination().getMunicipality().getProvince() != null) ? detail.getRoom().getAccommodation().getAddress().getDestination().getMunicipality().getProvince().getName() : getString(R.string.no_data));

        TextView address = (TextView) V.findViewById(R.id.tv_address);
        address.setText((detail.getRoom().getAccommodation().getAddress() != null) ? detail.getRoom().getAccommodation().getAddress().getFullAddress() : getString(R.string.no_data));

        TextView phone = (TextView) V.findViewById(R.id.tv_phone);
        String phoneNumber =  detail.getRoom().getAccommodation().getPhone().trim();*/

       // phoneNumber += ((phoneNumber != null && phoneNumber != "" && detail.getRoom().getDestinations().getMobile() != null  && detail.getRoom().getDestinations().getMobile().trim() != "") ? " / " : "") +  detail.getRoom().getDestinations().getMobile();
        /*phone.setText((phoneNumber.trim() != "") ? "(+53) " + phoneNumber : getString(R.string.no_data));*/

        return V;
    }
}
