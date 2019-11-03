//Written by Mitchell Ryzuk
//10/19/20
//HackRU Fall2019

package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private FusedLocationProviderClient locationClient;
    //ExecutorService executor;

    private String myPhone;
    private ArrayList<String> subNums;
    private int subSize;

    private String strLocation;
    private String username;
    private String fullName;

    private LocationRequest mLocationRequest;

    private long UPDATE_INTERVAL = 20000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        // init location client on MainActivity create
        locationClient = getFusedLocationProviderClient(this);

        TextView txt = findViewById(R.id.output);
        txt.setText("");
        username = "";
        fullName = "";
        myPhone = "";

        subNums = new ArrayList<String>();
        subSize = 0;

        startLocationUpdates();

        //Runnable thread = new NetworkThread();
        //executor.execute(thread);


        /*
         * Call getLastLocation() to get the last recorded location.
         * Also add on a method to execute when a successful location is found.
         */
        /*locationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        String message1 = location.toString();
                        TextView editText1 = findViewById(R.id.output);
                        editText1.setText(message1);
                        // Successfully got location (null check is important)
                        if (location != null) {
                            String message = location.toString();
                            TextView editText = findViewById(R.id.output);
                            editText.setText(message);
                        }
                    }
                });*/
    }


    // Trigger new location updates at interval
    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.userNum);
        String message = editText.getText().toString();
        Intent intent1 = intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

    }


    //captures phone number input from text boxes
    public void captureNumber(View view) {
        EditText number;
        Runnable thread;
        try {
            switch (view.getId()) {
                case R.id.button: //user's phone number
                    number = findViewById(R.id.userNum);
                    String temp = number.getText().toString();
                    long num = Long.parseLong(temp);
                    myPhone = number.getText().toString();

                    thread = new NetworkThread(username, fullName, myPhone, "register");
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.execute(thread);

                    View subNum = findViewById(R.id.subNum);
                    View button2 = findViewById(R.id.button2);
                    View button3 = findViewById(R.id.button3);
                    View yourFriends = findViewById(R.id.yourFriends);
                    subNum.setVisibility(View.VISIBLE);
                    button2.setVisibility(View.VISIBLE);
                    button3.setVisibility(View.VISIBLE);
                    yourFriends.setVisibility(View.VISIBLE);
                    executor.shutdown();
                    break;

                case R.id.button2: //appending numbers that user is subscribed to
                    number = findViewById(R.id.subNum);
                    num = Long.parseLong(number.getText().toString());
                    subNums.add(number.getText().toString());
                    subSize++;
                    number.setText("");

                    String out = "";

                    for (int i = 0; i < subNums.size(); i++) {
                        if (i == subNums.size() - 1) {
                            out += subNums.get(i);
                        }
                        else {
                            out += subNums.get(i) + ",";
                        }

                    }

                    thread = new NetworkThread(username, out, "sub");
                    executor = Executors.newSingleThreadExecutor();
                    executor.execute(thread);
                    executor.shutdown();
                    break;
                case R.id.button3: //removing numbers that user is subscribed to
                    if (subSize > 0) {
                        subNums.remove(subNums.size() - 1);
                        subSize--;
                    }
                    break;
                case R.id.button4: //setting username
                    number = findViewById(R.id.name);
                    username = number.getText().toString();

                    break;
                case R.id.button5:
                    number = findViewById(R.id.fullName);
                    fullName = number.getText().toString();

                    View userNum = findViewById(R.id.userNum);
                    View button = findViewById(R.id.button);
                    View yourNumber = findViewById(R.id.yourNumber);
                    userNum.setVisibility(View.VISIBLE);
                    button.setVisibility(View.VISIBLE);
                    yourNumber.setVisibility(View.VISIBLE);
                    break;
            }
            updateLog();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public void onLocationChanged(Location location) {
        strLocation = Double.toString(location.getLatitude()) + "," + Double.toString(location.getLongitude());
        updateLog();
        if (!username.equals("") && !fullName.equals(null) && !myPhone.equals(null)) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Runnable thread = new NetworkThread(username, strLocation, "loc");
            executor.execute(thread);
        }
    }

    //temporary debugging log that displays location coordinates and phone numbers on screen
    public void updateLog() {
        String message = "Username: " + username + " Full Name: " + fullName + "\nCurrent Location:\n" + strLocation + "\nYour Phone Number:\n" + myPhone + "\nSubscribers Phone Numbers:\n";
        for (int i = 0; i < subNums.size(); i++) {
            message += subNums.get(i) + "\n";
        }
        TextView logText = findViewById(R.id.output);
        logText.setText(message);
    }

    public void testCoords(View view) {
        String coords = "40.5025945,-74.4525496";
        if (!username.equals("") && !fullName.equals(null) && !myPhone.equals(null)) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Runnable thread = new NetworkThread(username, coords, "loc");
            executor.execute(thread);
        }

    }


}
