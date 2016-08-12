package com.accton.iot.rd.gps;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.HashMap;

public class SensorMapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private final static String TAG = "SensorMapsActivity";
    private final static boolean DEBUG = true;
    private GoogleMap mMap;

    private Circle mCircle1U = null;
    private Circle mCircle2U = null;
    private Circle mCircle3U = null;
    private Circle mCircle4U = null;
    private int mUnit = 500;
    final LatLng ACCTON_TAIPEI_OFFICE = new LatLng(25.043468, 121.557564);
    final LatLng ACCTON_Hsinchu_HQ = new LatLng(24.778367, 120.988137);
    private LatLng mGWPoistion = ACCTON_TAIPEI_OFFICE;
    private LatLng mCurrentPosition = null;
    private MarkerOptions mPseudoMarkerOpt = null;
    private HashMap<String, Marker> mPseudoMarkerDict = null;
    private MarkerOptions mSensorMarkerOpt = null;
    private HashMap<String, Marker> mSensorMarkerDict = null;
    private Boolean mIsMapReady = false;
    private Handler mQueryGPSHandler = new Handler();
    private final int QUERY_GPS_DATA_INTERVAL = 6000; // 6 seconds
    private XMSDevice mXMSDevice = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(DEBUG)
            Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sensor_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mPseudoMarkerDict = new HashMap<String, Marker>();
        mSensorMarkerDict = new HashMap<String, Marker>();
        if(DEBUG)
            Log.d(TAG, "onCreate end.");

        // Obtain XMSDevice
        mXMSDevice = MainApplication.getXMSDevice();
        if (mXMSDevice == null) {
            MainApplication.createXMSDevice();
            mXMSDevice = MainApplication.getXMSDevice();
        }

        // Create runnable...
        mQueryGPSHandler.removeCallbacks(mQueryGPSRunnable);
        mQueryGPSHandler.postDelayed(mQueryGPSRunnable, QUERY_GPS_DATA_INTERVAL);
}

    @Override
    public void onDestroy()
    {
        if(DEBUG)
            Log.d(TAG, "onDestroy");

        mQueryGPSHandler.removeCallbacks(mQueryGPSRunnable);

        super.onDestroy();
    }

    @Override
    public void onStart()
    {
        if(DEBUG)
            Log.d(TAG, "onStart");

        super.onStart();
    }

    @Override
    public void onStop()
    {
        if(DEBUG)
            Log.d(TAG, "onStop");

        super.onStop();
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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(DEBUG)
            Log.d(TAG, "onMapReady");
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        //
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                mCircle1U.remove();
                mCircle2U.remove();
                mCircle3U.remove();
                mCircle4U.remove();
            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                mGWPoistion = marker.getPosition();
                updateRing(mGWPoistion);
            }
        });

        //Marker Iot Gateway
        MarkerOptions markerGW = new MarkerOptions();
        markerGW.position(mGWPoistion);
        markerGW.title("IoT Gateway");
        markerGW.draggable(true);

        mMap.addMarker(markerGW);

        updateRing(mGWPoistion);
        mCurrentPosition = mGWPoistion;

        mPseudoMarkerOpt = new MarkerOptions();
        mPseudoMarkerOpt.position(mCurrentPosition);
        mPseudoMarkerOpt.draggable(false);
        mPseudoMarkerOpt.visible(false);
        mPseudoMarkerOpt.anchor(0.5f, 0.5f);
        mPseudoMarkerOpt.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        mSensorMarkerOpt = new MarkerOptions();
        mSensorMarkerOpt.position(mCurrentPosition);
        mSensorMarkerOpt.draggable(false);
        mSensorMarkerOpt.visible(false);
        mSensorMarkerOpt.anchor(0.5f, 0.5f);
        mSensorMarkerOpt.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        MarkerOptions pseudoMarkerOpt = new MarkerOptions();
        pseudoMarkerOpt.position(mCurrentPosition);
        pseudoMarkerOpt.draggable(false);
        pseudoMarkerOpt.visible(false);
        pseudoMarkerOpt.anchor(0.5f, 0.5f);
        pseudoMarkerOpt.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));

        for(int index = 0; index < 8; index++)
            mPseudoMarkerDict.put(""+index, mMap.addMarker(pseudoMarkerOpt));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCurrentPosition, 15)); // The desired zoom level, in the range of 2.0 to 21.0.
        mIsMapReady = true;
        //
    }

    private void updateRing(LatLng center) {
        mCircle1U = drawCircleROI(center, mUnit , 0x08AA00FF);
        mCircle2U = drawCircleROI(center, mUnit * 2, 0x08FF00FF);
        mCircle3U = drawCircleROI(center, mUnit * 3, 0x08FF00AA);
        mCircle4U = drawCircleROI(center, mUnit * 4, 0x08FFAAFF);
    }

    private void drawSquareROI(LatLng center, double sideLength, int roiColor) {
        // Polylines are useful for marking paths and routes on the map.
        mMap.addPolyline(new PolylineOptions().geodesic(true).color(roiColor)
                .add(new LatLng(center.latitude - sideLength, center.longitude - sideLength))
                .add(new LatLng(center.latitude + sideLength, center.longitude - sideLength))
                .add(new LatLng(center.latitude + sideLength, center.longitude + sideLength))
                .add(new LatLng(center.latitude - sideLength, center.longitude + sideLength))
                .add(new LatLng(center.latitude - sideLength, center.longitude - sideLength))
        );
    }

    private Circle drawCircleROI(LatLng center, double radius, int roiColor) {
        // Instantiates a new CircleOptions object and defines the center and radius
        CircleOptions circleOptions = new CircleOptions()
                .strokeWidth((float)(1.0))
                .fillColor(roiColor)
                .center(center)
                .radius(radius); // In meters

        // Get back the mutable Circle
        return mMap.addCircle(circleOptions);
    }

    private Runnable mQueryGPSRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            if(DEBUG)
                Log.d(TAG, "mQueryGPSRunnable run");

            // Query GPS data, restful api
            if (mXMSDevice != null)
                mXMSDevice.queryDeviceData(0); // TODO... Restful API test
                //mXMSDevice.checkValidation(); // TODO... Restful API test
            else
                Log.e (TAG, "XMSDevice is null!");

            // Re-start with timer
            mQueryGPSHandler.removeCallbacks(mQueryGPSRunnable);
            mQueryGPSHandler.postDelayed(mQueryGPSRunnable, QUERY_GPS_DATA_INTERVAL);
        }
    };
}
