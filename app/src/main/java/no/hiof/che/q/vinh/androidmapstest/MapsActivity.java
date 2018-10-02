package no.hiof.che.q.vinh.androidmapstest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int MY_PERMISSIONS_REQUEST_CURRENT_LOCATION = 100;
    // Vars
    private GoogleMap mMap;
    public static Location mLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void getPermission(GoogleMap map) {
        mMap = map;
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Gi permission pls", Toast.LENGTH_SHORT).show();

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "hmm?", Toast.LENGTH_SHORT).show();
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_CURRENT_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            mMap.setMyLocationEnabled(true);

            Location location;
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            location = locationManager.getLastKnownLocation(provider);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


            // Funker hvis det ligger en location fra før, hvis den er null kræsjer appen/funker ikke knappen
            // Midlertidig løsning
            if (location != null) {
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                LatLng latLng = new LatLng(lat, lng);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
            }
            else {
                locationPlaceholder();
            }
        }
    }

    // Callback
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CURRENT_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
                    mMap.setMyLocationEnabled(true);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        createMarkersFromJson();
        getPermission(mMap);

    }

    public void locationPlaceholder() {

    }

    // Test klasse, bare for å printe ut ting i logcat
    public void writeJSON() {
        try {
            JSONObject object = null;
            object = new JSONObject(loadJSONFromAsset());
            JSONArray jsonObj = object.getJSONArray("chargerstations");

            ArrayList<HashMap<String, String>> stasjonerListe = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> hMap;


            for (int i = 0; i < jsonObj.length(); i++) {
                JSONObject csmd = jsonObj.getJSONObject(i).getJSONObject("csmd");

                String navn = csmd.getString("name");
                String by = csmd.getString("City");

                // Splitter opp Stringen slik at vi får lat og long stående for seg selv.
                String[] index = csmd.getString("Position").replaceAll("\\(", "").replaceAll("\\)","").split(",");
                Double latitude = Double.parseDouble(index[0]);
                Double longitude = Double.parseDouble(index[1]);

                // Tester
                if(csmd.getString("Municipality") == "GJERSTAD") {
                    Log.d("DETAILS", csmd.getString("name"));
                }
                //Log.d("DETAILS", csmd.getString("Position"));
                //Log.d("DETAILS", "Lat: " + index[0] +
                //                            " Lon: " + index[1]);

                //Add your values in your `ArrayList` as below:

                hMap = new HashMap<String, String>();
                hMap.put("navn", navn);
                hMap.put("by", by);

                stasjonerListe.add(hMap);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void createMarkersFromJson() {
        try {
            JSONObject object = null;
            object = new JSONObject(loadJSONFromAsset());
            JSONArray jsonObj = object.getJSONArray("chargerstations");


                for (int i = 0; i < jsonObj.length(); i++) {

                    JSONObject csmd = jsonObj.getJSONObject(i).getJSONObject("csmd");
                    // Sortering på fylke
                    if(csmd.getString("Municipality").equals("SARPSBORG")) {
                        // Splitter opp Stringen slik at vi får lat og long stående for seg selv.
                        String[] index = csmd.getString("Position").replaceAll("\\(", "").replaceAll("\\)", "").split(",");
                        Double latitude = Double.parseDouble(index[0]);
                        Double longitude = Double.parseDouble(index[1]);


                        LatLng markerJson = new LatLng(latitude, longitude);
                        mMap.addMarker(new MarkerOptions().position(markerJson)
                                .title(csmd.getString("name")));
                    }

                    // Tester
                    //Log.d("DETAILS", csmd.getString("name"));
                    //Log.d("DETAILS", csmd.getString("Position"));
                    //Log.d("DETAILS", "Lat: " + index[0] +
                    //        " Lon: " + index[1]);
                }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("nobil.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
