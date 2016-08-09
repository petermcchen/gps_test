package com.accton.iot.rd.gps;

import android.annotation.TargetApi;
import android.app.Application;
import android.graphics.Point;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.util.DisplayMetrics;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by mc_chen on 2016/1/11.
 */
public class MainApplication extends Application {
    private static String TAG = "MainApplication";
    private static boolean DEBUG = true;

    private static MainApplication mInstance = null;

    private static int mWidth;
    private static int mHeight;
    private static int mRawWidth;
    private static int mRawHeight;
    private static float mDensity;
    private static int mRotation;

    private static WindowManager mWindowManager = null;
    private static DisplayMetrics mMetrics = null;


    //
    // Private
    //
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static void getRealDisplayData()
    {
        mRawWidth = 0;
        mRawHeight = 0;

        if(mWindowManager == null)
            return;

        Point point = new Point();

        mWindowManager.getDefaultDisplay().getRealSize(point);

        mRawWidth = point.x;
        mRawHeight = point.y;

        if(DEBUG)
            Log.d(TAG, "getRealDisplayData width:" + mRawWidth + " height:" + mRawHeight);
    }

    private static void getRawDisplayData()
    {
        mRawWidth = 0;
        mRawHeight = 0;

        if(mWindowManager == null)
            return;

        try
        {
            Display display = mWindowManager.getDefaultDisplay();

            mRotation = display.getRotation();

            Method mGetRawH = Display.class.getMethod("getRawHeight");
            Method mGetRawW = Display.class.getMethod("getRawWidth");

            mRawWidth = (Integer) mGetRawW.invoke(display);
            mRawHeight = (Integer) mGetRawH.invoke(display);

            if(DEBUG)
                Log.d(TAG, "getRawDisplayData width:" + mRawWidth + " height:" + mRawHeight);
        }
        catch(NoSuchMethodException e)
        {
            //e.printStackTrace();
        }
        catch(IllegalAccessException e)
        {
            //e.printStackTrace();
        }
        catch(InvocationTargetException e)
        {
            //e.printStackTrace();
        }
    }


    //
    // Public
    //
    @Override
    public void onCreate() {
        if (DEBUG)
            Log.d(TAG, "onCreate: " + this);

        super.onCreate();
    }

    @Override
    public void onLowMemory()
    {
        if(DEBUG)
            Log.d(TAG, "onLowMemory");

        super.onLowMemory();
    }

    @Override
    public void onTerminate()
    {
        if(DEBUG)
            Log.d(TAG, "onTerminate");

        mInstance = null;

        super.onTerminate();
    }

    public static String getApplicationName()
    {
        if(mInstance == null)
            return null;

        return mInstance.getPackageName();
    }

    //
    // Check System Version
    //
    public static boolean isGingerbreadOrBelow()
    {
        if(VERSION.SDK_INT <= 10)
            return true;

        return false;
    }

    public static boolean isGingerbreadOrAbove()
    {
        if(VERSION.SDK_INT >= 9)
            return true;

        return false;
    }

    public static boolean isFroyoOrGingerbread()
    {
        if(VERSION.SDK_INT == 8 || VERSION.SDK_INT == 9 || VERSION.SDK_INT == 10)
            return true;

        return false;
    }

    public static boolean isIceCreamOrBelow()
    {
        if(VERSION.SDK_INT <= 15)
            return true;

        return false;
    }

    public static boolean isIceCreamOrAbove()
    {
        if(VERSION.SDK_INT >= 14)
            return true;

        return false;
    }

    public static boolean isJellyBeanOrAbove()
    {
        if(VERSION.SDK_INT >= 17)
            return true;

        return false;
    }


    //
    // Metrics
    //
    public static int getDisplayWidth()
    {
        return mWidth;
    }

    public static int getDisplayHeight()
    {
        return mHeight;
    }

    public static int getScreenWidth()
    {
        if(mRawWidth > 0)
            return mRawWidth;

        return mWidth;
    }

    public static int getScreenHeight()
    {
        if(mRawHeight > 0)
            return mRawHeight;

        return mHeight;
    }

    public static float getDensity()
    {
        return mDensity;
    }

    public static int getRotation()
    {
        return mRotation;
    }

    public static void getSystemMetrics()
    {
        if(isJellyBeanOrAbove())
        {
            getRealDisplayData();
        }
        else if(isIceCreamOrAbove())
        {
            getRawDisplayData();
        }

        if(mWindowManager == null)
            return;

        mWindowManager.getDefaultDisplay().getMetrics(mMetrics);

        mRotation = mWindowManager.getDefaultDisplay().getRotation();

        mWidth = mMetrics.widthPixels;
        mHeight = mMetrics.heightPixels;
        mDensity = mMetrics.density;

        if(DEBUG)
            Log.d(TAG, "getSystemMetrics width:" + mWidth + " height:" + mHeight + " density:" + mDensity + " orientation:" + mRotation);
    }
}
