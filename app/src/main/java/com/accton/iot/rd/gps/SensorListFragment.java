package com.accton.iot.rd.gps;

/**
 * Created by peter on 2016/8/2.
 */

import android.app.Activity;
//import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
//import android.provider.MediaStore;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class SensorListFragment extends ListFragment {
    private static final String TAG = "SensorListFragment";
    private static final boolean DEBUG = true;

    private ArrayList<SensorDeviceInfo> mSensorDeviceList = new ArrayList<SensorDeviceInfo>();
    private Activity mActivity;
    private ListView mListView = null;
    private SensorListAdapter mAdapter = null;
    private OnSensorClickListener mListener;

    public interface OnSensorClickListener
    {
        void onSensorClick(String name, String gateway);
    }

    // private
    private void setupLayout()
    {
        int totalCount=0, index=0;
        String name=null, type=null, title=null;

        if(DEBUG)
            Log.d(TAG, "setupLayout");

        mListView = getListView();
        //
        mSensorDeviceList.add(new SensorDeviceInfo("gps", "Peter's GPS", "TPE-1"));
        mSensorDeviceList.add(new SensorDeviceInfo("gps", "Tristan's GPS", "TPE-1"));
        mSensorDeviceList.add(new SensorDeviceInfo("gps", "Alpha's GPS", "TPE-1"));
        //

        mAdapter = new SensorListAdapter(getActivity(), R.layout.fragment_sensor_list, mSensorDeviceList);
        setListAdapter(mAdapter);

        mListView.setFastScrollEnabled(false);
        mListView.setVerticalScrollBarEnabled(false);
        mListView.setSmoothScrollbarEnabled(true);

        //mListView.setCacheColorHint(android.R.color.transparent);

        setListShown(false);
    }

    // Fragment life cycle
    @Override
    public void onAttach(Activity activity)
    {
        if(DEBUG)
            Log.d(TAG, "onAttach activity:" + activity);

        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(DEBUG)
            Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onHiddenChanged(boolean hidden)
    {
        if(DEBUG)
            Log.d(TAG, "onHiddenChanged h:" + hidden);

        if(!hidden)
        {
            setListShown(true);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        if(DEBUG)
            Log.d(TAG, "onActivityCreated");

        super.onActivityCreated(savedInstanceState);

        setupLayout();
    }

    @Override
    public void onStart()
    {
        if(DEBUG)
            Log.d(TAG, "onStart");

        super.onStart();
    }

    @Override
    public void onResume()
    {
        if(DEBUG)
            Log.d(TAG, "onResume");

        super.onResume();
    }

    @Override
    public void onPause()
    {
        if(DEBUG)
            Log.d(TAG, "onPause");

        super.onPause();
    }

    @Override
    public void onDestroyView()
    {
        if(DEBUG)
            Log.d(TAG, "onDestroyView");

        super.onDestroyView();
    }

    @Override
    public void onDestroy()
    {
        if(DEBUG)
            Log.d(TAG, "onDestroy");

        super.onDestroy();
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id)
    {
        if(DEBUG)
            Log.d(TAG, "onListItemClick p:" + position + " i:" + id);

        if(position<0 || position>=mSensorDeviceList.size())
            return;

        SensorDeviceInfo info = mSensorDeviceList.get(position);

        String name = info.getName();
        String gateway = info.getGateway();
        String type = info.getType();
        if(DEBUG)
            Log.d(TAG, "onListItemClick gateway:" + gateway + ", name: " + name);
        /*
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                //File sdCard = Environment.getExternalStorageDirectory(); // ==> /storage/sdcard0
                //File file = new File(sdCard, path); // ==> /storage/sdcard0/storage/sdcard0/VoiceRecorder/Musette.3gp XXX
                //intent.setDataAndType(Uri.fromFile(file2), type);
                intent.setDataAndType(uri2, type);
                //intent.setDataAndType(uri, type);
                startActivity(intent);
                */

        if (mListener != null) {
            mListener.onSensorClick(name, gateway);
        }
    }

    //
    // List Click Listener
    //
    public void setOnSensorClickListener(OnSensorClickListener listener)
    {
        if(DEBUG)
            Log.d(TAG, "setOnSensorClickListener");

        mListener = listener;
    }
}