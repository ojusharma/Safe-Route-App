package com.example.project;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
    String start, end, mode;
    ArrayList<String> locationList, latLong;
    String[] latLongs;
    TextView textViewCar, textViewWalk;
    String[] durationDistance;
    String distanceTextWalk, durationTextWalk ,distanceTextCar, durationTextCar;
    int i;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routeinfo);
        Intent intent = this.getIntent();
        start = intent.getStringExtra("Start");
        end = intent.getStringExtra("End");
        ArrayList<String> locationList = new ArrayList<>();
        latLongs = new String[4];
        i=0;
        durationDistance = new String[2];
        distanceTextWalk = durationTextWalk =distanceTextCar= durationTextCar="";

        locationList.add(start);
        locationList.add(end);
        System.out.println(start + end);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        textViewCar = findViewById(R.id.textViewCar);
        textViewWalk = findViewById(R.id.textViewWalk);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;
            }
        });
        new Geocoding().execute(locationList);

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
                        Toast.makeText(routeinfo.this, "Error finding route. Check location names!", Toast.LENGTH_SHORT).show();
                        finish();
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
                Toast.makeText(routeinfo.this, "Failed to fetch location data.  Check location names!", Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    }

    private void fetchDirections(ArrayList<String> latLong1) {

        if(latLong1.size()==4)
        {
            String directionsUrl = "https://maps.googleapis.com/maps/api/directions/json" +
                    "?origin=" + latLong1.get(0) + "," + latLong1.get(1) +
                    "&destination=" + latLong1.get(2)+ "," + latLong1.get(3) +
                    "&key=AIzaSyC7ag49tvfpeOkIjnlZTSzKiKW6xR9wkAg";
            latLongs[0] = latLong1.get(0);
            latLongs[1] = latLong1.get(1);
            latLongs[2] = latLong1.get(2);
            latLongs[3] = latLong1.get(3);
            new FetchDirectionsTask().execute(directionsUrl);
        }
        else
        {
            Toast.makeText(routeinfo.this, "Failed to fetch data. Check location name!", Toast.LENGTH_SHORT).show(); finish();
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
                Toast.makeText(routeinfo.this, "Error getting directions!", Toast.LENGTH_SHORT).show();
                finish();
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
            DistanceMatrixRequest d = new DistanceMatrixRequest();
            d.execute(new String[]{start, end});
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(routeinfo.this, "Error in finalizing route!", Toast.LENGTH_SHORT).show();
            finish();

        }
    }

    public class DistanceMatrixRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String responses="";
            while(i!=2) {
                String response="";
                HttpURLConnection urlConnection = null;
                try {
                    String origin = strings[0];
                    String destination = strings[1];
                    String mode;
                    if (i == 0) {
                        mode = "walking";
                    } else {
                        mode = "driving";
                    }

                    String apiKey = "AIzaSyC7ag49tvfpeOkIjnlZTSzKiKW6xR9wkAg";

                    String urlString = "https://maps.googleapis.com/maps/api/distancematrix/json?" +
                            "origins=" + origin +
                            "&destinations=" + destination +
                            "&key=" + apiKey +
                            "&mode=" + mode;

                    URL url = new URL(urlString);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = urlConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    durationDistance[i] = stringBuilder.toString();
                    i++;

                } catch (Exception e) {
                    Toast.makeText(routeinfo.this, "Error retrieving Route Time Info!", Toast.LENGTH_SHORT).show();
                    finish();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
            return responses;
        }

        @Override
        protected void onPostExecute(String response) {
            try {
                JSONObject jsonObject = new JSONObject(durationDistance[0]);
                JSONObject element = jsonObject.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0);
                distanceTextWalk = element.getJSONObject("distance").getString("text");
                 durationTextWalk = element.getJSONObject("duration").getString("text");

                JSONObject jsonObject1 = new JSONObject(durationDistance[1]);
                JSONObject element1 = jsonObject1.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0);
                distanceTextCar = element1.getJSONObject("distance").getString("text");
                durationTextCar = element1.getJSONObject("duration").getString("text");
                setDistanceDuration();

            } catch (Exception e) {
                Toast.makeText(routeinfo.this, "Error retrieving Route Time Info!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void setDistanceDuration()
    {
        textViewWalk.setText("Walking\n"+durationTextWalk+"\n"+distanceTextWalk);
        textViewCar.setText("Driving\n"+durationTextCar+"\n"+distanceTextCar);
        textViewCar.setVisibility(View.VISIBLE);
        textViewWalk.setVisibility(View.VISIBLE);

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
        intent.putExtra("start", start);
        intent.putExtra("end", end);
        intent.putExtra("lat1", latLongs[0]);
        intent.putExtra("long1", latLongs[1]);
        intent.putExtra("lat2", latLongs[2]);
        intent.putExtra("long2", latLongs[3]);
        startActivity(intent);
    }

    public void onNavigation(View view)
    {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("start", start);
        intent.putExtra("end", end);
        intent.putExtra("lat1", latLongs[0]);
        intent.putExtra("long1", latLongs[1]);
        intent.putExtra("lat2", latLongs[2]);
        intent.putExtra("long2", latLongs[3]);
        startActivity(intent);
    }


}
