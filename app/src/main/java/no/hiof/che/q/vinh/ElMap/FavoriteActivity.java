package no.hiof.che.q.vinh.ElMap;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import no.hiof.che.q.vinh.ElMap.adapter.RecyclerViewAdapter;
import no.hiof.che.q.vinh.ElMap.model.Chargerstation;

public class FavoriteActivity extends AppCompatActivity {

    private ArrayList<String> CityNames = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        getData();
        RecyclerView recyclerView = findViewById(R.id.rView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(CityNames, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



    }

    private void getData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("charger").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Chargerstation chargerstation = document.toObject(Chargerstation.class);
                        CityNames.add(chargerstation.getCsmd().getName());
                        Log.d("Meeku", "onComplete: " + CityNames.size());
                    }

                }  else {
                    Log.d("Query Data", "Data is not valid");
                }
            }
        });

        Log.d("Meeku", "getData: " + CityNames.size());
    }
}




