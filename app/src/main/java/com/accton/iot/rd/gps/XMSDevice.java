package com.accton.iot.rd.gps;

/**
 * Created by peter on 2016/8/9.
 */
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class XMSDevice {
    private final static String TAG = "XMSDevice";
    private final static boolean DEBUG = true;

    private String mName;

    private int mRestfulPort=8000;	// = 18000;
    private int mRtspPort=554;		// = 10554
    private int mCgiPort=6561;		// = 16561;

    private Socket mCgiSocket;
    private OutputStream mCgiSocketOutput;

    private XMSRestCommand mRestCommand;

    private SystemInfo mSystemInfo = new SystemInfo();

    private void deviceValid()
    {
        if(DEBUG)
            Log.d(TAG, "deviceValid");

        //mDeviceBus.post(new DeviceConnectedEvent(this));
    }

    public XMSDevice(String name)
    {
        if (DEBUG)
            Log.d(TAG, "Constructor");
        mName = name;

        mRestCommand = new XMSRestCommand(mRestfulPort);
    }

    private void deviceInvalid(Throwable throwable)
    {
        if(DEBUG)
            Log.d(TAG, "deviceInvalid t:" + throwable);

        //if(!(throwable instanceof RetrofitError))
        //    return;

        //RetrofitError error = (RetrofitError) throwable;

        //if(error==null)
        //    return;

        //Response response = error.getResponse();

        //if(DEBUG)
        //    Log.d(TAG, "deviceInvalid r:" + response);

        //int status = ACCESS_CODE_AUTHORITY_ERROR;

        //if(response!=null)
        //    status = response.getStatus();

        //if(DEBUG)
        //    Log.d(TAG, "deviceInvalid s:" + status);

        //if(status==ACCESS_CODE_AUTHORITY_ERROR)
        //{
        //mCurrentAccessCodeState = ACCESS_CODE_CHECK_STATE.STATE_USER_CHECKING;

        //accessFail(throwable);
        //}
        //else
        //{
        //mDeviceBus.post(new DeviceConnectionFailEvent(this));
        //}
    }

    public void checkValidation()
    {
        if(DEBUG)
            Log.d(TAG, "checkValidation");

        mRestCommand.getSystemInfo()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnCompleted(() -> deviceValid())
                .subscribeOn(Schedulers.io()) // Fix android.os.NetworkOnMainThreadException issue
                .subscribe(info -> mSystemInfo.setSystemInfo(info.serialno, info.version, info.mac), response -> deviceInvalid(response));
    }

    public void startTalking()
    {
        if(DEBUG)
            Log.d(TAG, "startTalking");

        if(mCgiSocket!=null)
        {
            try
            {
                if(mCgiSocketOutput!=null)
                    mCgiSocketOutput.close();

                mCgiSocket.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

        try
        {
            mCgiSocket = new Socket("10.9.3.194", mCgiPort);

            mCgiSocketOutput = mCgiSocket.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(mCgiSocketOutput, "UTF8"));

            writer.write("POST /audio.play HTTP/1.1\r\n");
            writer.write("Content-Length: 0\r\n");
            writer.write("Content-Type: audio/x-ulaw\r\n");
            writer.write("\r\n");

            writer.flush();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void stopTalking()
    {
        if(DEBUG)
            Log.d(TAG, "stopTalking");

        if(mCgiSocket==null)
            return;

        try
        {
            if(mCgiSocketOutput!=null)
            {
                mCgiSocketOutput.flush();
                mCgiSocketOutput.close();
            }

            mCgiSocket.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void sendTalkingAudio(byte[] audioData)
    {
        if(DEBUG)
            Log.d(TAG, "sendTalkingAudio");

        if(mCgiSocket==null)
            return;

        if(mCgiSocketOutput==null)
            return;

        try
        {
            mCgiSocketOutput.write(audioData);
            mCgiSocketOutput.flush();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
