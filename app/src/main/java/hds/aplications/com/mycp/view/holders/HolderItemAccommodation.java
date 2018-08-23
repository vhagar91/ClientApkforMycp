package hds.aplications.com.mycp.view.holders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.richpath.RichPath;
import com.richpath.RichPathView;
import com.richpathanimator.AnimationBuilder;
import com.richpathanimator.AnimationListener;
import com.richpathanimator.RichPathAnimator;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import hds.aplications.com.mycp.MyCP;
import hds.aplications.com.mycp.R;
import hds.aplications.com.mycp.helpers.SAppData;
import hds.aplications.com.mycp.models.Accommodation;
import hds.aplications.com.mycp.models.Answer;
import hds.aplications.com.mycp.models.User;
import hds.aplications.com.mycp.services.UserClient;
import hds.aplications.com.mycp.view.activities.ActivityDetail;
import hds.aplications.com.mycp.view.others.ViewUtils;
import mgleon.common.com.DateUtils;
import mgleon.common.com.LogUtils;
import mgleon.common.com.services.Util.ProcessRetrofitError;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Miguel Gómez León on 3/9/17.
 */

public class HolderItemAccommodation extends RecyclerView.ViewHolder {
    private Context context;
    private static final String TAG = LogUtils.makeLogTag(HolderItemAccommodation.class);
    private Date checkIn;
    private Date checkOut;

