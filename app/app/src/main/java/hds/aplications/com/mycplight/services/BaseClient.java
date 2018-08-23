package hds.aplications.com.mycp.services;

import android.content.Context;

import hds.aplications.com.mycp.services.Util.Util;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by Miguel Gomez Leon on 11/24/15.
 * mgleonsc@gmail.com
 */
public class BaseClient {
    private static final String API_BASE_URL = "http://cbs.localsupper.com";
    public static final String API_RESPONSE_FORMAT = "json";
    public static final String API_KEY = "api-241c1e5ff6071fbec3505db53ce2d7a72";
    public static final String AUTH_TOKEN_NAME = "auth-token";

    public RestAdapter adapter;
    public Object service;
    public Context context;

    public BaseClient(Class<?> cls, Context context) {
        this.adapter  = this.getAdapter();
        this.service = adapter.create(cls);
        this.context = context;
    }

    protected final RestAdapter getAdapter() {
        return this.getBuilder()
                .build();
    }

    protected final RestAdapter.Builder getBuilder() {
        return new RestAdapter.Builder()
                .setConverter(new GsonConverter(Util.gsonWithDate()))
                .setEndpoint(API_BASE_URL);
                //.setClient(new OkClient(new OkHttpClient()));
    }

    /*public <S> S createService(Class<S> serviceClass)
    {
        return this.createService(serviceClass, false);
    }

    public <S> S createService(Class<S> serviceClass, boolean needTokenAuthentication)
    {
        RestAdapter.Builder builder = this.getBuilder();

        if(needTokenAuthentication) {
            final String authToken = BaseClient.AUTH_TOKEN_NAME;

            if (authToken != null) {
                builder.setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("Authorization", authToken);
                    }
                });
            }
        }

        RestAdapter adapter = builder.build();
        return adapter.create(serviceClass);
    }*/
}
