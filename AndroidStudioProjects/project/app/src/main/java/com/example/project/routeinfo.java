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
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class routeinfo extends AppCompatActivity {

    private MapView mapView;
    private GoogleMap googleMap;
    private Polyline routePolyline;
    String start, end;
    ArrayList<String> locationList, latLong;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routeinfo);
        Intent intent = this.getIntent();
        start = intent.getStringExtra("Start");
        end = intent.getStringExtra("End");

        ArrayList<String> locationList = new ArrayList<>();
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAA");

        locationList.add(start);
        locationList.add(end);
        System.out.println(start + end);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;
            }
        });
        new Geocoding().execute(locationList);

    }

    private void fetchDirections(ArrayList<String> latLong1) {

        if(latLong1.size()==4)
        {
            String directionsUrl = "https://maps.googleapis.com/maps/api/directions/json" +
                    "?origin=" + latLong1.get(0) + "," + latLong1.get(1) +
                    "&destination=" + latLong1.get(2)+ "," + latLong1.get(3) +
                    "&key=AIzaSyC7ag49tvfpeOkIjnlZTSzKiKW6xR9wkAg";

            new FetchDirectionsTask().execute(directionsUrl);
        }
        else
        {
            Toast.makeText(routeinfo.this, "Failed to fetch data", Toast.LENGTH_SHORT).show(); finish();
        }
    }

    private class Geocoding extends AsyncTask<List<String>, Void, String[]> {
        @Override
        protected String[] doInBackground(List<String>... params) {
            List<String> locations = params[0];
            String[] retVal = new String[2];
            int i=0;

            if (locations != null && !locations.isEmpty()) {
                StringBuilder stringBuilder;
                for (String location : locations) {
                    stringBuilder = new StringBuilder();
                    String apiKey = "AIzaSyC7ag49tvfpeOkIjnlZTSzKiKW6xR9wkAg";
                    String apiUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" + location + "&key=" + apiKey;

                    try {
                        URL url = new URL(apiUrl);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                        InputStream inputStream = connection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String line;

                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }

                        retVal[i] = stringBuilder.toString(); i++;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return retVal;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
            if (result != null)
            {
                ArrayList<String> latLong1= new ArrayList<String>();
                int j=0;
                try
                {
                    while(j!=2) {
                        JSONObject jsonObject = new JSONObject(result[j]);
                        JSONArray results = jsonObject.getJSONArray("results");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject location = results.getJSONObject(i).getJSONObject("geometry").getJSONObject("location");
                            double latitude = location.getDouble("lat");
                            double longitude = location.getDouble("lng");

                            // Use latitude and longitude as needed
                            // For example, display or utilize them in your app
                            String a = "" + latitude;
                            String b = "" + longitude;
                            latLong1.add(a);
                            latLong1.add(b);
                        }
                        j++;
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                fetchDirections(latLong1);
            }
            else {
                Toast.makeText(routeinfo.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }

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

            JSONObject route1 = routes.getJSONObject(0);
            JSONObject legs = route1.getJSONArray("legs").getJSONObject(0);
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
