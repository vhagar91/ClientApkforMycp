package hds.aplications.com.mycp.view.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;

import java.util.ArrayList;
import java.util.List;

import hds.aplications.com.mycp.MyCP;
import hds.aplications.com.mycp.R;
import hds.aplications.com.mycp.models.Country;
import hds.aplications.com.mycp.models.User;
import hds.aplications.com.mycp.services.CountryClient;
import hds.aplications.com.mycp.services.UserClient;
import hds.aplications.com.mycp.view.others.OnScrollViewChangedListener;
import hds.aplications.com.mycp.view.others.ViewUtils;
import mgleon.common.com.LogUtils;
import mgleon.common.com.MessageToast;
import mgleon.common.com.services.Util.ProcessRetrofitError;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActivityRegister extends ActivityBase{

    private static final String TAG = LogUtils.makeLogTag(ActivityRegister.class);
    private Spinner spinner;

    @Override
    protected void initialize(){
        super.initialize();

        menuOptions.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.vector_arrow_prev));

        View viewItem = View.inflate(ActivityRegister.this, R.layout.content_register, null);
        this.addView(viewItem);

        this.activityTitle = getString(R.string.label_register);

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

        CountryClient countryClient = new CountryClient(context);
        Callback<List<Country>> callback = new Callback<List<Country>>(){
            @Override
            public void success(List<Country> countries, Response response){
                initSpinnerCountry(countries);
            }

            @Override
            public void failure(RetrofitError error){

            }
        };
        countryClient.getAll(callback);
    }
    @Override
    protected void onResume(){
        super.onResume();
        sTracker.setScreenName("Register");
        sTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
    @Override
    protected void bindActions(){
        super.bindActions();

        textViewBarTitle.setCompoundDrawables(null, null, null, null);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        findViewById(R.id.register).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!((CheckBox)findViewById(R.id.term_condi)).isChecked()){
                    MessageToast.showError(context, getString(R.string.acep_t_c));
                    return;
                }
                final String password = ((EditText)findViewById(R.id.password)).getText().toString();
                String rPassword = ((EditText)findViewById(R.id.r_password)).getText().toString();
                if(!password.equals(rPassword)){
                    MessageToast.showError(context, getString(R.string.pass_eq));
                    return;
                }
                String firstName = ((EditText)findViewById(R.id.first_name)).getText().toString();
                if(firstName.isEmpty()){
                    MessageToast.showError(context, getString(R.string.name_r));
                    return;
                }
                String lastName = ((EditText)findViewById(R.id.last_name)).getText().toString();
                final String email = ((EditText)findViewById(R.id.email)).getText().toString();
                if(email.isEmpty()){
                    MessageToast.showError(context, getString(R.string.email_r));
                    return;
                }
                final Country country = ((Country) spinner.getSelectedItem()).getIdRef() == null ? null : (Country) spinner.getSelectedItem();
                User xuser = new User(password, firstName, lastName, email, country);

                UserClient userClient = new UserClient(context);
                Callback<User> callback = new Callback<User>(){
                    @Override
                    public void success(User user, Response response){
                        loadingMask.hide();
                        sTracker.send(new HitBuilders.EventBuilder().setCategory("Register Succes")
                        .setAction("Click").build());
                        Bundle bundle = new Bundle();
                        bundle.putString("action", ActivityLogin.TAG);
                        bundle.putString("user", email);
                        bundle.putString("password", password);
                        MyCP.getApplication(context).goLogin(true, null);
                    }

                    @Override
                    public void failure(RetrofitError error){
                        loadingMask.hide();
                        ProcessRetrofitError.showError(error, context);
                    }
                };
                loadingMask.show(getString(R.string.c_user));
                userClient.register(xuser, callback);
            }
        });

        ViewUtils.replaceBackgroundbyImage(context, (ViewGroup) findViewById(R.id.contss), R.id.image_to_remplace, R.drawable.image_habana);
    }

    @Override
    protected void onMenuOptions(){
        //super.onMenuOptions();
        finish();
    }

    protected void initSpinnerCountry(List<Country> xCountries){
        spinner = findViewById(R.id.spinner_countries);

        List<Country> countries = new ArrayList<>();
        countries.add(new Country("[None]"));
        for(Country country : xCountries){
            countries.add(country);
        }

        ArrayAdapter<Country> adaptador = new ArrayAdapter<Country>(context, R.layout.simple_spinner_item_y, countries){
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                TextView textView = (TextView)v.findViewById(android.R.id.text1);
                if (position == 0) {
                    textView.setTextColor(ContextCompat.getColor(context, R.color.hint_loging));
                    textView.setText(R.string.search_country);
                }
                else {
                    textView.setTextColor(ContextCompat.getColor(context, android.R.color.white));
                }
                return v;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                Country countrySelected = (Country) spinner.getSelectedItem();

                View v = super.getDropDownView(position, convertView, parent);
                if (position == 0) {
                    ((CheckedTextView)v.findViewById(android.R.id.text1)).setTextColor(ContextCompat.getColor(context, R.color.hint_loging));
                }
                else if(countrySelected != null && position == this.getPosition(countrySelected)){
                    ((CheckedTextView)v.findViewById(android.R.id.text1)).setTextColor(ContextCompat.getColor(context, R.color.bar_background));
                }
                else {
                    ((CheckedTextView)v.findViewById(android.R.id.text1)).setTextColor(ContextCompat.getColor(context, android.R.color.black));
                }
                return v;
            }
        };
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adaptador);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id){
            }

            public void onNothingSelected(AdapterView<?> parent){}
        });
    }

    private void register(){
        User registerUser = new User();
    }
}