package com.example.project;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class routeinfo extends AppCompatActivity {

    private MapView mapView;
    private GoogleMap googleMap;
    private Polyline routePolyline;
    String start, end;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routeinfo);
        Intent intent = this.getIntent();
        start = intent.getStringExtra("Start");
        end = intent.getStringExtra("End");

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;
                fetchDirections();
            }
        });
    }

    private void fetchDirections() {

        String directionsUrl = "https://maps.googleapis.com/maps/api/directions/json" +
                "?origin=49.9393,-119.3947" +
                "&destination=49.9322,-119.3992" +
                "&key=AIzaSyC7ag49tvfpeOkIjnlZTSzKiKW6xR9wkAg";

        new FetchDirectionsTask().execute(directionsUrl);
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

            JSONObject route1 = routes.getJSONObject(0);
            JSONObject legs = route1.getJSONArray("legs").getJSONObject(0);
            JSONObject startLocation = legs.getJSONObject("start_location");
            JSONObject endLocation = legs.getJSONObject("end_location");

            LatLng originLatLng = new LatLng(startLocation.getDouble("lat"), startLocation.getDouble("lng"));
            LatLng destinationLatLng = new LatLng(endLocation.getDouble("lat"), endLocation.getDouble("lng"));

            googleMap.addMarker(new MarkerOptions().position(originLatLng).title("UBCO"));
            googleMap.addMarker(new MarkerOptions().position(destinationLatLng).title("U3"));

            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    String markerTitle = marker.getTitle();
                    Toast.makeText(routeinfo.this, markerTitle, Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (LatLng point : options.getPoints()) {
                builder.include(point);
            }

            LatLngBounds.Builder builder1 = new LatLngBounds.Builder();
            for (LatLng point : options.getPoints()) {
                builder1.include(point);
            }
            LatLngBounds bounds = builder1.build();
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public void onBack(View view)
    {
        finish();
    }

    public void onHome(View view)
    {
        Intent intent = new Intent(this, homescreen.class);
        startActivity(intent);
    }

    public void onIncidentHistory(View view)
    {
        Intent intent = new Intent(this, incidentHistory.class);
        startActivity(intent);
    }

    public void onNavigation(View view)
    {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
