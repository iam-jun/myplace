package com.tu.place.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.tu.place.R;
import com.tu.place.adapter.AdapterPlace;
import com.tu.place.data.CategoryKeeper;
import com.tu.place.filter.FilterContract;
import com.tu.place.filter.FilterDialogFragment;
import com.tu.place.filter.FilterPresenter;
import com.tu.place.model.Place;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by SEV_USER on 4/25/2017.
 */

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, AdapterView.OnItemClickListener, AdapterView.OnClickListener, FilterContract.FilterView {
    public static GoogleMap mGoogleMap;
    private ListView lvPlace;
    private AdapterPlace adapterPlace;
    public static ArrayList<Place> arrPlace;
    public static Location myLocation;
    private ListView listView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    public int radiusFind = 1;
    public TextView radiusTextView;
    public FloatingActionButton radiusBtn;
    public static ArrayList<Place> arrPlaceResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        initData();
        initApp();
        for (Place place : arrPlace) {
            Location location = new Location("");
            location.setLatitude(place.getLatitu());
            location.setLongitude(place.getLongitu());
            float distace = myLocation.distanceTo(location) / 1000;
            place.setDistance(distace);
        }
        initViews();
        listView = (ListView) findViewById(R.id.lvPlace);

    }

    private void initApp() {
        initMap();
        initDrawerLayout();// moi them
    }

    private void initDrawerLayout() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,null,R.string.app_name,R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
    }

    private void initMap() {
        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        boolean network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location location;

        if (network_enabled) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if(location!=null){
                myLocation = location;
            }
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
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

    private void initViews() {
        lvPlace = (ListView) findViewById(R.id.lvPlace);
        adapterPlace = new AdapterPlace(this,arrPlace);
        radiusTextView = (TextView) findViewById(R.id.textViewRadius);
        radiusBtn = (FloatingActionButton) findViewById(R.id.btnReload);
        lvPlace.setAdapter(adapterPlace);
        lvPlace.setOnItemClickListener(this);
        radiusTextView.setText(radiusFind + " km");
        radiusBtn.setOnClickListener(this);
        SortPlaceByRadius(arrPlace);
    }

    private void initData() {
        arrPlace = new ArrayList<>();
        arrPlace.add(new Place(0,"Quán gà nướng Út Hân","Food","none","161, Đường 154, Tân Phú, Quận 9, Hồ Chí Minh, Việt Nam","none", "none",5,10.866729,106.809573));
        arrPlace.add(new Place(1,"Hope Hotel","Hotel","none","49 Đường số 154, Tân Phú, Quận 9, Hồ Chí Minh, Việt Nam","none", "none",5,10.867404,106.808307));
        arrPlace.add(new Place(2,"Cafe Mộc 47","Drink","none","47/1 Đường 120, Tân Phú, Quận 9, Hồ Chí Minh, Việt Nam","none", "none",5,10.865623,106.805228));
        arrPlace.add(new Place(3,"Suối Tiên","Play","none","Xa lộ Hà Nội, Tân Phú, Quận 9, Hồ Chí Minh, Việt Nam","none", "none",5,10.863821,106.802020));
        arrPlace.add(new Place(4,"Lotte Cinema Cộng Hoà","Food","none","20 Cộng Hòa, Phường 12, Tân Bình, Hồ Chí Minh, Việt Nam","none", "none",5,10.800864,106.653084));
        arrPlace.add(new Place(5,"Cafe Panda","Drink","none","21, Đường số 120, Tân Phú, Hồ Chí Minh, Việt Nam","none", "none",5,10.866358, 106.804271));
        arrPlace.add(new Place(6,"Cà Phê Ngộ","Drink","none","Đường số 154, Long Thạnh Mỹ, Hồ Chí Minh, Việt Nam","none", "none",5,10.864662, 106.811713));
        arrPlace.add(new Place(7,"Siêu thị Co.opXtra Thủ Đức","Shop","none","934 QL 1A, Linh Trung, Hồ Chí Minh, Việt Nam","none", "none",5,10.868411, 106.776179));
        arrPlace.add(new Place(8,"Shop Nghèo","Shop","none","75 Nam Cao, Tân Phú, Quận 9, Hồ Chí Minh, Việt Nam","none", "none",5,10.861066, 106.798859));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this,NoteActivity.class);
        Object o = listView.getItemAtPosition(i);
        Place place = (Place) o;
        intent.putExtra("ID_PLACE",place.getId() );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            //LinearLayout layout = (LinearLayout) view.findViewById(R.id.panel_place);
            //ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(this,layout,"panel_place");
            startActivity(intent);
        }
        else {
            startActivity(intent);
        }
    }

    @Override
    public final boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu items for use in the action bar
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.map_action:
                Intent intent = new Intent(this,MapActivity.class);
                startActivity(intent);
                return true;
            case R.id.filter_in_list:
                final FilterDialogFragment dialogFragment = new FilterDialogFragment();
                final FilterPresenter filterPresenter = new FilterPresenter();
                dialogFragment.setPresenter(filterPresenter);
                dialogFragment.show(getFragmentManager(),"dialog_fragment");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public final void onFilterDialogClose(final boolean applyFilter) {
        if (applyFilter) {
            arrPlaceResult = new ArrayList<>(arrPlace);
            arrPlaceResult = (ArrayList<Place>) filterPlaces(arrPlaceResult);
            SortPlaceByRadius(arrPlaceResult);
            adapterPlace = new AdapterPlace(this,arrPlaceResult);
            lvPlace.setAdapter(adapterPlace);
        }

    }

    private static List<Place> filterPlaces(ArrayList<Place> foundPlaces){
        final Collection<Place> placesToRemove = new ArrayList<>();
        final CategoryKeeper keeper = CategoryKeeper.getInstance();
        final List<String> selectedTypes = keeper.getSelectedTypes();
        if (!selectedTypes.isEmpty()){
            for (final Place p: foundPlaces) {
                for (final String filter : selectedTypes){
                    if (filter.equalsIgnoreCase(p.getContent())){
                        placesToRemove.add(p);
                    }
                }
            }
        }
        if (!placesToRemove.isEmpty()){
            foundPlaces.removeAll(placesToRemove);
        }
        //Log.i("FilteredPlaces", "After filtering on categories, there are " + foundPlaces.size());
        return foundPlaces;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnReload:
                radiusFind ++;
                radiusTextView.setText(radiusFind + " km");
                arrPlaceResult = new ArrayList<>(arrPlace);
                SortPlaceByRadius(arrPlaceResult);
                arrPlaceResult = (ArrayList<Place>) filterPlaces(arrPlaceResult);
                break;

        }
    }

    public void SortPlaceByRadius(ArrayList<Place> foundPlaces)
    {
        arrPlaceResult = new ArrayList<>();
        for (Place place : foundPlaces) {
            if(place.getDistance() <= radiusFind)
                arrPlaceResult.add(place);
        }
        adapterPlace = new AdapterPlace(this,arrPlaceResult);
        lvPlace.setAdapter(adapterPlace);
    }

}
