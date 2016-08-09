package com.accton.iot.rd.gps;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class SystemInfo
{
    private final String TAG = "SystemInfo";
    private final boolean DEBUG = false;

    private String mSerialNumber;
    private String mVersion;
    private String mMacAddress;

    private float mHumidity = 0.0f;
    private float mTemperatureCelsius = 0.0f;
    private float mTemperatureFahrenheit = 0.0f;

    private String mDns1;
    private String mDns2;

    private String mDateTime;

    private TimeZone mTimeZone = TimeZone.getTimeZone("Asia/Taipei");
    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Calendar mTime = Calendar.getInstance(mTimeZone, Locale.ENGLISH);

    public void setSystemInfo(String serialno, String version, String mac)
    {
        if(DEBUG)
            Log.d(TAG, "setSystemInfo s:" + serialno + " v:" + version + " m:" + mac);

        mSerialNumber = serialno;
        mVersion = version;
        mMacAddress = mac;
    }

    public void setDateTime(String time)
    {
        mDateTime = time;

        try
        {
            mTime.setTime(mFormatter.parse(time));
        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }
    }

    public Calendar getTime()
    {
        return mTime;
    }

    public String getDateTimeText()
    {
        return mDateTime;
    }

    public String getSerialNumber()
    {
        return mSerialNumber;
    }

    public String getMacAddress()
    {
        return mMacAddress;
    }

    public String getFirmwareVersion()
    {
        return mVersion;
    }

    public void setDns(String dns1, String dns2)
    {
        mDns1 = dns1;
        mDns2 = dns2;
    }

    public String getDns1()
    {
        return mDns1;
    }

    public String getDns2()
    {
        return mDns2;
    }

    public void setHumidityTemperature(float humidity, float temperature)
    {
        if(DEBUG)
            Log.d(TAG, "setHumidityTemperature h:" + humidity + " t:" + temperature);

        setHumidity(humidity);
        setTemperature(temperature);
    }

    public void setHumidity(float humidity)
    {
        mHumidity = humidity;
    }

    public float getHumidity()
    {
        return mHumidity;
    }

    public void setTemperature(float temperature)
    {
        setTemperature(temperature, true);
    }

    public void setTemperature(float temperature, boolean isCelsius)
    {
        if(isCelsius)
        {
            mTemperatureCelsius = temperature;
            mTemperatureFahrenheit = mTemperatureCelsius*9/5+32;
        }
        else
        {
            mTemperatureFahrenheit = temperature;
            mTemperatureCelsius = (mTemperatureFahrenheit-32)*5/9;
        }
    }

    public float getTemperature()
    {
        return getTemperature(true);
    }

    public float getTemperature(boolean isCelsius)
    {
        return isCelsius?mTemperatureCelsius:mTemperatureFahrenheit;
    }
}
