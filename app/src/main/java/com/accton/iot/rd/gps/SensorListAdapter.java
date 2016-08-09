package com.accton.iot.rd.gps;

/**
 * Created by peter on 2016/8/2.
 */
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by mc_chen on 2015/12/31.
 */
public class SensorListAdapter extends ArrayAdapter<SensorDeviceInfo> {
    private final String TAG = "SensorListAdapter";
    private final boolean DEBUG = true;

    private final Context mContext;
    private List<SensorDeviceInfo> mContents;
    private int mCount;
    private final int mResourceId;
    private final LayoutInflater mLayoutInflator;

    public SensorListAdapter(Context context, int textViewResourceId, List<SensorDeviceInfo> objects) // constructor
    {
        super(context, textViewResourceId, objects);

        if (DEBUG)
            Log.d(TAG, "SensorListAdapter c:" + context + " r:" + textViewResourceId);

        mContext = context;
        mResourceId = textViewResourceId;
        mContents = objects;

        mLayoutInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) // ArrayList
    {
        if (DEBUG)
            Log.d(TAG, "getView p:" + position + " v:" + convertView + " p:" + parent + " r:" + mResourceId);

        View view = convertView;

        if (convertView == null)
            view = mLayoutInflator.inflate(mResourceId, null);

        ImageView typeView = (ImageView) view.findViewById(R.id.list_type);
        ImageView buttonSetting = (ImageView) view.findViewById(R.id.button_more_vert);
        TextView nameView = (TextView) view.findViewById(R.id.file_name);

        if (position < 0 || position >= getCount())
            return view;

        SensorDeviceInfo mFileInfo = getItem(position);

        nameView.setText(mFileInfo.getName());
        if (mFileInfo.getType() == "gps") {
            typeView.setBackgroundResource(R.drawable.ic_gps);
        }
        else {
            typeView.setBackgroundResource(R.drawable.ic_room);
        }
        buttonSetting.setVisibility(View.INVISIBLE);

        buttonSetting.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (DEBUG)
                    Log.d(TAG, "onClick buttonSetting");
            }
        });

        return view;
    }
}