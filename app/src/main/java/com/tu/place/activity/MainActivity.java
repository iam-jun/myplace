package com.tu.place.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.firebase.database.DataSnapshot;
import com.squareup.picasso.Picasso;
import com.tu.place.R;
import com.tu.place.data.CategoryKeeper;
import com.tu.place.filter.FilterDialogFragment;
import com.tu.place.filter.FilterPresenter;
import com.tu.place.firebase.FirebaseManager;
import com.tu.place.fragment.MapFragment;
import com.tu.place.fragment.PlaceDetailFragment;
import com.tu.place.fragment.PlaceFragment;
import com.tu.place.fragment.PlaceListFragment;
import com.tu.place.model.ArrayList;
import com.tu.place.model.Place;
import com.tu.place.model.User;
import com.tu.place.utils.AppContants;
import com.tu.place.utils.AppUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Huynh Tran Hien on 19/11/2017.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton btnFilter, btnSwitch;
    private PlaceFragment placeFragment;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private View navHeader;
    private ActionBarDrawerToggle toggle;
    private ImageView imAvatar;
    private TextView tvName;
    public static User user;
    private String TAG = "Main_Activity";
    private String current_fragment;
    public PlaceAutocompleteFragment autocompleteFragment;
    public java.util.ArrayList<Place> listPlace;
    public java.util.ArrayList<Place> filterListPlace;
    public java.util.ArrayList<String> filterTypeList;
    public MapFragment mapFragment;
    public PlaceListFragment placeListFragment;
    public Location myLocation;
    public Float radius;
    public String placeKey;
    public int score;
    public LinearLayout lnControl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initApp();
    }

    private void initApp() {
        initViews();
        initFragment();
        initDrawerLayout();
    }

    private void initDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //toggle = new ActionBarDrawerToggle(this,drawerLayout,null,R.string.app_name,R.string.app_name);
        //drawerLayout.addDrawerListener(toggle);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_logout:
                        PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putBoolean(AppContants.PRE_IS_LOGGED_IN, false).commit();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        return true;

                    default:
                        // navItemIndex = 0;

                }
                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.title_activity_maps, R.string.title_activity_maps) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navHeader = navigationView.getHeaderView(0);
        imAvatar = (ImageView) navHeader.findViewById(R.id.imAvatar);
        tvName = (TextView) navHeader.findViewById(R.id.tvName);
        lnControl = (LinearLayout) findViewById(R.id.lnControl);
        btnFilter = (FloatingActionButton) findViewById(R.id.btnFilter);
        btnSwitch = (FloatingActionButton) findViewById(R.id.btnSwitch);
        btnFilter.setOnClickListener(this);
        btnSwitch.setOnClickListener(this);
        imAvatar.setOnClickListener(this);
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        listPlace = new ArrayList<>();
        filterListPlace = new ArrayList<>();
        filterTypeList = new ArrayList<>();
        final CategoryKeeper keeper = CategoryKeeper.getInstance();
        final java.util.ArrayList<String> selectedTypes = new java.util.ArrayList<>();
        selectedTypes.addAll(keeper.getSelectedTypes());
        filterTypeList.clear();
        filterTypeList.addAll(selectedTypes);
        radius = 1000f;
        score = 1;
        placeKey = "";


    }
    public void saveCurrentLocation(double lat, double longi){
        FirebaseManager firebaseManager = new FirebaseManager();
        if(user!=null) {
            user.latitu = lat;
            user.longitu = longi;
            firebaseManager.writeRef(AppContants.FIREBASE_USER_TABLE, user.username, user, new FirebaseManager.Callback() {
                @Override
                public void success(DataSnapshot dataSnapshot) {

                }

                @Override
                public void failed() {

                }
            });
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(User user) {
        tvName.setText(user.name);
        if(!AppUtils.isEmptyString(user.url)) {
            Picasso.with(this).load(user.url).into(imAvatar);
        }
        this.user = user;
    };

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnFilter:
                final FilterDialogFragment dialogFragment = new FilterDialogFragment();
                final FilterPresenter filterPresenter = new FilterPresenter();
                dialogFragment.setPresenter(filterPresenter);
                dialogFragment.show(getFragmentManager(),"dialog_fragment");
                break;
            case R.id.imAvatar:
                Intent intent = new Intent(this,ChangeInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.btnSwitch:
                changeViewMode();
                break;
        }
    }

    public void filterPlace(){
        Log.d(AppContants.TAG, "filterList size "+filterListPlace.size());
        Log.d(AppContants.TAG, "Distance "+radius+" score "+score +" key "+placeKey);
//        filterListPlace.clear();
//        filterListPlace.addAll(listPlace);
        Log.d(AppContants.TAG, "filterList size 1 "+filterListPlace.size());
        Log.d(AppContants.TAG, "listPlace size "+listPlace.size());
        if (!listPlace.isEmpty()){
            filterListPlace.clear();
            for (int i=0; i < listPlace.size(); i++) {
                for (final String filter : filterTypeList){
                    if (filter.trim().equalsIgnoreCase(listPlace.get(i).getContent().trim())){
                        Location location = new Location(listPlace.get(i).getTitle());
                        location.setLatitude(listPlace.get(i).getLatitu());
                        location.setLongitude(listPlace.get(i).getLongitu());
                        Log.d(AppContants.TAG, "Distance "+myLocation.distanceTo(location) +" score "+listPlace.get(i).getScore());
                        if(listPlace.get(i).getScore()>=score){
                            Log.d(AppContants.TAG, listPlace.get(i).getTitle()+" case 1");
                            if(AppUtils.isEmptyString(placeKey)) {
                                filterListPlace.add(listPlace.get(i));
                                Log.d(AppContants.TAG, listPlace.get(i).getTitle()+" case 2");
                            }else {
                                if (AppUtils.checkContains(listPlace.get(i).getAddress(), placeKey) || AppUtils.checkContains(listPlace.get(i).getTitle(), placeKey)) {
                                    filterListPlace.add(listPlace.get(i));
                                    Log.d(AppContants.TAG, listPlace.get(i).getTitle() + " case 3");
                                }
                            }
                        }
                    }
                }
            }
        }
        mapFragment.mapController.drawPlace();
        placeListFragment.showPlace();
        Log.d(AppContants.TAG, "filterList size 2 "+filterListPlace.size());

//        if (!filterListPlace.isEmpty()){
//            foundPlaces.removeAll(placesToRemove);
//        }
    }


    private void changeViewMode(){
        if(current_fragment.equals(mapFragment.getClass().getName())){
            current_fragment = placeListFragment.getClass().getName();
            changeFabBg(android.R.drawable.ic_dialog_map);
            AppUtils.replaceFragmentWithAnimation(getSupportFragmentManager(), placeListFragment);
        }else {
            current_fragment = mapFragment.getClass().getName();
            changeFabBg(android.R.drawable.ic_menu_sort_by_size);
            AppUtils.replaceFragmentWithAnimation(getSupportFragmentManager(), mapFragment);
        }
    }

    private void changeFabBg(int res){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btnSwitch.setImageDrawable(getResources().getDrawable(res, this.getTheme()));
        } else {
            btnSwitch.setImageDrawable(getResources().getDrawable(res));
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
        mapFragment = new MapFragment();
        placeListFragment = new PlaceListFragment();
        current_fragment = mapFragment.getClass().getName();
        AppUtils.replaceFragmentWithAnimation(getSupportFragmentManager(), mapFragment);
    }

    public void showDirection(Place place){
        current_fragment = mapFragment.getClass().getName();
        changeFabBg(android.R.drawable.ic_menu_sort_by_size);
        AppUtils.replaceFragmentWithAnimation(getSupportFragmentManager(), mapFragment);
        mapFragment.showPlace(place);
    }

    public void showFragment(Place place) {
//        placeFragment = new PlaceFragment(place);
//        AppUtils.replaceFragmentWithAnimation(getSupportFragmentManager(), placeFragment);
//        placeFragment.setData(place);
//        AppDialogManager.onShowPlaceDialog(this, place);

        AppUtils.replaceFragmentWithAnimation(getSupportFragmentManager(), new PlaceDetailFragment(place));
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
    public void onBackPressed() {
        super.onBackPressed();
// dont call **super**, if u want disable back button in current screen.
    }
}

