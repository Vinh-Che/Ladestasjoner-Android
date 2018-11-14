package no.hiof.che.q.vinh.ElMap.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class markerItem implements ClusterItem {
    private  LatLng mPosition;
    private  String mTitle;
    private String mSnippet;

    public markerItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    public markerItem(double lat, double lng, String title, String snippet) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
    }

    public markerItem(double lat, double lng, String title) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }
}

