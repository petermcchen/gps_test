package com.accton.iot.rd.gps;

import android.util.Log;

/**
 * Created by peter on 2016/8/12.
 */
public class GPSDataInfo {
    private final String TAG = "GPSDataInfo";
    private final boolean DEBUG = false;

    String mGatewayId;
    String mGPRMC;
    String mRSSI;

    public void GPSDataInfo(String gwid, String gprmc, String rssi)
    {
        if(DEBUG)
            Log.d(TAG, "GPSDataInfo gw:" + gwid + " gps:" + gprmc + " rssi:" + rssi);

        mGatewayId = gwid;
        mGPRMC = gprmc;
        mRSSI = rssi;
    }
    public void setGprmc(String gprmc) {
        mGPRMC = gprmc;
    }

    public String getGprmc() {
        return mGPRMC;
    }
}
