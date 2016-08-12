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

    public void setSystemInfo(String serialno, String version, String mac)
    {
        if(DEBUG)
            Log.d(TAG, "setSystemInfo s:" + serialno + " v:" + version + " m:" + mac);

        mSerialNumber = serialno;
        mVersion = version;
        mMacAddress = mac;
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
}
