package no.hiof.che.q.vinh.ElMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.GetChars;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.view.SimpleDraweeView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import no.hiof.che.q.vinh.ElMap.model.Chargerstation;
import retrofit2.Retrofit;

public class Ladestasjon extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private ProgressBar progressBar;
    private String nobilUrl = "http://nobil.no/img/ladestasjonbilder/";
    //Extra fra main
    private String lsName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(no.hiof.che.q.vinh.ElMap.R.layout.activity_ladestasjon);
        Toolbar toolbar = findViewById(no.hiof.che.q.vinh.ElMap.R.id.toolbar);
        setSupportActionBar(toolbar);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        getData();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    /*
    private void getData() {

        mDatabase = FirebaseDatabase.getInstance().getReference().child("chargerstations");


        ValueEventListener chargeListener = (new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nobilUrl = "http://nobil.no/img/ladestasjonbilder/";
                //Extra fra main
                String lsName = getIntent().getStringExtra("name");

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (dataSnapshot.exists()) {
                        String name = (String) ds.child("csmd").child("name").getValue();
                        if (name.equals(lsName)) {
                            // Henter bilde strengen til URL'en fra databasen

                            TextView tview = findViewById(R.id.tView);
                            TextView tview1 = findViewById(no.hiof.che.q.vinh.ElMap.R.id.tView1);
                            TextView tview8 = findViewById(no.hiof.che.q.vinh.ElMap.R.id.tView8);
                            TextView tview5 = findViewById(R.id.tView5);
                            TextView tview10 = findViewById(R.id.tView10);
                            TextView tview12 = findViewById(R.id.tView12);
                            TextView tview14 = findViewById(R.id.tView14);
                            TextView tview16 = findViewById(R.id.tView16);
                            TextView tview18 = findViewById(R.id.tView18);
                            CardView kommentar = findViewById(R.id.cvKommentar);
                            CardView kontakt = findViewById(R.id.cvKontakt);

                            // Csmd
                            String lsChargingports = ds.child("csmd").child("Available_charging_points").getValue().toString();
                            String lsStr = ds.child("csmd").child("Street").getValue().toString();
                            String lsZip = ds.child("csmd").child("Zipcode").getValue().toString();
                            String lsCont = ds.child("csmd").child("Contact_info").getValue().toString();
                            String lsComment = ds.child("csmd").child("User_comment").getValue().toString();
                            //Attr
                            String lsPark = ds.child("attr").child("st").child("7").child("attrvalid").getValue().toString();
                            String lsPlace = ds.child("attr").child("st").child("3").child("attrvalid").getValue().toString();
                            String lsAvail = ds.child("attr").child("st").child("2").child("attrvalid").getValue().toString();
                            String lsOpen241 = ds.child("attr").child("st").child("24").child("attrvalid").getValue().toString();
                            String lsOpen242 = ds.child("attr").child("st").child("24").child("attrval").getValue().toString();
                            String lsTime = ds.child("attr").child("st").child("6").child("attrvalid").getValue().toString();
                            String lsTimeH = ds.child("attr").child("st").child("6").child("attrval").getValue().toString();

                            // 1 == ja 2 == nei
                            // Tidsbegrensning
                            if (lsTime.equals("1")) {
                                tview18.setText(lsTimeH);
                            } else {
                                tview18.setText("Nei");
                            }

                            // Døgnåpen
                            if (lsOpen241.equals("1")) {
                                tview16.setText("Ja");
                            } else {
                                tview16.setText(lsOpen242);
                            }
                            // Parkering
                            if (lsPark.equals("1")) {
                                tview12.setText("Ja");
                            } else {
                                tview12.setText("Nei");
                            }
                            // Kommentar
                            if (lsComment.equals("")) {
                                kommentar.setVisibility(View.GONE);
                                tview.setVisibility(View.GONE);
                            } else {
                                tview.setText(lsComment);
                            }

                            // Kontakter
                            if (lsCont.equals("")) {
                                kontakt.setVisibility(View.GONE);
                                tview5.setVisibility(View.GONE);
                            } else {
                                tview5.setText(lsCont);
                            }
                            // Sted
                            switch (lsPlace) {
                                case "1":
                                    tview10.setText("Gate");
                                    break;
                                case "2":
                                    tview10.setText("Parkering garage");
                                    break;
                                case "3":
                                    tview10.setText("Flyplass");
                                    break;
                                case "4":
                                    tview10.setText("Kjøpesenter");
                                    break;
                                case "5":
                                    tview10.setText("transportnav");
                                    break;
                                case "6":
                                    tview10.setText("Hotel & restauranter");
                                    break;
                                case "7":
                                    tview10.setText("Bensinstasjon");
                                    break;
                                default: tview10.setText("");
                                    break;
                            }
                            // Tilgjengelighet
                            switch (lsAvail) {
                                case "1":
                                    tview14.setText("Alle");
                                    break;
                                case "2":
                                    tview14.setText("Besøkende");
                                    break;
                                case "3":
                                    tview14.setText("Ansatte");
                                    break;
                                case "4":
                                    tview14.setText("Etter avtale");
                                    break;
                                case "5":
                                    tview14.setText("Beboende");
                                    break;
                                default: tview14.setText("");
                                    break;
                            }




                            tview1.setText(lsName + "\n" + lsStr + " " + lsZip);
                            tview8.setText(lsChargingports);
                            tview5.setText(lsCont);


                            ConnectivityManager manager = (ConnectivityManager) getApplicationContext()
                                    .getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
                            // Sjekker om det er nett tilgjengelig
                            if (null != activeNetwork) {
                                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                                    SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.my_image_view);
                                    //draweeView.getHierarchy().setProgressBarImage(new ProgressBarDrawable());
                                    draweeView.setImageURI(nobilUrl + ds.child("csmd").child("Image").getValue().toString());
                                }
                            } else {
                                Toast.makeText(Ladestasjon.this, "Ingen nettverk", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
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

    }*/

    private void getData() {

        final TextView tview = findViewById(R.id.tView);
        final TextView tview1 = findViewById(no.hiof.che.q.vinh.ElMap.R.id.tView1);
        final TextView tview8 = findViewById(no.hiof.che.q.vinh.ElMap.R.id.tView8);
        final TextView tview5 = findViewById(R.id.tView5);
        final TextView tview10 = findViewById(R.id.tView10);
        final TextView tview12 = findViewById(R.id.tView12);
        final TextView tview14 = findViewById(R.id.tView14);
        final TextView tview16 = findViewById(R.id.tView16);
        final TextView tview18 = findViewById(R.id.tView18);
        final CardView kommentar = findViewById(R.id.cvKommentar);
        final CardView kontakt = findViewById(R.id.cvKontakt);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final String lsName = getIntent().getStringExtra("name");



        db.collection("charger").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Chargerstation chargerstation = document.toObject(Chargerstation.class);
                            if (chargerstation.getCsmd().getName().equals(lsName)) {

                                CardView kommentar = findViewById(R.id.cvKommentar);
                                CardView kontakt = findViewById(R.id.cvKontakt);

                                String lsComment = chargerstation.getCsmd().getUserComment();
                                String lsContact = chargerstation.getCsmd().getContactInfo();

                                String lsChargeports = chargerstation.getCsmd().getAvailableChargingPoints().toString();
                                String lsPlace = chargerstation.getAttr().getSt().get3().getAttrvalid();
                                String lsPark = chargerstation.getAttr().getSt().get7().getAttrvalid();
                                String lsAvailable = chargerstation.getAttr().getSt().get2().getAttrvalid();

                                String ls241 = chargerstation.getAttr().getSt().get24().getAttrvalid();
                                String ls242 = chargerstation.getAttr().getSt().get24().getAttrval();

                                String lsTime1 = chargerstation.getAttr().getSt().get6().getAttrvalid();
                                String lsTime2 = chargerstation.getAttr().getSt().get6().getAttrval();

                                // 1 == ja 2 == nei
                                // Tidsbegrensning

                            if (lsTime1.equals("1")) {
                                tview18.setText(lsTime2);
                            } else {
                                tview18.setText("Nei");
                            }

                            // Døgnåpen
                            if (ls241.equals("1")) {
                                tview16.setText("Ja");
                            } else {
                                tview16.setText(ls242);
                            }
                            // Parkering
                            if (lsPark.equals("1")) {
                                tview12.setText("Ja");
                            } else {
                                tview12.setText("Nei");
                            }
                            // Kommentar
                            if (lsComment.equals("")) {
                                kommentar.setVisibility(View.GONE);
                                tview.setVisibility(View.GONE);
                            } else {
                                tview.setText(lsComment);
                            }

                            // Kontakter
                            if (lsContact.equals("")) {
                                kontakt.setVisibility(View.GONE);
                                tview5.setVisibility(View.GONE);
                            } else {
                                tview5.setText(lsContact);
                            }
                            // Sted
                            switch (lsPlace) {
                                case "1":
                                    tview10.setText("Gate");
                                    break;
                                case "2":
                                    tview10.setText("Parkering garage");
                                    break;
                                case "3":
                                    tview10.setText("Flyplass");
                                    break;
                                case "4":
                                    tview10.setText("Kjøpesenter");
                                    break;
                                case "5":
                                    tview10.setText("transportnav");
                                    break;
                                case "6":
                                    tview10.setText("Hotel & restauranter");
                                    break;
                                case "7":
                                    tview10.setText("Bensinstasjon");
                                    break;
                                default: tview10.setText("");
                                    break;
                            }
                            // Tilgjengelighet
                            switch (lsAvailable) {
                                case "1":
                                    tview14.setText("Alle");
                                    break;
                                case "2":
                                    tview14.setText("Besøkende");
                                    break;
                                case "3":
                                    tview14.setText("Ansatte");
                                    break;
                                case "4":
                                    tview14.setText("Etter avtale");
                                    break;
                                case "5":
                                    tview14.setText("Beboende");
                                    break;
                                default: tview14.setText("");
                                    break;
                            }



                                tview.setText(lsComment);
                                tview5.setText(lsContact);

                                tview8.setText(lsChargeports);
                                tview10.setText(lsPlace);
                                tview12.setText(lsPark);
                                tview14.setText(lsAvailable);

                                ConnectivityManager manager = (ConnectivityManager) getApplicationContext()
                                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
                                // Sjekker om det er nett tilgjengelig
                                if (null != activeNetwork) {
                                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                                        SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.my_image_view);
                                        draweeView.getHierarchy().setProgressBarImage(new ProgressBarDrawable());
                                        draweeView.setImageURI(nobilUrl + chargerstation.getCsmd().getImage());
                                    }
                                } else {
                                    Toast.makeText(Ladestasjon.this, "Ingen nettverk", Toast.LENGTH_SHORT).show();
                                }



                            }
                        }
                    }  else {
                        Log.d("Query Data", "Data is not valid");
                    }
            }
        });
            }





}
