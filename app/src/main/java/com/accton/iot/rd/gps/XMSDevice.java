package com.accton.iot.rd.gps;

/**
 * Created by peter on 2016/8/9.
 */
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.HttpException;
import retrofit2.Response;
import rx.Observable;
import rx.schedulers.Schedulers;

public class XMSDevice {
    private final static String TAG = "XMSDevice";
    private final static boolean DEBUG = true;

    private String mName;
    private int mRestfulPort=8000;
    private XMSRestCommand mRestCommand;
    private SystemInfo mSystemInfo = new SystemInfo();

    private List<SensorDeviceInfo> mSensorDeviceList = new ArrayList<SensorDeviceInfo>();

    public XMSDevice(String name)
    {
        if (DEBUG)
            Log.d(TAG, "Constructor");
        mName = name;

        mRestCommand = new XMSRestCommand(mRestfulPort);
    }

    public void checkValidation()
    {
        if(DEBUG)
            Log.d(TAG, "checkValidation");

        mRestCommand.getSystemInfo()
                .subscribeOn(Schedulers.newThread())
                .doOnCompleted(() -> deviceValid())
                .subscribe(info -> mSystemInfo.setSystemInfo(info.serialno, info.version, info.mac), response -> deviceInvalid(response));
    }

    public void queryDeviceData(int did)
    {
        if (DEBUG)
            Log.d(TAG, "queryDeviceData, did: " + did);

        mRestCommand.getDeviceData(did)
                .subscribeOn(Schedulers.newThread())
                //.flatMap(device -> Observable.from(device))
                .doOnCompleted(() -> getDeviceDataSuccess())
                .subscribe(device -> getDeviceData(device.gatewayid, device.gprmc, device.rssi), response -> getDeviceDatFail(response));
    }

    //public void queryDevices()
    //{
    //    if (DEBUG)
    //        Log.d(TAG, "querySensorDevices");

    //    mSensorDeviceList.clear();

    //    mRestCommand.queryGPSData()
    //            .subscribeOn(Schedulers.newThread())
    //            .flatMap(device -> Observable.from(device))
    //            .doOnCompleted(() -> addSensorSuccess())
    //            .subscribe(device -> addSensorDevice(Integer.valueOf(device.deviceid), device.devicename, device.serial, device.mac), response -> addSensorFail(response));
    //}

    private void getDeviceData(String gid, String gprmc, String rssi)
    {
        if (DEBUG)
            Log.d(TAG, "getDeviceData gps:" + gprmc);

        GPSDataInfo mGPS = new GPSDataInfo();

        mGPS.setGprmc(gprmc);
    }

    private void getDeviceDataSuccess()
    {
        if(DEBUG)
            Log.d(TAG, "getDeviceDataSuccess");
    }

    private void getDeviceDatFail(Throwable throwable)
    {
        if(DEBUG)
            Log.d(TAG, "getDeviceDatFail t:" + throwable);

        if(throwable instanceof HttpException)
        {
            HttpException exception = (HttpException) throwable;

            if(DEBUG)
                Log.d(TAG, "deviceInvalid e:" + exception);

            if(exception==null)
                return;

            Response response = exception.response();

            if(DEBUG)
                Log.d(TAG, "deviceInvalid r:" + response);

            int status = -1;

            if(response!=null)
                status = response.code();

            if(DEBUG)
                Log.d(TAG, "deviceInvalid s:" + status);
        }
        else if(throwable instanceof IOException)
        {
        }
    }

    private void deviceValid()
    {
        if(DEBUG)
            Log.d(TAG, "deviceValid");

        //mDeviceBus.post(new DeviceConnectedEvent(this));
    }

    private void deviceInvalid(Throwable throwable)
    {
        if(DEBUG)
            Log.d(TAG, "deviceInvalid t:" + throwable);

        if(throwable instanceof HttpException)
        {
            HttpException exception = (HttpException) throwable;

            if(DEBUG)
                Log.d(TAG, "deviceInvalid e:" + exception);

            if(exception==null)
                return;

            Response response = exception.response();

            if(DEBUG)
                Log.d(TAG, "deviceInvalid r:" + response);

            int status = -1;

            if(response!=null)
                status = response.code();

            if(DEBUG)
                Log.d(TAG, "deviceInvalid s:" + status);
        }
        else if(throwable instanceof IOException)
        {
        }
    }
}
