package hds.aplications.com.mycp.view.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.richpath.RichPath;
import com.richpath.RichPathView;
import com.richpathanimator.RichPathAnimator;

import java.util.Arrays;
import java.util.List;

import hds.aplications.com.mycp.MyCP;
import hds.aplications.com.mycp.R;
import hds.aplications.com.mycp.helpers.SAppData;
import hds.aplications.com.mycp.models.Accommodation;
import hds.aplications.com.mycp.models.User;
import hds.aplications.com.mycp.view.activities.ActivityMap;
import hds.aplications.com.mycp.view.others.ViewUtils;
import mgleon.common.com.LogUtils;
import mgleon.common.com.MessageToast;



public class FragmentDetail extends Fragment{
    private Listener listener;
    private View contenView;
    private Accommodation accommodation;
    private static final String TAG = LogUtils.makeLogTag(FragmentDetail.class);
    private boolean visible = true;

    public void setListener(Listener listener){
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        contenView = view;

        ((TextView) contenView.findViewById(R.id.home_name)).setText(accommodation.getName());
        ((TextView) contenView.findViewById(R.id.own_name)).setText(accommodation.getOwnerName());

        String des = accommodation.getDescription();
        if(des.length() > 160){
            des = des.substring(0, 160) + "...";
        }

        WebView webViewDesc = (WebView) contenView.findViewById(R.id.description_2);
        String text = "<html><body style=\"margin: 0; padding: 0\"><p align=\"justify\" style=\"margin: 0; padding: 0; color: #6f868d\">";
        text += des;
        text += "</p></body></html>";
        webViewDesc.loadData(text, "text/html; charset=utf-8", "UTF-8");
        webViewDesc.setBackgroundColor(Color.TRANSPARENT);
        webViewDesc.setOnTouchListener(new View.OnTouchListener(){
            private boolean startTouch = false;
            private final static long MAX_TOUCH_DURATION = 500;
            private long timeStartTouch;

            @Override
            public boolean onTouch(View v, MotionEvent event){

                if(v.getId() == R.id.description_2){
                    switch(event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            LogUtils.LOGI(TAG, "ACTION_DOWN");
                            startTouch = true;
                            timeStartTouch = event.getEventTime();
                            break;
                        case MotionEvent.ACTION_UP:
                            LogUtils.LOGI(TAG, "ACTION_UP");
                            if(/*startTouch &&*/ (event.getEventTime() - timeStartTouch <= MAX_TOUCH_DURATION)){
                               listener.showDesc();
                                startTouch = false;
                            }
                            break;
                        case MotionEvent.ACTION_MOVE:
                            LogUtils.LOGI(TAG, "ACTION_MOVE");
                            startTouch = false;
                            break;
                    }
                }
                return false;
            }
        });

        ImageView imageView = (ImageView) view.findViewById(R.id.circle_image_view_profile_image);
        if(accommodation.getPhotoOwn() != null && !accommodation.getPhotoOwn().isEmpty()){
            MyCP.getPicassoBuilder(getContext()).build().load(MyCP.SERVER2 + accommodation.getPhotoOwn())/*.placeholder(R.mipmap.user_white)*/.fit().centerCrop().into(imageView);
        }
        else{
            MyCP.getPicassoBuilder(getContext()).build().load(MyCP.SERVER + accommodation.getPhotos().get(0))/*.placeholder(R.mipmap.user_white)*/.fit().centerCrop().into(imageView);
        }

        view.findViewById(R.id.linear_layout_action_map).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                goToMap();
            }
        });

        view.findViewById(R.id.services_view_all).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                hide();
                if(listener != null){
                    listener.seeAllServicesAction();
                }
            }
        });

        if(accommodation.getStars() >= 0){

            List<RichPathView> richPathStars = Arrays.asList(
                    (RichPathView) contenView.findViewById(R.id.star_1),
                    (RichPathView) contenView.findViewById(R.id.star_2),
                    (RichPathView) contenView.findViewById(R.id.star_3),
                    (RichPathView) contenView.findViewById(R.id.star_4),
                    (RichPathView) contenView.findViewById(R.id.star_5));
            for(int i = 0; i < 5; i++){
                if(i >= accommodation.getStars()){
                    final RichPath richPathStar = richPathStars.get(i).findRichPathByName("start");
                    RichPathAnimator.animate(richPathStar)
                        /*.interpolator(new LinearInterpolator())
                        .duration(2000)*/
                            .fillColor(ContextCompat.getColor(getContext(), android.R.color.transparent))
                            .start();
                }
            }
        }

        String currency = "EUR";
        User user = SAppData.getInstance().getUser();
        if(user != null){
            currency = user.getCurrency().getCode();
        }
        ((TextView) contenView.findViewById(R.id.min_price)).setText(currency + " " + ViewUtils.getRoundedPrice(accommodation.getMinPrice()));
        ((TextView) contenView.findViewById(R.id.accommodation_destination)).setText(accommodation.getDestination());
        ((TextView) contenView.findViewById(R.id.accommodation_type)).setText(accommodation.getAccommodationType().getLocaleName(user.getLangCode()));

        String minNumberGuests = getString(R.string.min_guests, accommodation.getMaximunNumberGuests());
        ((TextView) contenView.findViewById(R.id.home_capacity)).setText(minNumberGuests);

        String l_rooms = getString(R.string.beedrooms, accommodation.getRooms().size());
        ((TextView) contenView.findViewById(R.id.accommodation_beedrooms)).setText(l_rooms);

        String l = "Spanish";
        String la = accommodation.getLanguages();
        List<String> lang = Arrays.asList("English", "French", "German", "Italian");
        for(int i = 0; i < lang.size(); i++){
            String s = lang.get(i);
            if(la.length() > i && String.valueOf(la.charAt(i)).equals("1")){
                l += " & " + s;
            }
        }
        ((TextView) contenView.findViewById(R.id.accommodation_lan)).setText(l);
        final String finalL = l;
        contenView.findViewById(R.id.content_lang).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                MessageToast.showInfo(getContext(), finalL);
            }
        });

        if(accommodation.getBreakfast()){
            ((TextView) contenView.findViewById(R.id.breakfast_price)).setText(currency + " " + ViewUtils.getRoundedPrice(accommodation.getBreakfastPrice()));
        }
        else {
            contenView.findViewById(R.id.breakfast).setVisibility(View.GONE);
        }

        if(accommodation.getDinner()){
            ((TextView) contenView.findViewById(R.id.dinner_price)).setText(currency + " " + ViewUtils.getRoundedPrice(accommodation.getDinnerPriceFrom()));
        }
        else {
            contenView.findViewById(R.id.dinner).setVisibility(View.GONE);
        }

        if(!accommodation.getParking()){
            contenView.findViewById(R.id.parking).setVisibility(View.GONE);
        }
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

    private void goToMap(){
        SAppData.getInstance().accommodation = accommodation;
        Intent intent = new Intent(getContext(), ActivityMap.class);
        Bundle bundle = new Bundle();
        bundle.putLong("id_own", accommodation.getIdRef());
        bundle.putString("code_own", accommodation.getCode());
        intent.putExtras(bundle);

        startActivity(intent);
    }

    public boolean isVisiblee(){
        return visible;
    }

    public interface Listener{
        void seeAllServicesAction();

        void showDesc();
    }

    public void setAccommodation(Accommodation accommodation){
        this.accommodation = accommodation;
    }
}
