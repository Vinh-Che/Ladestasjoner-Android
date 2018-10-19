package no.hiof.che.q.vinh.ElMap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.support.v7.widget.SearchView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Vars
    private static final int MY_PERMISSIONS_REQUEST_CURRENT_LOCATION = 100;
    private GoogleMap mMap;
    private View mapView;
    private DatabaseReference mDatabase;
    private static FirebaseDatabase firebaseDatabase;
    private Marker marker;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(no.hiof.che.q.vinh.ElMap.R.menu.options_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(no.hiof.che.q.vinh.ElMap.R.id.search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(no.hiof.che.q.vinh.ElMap.R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(no.hiof.che.q.vinh.ElMap.R.id.map);
        mapView = mapFragment.getView();
        mapFragment.getMapAsync(this);

        /*
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
        */

        // Toolbar
        Toolbar myToolbar = findViewById(no.hiof.che.q.vinh.ElMap.R.id.my_toolbar);
        setSupportActionBar(myToolbar);

    }

    private void doMySearch(String query) {
    }


    public void getPermission(GoogleMap map) {
        mMap = map;
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Ingen tilgang, vennligst gi tilgang for å benytte tjenesten", Toast.LENGTH_SHORT).show();

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "Need permission", Toast.LENGTH_SHORT).show();
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_CURRENT_LOCATION);
            }

        } else {
            mMap.setMyLocationEnabled(true);

            Location location;
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            location = locationManager.getLastKnownLocation(provider);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            // Gjeldende location
            if (location != null) {
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                LatLng latLng = new LatLng(lat, lng);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
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
    public void onMapReady(GoogleMap Map) {
        mMap = Map;
        //Placeholder: I tilfelle hvis brukeren ikke har last location known
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(59.913868, 10.752245), 7));

        //Sjekker først om brukeren har gitt appen tillatelse for GPS
        getPermission(mMap);

        //Offline db
        if (firebaseDatabase == null) {
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.setPersistenceEnabled(true);
        }
        // Laster inn data fra database
        getData();

        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {

            // Button View
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            // Nederst til høyre (som Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            // Position på knappene
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 30, 150);

        }
    }

    private void getData() {

        mDatabase = FirebaseDatabase.getInstance().getReference().child("chargerstations");

        ValueEventListener chargeListener = (new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    // Eksempel på sortereing på f.eks. sarpsborg
                    //if (ds.child("csmd").child("Municipality").getValue().equals("SARPSBORG")) {
                        String[] index = ds.child("csmd").child("Position").getValue().toString().replaceAll("\\(", "").replaceAll("\\)", "").split(",");

                        Double latitude = Double.parseDouble(index[0]);
                        Double longitude = Double.parseDouble(index[1]);

                        LatLng markerJson = new LatLng(latitude, longitude);
                        marker = mMap.addMarker(new MarkerOptions()
                                .position(markerJson)
                                .title(ds.child("csmd").child("name").getValue().toString()));
                        marker.setTag(ds);


                    //}

                    //http://nobil.no/img/ladestasjonbilder/55.jpg
                    //imageURL = ds.child("csmd").child("Image").getValue().toString();

                    // Infowindow
                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            Intent intent = new Intent(MapsActivity.this, Ladestasjon.class);
                            // Sender inn ladestasjonens navn for å hente riktig informasjon i Ladestasjon activity
                            intent.putExtra("name", marker.getTitle());
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("firebasefail", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
        mDatabase.addValueEventListener(chargeListener);
        mDatabase.keepSynced(true);
    }


    // Sjekker om det er tilkobling mot firebase
    public void hasConnection() {
        mDatabase  = FirebaseDatabase.getInstance().getReference(".info/connected");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    Log.d("connectionfirebase", "Connected");
                    Toast.makeText(MapsActivity.this, "Connected", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("connectionfirebase", "Disconnected");
                    Toast.makeText(MapsActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });
    }


    // Med asset
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

    // Lese inn JSON fra datadump JSON filen i assets
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
