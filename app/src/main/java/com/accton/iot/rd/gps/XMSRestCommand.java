package com.accton.iot.rd.gps;

/**
 * Created by peter on 2016/8/9.
 */
import android.util.Log;

import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;
import retrofit2.http.GET;
// include below to assign specifically. Otherwise android.database.Observable will be selected.
import rx.Observable;

import okhttp3.OkHttpClient;
//import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

public class XMSRestCommand {
    private final String TAG = "XMSRestCommand";
    private final boolean DEBUG = true;
    private final boolean DEBUG_DETAIL = false;

    private Retrofit mRetrofit;
    private RestfulService mRestfulService;

    private int mPort;
    private final String DEFAULT_URL = "10.9.3.194";

    private final int DEFAULT_TIMEOUT_CONNECTION = 60;
    private final int DEFAULT_TIMEOUT_READ = 60;

    //private OkHttpClient mHttpClient = new OkHttpClient();
    private OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private OkHttpClient mHttpClient = null;
    private XMSRestInterceptor mInterceptor = new XMSRestInterceptor();

    private interface RestfulService {
        @GET("/System/info")
        Observable<SystemInfoResponse> getSystemInfo(); // RxJava form...
    }

    public XMSRestCommand(int port)
    {
        if (DEBUG)
            Log.d(TAG, "XMSRestCommand constructor");
        setPort(port);
    }

    public void setPort(int port)
    {
        if (DEBUG)
            Log.d(TAG, "setPort");
        mPort = port;

        //mInterceptor.registerBus(this);

        httpClient.connectTimeout(DEFAULT_TIMEOUT_CONNECTION, TimeUnit.SECONDS);
        httpClient.readTimeout(DEFAULT_TIMEOUT_READ, TimeUnit.SECONDS);
        mInterceptor.setAccessCode("000000");
        //httpClient.addInterceptor(mInterceptor);
        httpClient.interceptors().add(mInterceptor);
        mHttpClient = httpClient.build();

        //mHttpClient.setConnectTimeout(DEFAULT_TIMEOUT_CONNECTION, TimeUnit.SECONDS);
        //mHttpClient.setReadTimeout(DEFAULT_TIMEOUT_READ, TimeUnit.SECONDS);
        //mHttpClient.interceptors().add(mInterceptor);

        if(DEBUG_DETAIL)
        {
            mRetrofit = new Retrofit.Builder()
                    //.setEndpoint("http://" + DEFAULT_URL + ":" + mPort)
                    //.setClient(new OkClient(mHttpClient))
                    .baseUrl("http://" + DEFAULT_URL + ":" + mPort)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(mHttpClient)
                    //.setLogLevel(RestAdapter.LogLevel.FULL)
                    // .setLog(new AndroidLog(TAG))
                    .build();
        }
        else
        {
            if (DEBUG)
                Log.d(TAG, "full path: " + "http://" + DEFAULT_URL + ":" + mPort);
            mRetrofit = new Retrofit.Builder()
                    .baseUrl("http://" + DEFAULT_URL + ":" + mPort)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(mHttpClient)
                    .build();
        }

        mRestfulService = mRetrofit.create(RestfulService.class);
    }

    public Observable<SystemInfoResponse> getSystemInfo()
    {
        if (DEBUG)
            Log.d(TAG, "getSystemInfo call RestfulService");

        return mRestfulService.getSystemInfo();
    }
}