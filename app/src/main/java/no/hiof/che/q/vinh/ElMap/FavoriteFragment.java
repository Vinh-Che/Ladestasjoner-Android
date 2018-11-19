package no.hiof.che.q.vinh.ElMap;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import no.hiof.che.q.vinh.ElMap.adapter.RecyclerViewAdapter;

public class FavoriteFragment extends DialogFragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }

    public FavoriteFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);

    }



    private void initRecView() {
        RecyclerView recyclerView = getView().findViewById(R.id.rView);
        //mAdapter = new RecyclerViewAdapter(list);
        recyclerView.setAdapter(mAdapter);


    }
}