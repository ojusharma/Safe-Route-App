package com.example.project;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.example.project.databinding.ActivityMapsBinding;
import com.google.maps.android.PolyUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class incidentHistory extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Polyline routePolyline;
    private Marker currentLocationMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        String directionsUrl = "https://maps.googleapis.com/maps/api/directions/json" +
                "?origin=49.9393,-119.3947" +
                "&destination=49.9322,-119.3992" +
                "&key=AIzaSyC7ag49tvfpeOkIjnlZTSzKiKW6xR9wkAg";

        new FetchDirectionsTask().execute(directionsUrl);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }

        mMap.setMyLocationEnabled(true);
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
            routePolyline = mMap.addPolyline(options);

            JSONObject route1 = routes.getJSONObject(0);
            JSONObject legs = route1.getJSONArray("legs").getJSONObject(0);
            JSONObject startLocation = legs.getJSONObject("start_location");
            JSONObject endLocation = legs.getJSONObject("end_location");

            LatLng originLatLng = new LatLng(startLocation.getDouble("lat"), startLocation.getDouble("lng"));
            LatLng destinationLatLng = new LatLng(endLocation.getDouble("lat"), endLocation.getDouble("lng"));

            mMap.addMarker(new MarkerOptions().position(originLatLng).title("UBCO"));
            mMap.addMarker(new MarkerOptions().position(destinationLatLng).title("U3"));

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(originLatLng);
            builder.include(destinationLatLng);
            LatLngBounds bounds = builder.build();
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
            }
        }
    }

    public void onBack(View view) {
        finish();
    }

    public void onPro(View view)
    {
        Intent intent = new Intent(this, homescreen.class);
        startActivity(intent);
    }
}
