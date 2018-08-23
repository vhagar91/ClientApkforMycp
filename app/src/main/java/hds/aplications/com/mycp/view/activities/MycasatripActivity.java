package hds.aplications.com.mycp.view.activities;


import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;

import java.util.List;
import hds.aplications.com.mycp.R;
import hds.aplications.com.mycp.helpers.SAppData;
import hds.aplications.com.mycp.models.Reservation;
import hds.aplications.com.mycp.models.ReservationDetail;
import hds.aplications.com.mycp.models.User;
import hds.aplications.com.mycp.repositories.ReservationRepository;
import hds.aplications.com.mycp.sync.DataSynchronizer;
import hds.aplications.com.mycp.view.adapters.ReservationAdapter;
import hds.aplications.com.mycp.view.components.LoadMask;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
/**
 * Created by Orlando Viera on 15/05/2018.
 * mgleonsc@gmail.com
 */
public class MycasatripActivity extends ActivityBase {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;


    private List<ReservationDetail> reservationsDetail;
    private List<Reservation> reservations;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    @Override
    protected void initialize(){
        super.initialize();

        this.activityTitle = getString(R.string.reservation_details);

        View viewItem = View.inflate(MycasatripActivity.this, R.layout.activity_mycasatrip, null);

        textViewBarTitle.setCompoundDrawables(null, null, null, null);
        this.addView(viewItem);
        fabMenu.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);






    }
    @Override
    protected void onResume(){
        super.onResume();
        sTracker.setScreenName("MyCasa Trip");
        sTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
    public void SyncData(View v){

        final Callback<User> callBackSync = new Callback<User>() {
            @Override
            public void success(User user, Response response) {

                SAppData.getInstance().setUser(user);
                MycasatripActivity.this.finish();
                Intent intent = new Intent(MycasatripActivity.this, MycasatripActivity.class);
                startActivity(intent);
            }

            @Override
            public void failure(RetrofitError retrofitError) {


            }
        };

        DataSynchronizer synchronizer = new DataSynchronizer(loadingMask);
        synchronizer.synchronizeReservationsData(context, callBackSync);

    }


    /**
     * A placeholder for the reservations .
     */
    public static class BookedFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        protected static LoadMask loadingMask;
        private static final String ARG_SECTION_NUMBER = "section_number";


        private static List<ReservationDetail> reservationsDetail;
        private static List<Reservation> reservations;

        public BookedFragment() {

        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */

        public static BookedFragment newInstance(int sectionNumber) {


            BookedFragment fragment = new BookedFragment();

            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                                 Bundle savedInstanceState) {


            loadingMask = new LoadMask(getActivity());
            loadingMask.show();

            final View rootView = inflater.inflate(R.layout.fragment_mycasatrip, container, false);



            User currentUser = SAppData.getInstance().getUser();
            ReservationRepository repository = new ReservationRepository();
            List<Reservation> items = repository.getReservationsByStatus(currentUser,5);


            if(items.size() > 0) {
                // Sets the data behind this ListView
                loadingMask.hide();
                ListView listView = (ListView) rootView.findViewById(R.id.content_book_room);
                listView.setAdapter(new ReservationAdapter(getContext(), items, listView));
            }
            else{
                final Callback<User> callBackSync = new Callback<User>() {
                    @Override
                    public void success(User user, Response response) {
                        loadingMask.hide();
                        SAppData.getInstance().setUser(user);
                        User currentUser = SAppData.getInstance().getUser();
                        ReservationRepository repository = new ReservationRepository();
                        List<Reservation> items = repository.getReservationsByStatus(currentUser,5);
                        if(items.size() > 0) {
                            // Sets the data behind this ListView
                            loadingMask.hide();

                            ListView listView = (ListView) rootView.findViewById(R.id.content_book_room);
                            listView.setAdapter(new ReservationAdapter(getContext(), items, listView));
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        loadingMask.hide();
                    }
                };

                DataSynchronizer synchronizer = new DataSynchronizer(loadingMask);
                synchronizer.synchronizeReservationsData(getContext(), callBackSync);
//                Callback<List<Reservation>> callback = new Callback<List<Reservation>>(){
//                    @Override
//                    public void success(List<Reservation> xreservations, Response response){
//                        loadingMask.hide();
//
//                        reservations = xreservations;
//
//                        reservationsDetail = new ArrayList<>();
//                        for(Reservation reservation : xreservations){
//                            List<ReservationDetail> reservationDetails = reservation.getReservationDetails();
//                            for(ReservationDetail reservationDetail : reservationDetails){
//                                reservationDetail.getRoom().setAccommodation(reservation.getAccommodation());
//                                reservationsDetail.add(reservationDetail);
//
//                            }
//                        }
//                        for(ReservationDetail reservationDetail : reservationsDetail){
//                            reservationDetail.setActive(true);
//                            FragmentReservation fragmentPaymentRoom = new FragmentReservation();
//                            fragmentPaymentRoom.setReservationDetail(reservationDetail);
//                            fragmentPaymentRoom.setListenerBook(listenerPaymentRoom);
//
//
//
//
//                            LinearLayout content = (LinearLayout) rootView.findViewById(R.id.content_book_room);
//
//                            getActivity().getSupportFragmentManager().beginTransaction().add(content.getId(), fragmentPaymentRoom).commit();
//                        }
//
//
//
//
//                    }
//
//                    @Override
//                    public void failure(RetrofitError error){
//                        loadingMask.hide();
//                    }
//                };
//
//                reservationClient.getReservationsBooked(callback);
            }


            return rootView;


        }

    }
    public static class PendingFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        protected static LoadMask loadingMask;
        private static final String ARG_SECTION_NUMBER = "section_number";


        private static List<ReservationDetail> reservationsDetail;
        private static List<Reservation> reservations;

        public PendingFragment() {

        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */

        public static PendingFragment newInstance(int sectionNumber) {


            PendingFragment fragment = new PendingFragment();

            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                                 Bundle savedInstanceState) {
            loadingMask = new LoadMask(getActivity());
            loadingMask.show();
            final View rootView = inflater.inflate(R.layout.fragment_mycasatrip2, container, false);

            User currentUser = SAppData.getInstance().getUser();
            ReservationRepository repository = new ReservationRepository();

            List<Reservation> items = repository.getReservationsByStatus(currentUser,1);


            if(items.size() > 0) {
                // Sets the data behind this ListView
                loadingMask.hide();
                ListView listView = (ListView) rootView.findViewById(R.id.content_book_room2);
                listView.setAdapter(new ReservationAdapter(getContext(), items, listView));

            }
            else{
                final Callback<User> callBackSync = new Callback<User>() {
                    @Override
                    public void success(User user, Response response) {
                        loadingMask.hide();
                        SAppData.getInstance().setUser(user);
                        User currentUser = SAppData.getInstance().getUser();
                        ReservationRepository repository = new ReservationRepository();
                        List<Reservation> items = repository.getReservationsByStatus(currentUser,1);
                        if(items.size() > 0) {
                            // Sets the data behind this ListView
                            loadingMask.hide();
                            ListView listView = (ListView) rootView.findViewById(R.id.content_book_room2);
                            listView.setAdapter(new ReservationAdapter(getContext(), items, listView));
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        loadingMask.hide();
                    }
                };

                DataSynchronizer synchronizer = new DataSynchronizer(loadingMask);
                synchronizer.synchronizeReservationsData(getContext(), callBackSync);
//                Callback<List<Reservation>> callback = new Callback<List<Reservation>>(){
//                    @Override
//                    public void success(List<Reservation> xreservations, Response response){
//                        loadingMask.hide();
//
//                        reservations = xreservations;
//
//                        reservationsDetail = new ArrayList<>();
//                        for(Reservation reservation : xreservations){
//                            List<ReservationDetail> reservationDetails = reservation.getReservationDetails();
//                            for(ReservationDetail reservationDetail : reservationDetails){
//                                reservationDetail.getRoom().setAccommodation(reservation.getAccommodation());
//                                reservationsDetail.add(reservationDetail);
//
//                            }
//                        }
//                        for(ReservationDetail reservationDetail : reservationsDetail){
//                            reservationDetail.setActive(true);
//                            FragmentReservation fragmentPaymentRoom = new FragmentReservation();
//                            fragmentPaymentRoom.setReservationDetail(reservationDetail);
//                            fragmentPaymentRoom.setListenerBook(listenerPaymentRoom);
//
//
//
//
//                            LinearLayout content = (LinearLayout) rootView.findViewById(R.id.content_book_room);
//
//                            getActivity().getSupportFragmentManager().beginTransaction().add(content.getId(), fragmentPaymentRoom).commit();
//                        }
//
//
//
//
//                    }
//
//                    @Override
//                    public void failure(RetrofitError error){
//                        loadingMask.hide();
//                    }
//                };
//
//                reservationClient.getReservationsBooked(callback);
            }


            return rootView;

        }

    }
    public static class AvailableFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        protected static LoadMask loadingMask;
        private static final String ARG_SECTION_NUMBER = "section_number";


        private static List<ReservationDetail> reservationsDetail;
        private static List<Reservation> reservations;

        public AvailableFragment() {

        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */

        public static AvailableFragment newInstance(int sectionNumber) {


            AvailableFragment fragment = new AvailableFragment();

            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                                 Bundle savedInstanceState) {
            loadingMask = new LoadMask(getActivity());
            loadingMask.show();
            final View rootView = inflater.inflate(R.layout.fragment_mycasatrip3, container, false);

            User currentUser = SAppData.getInstance().getUser();
            ReservationRepository repository = new ReservationRepository();
            List<Reservation> items = repository.getReservationsByStatus(currentUser,2);


            if(items.size() > 0) {
                // Sets the data behind this ListView
                loadingMask.hide();

                reservations = items;

                ListView listView = (ListView) rootView.findViewById(R.id.content_book_room3);
                listView.setAdapter(new ReservationAdapter(getContext(), items, listView));
            }
            else{
                final Callback<User> callBackSync = new Callback<User>() {
                    @Override
                    public void success(User user, Response response) {
                        loadingMask.hide();
                        SAppData.getInstance().setUser(user);
                        User currentUser = SAppData.getInstance().getUser();
                        ReservationRepository repository = new ReservationRepository();
                        List<Reservation> items = repository.getReservationsByStatus(currentUser,2);
                        if(items.size() > 0) {
                            // Sets the data behind this ListView
                            loadingMask.hide();

                            ListView listView = (ListView) rootView.findViewById(R.id.content_book_room3);
                            listView.setAdapter(new ReservationAdapter(getContext(), items, listView));
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        loadingMask.hide();
                    }
                };

                DataSynchronizer synchronizer = new DataSynchronizer(loadingMask);
                synchronizer.synchronizeReservationsData(getContext(), callBackSync);
//                Callback<List<Reservation>> callback = new Callback<List<Reservation>>(){
//                    @Override
//                    public void success(List<Reservation> xreservations, Response response){
//                        loadingMask.hide();
//
//                        reservations = xreservations;
//
//                        reservationsDetail = new ArrayList<>();
//                        for(Reservation reservation : xreservations){
//                            List<ReservationDetail> reservationDetails = reservation.getReservationDetails();
//                            for(ReservationDetail reservationDetail : reservationDetails){
//                                reservationDetail.getRoom().setAccommodation(reservation.getAccommodation());
//                                reservationsDetail.add(reservationDetail);
//
//                            }
//                        }
//                        for(ReservationDetail reservationDetail : reservationsDetail){
//                            reservationDetail.setActive(true);
//                            FragmentReservation fragmentPaymentRoom = new FragmentReservation();
//                            fragmentPaymentRoom.setReservationDetail(reservationDetail);
//                            fragmentPaymentRoom.setListenerBook(listenerPaymentRoom);
//
//
//
//
//                            LinearLayout content = (LinearLayout) rootView.findViewById(R.id.content_book_room);
//
//                            getActivity().getSupportFragmentManager().beginTransaction().add(content.getId(), fragmentPaymentRoom).commit();
//                        }
//
//
//
//
//                    }
//
//                    @Override
//                    public void failure(RetrofitError error){
//                        loadingMask.hide();
//                    }
//                };
//
//                reservationClient.getReservationsBooked(callback);
            }


            return rootView;


        }

    }
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment =null;
            switch (position) {
                case 0:
                    fragment = new PendingFragment();
                    break;
                case 1:
                    fragment = new AvailableFragment();
                    break;
                case 2:
                    fragment = new BookedFragment();
                    break;          }
            return fragment;
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.booked_name);
                case 1:
                    return getString(R.string.pendig_name);
                case 2:
                    return getString(R.string.available_name);
            }
            return null;
        }
    }
}
