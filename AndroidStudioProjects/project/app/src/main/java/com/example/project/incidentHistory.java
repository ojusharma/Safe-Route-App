package com.example.project;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class incidentHistory extends AppCompatActivity {

    private MapView incidentMapView;
    private GoogleMap googleMap;
    private Polyline routePolyline;
    String start, end, lat1, long1, lat2, long2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_history);
        Intent intent = this.getIntent();
        start = intent.getStringExtra("start");
        end = intent.getStringExtra("end");
        lat1 = intent.getStringExtra("lat1");
        long1 = intent.getStringExtra("long1");
        lat2 = intent.getStringExtra("lat2");
        long2 = intent.getStringExtra("long2");


        incidentMapView = findViewById(R.id.incidentMapView);
        incidentMapView.onCreate(savedInstanceState);

        incidentMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;
                fetchIncidentLocations();
            }
        });
    }

    private void fetchIncidentLocations() {

        String incidentLocationUrl = "https://maps.googleapis.com/maps/api/directions/json" +
                "?origin=" + lat1 + "," + long1 +
                "&destination=" + lat2 + "," + long2 +
                "&key=AIzaSyC7ag49tvfpeOkIjnlZTSzKiKW6xR9wkAg";

        new FetchIncidentLocationsTask().execute(incidentLocationUrl);
    }
    private class FetchIncidentLocationsTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String locationData) {
            if (locationData != null) {
                plotIncidentLocations(locationData);
            }
        }
    }

    private void plotIncidentLocations(String locationData) {
        try {
            JSONObject jsonObject = new JSONObject(locationData);
            JSONArray routes = jsonObject.getJSONArray("routes");

            if (routes.length() > 0) {
                JSONObject route = routes.getJSONObject(0);
                JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
                String encodedPolyline = overviewPolyline.getString("points");

                PolylineOptions options = new PolylineOptions().addAll(PolyUtil.decode(encodedPolyline));
                routePolyline = googleMap.addPolyline(options);

                JSONObject legs = route.getJSONArray("legs").getJSONObject(0);
                JSONObject startLocation = legs.getJSONObject("start_location");
                JSONObject endLocation = legs.getJSONObject("end_location");

                LatLng originLatLng = new LatLng(startLocation.getDouble("lat"), startLocation.getDouble("lng"));
                LatLng destinationLatLng = new LatLng(endLocation.getDouble("lat"), endLocation.getDouble("lng"));

                googleMap.addMarker(new MarkerOptions().position(originLatLng).title(start));
                googleMap.addMarker(new MarkerOptions().position(destinationLatLng).title(end));

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        String markerTitle = marker.getTitle();
                        Toast.makeText(incidentHistory.this, markerTitle, Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (LatLng point : options.getPoints()) {
                    builder.include(point);
                }

                LatLngBounds bounds = builder.build();
                googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class FetchDirectionsTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String directionsData) {
            if (directionsData != null) {
                drawRoute(directionsData);
            }
        }
    }

    private void drawRoute(String directionsData) {
        try {
            JSONObject jsonObject = new JSONObject(directionsData);
            JSONArray routes = jsonObject.getJSONArray("routes");
            JSONObject route = routes.getJSONObject(0);
            JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
            String encodedPolyline = overviewPolyline.getString("points");

            PolylineOptions options = new PolylineOptions().addAll(PolyUtil.decode(encodedPolyline));
            routePolyline = googleMap.addPolyline(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBack(View view) {
        finish();
    }

    public void profile(View view) {
        Intent intent = new Intent(incidentHistory.this, profile.class);
        startActivity(intent);
    }

    public void onClick4(View v) {
        Intent intent = new Intent(incidentHistory.this, helpchat.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        incidentMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        incidentMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        incidentMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        incidentMapView.onLowMemory();
    }
}
