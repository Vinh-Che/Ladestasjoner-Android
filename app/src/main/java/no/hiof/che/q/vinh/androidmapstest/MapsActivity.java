package no.hiof.che.q.vinh.androidmapstest;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

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
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        createMarkersFromJson();
        //writeJSON();
    }
    // Funker ikke

    // Test klasse, bare for å printe ut ting i logcat
    public void writeJSON() {
        try {
            JSONObject object = null;
            object = new JSONObject(loadJSONFromAsset());
            JSONArray jsonObj = object.getJSONArray("chargerstations");

            ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> hMap;

            // -2800 er for å ikke kræsje appen ved at det leses inn for mye data
            for (int i = 0; i < jsonObj.length()-2800; i++) {
                JSONObject csmd = jsonObj.getJSONObject(i).getJSONObject("csmd");

                String navn = csmd.getString("name");
                String by = csmd.getString("City");

                // Splitter opp Stringen slik at vi får lat og long stående for seg selv.
                String[] index = csmd.getString("Position").replaceAll("\\(", "").replaceAll("\\)","").split(",");
                Double latitude = Double.parseDouble(index[0]);
                Double longitude = Double.parseDouble(index[1]);

                // Tester
                //Log.d("DETAILS", csmd.getString("name"));
                //Log.d("DETAILS", csmd.getString("Position"));
                Log.d("DETAILS", "Lat: " + index[0] +
                                            " Lon: " + index[1]);

                //Add your values in your `ArrayList` as below:
                /*hMap = new HashMap<String, String>();
                hMap.put("formule", navn);
                hMap.put("url", by);

                formList.add(hMap);*/
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

            // -2800 er for å ikke kræsje appen ved at det leses inn for mye data
            for (int i = 0; i < jsonObj.length(); i++) {
                JSONObject csmd = jsonObj.getJSONObject(i).getJSONObject("csmd");

                // Splitter opp Stringen slik at vi får lat og long stående for seg selv.
                String[] index = csmd.getString("Position").replaceAll("\\(", "").replaceAll("\\)","").split(",");
                Double latitude = Double.parseDouble(index[0]);
                Double longitude = Double.parseDouble(index[1]);


                LatLng markerJson = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(markerJson)
                                                  .title(csmd.getString("name")));
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


                // Tester
                //Log.d("DETAILS", csmd.getString("name"));
                //Log.d("DETAILS", csmd.getString("Position"));
                Log.d("DETAILS", "Lat: " + index[0] +
                        " Lon: " + index[1]);
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
