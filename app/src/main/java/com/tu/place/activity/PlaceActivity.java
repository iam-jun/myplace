package com.tu.place.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;
import com.tu.place.R;
import com.tu.place.adapter.AdapterPlace;
import com.tu.place.model.Place;

import java.util.ArrayList;

/**
 * Created by SEV_USER on 4/25/2017.
 */

public class PlaceActivity extends NavigationActivity implements AdapterView.OnItemClickListener {
    private ListView lvPlace;
    private AdapterPlace adapterPlace;
    private ArrayList<Place> arrPlace;
    private Location myLocation;
    private GoogleMap googleMap;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        initData();

        myLocation = MainActivity2.mGoogleMap.getMyLocation();

        for (Place place:arrPlace) {
            Location location = new Location("");
            location.setLatitude(place.getLatitu());
            location.setLongitude(place.getLongitu());
            float distace = myLocation.distanceTo(location)/1000;
//            place.setDistance(distace);
        }
        initViews();
    }


    private void initViews() {
//        lvPlace = (ListView) findViewById(R.id.lvPlace);
//        adapterPlace = new AdapterPlace(this,arrPlace);
//        lvPlace.setAdapter(adapterPlace);
//        lvPlace.setOnItemClickListener(this);
    }

    private void initData() {
        arrPlace = new ArrayList<>();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this,NoteActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.panel_place);
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(this,layout,"panel_place");
            startActivity(intent,compat.toBundle());
        }else {
            startActivity(intent);
        }
    }
}
