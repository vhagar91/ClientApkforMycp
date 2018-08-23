package hds.aplications.com.mycp.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import hds.aplications.com.mycp.R;
import hds.aplications.com.mycp.helpers.SAppData;
import hds.aplications.com.mycp.models.Room;
import hds.aplications.com.mycp.models.User;
import hds.aplications.com.mycp.view.others.ViewUtils;
import mgleon.common.com.MessageToast;

/**
 * Created by Miguel Gomez Leon.
 * mgleonsc@gmail.com
 */

public class FragmentServices extends Fragment{
    private Listener listener;
    private View contenView;
    private List<Room> rooms;
    private int index = 0;
    private String currency = "EUR";
    private boolean visible = true;

    public void setListener(Listener listener){
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_services, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contenView = view;

        view.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                hide();
                if(listener != null){
                    listener.closeAction();
                }
            }
        });
        view.findViewById(R.id.btn_prev).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                actionPrev();
            }
        });
        view.findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                actionNext();
            }
        });
        showRoomServices(rooms.get(index));
        initSetOnClickService();

        User user = SAppData.getInstance().getUser();
        if(user != null){
            currency = user.getCurrency().getCode();
        }
    }

    private void actionPrev(){
        if(index - 1 < 0){
            index = rooms.size() - 1;
        }
        else {
            index--;
        }
        showRoomServices(rooms.get(index));
    }

    private void actionNext(){
        if(index + 1 >= rooms.size()){
            index = 0;
        }
        else {
            index++;
        }
        showRoomServices(rooms.get(index));
    }

    private void initSetOnClickService(){
        final User user = SAppData.getInstance().getUser();
        contenView.findViewById(R.id.service_audiovisual).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Room room = rooms.get(index);
                String t = getString(R.string.label_audiovisual) + "\n" + room.getRoomAudiovisual().getLocaleName(user.getLangCode());
                MessageToast.showInfo(getContext(), t);
            }
        });

        contenView.findViewById(R.id.service_beds).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Room room = rooms.get(index);
                String t = getString(R.string.label_beds) + "\n" + String.valueOf(room.getBeds());
                MessageToast.showInfo(getContext(), t);
            }
        });

        contenView.findViewById(R.id.service_balcony).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Room room = rooms.get(index);
                String t = getString(R.string.label_balcony) + "\n" + String.valueOf(room.getBalcony());
                MessageToast.showInfo(getContext(), t);
            }
        });

        contenView.findViewById(R.id.service_windows).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Room room = rooms.get(index);
                String t = getString(R.string.label_windows) + "\n" + String.valueOf(room.getBalcony());
                MessageToast.showInfo(getContext(), t);
            }
        });

        contenView.findViewById(R.id.service_hvac).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Room room = rooms.get(index);
                String t = getString(R.string.label_hvac) + "\n" + room.getClima().getLocaleName(user.getLangCode());
                MessageToast.showInfo(getContext(), t);
            }
        });

        contenView.findViewById(R.id.service_bath).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Room room = rooms.get(index);
                String t = getString(R.string.label_bath) + "\n" + room.getBathroom().getLocaleName(user.getLangCode());
                MessageToast.showInfo(getContext(), t);
            }
        });
    }

    public void showRoomServices(Room room){
        final User user = SAppData.getInstance().getUser();
        contenView.findViewById(R.id.type_price).setVisibility(View.INVISIBLE);

        ((TextView)contenView.findViewById(R.id.room_type)).setText(room.getRoomType().getLocaleName(user.getLangCode()) + " #" + String.valueOf(room.getNumber()));

        String tc = getString(R.string.from_c) + " " + currency + " ";
        ((TextView) contenView.findViewById(R.id.min_price)).setText(tc + ViewUtils.getRoundedPrice(room.getPriceDownTo()));

        int visible = room.isStereo() ? View.VISIBLE : View.GONE;
        contenView.findViewById(R.id.service_stereo).setVisibility(visible);

        visible = (room.getRoomAudiovisual() != null && !room.getRoomAudiovisual().getName().equals("No")) ? View.VISIBLE : View.GONE;
        contenView.findViewById(R.id.service_audiovisual).setVisibility(visible);
        if(visible == View.VISIBLE){
            ((TextView) contenView.findViewById(R.id.service_audiovisual_value)).setText(room.getRoomAudiovisual().getLocaleName(user.getLangCode()));
        }

        visible = room.isSafe() ? View.VISIBLE : View.GONE;
        contenView.findViewById(R.id.service_safe).setVisibility(visible);

        visible = room.isSmoke() ? View.VISIBLE : View.GONE;
        contenView.findViewById(R.id.service_smoke).setVisibility(visible);

        visible = room.isCradle() ? View.VISIBLE : View.GONE;
        contenView.findViewById(R.id.service_cradle).setVisibility(visible);

        visible = room.isTerrace() ? View.VISIBLE : View.GONE;
        contenView.findViewById(R.id.service_terrace).setVisibility(visible);

        visible = room.isYard() ? View.VISIBLE : View.GONE;
        contenView.findViewById(R.id.service_yard).setVisibility(visible);

        visible = room.getBeds() != null && room.getBeds() > 0 ? View.VISIBLE : View.GONE;
        contenView.findViewById(R.id.service_beds).setVisibility(visible);
        if(visible == View.VISIBLE){
            ((TextView)contenView.findViewById(R.id.service_beds_value)).setText(String.valueOf(room.getBeds()));
        }

        visible = room.getBalcony() != null && room.getBalcony() > 0 ? View.VISIBLE : View.GONE;
        contenView.findViewById(R.id.service_balcony).setVisibility(visible);
        if(visible == View.VISIBLE){
            ((TextView)contenView.findViewById(R.id.service_balcony_value)).setText(String.valueOf(room.getBalcony()));
        }

        visible = room.getWindows() != null && room.getWindows() > 0 ? View.VISIBLE : View.GONE;
        contenView.findViewById(R.id.service_windows).setVisibility(visible);
        if(visible == View.VISIBLE){
            ((TextView)contenView.findViewById(R.id.service_windows_value)).setText(String.valueOf(room.getBalcony()));
        }

        visible = room.getClima() != null ? View.VISIBLE : View.GONE;
        contenView.findViewById(R.id.service_hvac).setVisibility(visible);
        if(visible == View.VISIBLE){
            ((TextView)contenView.findViewById(R.id.service_hvac_value)).setText(room.getClima().getLocaleName(user.getLangCode()));
        }

        visible = room.getBathroom() != null ? View.VISIBLE : View.GONE;
        contenView.findViewById(R.id.service_bath).setVisibility(visible);
        if(visible == View.VISIBLE){
            ((TextView)contenView.findViewById(R.id.service_bath_value)).setText(room.getBathroom().getLocaleName(user.getLangCode()));
        }

        contenView.findViewById(R.id.type_price).setVisibility(View.VISIBLE);
    }

    public void show(){
        if(contenView != null){
            contenView.setVisibility(View.VISIBLE);
            visible = true;
        }
    }

    public void hide(){
        if(contenView != null){
            contenView.setVisibility(View.GONE);
            visible = false;
        }
    }

    public boolean isVisiblee(){
        return visible;
    }

    public void setRooms(List<Room> rooms){
        this.rooms = rooms;
    }

    public interface Listener{
        void closeAction();
    }
}
