package no.hiof.che.q.vinh.ElMap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import no.hiof.che.q.vinh.ElMap.adapter.RecyclerViewAdapter;
import no.hiof.che.q.vinh.ElMap.model.markerItem;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Vars
    private static final int MY_PERMISSIONS_REQUEST_CURRENT_LOCATION = 100;
    private static final int MY_AUTO_PLACE = 101;
    private static final int RC_SIGN_IN = 102;


    private static final Object Json = null ;
    private GoogleMap mMap;
    private View mapView;
    private DatabaseReference mDatabase;
    private static FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mAuth;
    private Marker marker;
    private ClusterManager<markerItem> mClusterManager;
    List<Marker> list = new ArrayList<>();
    List<LatLng> latList = new ArrayList<>();
    List<String> fav = new ArrayList<>();
    ProgressBar pbar;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(no.hiof.che.q.vinh.ElMap.R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(no.hiof.che.q.vinh.ElMap.R.id.map);
        mapView = mapFragment.getView();
        mapFragment.getMapAsync(this);

        // Autocomplete searchbar på toppen
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                mMap.animateCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                //Log.i("placeslog", "Place: " + place.getName());
                //Log.i("placeslog", "Rating: " + place.getRating());
                //Log.i("placeslog", "Website: " + place.getWebsiteUri());

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("placeslog", "An error occurred: " + status);
            }
        });

        // Filter - kun addresser i Norge
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setCountry("NO")
                .build();
        autocompleteFragment.setFilter(typeFilter);

        if (firebaseDatabase == null) {
            firebaseDatabase = FirebaseDatabase.getInstance();
            mAuth = FirebaseAuth.getInstance();
            createSignInIntent();
            firebaseDatabase.setPersistenceEnabled(true);

        }

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        if (mMap == null) {
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            FirebaseData fb = new FirebaseData();
            fb.execute(list);
        }



    }

    @Override
    protected void onStart() {
        super.onStart();

        FloatingActionButton floatingAb = findViewById(R.id.fab);
        floatingAb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFav();
            }
        });
    }

    //Starter favoriteActivity
    private void startFav() {
        Intent intent = new Intent(this, FavoriteActivity.class);
        //intent.putExtra("mylist", list.);

        startActivity(intent);
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


        // Cluster
        if (mClusterManager == null) {
            mClusterManager = new ClusterManager<markerItem>(this, mMap);

        }
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnInfoWindowClickListener(mClusterManager.getMarkerManager());
        mMap.setOnMarkerClickListener(mClusterManager.getMarkerManager());


        // Laster inn data fra database


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

    private class FirebaseData extends AsyncTask<List<Marker>, Void, List<Marker>> {



        @Override
        protected void onPreExecute() {
            //findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
            //findViewById(R.id.map).setVisibility(View.INVISIBLE);
            super.onPreExecute();
            Toast.makeText(MapsActivity.this, "Laster ...", Toast.LENGTH_LONG).show();

        }

        @Override
        protected List<Marker> doInBackground(List<Marker>... lists) {
            if (list.size() == 0) {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("chargerstations");
                ValueEventListener chargeListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            // Eksempel på sortereing på f.eks. sarpsborg
                            if (ds.child("csmd").child("Land_code").getValue().equals("NOR")) {
                                //findViewById(R.id.progressbar).setVisibility(View.GONE);
                                //findViewById(R.id.map).setVisibility(View.VISIBLE);

                                String[] index = ds.child("csmd").child("Position").getValue().toString().replaceAll("\\(", "").replaceAll("\\)", "").split(",");

                                Double latitude = Double.parseDouble(index[0]);
                                Double longitude = Double.parseDouble(index[1]);

                                LatLng markerJson = new LatLng(latitude, longitude);
                                marker = mMap.addMarker(new MarkerOptions()
                                        .position(markerJson)
                                        .title(ds.child("csmd").child("name").getValue().toString())
                                        .visible(false));
                                //list.add(marker);

                                //mClusterManager.addItem(new markerItem(latitude, longitude, marker.getTitle(), ds.child("csmd").child("Description_of_location").getValue().toString()));
                                mClusterManager.addItem(new markerItem(latitude, longitude, marker.getTitle()));
                            }


                        }
                        mMap.clear();
                        // Cluster
                        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<markerItem>() {
                            @Override
                            public boolean onClusterClick(Cluster<markerItem> cluster) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(cluster.getPosition()));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
                                return true;
                            }
                        });

                        mMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
                            @Override
                            public void onInfoWindowLongClick(Marker marker) {
                                fav.add(marker.getTitle());


                                Toast.makeText(MapsActivity.this, marker.getTitle(), Toast.LENGTH_SHORT).show();

                            }
                        });




                        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<markerItem>() {
                            @Override
                            public boolean onClusterItemClick(markerItem markerItem) {
                                marker.showInfoWindow();

                                String Distance = "Avstand: " + getDistanceInMeter(currentPosition(), markerItem.getPosition()) + "KM";
                                Toast.makeText(MapsActivity.this, Distance, Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        });


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
                        mClusterManager.cluster();
                        //mMap.setClustering(new ClusteringSettings().enabled(false).addMarkersDynamically(true));

                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Log.w("firebasefail", "loadPost:onCancelled", databaseError.toException());
                        // ...
                    }
                };
                mDatabase.addValueEventListener(chargeListener);
                mDatabase.keepSynced(true);

            }
            return list;
        }
    }


    @SuppressLint("MissingPermission")
    private LatLng currentPosition() {
        LatLng latLng = null;
        Location location;
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        location = locationManager.getLastKnownLocation(provider);

        // Gjeldende location
        if (location != null) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            latLng = new LatLng(lat, lng);
            return latLng;
        } else {
            // En placeholder i tilfelle last location ikke er i telefonen
            return new LatLng(59.913868, 10.752245);
        }


    }

    private String getDistanceInMeter(LatLng start, LatLng end) {
        Location startPoint = new Location("locationA");
        startPoint.setLatitude(start.latitude);
        startPoint.setLongitude(start.longitude);

        Location endPoint = new Location("locationB");
        endPoint.setLatitude(end.latitude);
        endPoint.setLongitude(end.longitude);

        double distance = startPoint.distanceTo(endPoint);
        distance = (float) Math.round(distance * 100) / 100;
        distance = distance / 1000;
        String distanceInKm = String.format("%.2f", distance);
        return distanceInKm;
    }

    public void createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
        // [END auth_fui_create_intent]
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }


}