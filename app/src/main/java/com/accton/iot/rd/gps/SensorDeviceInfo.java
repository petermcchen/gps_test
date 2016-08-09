package com.accton.iot.rd.gps;

/**
 * Created by peter on 2016/8/2.
 */
public class SensorDeviceInfo {
    private final String TAG = "SensorDeviceInfo";
    private final boolean DEBUG = false;

    private String mType;
    private String mName;
    private String mGateway;

    public SensorDeviceInfo(String type, String name, String gateway)
    {
        mType = type;
        mName = name;
        mGateway = gateway;
    }

    public void setGateway(String gateway) { mGateway = gateway; }
    public void setName(String name) { mName = name; }
    public void setType(String type) { mType = type; }

    public String getGateway()
    {
        return mGateway;
    }
    public String getName()
    {
        return mName;
    }
    public String getType()
    {
        return mType;
    }
}