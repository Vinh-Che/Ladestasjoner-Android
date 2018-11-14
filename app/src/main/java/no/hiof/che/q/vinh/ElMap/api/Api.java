package no.hiof.che.q.vinh.ElMap.api;

import no.hiof.che.q.vinh.ElMap.model.Charger;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    //Base url
    String ELBIL_URL = "https://firebasestorage.googleapis.com/v0/b/restaurantfinder-ea005.appspot.com/o/";

    //https://firebasestorage.googleapis.com/v0/b/restaurantfinder-ea005.appspot.com/o/elbil.json?alt=media&token=93d43010-0873-4715-bf3e-677803dd144f

    //Places
    @GET("elbil.json")
    Call<Charger> getCharger(
            @Query("alt") String location,
            @Query("token") String radius
    );

}
