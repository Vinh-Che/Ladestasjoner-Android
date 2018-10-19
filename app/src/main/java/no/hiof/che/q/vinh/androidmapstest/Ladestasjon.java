package no.hiof.che.q.vinh.androidmapstest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;

public class Ladestasjon extends AppCompatActivity {
    ImageView img;
    Bitmap bMap;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ladestasjon);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getData();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void getData() {

        mDatabase = FirebaseDatabase.getInstance().getReference().child("chargerstations");


        ValueEventListener chargeListener = (new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nobilUrl = "http://nobil.no/img/ladestasjonbilder/";
                String lsName = getIntent().getStringExtra("name");

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = (String)ds.child("csmd").child("name").getValue();
                    if ( name.equals(lsName)) {
                        String imageURL = ds.child("csmd").child("Image").getValue().toString();

                        TextView txtNede = findViewById(R.id.tView1);
                        TextView txtOppe = findViewById(R.id.tView2);
                        ImageView iView = findViewById(R.id.imageView);

                        String lsCity = ds.child("csmd").child("City").getValue().toString();

                        txtOppe.setText(lsName);
                        txtNede.setText(lsCity);

                        if (imageURL.equals("Kommer") || imageURL.equals("no.image.swg")) {
                            // Sett inn en placeholder senere
                            Log.d("ingenbilde", "Ingen bilde ...");
                        } else {
                            new GetImageFromURL(iView).execute(nobilUrl + imageURL);
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

    }



    public class GetImageFromURL extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public GetImageFromURL(ImageView imgV){
            this.imageView = imgV;
        }

        @Override
        protected Bitmap doInBackground(String... url) {

            String urldisplay = url[0];
            img = null;
                try {
                    InputStream src = new java.net.URL(urldisplay).openStream();
                    bMap = BitmapFactory.decodeStream(src);
                } catch (Exception e) {
                    e.printStackTrace();
             }
                return bMap;
            }


        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bMap);
        }
    }



}
