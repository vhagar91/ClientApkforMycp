package hds.aplications.com.mycp.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.activeandroid.Model;
import com.google.android.gms.analytics.HitBuilders;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hds.aplications.com.mycp.MyCP;
import hds.aplications.com.mycp.R;
import hds.aplications.com.mycp.helpers.SAppData;
import hds.aplications.com.mycp.map.downloader.AbstractMap;
import hds.aplications.com.mycp.map.marker.CustomMarker;
import hds.aplications.com.mycp.map.ui.MapDownloaderFragment;
import hds.aplications.com.mycp.map.ui.MapFragment;
import hds.aplications.com.mycp.map.ui.MapFragmentListener;
import hds.aplications.com.mycp.map.util.event.MapSuccessfulDownloadedEvent;
import hds.aplications.com.mycp.models.Accommodation;
import hds.aplications.com.mycp.models.Reservation;
import hds.aplications.com.mycp.repositories.AccommodationRepository;
import hds.aplications.com.mycp.repositories.ReservationRepository;
import hds.aplications.com.mycp.view.components.ViewPagerHelp;
import hds.aplications.com.mycp.view.components.LoadMask;
import hds.aplications.com.mycp.view.fragments.FragmentSlidePageHelp;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityMap extends AppCompatActivity {

    private Fragment mCurrentFragment;
    LoadMask loadingMask;
    private RelativeLayout frameHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        if (savedInstanceState == null) {
            updateContent(false);
        }

        loadingMask = new LoadMask(this);
        this.frameHelp = (RelativeLayout) this.findViewById(R.id.frame_help);

        createAllPoint();
        initViewPagerHelp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyCP.getApplication(this).registerOttoBus(this);




    }

    @Override
    protected void onPause() {
        super.onPause();
        MyCP.getApplication(this).unregisterOttoBus(this);
    }

    @Subscribe
    public void onMapDownloaded(MapSuccessfulDownloadedEvent event) {
        findViewById(android.R.id.content).post(new Runnable() {
            @Override
            public void run() {
                updateContent(true);
            }
        });
    }

    private void updateContent(boolean force) {
        if (mCurrentFragment == null || force) {

            if (AbstractMap.instance().exist(this)){
                mCurrentFragment = new MapFragment();
                ((MapFragment)mCurrentFragment).mapFragmentListener = new MapFragmentListener(){
                    @Override
                    public boolean onCreate() {
                        //User user = (User)(new UserRepository().getById(SAppData.getInstance().getUser().getId()));
                        //List<Reservation> reservations = (new ReservationRepository().getReservationsByUser(user));
                        SAppData sAppData = SAppData.getInstance();
                        List<Model> accomodations = new ArrayList<>();
                        Reservation firstReservation = null;

                        Bundle bundle = ActivityMap.this.getIntent().getExtras();
                        if(bundle != null && bundle.containsKey("code_own") && SAppData.getInstance().accommodation != null && bundle.getString("code_own").equals(SAppData.getInstance().accommodation.getCode())){
                            Model model = SAppData.getInstance().accommodation;
                            SAppData.getInstance().accommodation = null;
                            accomodations = new ArrayList<>(Arrays.asList(model));
                        }
                        else if(sAppData.getUser() != null){
                            accomodations = AccommodationRepository.getAcomodationReservedUser(sAppData.getUser());
                            List<Reservation> reservations = (new ReservationRepository().getReservationsByUser(sAppData.getUser()));
                            firstReservation =  (reservations.size() > 0) ? (reservations.get(0)) : null;
                        }

                        boolean centerMap = false;
                        for(Model accomodation: accomodations){
                            CustomMarker customMarker = ((MapFragment)mCurrentFragment).addMarker(((Accommodation)accomodation), R.mipmap.home);
                            if(!centerMap && (firstReservation == null || (accomodation.getId() == firstReservation.getAccommodation().getId()))){
                                    ((MapFragment)mCurrentFragment).centerMapTo(customMarker.model.getLocation());
                                    centerMap = true;
                            }
                        }

                        return false;
                    }

                    @Override
                    public boolean onInstructions(Bundle bundle) {
                        Intent intent = new Intent(ActivityMap.this, ActivityInstructions.class);
                        intent.putExtras(bundle);

                        SAppData.getInstance().setAppCompatActivity(ActivityMap.this);

                        ActivityMap.this.startActivity(intent);
                        return true;
                    }
                };
            }
            else{
                mCurrentFragment = new MapDownloaderFragment();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, mCurrentFragment).commit();
        }
    }

    public void backAction(View view) {
        this.finish();
        SAppData.getInstance().setInstructions(null);
        SAppData.getInstance().setAppCompatActivity(null);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        SAppData.getInstance().setInstructions(null);
        SAppData.getInstance().setAppCompatActivity(null);
    }

    @Override
    protected void attachBaseContext(Context newBase){
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void reservationsList(View view) {
        //ActivityMap.this.startActivity(new Intent(ActivityMap.this, ActivityReservations.class));
    }

    public Fragment getmCurrentFragment() {
        return mCurrentFragment;
    }

    public void clearDistance(){
        ((MapFragment)mCurrentFragment).clearDistance();
    }

    public void helpOpenAction(View view){
        ((MapFragment)mCurrentFragment).setEnable(false);
        this.findViewById(R.id.frame_help_back).setVisibility(View.VISIBLE);
        this.frameHelp.setVisibility(View.VISIBLE);
    }

    public void helpCloseAction(View view) {
        ((MapFragment)mCurrentFragment).setEnable(true);
        this.findViewById(R.id.frame_help_back).setVisibility(View.INVISIBLE);
        this.frameHelp.setVisibility(View.INVISIBLE);
    }
    /* ************************************************************************************************* */

    //private final static int ITEMS_MAX_VALUE = 100;
    public static final int NUM_PAGES = 7;
    private ViewPagerHelp mViewPagerHelp;
    private PagerAdapter mPagerAdapter;
    private static int currentPage = -1;

    private void initViewPagerHelp(){
        // Instantiate a CustomViewPager and a PagerAdapter.
        mViewPagerHelp = (ViewPagerHelp) findViewById(R.id.view_pager_help);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mViewPagerHelp.setAdapter(mPagerAdapter);
        mViewPagerHelp.addOnPageChangeListener(new ViewPagerHelp.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                position = position % NUM_PAGES;
                navegate(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //position = position % NUM_PAGES;
            Fragment fragment = FragmentSlidePageHelp.create(position);
            mViewPagerHelp.addFragment(fragment, position);
            return fragment;
        }

        @Override
        public int getCount() {
            return /*ITEMS_MAX_VALUE * */NUM_PAGES;
        }
    }

    private View createPoint(boolean orange){
        LayoutInflater inflater = (LayoutInflater) this.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(orange){
            return inflater.inflate(R.layout.point_orange, null);
        }
        return inflater.inflate(R.layout.point_white, null);
    }

    private void createAllPoint(){
        LinearLayout allPoint = (LinearLayout)this.findViewById(R.id.layout_all_pointx);
        allPoint.removeAllViews();
        for (int i = 0; i < NUM_PAGES; i++) {
            View view = createPoint((i != 0) ? (true) : (false));
            allPoint.addView(view);
        }
        currentPage = 0;
    }

    public void navegate(int position){
        LinearLayout allPoint = (LinearLayout)this.findViewById(R.id.layout_all_pointx);

        if (currentPage != -1){
            allPoint.removeViewAt(currentPage);
            allPoint.addView(createPoint(true), currentPage);
        }

        allPoint.removeViewAt(position);
        allPoint.addView(createPoint(false), position);

        currentPage = position;
    }

    public void prevAction(View view) {
        int p = currentPage - 1;
        p = (p == -1) ? (NUM_PAGES-1) : (p);
        mViewPagerHelp.setCurrentItem(p);
    }

    public void nextAction(View view) {
        int p = currentPage + 1;
        p = (p == NUM_PAGES) ? (0) : (p);
        mViewPagerHelp.setCurrentItem(p);
    }
    /* ************************************************************************************************* */
}
