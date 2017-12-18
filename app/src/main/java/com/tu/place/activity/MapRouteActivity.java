package com.tu.place.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.tu.place.R;
import com.tu.place.controller.GMapV2Direction;
import com.tu.place.controller.GMapV2DirectionAsyncTask;
import com.tu.place.controller.MapController;
import com.tu.place.fragment.PlaceFragment;
import com.tu.place.model.Place;

import org.w3c.dom.Document;

import java.util.ArrayList;

/**
 * Created by hien.huynhtran2 on 11/27/2017.
 */

public class MapRouteActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener{
    public static GoogleMap mGoogleMap;
    private MapController mapController;
    private SupportMapFragment supportMapFragment;

    private PlaceFragment placeFragment = new PlaceFragment();
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private ImageView imAvatar;
    private Place placeChoose;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initApp();

        int id = getIntent().getExtras().getInt("ID_PLACE");
        placeChoose = MainActivity2.arrPlace.get(id);


}

    private void initApp() {
        initMap();
        initViews();
        initFragment();
        initDrawerLayout();
    }

    private void initDrawerLayout() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,null,R.string.app_name,R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
    }

    private void initViews() {

    }

    private void initMap() {
//        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        GMapV2Direction md = new GMapV2Direction();
        this.route(new LatLng(MainActivity2.myLocation.getLatitude(), MainActivity2.myLocation.getLongitude()), new LatLng(placeChoose.getLatitu(),placeChoose.getLongitu()),
                GMapV2Direction.MODE_WALKING);

        LatLng latln = new LatLng(placeChoose.getLatitu(), placeChoose.getLongitu());
        CameraPosition cameraPosition = new CameraPosition(latln, 15, 0, 0);
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    protected void route(LatLng sourcePosition, LatLng destPosition, String mode) {
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                try {
                    Document doc = (Document) msg.obj;
                    GMapV2Direction md = new GMapV2Direction();
                    ArrayList<LatLng> directionPoint = md.getDirection(doc);
                    PolylineOptions rectLine = new PolylineOptions().width(15).color(Color.RED);

                    for (int i = 0; i < directionPoint.size(); i++) {
                        rectLine.add(directionPoint.get(i));
                    }
                    Polyline polylin = mGoogleMap.addPolyline(rectLine);md.getDurationText(doc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ;
        };

        new GMapV2DirectionAsyncTask(handler, sourcePosition, destPosition, GMapV2Direction.MODE_DRIVING).execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnReload:
                mapController.loadPlace();
                break;
            case R.id.imAvatar:
                Intent intent = new Intent(this,ChangeInfoActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void startActivityWithTaget(Class target) {
        Intent intent = new Intent(this, target);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            LinearLayout control = (LinearLayout) findViewById(R.id.control);
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, control, "control");
            startActivity(intent, compat.toBundle());
        } else {
            startActivity(intent);
        }
    }


    private void initFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.show, R.anim.exit);
        transaction.add(R.id.info_place, placeFragment);
        transaction.hide(placeFragment);
        transaction.commit();
    }

    public void showFragment(Place place) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.show, R.anim.exit);
        transaction.show(placeFragment);
        transaction.commit();
        placeFragment.setData(place);
    }

    public void hideFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.show, R.anim.exit);
        transaction.hide(placeFragment);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }
}
