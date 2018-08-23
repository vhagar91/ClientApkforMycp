package hds.aplications.com.mycp.view.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.webkit.WebView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.richpath.RichPath;
import com.richpath.RichPathView;
import com.richpathanimator.AnimationBuilder;
import com.richpathanimator.AnimationListener;
import com.richpathanimator.RichPathAnimator;
import com.tobishiba.circularviewpager.library.BaseCircularViewPagerAdapter;
import com.tobishiba.circularviewpager.library.CircularViewPagerHandler;

import java.util.Date;
import java.util.List;

import hds.aplications.com.mycp.MyCP;
import hds.aplications.com.mycp.R;
import hds.aplications.com.mycp.helpers.SAppData;
import hds.aplications.com.mycp.models.Accommodation;
import hds.aplications.com.mycp.models.Answer;
import hds.aplications.com.mycp.models.User;
import hds.aplications.com.mycp.services.AccommodationClient;
import hds.aplications.com.mycp.services.UserClient;
import hds.aplications.com.mycp.view.others.OnScrollViewChangedListener;
import hds.aplications.com.mycp.view.fragments.FragmentDetail;
import hds.aplications.com.mycp.view.fragments.FragmentServices;
import hds.aplications.com.mycp.view.fragments.FragmentSlide;
import mgleon.common.com.DateUtils;
import mgleon.common.com.LogUtils;
import mgleon.common.com.services.Util.ProcessRetrofitError;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActivityDetail extends ActivityBase{

    private static final String TAG = LogUtils.makeLogTag(ActivityDetail.class);
    private Accommodation accommodation;

    private ViewPager viewPager;
    private PagerAdapter mPagerAdapter;

    FragmentDetail fragmentDetail;
    FragmentServices fragmentServices;

    private Date checkIn;
    private Date checkOut;

    private int intent = 0;

    @Override
    protected void initialize(){
        super.initialize();

        View viewItem = View.inflate(ActivityDetail.this, R.layout.content_detail, null);
        this.addView(viewItem);

        this.activityTitle = getString(R.string.detail);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.containsKey("code_own") && bundle.containsKey("id_own")){
            String code = bundle.getString("code_own");
            String idOwn = bundle.getString("id_own");
            this.activityTitle += " " + code;
            findAccommodationData(idOwn);
        }

        if(bundle != null && bundle.containsKey("check_in") && bundle.containsKey("check_out")){
            String scheckIn = bundle.getString("check_in");
            String scheckOut = bundle.getString("check_out");

            checkIn = DateUtils.generateDayFromString(scheckIn);
            checkOut = DateUtils.generateDayFromString(scheckOut);
        }

        /*********/
        ScrollView scrollView = (ScrollView) findViewById(R.id.scroll_view);
        OnScrollViewChangedListener onScrollViewChangedListener = new OnScrollViewChangedListener(scrollView){
            @Override
            public void onHide(){
                hideFabBottom();
            }

            @Override
            public void onShow(){
                showFabBottom();
            }
        };
        scrollView.getViewTreeObserver().addOnScrollChangedListener(onScrollViewChangedListener);
        /********/
    }
    @Override
    protected void onResume(){
        super.onResume();
        sTracker.setScreenName("Accomodation Details");
        sTracker.send(new HitBuilders.ScreenViewBuilder().build());
        menuOptions.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.vector_arrow_prev));

    }
    @Override
    protected void onMenuOptions(){
        //super.onMenuOptions();
        finish();
    }
    private void findAccommodationData(final String idOwn){
        //findViewById(R.id.content_all).setVisibility(View.INVISIBLE);
        loadingMask.show(getString(R.string.loading_accomodations_data));
        AccommodationClient accommodationClient = new AccommodationClient(context);
        Callback<Accommodation> accommodationCallback = new Callback<Accommodation>(){
            @Override
            public void success(Accommodation accommodationx, Response response){
                loadingMask.hide();
                accommodation = accommodationx;

                /*View view = findViewById(R.id.skeleton);
                ViewGroup viewParent = (ViewGroup) view.getParent();
                if(viewParent != null){
                    viewParent.removeView(view);
                }*/

                findViewById(R.id.content_all).setVisibility(View.VISIBLE);

                initView();
                initViewPager();
                showDetail();
            }

            @Override
            public void failure(RetrofitError error){
                ProcessRetrofitError.showError(error, context);

                if(intent <= 3){
                    intent++;
                    findAccommodationData(idOwn);
                }
                else {
                    finish();
                }
                //loadingMask.hide();

            }
        };
        accommodationClient.getAccommodation(idOwn, accommodationCallback);
    }

    private void initView(){
        RichPathView richViewFavorite = (RichPathView) findViewById(R.id.rich_view_favorite);
        initOnClickFavorite(accommodation, richViewFavorite);
        final User user = SAppData.getInstance().getUser();
        if(!accommodation.getInmediateBooking()){
            findViewById(R.id.inmediate_booking).setVisibility(View.GONE);
        }

        ((TextView)findViewById(R.id.category)).setText(accommodation.getAccommodationCategory().getLocaleName(user.getLangCode()));

        //((TextView)findViewById(R.id.description_1)).setText(accommodation.getDescription());
        WebView webViewDesc = (WebView) findViewById(R.id.description_1);
        String text;
        //text = "<html><body style=\"margin: 0; padding: 0\"><p align=\"justify\" style=\"margin: 0; padding: 0; color: #045078; overflow:hidden; white-space:nowrap; text-overflow:ellipsis\">";
        text = "<html><body style=\"margin: 0; padding: 0\"><p align=\"justify\" style=\"margin: 0; padding: 0; color: #045078\">";
        text+= accommodation.getDescription();
        text+= "</p></body></html>";
        webViewDesc.loadData(text, "text/html; charset=utf-8", "UTF-8");
        webViewDesc.setBackgroundColor(Color.TRANSPARENT);


        findViewById(R.id.btn_close_description).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                findViewById(R.id.frame_description).setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void bindActions(){
        super.bindActions();

        findViewById(R.id.btn_prev).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                prevAction();
            }
        });
        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                nextAction();
            }
        });

        textViewBarTitle.setCompoundDrawables(null, null, null, null);

        /*ScrollView scrollView = (ScrollView) findViewById(R.id.scroll_view);*/
        findViewById(R.id.action_map).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                goToMap();
            }
        });

        findViewById(R.id.action_galery).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                SAppData.getInstance().accommodation = accommodation;
                Intent intent = new Intent(context, ActivityGalery.class);
                Bundle bundle = new Bundle();
                bundle.putLong("id_own", accommodation.getIdRef());
                bundle.putString("code_own", accommodation.getCode());
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        findViewById(R.id.book_now).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //MyCP.getApplication(context).bookNow(accommodation);
                MyCP.getApplication(context).bookNow(null, accommodation, checkIn, checkOut);
            }
        });
    }

    private void initOnClickFavorite(final Accommodation accommodation, RichPathView richViewFavorite) {
        boolean favorite = accommodation.isFavorite();

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

    private void showDetail(){
        if(fragmentDetail == null){
            fragmentDetail = new FragmentDetail();
            fragmentDetail.setAccommodation(accommodation);
            fragmentDetail.setListener(new FragmentDetail.Listener(){
                @Override
                public void seeAllServicesAction(){
                    showServices();
                }

                @Override
                public void showDesc(){
                    findViewById(R.id.frame_description).setVisibility(View.VISIBLE);
                }
            });
            getSupportFragmentManager().beginTransaction().add(R.id.container_fragment, fragmentDetail).commit();
        }
        else {
            fragmentDetail.show();
        }
    }

    private void showServices(){
        if(fragmentServices == null){
            fragmentServices = new FragmentServices();
            fragmentServices.setRooms(accommodation.getRooms());
            fragmentServices.setListener(new FragmentServices.Listener(){
                @Override
                public void closeAction(){
                    showDetail();
                }
            });
            getSupportFragmentManager().beginTransaction().add(R.id.container_fragment, fragmentServices).commit();
        }
        else {
            fragmentServices.show();
        }
    }

    private void goToMap(){
        SAppData.getInstance().accommodation = accommodation;
        Intent intent = new Intent(context, ActivityMap.class);
        Bundle bundle = new Bundle();
        bundle.putLong("id_own", accommodation.getIdRef());
        bundle.putString("code_own", accommodation.getCode());
        intent.putExtras(bundle);

        startActivity(intent);
    }

    private static final float MIN_SCALE = 0.75f;

    private void initViewPager(){
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setVisibility(View.VISIBLE);
        mPagerAdapter = new FragmentAdapterSlide(context, getSupportFragmentManager(), accommodation.getPhotos());
        viewPager.setAdapter(mPagerAdapter);
        CircularViewPagerHandler circularViewPagerHandler = new CircularViewPagerHandler(viewPager);
        viewPager.addOnPageChangeListener(circularViewPagerHandler);
        viewPager.setPageTransformer(false, new ViewPager.PageTransformer(){
            @Override
            public void transformPage(View view, float position){
                if(position <= 0f){
                    view.setTranslationX(0f);
                    view.setScaleX(1f);
                    view.setScaleY(1f);
                }
                else if(position <= 1f){
                    final float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
                    view.setAlpha(1 - position);
                    view.setPivotY(0.5f * view.getHeight());
                    view.setTranslationX(view.getWidth() * -position);
                    view.setScaleX(scaleFactor);
                    view.setScaleY(scaleFactor);
                }
            }
        });
    }

    private class FragmentAdapterSlide extends BaseCircularViewPagerAdapter<String>{
        private final Context mContext;

        public FragmentAdapterSlide(final Context context, final FragmentManager fragmentManager, final List<String> memes) {
            super(fragmentManager, memes);
            mContext = context;
        }

        @Override
        protected Fragment getFragmentForItem(final String meme) {
            //return MemeViewPagerItemFragment.instantiateWithArgs(mContext, meme);
            Fragment fragment = FragmentSlide.create(meme, false);
            return fragment;
        }
    }

    public void prevAction(){
        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
    }

    public void nextAction(){
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        LogUtils.LOGI(TAG, "config changed");
        super.onConfigurationChanged(newConfig);

        int orientation = newConfig.orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT)
            LogUtils.LOGI(TAG, "Portrait");
        else if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            LogUtils.LOGI(TAG, "Landscape");
        else
            LogUtils.LOGI(TAG, "other: " + orientation);
    }

    @Override
    public void onBackPressed(){
        if(fragmentServices != null && fragmentServices.isVisiblee()){
            fragmentServices.hide();
            fragmentDetail.show();
            return;
        }

        super.onBackPressed();
    }
}