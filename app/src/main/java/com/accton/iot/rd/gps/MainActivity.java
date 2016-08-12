package com.accton.iot.rd.gps;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioTrack;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.media.AudioManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SensorListFragment.OnSensorClickListener {
    private final static String TAG = "MainActivity";
    private final static boolean DEBUG = false;

    private AboutFragment mAboutFragment;
    private SensorListFragment mSensorListFragment;
    private FragmentManager mFragmentManager;

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private String mTitle;

    //private static AudioManager mAudioManager = null;
    //private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    //private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 2;

    private XMSDevice mXMSDevice = null;

    //
    // Private
    //
    private void setupLayout()
    {
        if(DEBUG)
            Log.d(TAG, "setupLayout");

        // UI instances
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        // System instances
        mFragmentManager = getSupportFragmentManager();

        // Fragment instances
        mSensorListFragment = new SensorListFragment();
        mAboutFragment = new AboutFragment();

        mSensorListFragment.setOnSensorClickListener(this);

        // Fragmenr management
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, mSensorListFragment);
        fragmentTransaction.add(R.id.container, mAboutFragment);

        fragmentTransaction.hide(mSensorListFragment);
        fragmentTransaction.hide(mAboutFragment);
        fragmentTransaction.commit();

    }

    private void showSensorList() {
        if(DEBUG)
            Log.d(TAG, "showSensorList");

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.hide(mAboutFragment);
        fragmentTransaction.show(mSensorListFragment);
        fragmentTransaction.commit();

        mToolbar.setTitle("Sensor List");
    }

    private void showSoftwareInfo() {
        if(DEBUG)
            Log.d(TAG, "showSoftwareInfo");

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.hide(mSensorListFragment);
        fragmentTransaction.show(mAboutFragment);
        fragmentTransaction.commit();

        mToolbar.setTitle("Software Information");
    }

    //
    // Public
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(DEBUG)
            Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mXMSDevice = MainApplication.getXMSDevice();
        if (mXMSDevice == null) {
            MainApplication.createXMSDevice();
            mXMSDevice = MainApplication.getXMSDevice();
        }

        if (mXMSDevice != null)
            mXMSDevice.checkValidation(); // Restful API test
        else
            Log.e (TAG, "XMSDevice is null!");

        setupLayout();

        showSensorList();
    }

    @Override
    public void onDestroy()
    {
        if(DEBUG)
            Log.d(TAG, "onDestroy");

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

    // FragmentActivity only...
    @Override
    protected void onResumeFragments()
    {
        if(DEBUG)
            Log.d(TAG, "onResumeFragments");

        super.onResumeFragments();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        if(DEBUG)
            Log.d(TAG, "onSaveInstanceState b:" + outState);

        super.onSaveInstanceState(new Bundle());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        if(DEBUG)
            Log.d(TAG, "onRestoreInstanceState b:" + savedInstanceState);

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if(DEBUG)
            Log.d(TAG, "onBackPressed c:" + mFragmentManager.getBackStackEntryCount());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(DEBUG)
            Log.d(TAG, "onCreateOptionsMenu");

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(DEBUG)
            Log.d(TAG, "onOptionsItemSelected");

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //@SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if(DEBUG)
            Log.d(TAG, "onNavigationItemSelected");

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_sensor) {
            // Show sensor list
            showSensorList();
        } else if (id == R.id.nav_about) {
            // Show app info
            showSoftwareInfo();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSensorClick(String name, String gateway)
    {
        if(DEBUG)
            Log.d(TAG, "onSensorClick, gateway: " + gateway);

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.hide(mSensorListFragment);
        //fragmentTransaction.show();
        fragmentTransaction.commit();

        try
        {
            Intent intent = new Intent(getApplicationContext(), SensorMapsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            startActivity(intent);
        }
        catch(ActivityNotFoundException e)
        {
        }
    }
}
