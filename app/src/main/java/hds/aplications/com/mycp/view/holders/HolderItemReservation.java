package hds.aplications.com.mycp.view.holders;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.richpath.RichPath;
import com.richpath.RichPathView;
import com.richpathanimator.AnimationListener;
import com.richpathanimator.RichPathAnimator;
import com.squareup.picasso.Picasso;

import hds.aplications.com.mycp.R;
import hds.aplications.com.mycp.models.Accommodation;
import mgleon.common.com.LogUtils;


public class HolderItemReservation extends RecyclerView.ViewHolder {
    private final TextView title;
    private RichPathView richViewFavorite;

    private Context context;
    private static final String TAG = LogUtils.makeLogTag(HolderItemReservation.class);

    public HolderItemReservation(Context pContext, final View parent, TextView itemTextView, RichPathView pRichViewFavorite) {
        super(parent);
        title = itemTextView;
        richViewFavorite = pRichViewFavorite;
        context = pContext;
    }

    public static HolderItemReservation newInstance(View parent, final Context context) {
        TextView itemTextView = (TextView) parent.findViewById(R.id.title_accommodation);
        RichPathView prichViewFavorite = (RichPathView) parent.findViewById(R.id.rich_view_favorite);
        HolderItemReservation holderItemAccommodation = new HolderItemReservation(context, parent, itemTextView, prichViewFavorite);

        holderItemAccommodation.setIsRecyclable(false);

        return holderItemAccommodation;
    }

    public void setTitle(CharSequence text) {
        title.setText(text);
    }

    public void initOnClickFavorite(final Accommodation accommodation) {
        boolean favorite = accommodation.isFavorite();

        //richViewFavorite.setLayoutParams(new RelativeLayout.LayoutParams(20, 20));
        //richViewFavorite.setVectorDrawable(R.drawable.vector_favorite_w);

        final RichPath richPathHeart = richViewFavorite.findRichPathByName("heart");
        richPathHeart.setPivotToCenter(true);
        if (favorite) {
            richPathHeart.setFillColor(ContextCompat.getColor(context, R.color.options_red));
        }


        RichPathAnimator
                .animate(richPathHeart)
                .fillColor(ContextCompat.getColor(context, R.color.text_loging)).start();

        richViewFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!accommodation.isFavorite()) {
                    RichPathAnimator.animate(richPathHeart)
                            .interpolator(new LinearInterpolator())
                            .duration(2000)
                            .repeatMode(RichPathAnimator.RESTART)
                            .repeatCount(RichPathAnimator.INFINITE)
                            .animationListener(new AnimationListener() {
                                @Override
                                public void onStart() {
                                }

                                @Override
                                public void onStop() {
                                    accommodation.setFavorite(true);
                                }
                            })
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
                                    ContextCompat.getColor(context, R.color.options_red))
                            .start();
                } else {
                    accommodation.setFavorite(false);
                    richPathHeart.setFillColor(ContextCompat.getColor(context, android.R.color.white));
                }
            }
        });
    }

    public void bindView(final Accommodation accommodation){
        /*itemView.findViewById(R.id.btn_book_now).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(context, ActivityBook.class);
                Bundle bundle = new Bundle();
                bundle.putString("code_own", accommodation.getCode());
                intent.putExtras(bundle);

                context.startActivity(intent);
            }
        });*/
    }
}