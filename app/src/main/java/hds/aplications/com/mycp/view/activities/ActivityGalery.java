package hds.aplications.com.mycp.view.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.tobishiba.circularviewpager.library.BaseCircularViewPagerAdapter;
import com.tobishiba.circularviewpager.library.CircularViewPagerHandler;

import java.util.List;

import hds.aplications.com.mycp.R;
import hds.aplications.com.mycp.helpers.SAppData;
import hds.aplications.com.mycp.models.Accommodation;
import hds.aplications.com.mycp.view.fragments.FragmentSlide;
import mgleon.common.com.LogUtils;

public class ActivityGalery extends ActivityBase{

    private static final String TAG = LogUtils.makeLogTag(ActivityGalery.class);
    private List<String> imagesUrl;
    private Accommodation accommodation;

    private ViewPager viewPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void initialize(){
        super.initialize();

        this.activityTitle = "Images ";
        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.containsKey("code_own") && SAppData.getInstance().accommodation != null && bundle.getString("code_own").equals(SAppData.getInstance().accommodation.getCode())){
            accommodation = SAppData.getInstance().accommodation;
            SAppData.getInstance().accommodation = null;
            imagesUrl = accommodation.getPhotos();

            this.activityTitle += accommodation.getCode();
        }

        View viewItem = View.inflate(ActivityGalery.this, R.layout.content_galery, null);
        this.addView(viewItem);
        initViewPager();
    }
    @Override
    protected void onResume(){
        super.onResume();
        sTracker.setScreenName("Galery");
        sTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
    private void initViewPager(){
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setVisibility(View.VISIBLE);
        mPagerAdapter = new FragmentAdapterSlide(context, getSupportFragmentManager(), imagesUrl);
        viewPager.setAdapter(mPagerAdapter);
        CircularViewPagerHandler circularViewPagerHandler = new CircularViewPagerHandler(viewPager);
        circularViewPagerHandler.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){}

            @Override
            public void onPageSelected(int position){
                int v = position + 1;
                if(v == imagesUrl.size() + 1){
                    v = 1;
                }
                else if(v == 0){
                    v = imagesUrl.size();
                }

                String p = String.valueOf(v);
                ((TextView)findViewById(R.id.page)).setText(p + "/" + imagesUrl.size());
            }

            @Override
            public void onPageScrollStateChanged(int state){}
        });
        viewPager.addOnPageChangeListener(circularViewPagerHandler);
        viewPager.setPageTransformer(false, new ViewPager.PageTransformer(){
            @Override
            public void transformPage(View view, float position){
                final float height = view.getHeight();
                final float width = view.getWidth();
                final float scale = min(position < 0 ? 1f : Math.abs(1f - position), 0.5f);

                view.setScaleX(scale);
                view.setScaleY(scale);
                view.setPivotX(width * 0.5f);
                view.setPivotY(height * 0.5f);
                float translationX = position < 0 ? width * position : -width * position * 0.25f;
                //noinspection ResourceType
                view.setTranslationX(translationX);
            }
        });
    }

    private static final float min(float val, float min) {
        return val < min ? min : val;
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
        findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
        findViewById(R.id.btn_prevv).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        textViewBarTitle.setCompoundDrawables(null, null, null, null);

        this.fabMenu.setVisibility(View.GONE);
        this.appBarLayout.setVisibility(View.GONE);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) this.relativeLayout.getLayoutParams();
        layoutParams.topMargin = 0;
        this.relativeLayout.setLayoutParams(layoutParams);
        ((TextView)findViewById(R.id.page)).setText("1/" + imagesUrl.size());

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
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
            Fragment fragment = FragmentSlide.create(meme, true);
            return fragment;
        }
    }

    public void prevAction(){
        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
    }

    public void nextAction(){
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
    }
}