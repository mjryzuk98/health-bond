//Written by Mitchell Ryzuk
//10/19/20
//HackRU Fall2019

package com.example.myfirstapp;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Looper;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Response;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class NetworkThread extends AppCompatActivity implements Runnable {

    private FusedLocationProviderClient locationClient;

    private LocationRequest mLocationRequest;

    private long UPDATE_INTERVAL = 5 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private String username, fullName, myPhone, req;
    private String subNum;
    private String location;
    private Thread worker;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public NetworkThread(String username, String fullName, String myPhone, String req) {
        this.username = username;
        this.fullName = fullName;
        this.myPhone = myPhone;
        this.req = req;
    }

    public NetworkThread(String username, String subNum, String req) {
        this.username = username;
        this.req = req;
        if (this.req.equals("sub")) {
            this.subNum = subNum;
        }
        else {
            location = subNum;
        }
    }


    public void start() {
        worker = new Thread(this);
        worker.start();
    }

    public void stop() {
        running.set(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void run() {
        /*try {
            URL url = new URL("https://flask-app-256509.appspot.com/register/" + username + "/" + fullName + "/" + myPhone);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            System.out.println(out);
            //writeStream(out);
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            System.out.println(in);
            //readStream(in);
            urlConnection.disconnect();
        } catch (Exception e) {
            System.out.println(e);
        } */
        String url;
        if (req.equals("register")) {
            url = "https://flask-app-256509.appspot.com/register/" + username + "/" + fullName + "/" + myPhone;
            System.out.println("register");
        }
        else if (req.equals("sub")) {
            url = "https://flask-app-256509.appspot.com/" + username + "/update/" + subNum;
            System.out.println("sub");
        }
        else {
            url = "https://flask-app-256509.appspot.com/" + username + "/coords/" + location;
        }
        try {
            HttpURLConnection httpClient =
                    (HttpURLConnection) new URL(url).openConnection();

            httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = httpClient.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpClient.getInputStream()))) {

                StringBuilder response = new StringBuilder();
                String line;

                while ((line = in.readLine()) != null) {
                    response.append(line);
                }

                //print result
                System.out.println(response.toString());

            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        stop();
    }

}


    // Trigger new location updates at interval
    /*protected void startLocationUpdates() {

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

    public void onLocationChanged(Location location) {
        String message = Double.toString(location.getLatitude()) + " " + Double.toString(location.getLongitude());
        TextView editText = findViewById(R.id.output);
        editText.setText(message);
    }

    private void processCommand() {
        try {
            Thread.sleep(5000);
            //run();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }*/
  //  }

//}
