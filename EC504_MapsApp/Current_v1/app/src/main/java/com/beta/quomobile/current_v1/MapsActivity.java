package com.beta.quomobile.current_v1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.SyncStateContract;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements
        ConnectionCallbacks, OnConnectionFailedListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener
{
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    //Menu drawer
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    //Google maps location (used for button)
    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    //When the app is open
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_activity);
        setUpMapIfNeeded();
        //Implements drawer
        mDrawerList = (ListView)findViewById(R.id.navList);
        //Force implement scrolling
        mDrawerList.setOverScrollMode(0);
        //Making sure Google maps is gucci
        if (checkPlayServices())
        {
            // Building the GoogleApi client
            buildGoogleApiClient();
        }
        //Declaring map dependencies
        displayLocation();
        onMapReady(mMap);
    }
    //Function to populate navigation drawer
    public void addDrawerItems(String input[])
    {
        String[] osArray = input;
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
    }
    //Creates the map
    private void setUpMap()
    {
        //Live locations
        mMap.setMyLocationEnabled(true);
        //Remove buildings
        mMap.setBuildingsEnabled(false);
        //Turn off basic menu
        mMap.getUiSettings().setMapToolbarEnabled(false);
        //Set up map click listeners
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        // Zoom in the Google Map
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(42.3496, -71.0997), 14.0f));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.8282, -98.5795), 3.0f));
    }
    //Set up the map the first time
    private void setUpMapIfNeeded()
    {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null)
            {
                setUpMap();
            }
        }
    }
    //Passes in Map to do some intialization nothing in this case
    public void onMapReady(GoogleMap map)
    {
        /*map.addMarker(new MarkerOptions()
                .position(new LatLng(42.349220, -71.106121))
                .title("Photonics Center"));*/
    }
    //Connects app to Google API
    @Override
    protected void onStart()
    {
        super.onStart();
        if (mGoogleApiClient != null)
        {
            mGoogleApiClient.connect();
        }
        displayLocation();
    }
    //Upon returning to app
    @Override
    protected void onResume()
    {
        super.onResume();
        setUpMapIfNeeded();
    }
    //Detecting a single map click
    public void onMapClick(LatLng point)
    {
        //Creating a string to log the coordinate of the click
        String coords = point.latitude + " " + point.longitude;
        //Changing the coordinates display on the top left
        final TextView textViewToChange = (TextView) findViewById(R.id.location);
        textViewToChange.setText
                (
                        "(" + String.format("%.7f", point.latitude) + ")" + " , " + "(" + String.format("%.7f", point.longitude) + ")"
                );
        //Run an asyncTask (background thread)
        new ServerQuery().execute(coords);
    }
    //Detecing a long click (pressing a while on the map)
    public void onMapLongClick(LatLng point)
    {
        //Changing the coordinates display on the top left
        final TextView textViewToChange = (TextView) findViewById(R.id.location);
        textViewToChange.setText
                (
                        "(" + String.format("%.7f", point.latitude) + ")" + " , " + "(" + String.format("%.7f", point.longitude) + ")"
                );
        //Run an asyncTask (background thread)
        new ServerQuery().execute(mMap.getProjection().getVisibleRegion().latLngBounds.toString());
    }
    //When connected to API
    @Override
    public void onConnected(Bundle bundle)
    {
        displayLocation();
    }
    //When disconnected, try to recon
    @Override
    public void onConnectionSuspended(int i)
    {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {

    }
    //Connecting to actual client
    protected synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }
    //Checking if we are connected
    private boolean checkPlayServices()
    {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS)
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {

            }
            else
            {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        else
        {
            return true;
        }
    }
    //Update the live location dot
    private void displayLocation()
    {
        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null)
        {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
           // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 14.0f));
        }
        else
        {

        }
    }
    //Display markers on the map based on parsed input
    public void showMarkers(String input)
    {
        //Clear the map of current markers
        mMap.clear();
        //Split the input
        String split[] = input.split("\\s+");
        //String list to populate menu
        List<String> toDrawer = new ArrayList<>();
        //Go through the splitted input
        for (int x = 0; x < split.length; x += 4)
        {
            //Parse the strings and add markers to map
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(split[x + 2]), Double.parseDouble(split[x + 3])))
                    .title(split[x] + " , " + split[x + 1])
                    .snippet(split[x + 2] + " , " + split[x + 3])
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            //Add data to navigation drawer
            toDrawer.add(split[x] + "," + split[x+1] + " " + split[x+2] + " , " + split[x+3]);
        }
        //Convert List of strings to Array of strings and add into drawer
        toDrawer.add(0,"Displaying " + toDrawer.size() + " points");
        String[] inputToDrawer = new String[toDrawer.size()];
        inputToDrawer = toDrawer.toArray(inputToDrawer);
        addDrawerItems(inputToDrawer);
    }
    //asyncTask, or background thread to query the server
    private class ServerQuery extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            //Initialize dependencies
            Socket socket = null;
            DataOutputStream dataOutputStream = null;
            DataInputStream dataInputStream = null;
            String rec = "";

            try
            {
                //NEED TO GRAB IP!!! (based on eng-grid session)
                //Connect to the java server running on eng-grid, IP varying
                socket = new Socket("128.197.115.32", 8888);
                //Create data streams
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());
                //Take input to send from function params
                String toSend = params[0];
                //TCP send UTF-8 strings through the sockets
                dataOutputStream.writeUTF(toSend);
                //Receive the reply from the server
                rec = dataInputStream.readUTF();
            }
            catch (UnknownHostException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            finally
            {
                //Clean up the socket
                if (socket != null)
                {
                    try
                    {
                        socket.close();
                    }
                    catch (IOException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            //Return the server response to onPostExecute
            return rec;
        }
        //After the asyncTask is finished
        @Override
        protected void onPostExecute(String result)
        {
            //Draw the markers onto the map
            showMarkers(result);
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
    //When the user presses the backButton, clear the map of points
    @Override
    public void onBackPressed()
    {
        mMap.clear();
    }
}