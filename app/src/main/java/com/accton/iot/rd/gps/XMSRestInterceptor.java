package com.accton.iot.rd.gps;

/**
 * Created by peter on 2016/8/9.
 */
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
//import com.squareup.okhttp.Interceptor;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.Response;

public class XMSRestInterceptor implements Interceptor {
    private final String TAG = "XMSRestInterceptor";
    private final boolean DEBUG = true;

    private final String USER_TOKEN_HEADER = "SMC_UT";
    private final String ACCESS_HEADER = "SMC_AC";

    private final String ARM= "ARM";
    private final String ARM_STATUS_HEADER = "ARMStatus";

    private final String DEFAULT_ACCESS_CODE_HEADER = "DefaultAC";
    private final String DEFAULT_ACCESS_CODE = "1";

    //private Bus mInterceptorBus = new Bus(ThreadEnforcer.ANY);

    private String mUserToken;
    private String mAccessCode;// = DEFAULT_ACCESS_CODE;

    public void setUserToken(String usertoken)
    {
        if(DEBUG)
            Log.d(TAG, "setUserToken t:" + usertoken);

        mUserToken = usertoken;
    }

    public String getUserToken()
    {
        return mUserToken;
    }

    public void setAccessCode(String accessCode)
    {
        if(DEBUG)
            Log.d(TAG, "setAccessCode a:" + accessCode);

        mAccessCode = accessCode;
    }

    public String getAccessCode()
    {
        return mAccessCode;
    }

    public void registerBus(Object object)
    {
        if(DEBUG)
            Log.d(TAG, "registerBus o:" + object);

        //mInterceptorBus.register(object);
    }

    public void unregisterBus(Object object)
    {
        if(DEBUG)
            Log.d(TAG, "unregisterBus o:" + object);

        //mInterceptorBus.unregister(object);
    }

    @Override
    public Response intercept(Chain chain) throws IOException
    {
        if(DEBUG)
            Log.d(TAG, "intercept u:" + mUserToken + " a:" + mAccessCode);

        Request request = chain.request();

        Request.Builder requestBuilder = request.newBuilder();

        if(mUserToken!=null)
        {
            if(DEBUG)
                Log.d(TAG, "mUserToken!=null");
            requestBuilder.addHeader(USER_TOKEN_HEADER, mUserToken);
        }

        if(mAccessCode==null || mAccessCode.isEmpty() || mAccessCode.length()<=0)
        {
            if(DEBUG)
                Log.d(TAG, "mAccessCode==null");
        }
        else
        {
            if(DEBUG)
                Log.d(TAG, "mAccessCode!=null");
            requestBuilder.addHeader(ACCESS_HEADER, mAccessCode);
        }

        Response response = chain.proceed(requestBuilder.build());

        if(DEBUG)
            Log.d(TAG, "intercept h:" + response.headers());

        String armstatus = response.header(ARM_STATUS_HEADER);
        String access = response.header(DEFAULT_ACCESS_CODE_HEADER);

        if(DEBUG)
            Log.d(TAG, "intercept response a:" + armstatus + " a: " + access);

        if(armstatus!=null)
        {
            boolean isArm = armstatus.equals(ARM);

            //mInterceptorBus.post(new GuardianEvent(isArm));
        }

        if(access==null)
        {
            //mInterceptorBus.post(new AccessCodeDefaultEvent(false));
        }
        else
        {
            if(DEBUG)
                Log.d(TAG, "intercept code default");

            boolean codeDefault = access.equals(DEFAULT_ACCESS_CODE);

            //mInterceptorBus.post(new AccessCodeDefaultEvent(codeDefault));
        }

        return response;
    }
}