    public HolderItemAccommodation(Context pContext, final View parent, Date checkIn, Date checkOut) {
        super(parent);
        context = pContext;

        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public static HolderItemAccommodation newInstance(View parent, final Context context, Date checkIn, Date checkOut) {
        HolderItemAccommodation holderItemAccommodation = new HolderItemAccommodation(context, parent, checkIn, checkOut);

        holderItemAccommodation.setIsRecyclable(false);

        return holderItemAccommodation;
    }

    private void loadImage(final Accommodation accommodation, ImageView image) {
        String imageUri = MyCP.SERVER + accommodation.getPhoto();
        //Picasso.with(context).load(imageUri).fit().centerCrop().into(image);
        try {
            MyCP.getPicassoBuilder(context).build().load(imageUri)/*.networkPolicy(NetworkPolicy.OFFLINE)*/.placeholder(R.drawable.animated_load_image).fit().centerCrop().into(image);
        }
        catch (Exception e) {
            LogUtils.LOGE(TAG, "Error loading image: " + accommodation.getPhoto());
        }


        image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                goDetail(accommodation);
            }
        });
    }

    private void initOnClickFavorite(final Accommodation accommodation, RichPathView richViewFavorite) {
        boolean favorite = accommodation.isFavorite();

        //richViewFavorite.setLayoutParams(new RelativeLayout.LayoutParams(20, 20));
        //richViewFavorite.setVectorDrawable(R.drawable.vector_favorite_w);

        final User user = SAppData.getInstance().getUser();
        if(user == null){
            richViewFavorite.setVisibility(View.GONE);
            return;
        }

        final RichPath richPathHeart = richViewFavorite.findRichPathByName("heart");
        richPathHeart.setPivotToCenter(true);
        if (favorite) {
            richPathHeart.setFillColor(ContextCompat.getColor(context, R.color.options_red));
        }

        richViewFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserClient userClient = new UserClient(context);
                Callback<Answer> answerCallback = null;
                String action = "1";

                if (!accommodation.isFavorite()) {
                    final AnimationBuilder animationBuilder = RichPathAnimator.animate(richPathHeart)
                            .interpolator(new LinearInterpolator())
                            .duration(2000)
                            .repeatMode(RichPathAnimator.RESTART)
                            .repeatCount(RichPathAnimator.INFINITE)
                            .scale(1,
                                    0.9f,
                                    0.7f,
                                    0.9f,
                                    1,
                                    0.9f,
                                    0.7f,
                                    0.9f,
                                    1)
                            .fillColor(ContextCompat.getColor(context, R.color.options_red),
                                    ContextCompat.getColor(context, R.color.options_red_1),
                                    ContextCompat.getColor(context, R.color.options_red_2),
                                    ContextCompat.getColor(context, R.color.options_red_1),
                                    ContextCompat.getColor(context, R.color.options_red),
                                    ContextCompat.getColor(context, R.color.options_red_1),
                                    ContextCompat.getColor(context, R.color.options_red_2),
                                    ContextCompat.getColor(context, R.color.options_red_1),
                                    ContextCompat.getColor(context, R.color.options_red));
                    animationBuilder.start();

                    answerCallback = new Callback<Answer>(){
                        @Override
                        public void success(Answer answer, Response response){
                            //animationBuilder.duration(1000);
                            animationBuilder.repeatCount(1);
                            if(answer.isSuccess()){
                                accommodation.setFavorite(true);
                            }
                        }

                        @Override
                        public void failure(RetrofitError error){
                            //animationBuilder.duration(1000);
                            animationBuilder.repeatCount(1);
                            animationBuilder.animationListener(new AnimationListener() {
                                @Override
                                public void onStart() {
                                }

                                @Override
                                public void onStop() {
                                    richPathHeart.setFillColor(ContextCompat.getColor(context, android.R.color.white));
                                }
                            });

                            ProcessRetrofitError.showError(error, context);
                        }
                    };
                }
                else {
                    final AnimationBuilder animationBuilder = RichPathAnimator.animate(richPathHeart)
                            .interpolator(new LinearInterpolator())
                            .duration(2000)
                            .repeatMode(RichPathAnimator.RESTART)
                            .repeatCount(RichPathAnimator.INFINITE)
                            .scale(1,
                                    0.9f,
                                    0.7f,
                                    0.9f,
                                    1,
                                    0.9f,
                                    0.7f,
                                    0.9f,
                                    1)
                            .fillColor(ContextCompat.getColor(context, android.R.color.white),
                                    ContextCompat.getColor(context, R.color.hint_loging),
                                    ContextCompat.getColor(context, R.color.btn_text_dialog),
                                    ContextCompat.getColor(context, R.color.hint_loging),
                                    ContextCompat.getColor(context, android.R.color.white),
                                    ContextCompat.getColor(context, R.color.hint_loging),
                                    ContextCompat.getColor(context, R.color.btn_text_dialog),
                                    ContextCompat.getColor(context, R.color.hint_loging),
                                    ContextCompat.getColor(context, android.R.color.white));
                    animationBuilder.start();

                    action = "0";
                    answerCallback = new Callback<Answer>(){
                        @Override
                        public void success(Answer answer, Response response){
                            //animationBuilder.duration(1000);
                            animationBuilder.repeatCount(1);
                            if(answer.isSuccess()){
                                accommodation.setFavorite(false);
                            }
                        }

                        @Override
                        public void failure(RetrofitError error){
                            //animationBuilder.duration(1000);
                            animationBuilder.repeatCount(1);
                            animationBuilder.animationListener(new AnimationListener() {
                                @Override
                                public void onStart() {
                                }

                                @Override
                                public void onStop() {
                                    richPathHeart.setFillColor(ContextCompat.getColor(context, R.color.options_red));
                                }
                            });
                            ProcessRetrofitError.showError(error, context);
                        }
                    };
                }

                userClient.favorite(String.valueOf(user.getIdRef()), user.getToken(), String.valueOf(accommodation.getIdRef()), action, answerCallback);
            }
        });
    }

    public void bindView(final Accommodation accommodation){
        ((TextView) itemView.findViewById(R.id.accommodation_title)).setText(accommodation.getName());
        ((TextView) itemView.findViewById(R.id.accommodation_destination)).setText(accommodation.getDestination());

        String a = context.getString(R.string.home_capacity);
        ((TextView) itemView.findViewById(R.id.home_capacity)).setText(a + " " + String.valueOf(accommodation.getMaximunNumberGuests()));

        String currency = "EUR";
        User user = SAppData.getInstance().getUser();
        if(user != null){
            currency = user.getCurrency().getCode();
        }
        ((TextView) itemView.findViewById(R.id.min_price_eur)).setText(currency + " " + ViewUtils.getRoundedPrice(accommodation.getMinPrice()));

        if(!accommodation.getInmediateBooking()){
            itemView.findViewById(R.id.inmediate_booking).setVisibility(View.GONE);
        }

        ImageView image = (ImageView) itemView.findViewById(R.id.image_remote);
        loadImage(accommodation, image);

        RichPathView richViewFavorite = (RichPathView) itemView.findViewById(R.id.rich_view_favorite);
        initOnClickFavorite(accommodation, richViewFavorite);

        itemView.findViewById(R.id.btn_book_now).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                MyCP.getApplication(context).bookNow(null, accommodation, checkIn, checkOut);
            }
        });

        itemView.findViewById(R.id.btn_detail).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                goDetail(accommodation);
            }
        });

        if(accommodation.getStars() >= 0){
            List<RichPathView> richPathStars = Arrays.asList(
                    (RichPathView) itemView.findViewById(R.id.star_1),
                    (RichPathView) itemView.findViewById(R.id.star_2),
                    (RichPathView) itemView.findViewById(R.id.star_3),
                    (RichPathView) itemView.findViewById(R.id.star_4),
                    (RichPathView) itemView.findViewById(R.id.star_5));
            for(int i = 0; i < 5; i++){
                if(i >= accommodation.getStars()){
                    final RichPath richPathStar = richPathStars.get(i).findRichPathByName("start");
                    RichPathAnimator.animate(richPathStar)
                        /*.interpolator(new LinearInterpolator())
                        .duration(2000)*/
                            .fillColor(ContextCompat.getColor(context, android.R.color.transparent))
                            .start();
                }
            }
        }
    }

    private void goDetail(Accommodation accommodation){
        Intent intent = new Intent(context, ActivityDetail.class);
        Bundle bundle = new Bundle();
        bundle.putString("code_own", accommodation.getCode());
        bundle.putString("id_own", String.valueOf(accommodation.getIdRef()));

        if(checkIn != null && checkOut != null){
            bundle.putString("check_in", DateUtils.generateStringDate(checkIn));
            bundle.putString("check_out", DateUtils.generateStringDate(checkOut));
        }

        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}