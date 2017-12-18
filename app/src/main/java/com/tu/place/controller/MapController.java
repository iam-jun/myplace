package com.tu.place.controller;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tu.place.R;
import com.tu.place.activity.MainActivity;
import com.tu.place.activity.PlaceMgrActivity;
import com.tu.place.model.Place;
import com.tu.place.utils.AppContants;
import com.tu.place.utils.AppDialogManager;
import com.tu.place.utils.AppUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SEV_USER on 4/14/2017.
 */

public class MapController implements LocationListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnInfoWindowClickListener {
    private ArrayList<Place> arrPlace = new ArrayList<>();
    private ArrayList<Marker> arrMarker = new ArrayList<>();
    private Place place;

    private GoogleMap googleMap;
    private Context context;
    private Geocoder geocoder;
    private Marker mMarker;
    private MainActivity mainActivity;
    private Marker newPoint;

    public MapController(GoogleMap googleMap, Context context) {
        this.googleMap = googleMap;
        this.context = context;
        geocoder = new Geocoder(context);
        mainActivity = (MainActivity) context;
    }

    public void loadPlace() {
        Log.d("Firebase", "getLongitude "+ String.valueOf(mainActivity.myLocation.getLongitude()));
        if(AppUtils.isNetworkAvailable(context)) {
            mainActivity.listPlace.clear();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child(AppContants.FIREBASE_PLACE_TABLE)
//                    .orderByChild(AppContants.FIREBASE_LATITU_COL)
//                    .startAt((int)myLocation.getLatitude())
                    .orderByChild(AppContants.FIREBASE_LONGITU_COL)
                    .startAt(mainActivity.myLocation.getLongitude())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
//                                Log.d("Firebase", dataSnapshot.toString());
                                for (DataSnapshot child: dataSnapshot.getChildren()) {
                                    Place place = child.getValue(Place.class);
                                    Location location = new Location(place.getTitle());
                                    location.setLatitude(place.getLatitu());
                                    location.setLongitude(place.getLongitu());
                                    if(mainActivity.myLocation.distanceTo(location)<=mainActivity.radius)
                                         mainActivity.listPlace.add(place);
                                }
                                mainActivity.filterPlace();
//                                drawPlace();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("Firebase", databaseError.toString());
                            Toast.makeText(context, "Đã có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_LONG).show();
                        }
                    });
        }else Toast.makeText(context, "Vui lòng kiểm tra lại kết nối", Toast.LENGTH_LONG).show();


    }


    public void initMap() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.setMyLocationEnabled(true);

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

        //You can still do this if you like, you might get lucky:
        if (mainActivity.myLocation == null) {
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                mainActivity.myLocation = location;
                mainActivity.saveCurrentLocation(location.getLatitude(), location.getLongitude());
            }
        }
        if (mainActivity.myLocation != null){
            if (mMarker == null) {
                mMarker = addMarker("My location", BitmapDescriptorFactory.HUE_RED, mainActivity.myLocation.getLatitude(), mainActivity.myLocation.getLongitude(), "");
            } else
                mMarker.setPosition(new LatLng(mainActivity.myLocation.getLatitude(), mainActivity.myLocation.getLongitude()));

             googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mainActivity.myLocation.getLatitude(), mainActivity.myLocation.getLongitude()), 15.0f));
        }
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMapClickListener(this);
        googleMap.setOnMapLongClickListener(this);
        googleMap.setOnInfoWindowClickListener(this);
    }

    public void listionLocationChange() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10, 1, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 10, 1, this);
    }

    public void moveToPlace(Location location){
        mainActivity.myLocation = location;
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latln = new LatLng(latitude, longitude);
        if (mMarker == null) {
            mMarker = addMarker("My location", BitmapDescriptorFactory.HUE_RED, latitude, longitude,"");
            //googleMap.moveCamera(CameraUpdateFactory.zoomTo(17.0f));
        } else {
            mMarker.setPosition(latln);
        }
        CameraPosition cameraPosition = new CameraPosition(latln, 15, 0, 0);
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    @Override
    public void onLocationChanged(Location location) {
//        myLocation = location;
//        double latitude = location.getLatitude();
//        double longitude = location.getLongitude();
//        LatLng latln = new LatLng(latitude, longitude);
//        if (mMarker == null) {
//            mMarker = addMarker("My location", BitmapDescriptorFactory.HUE_RED, latitude, longitude,"");
//        } else {
//            mMarker.setPosition(latln);
//        }
//        CameraPosition cameraPosition = new CameraPosition(latln, 15, 0, 0);
//        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private String getLocationNameFromLocation(double latitude, double longitude) {
        String result = "";
        try {
            List<Address> arrAddress = geocoder.getFromLocation(latitude, longitude, 1);
            if (arrAddress.size() == 1) {
                result = arrAddress.get(0).getAddressLine(0);
                result += " - " + arrAddress.get(0).getAddressLine(2);
                result += " - " + arrAddress.get(0).getAddressLine(2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Marker addMarker(String title, float hue, double latitude, double longitude,String address) {
        MarkerOptions options = new MarkerOptions();
        options.title(title);
        options.snippet(address);
        options.icon(BitmapDescriptorFactory.defaultMarker(hue));
        options.position(new LatLng(latitude, longitude));
        return googleMap.addMarker(options);
    }

    public void drawPlace(){
        arrPlace.clear();
        arrPlace.addAll(mainActivity.filterListPlace);
        for (Marker marker: arrMarker) {
            marker.remove();
        }
        arrMarker.clear();
        for (Place place:arrPlace) {
            Marker marker = addMarkerWithIcon(place.getTitle(), AppUtils.getPlaceIcon(context, place.getContent(),place.getScore()),place.getLatitu(),place.getLongitu(),place.getAddress());
            marker.setTag(place);
            arrMarker.add(marker);
        }
    }



    @Override
    public boolean onMarkerClick(Marker marker) {
//        myLocation = googleMap.getMyLocation();
        Double lat = marker.getPosition().latitude;
        Double log = marker.getPosition().longitude;
        if (marker.equals(this.mMarker)) {
            marker.setSnippet(getLocationNameFromLocation(lat,log));
            return false;
        }
        Place place = (Place) marker.getTag();
        Location location = new Location("");
        location.setLatitude(lat);
        location.setLongitude(log);
        float distance = mainActivity.myLocation.distanceTo(location) / 1000;
//        place.setDistance(distance);
        mainActivity.showFragment(place);
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        //mainActivity.hideFragment();
        if (newPoint!=null){
            newPoint.remove();
        }
    }

    @Override
    public void onMapLongClick(LatLng lng) {
        onMapClick(lng);
        String address = getLocationNameFromLocation(lng.latitude,lng.longitude);
        newPoint= addMarkerWithIcon("New place" , R.drawable.ic_newpoint ,lng.latitude, lng.longitude,address);
        newPoint.setSnippet(getLocationNameFromLocation(lng.latitude, lng.longitude));
        newPoint.showInfoWindow();
        AppDialogManager.onShowAddPlaceDialog(context, lng);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (marker.equals(newPoint)){
            Intent intent = new Intent(context, PlaceMgrActivity.class);
            mainActivity.startActivity(intent);
        }
    }

    public Marker addMarkerWithIcon(String title, int icon, double latitude, double longitude,String address) {
        MarkerOptions options = new MarkerOptions();
        options.title(title);
        options.snippet(address);
        options.icon(BitmapDescriptorFactory.fromResource(icon));
        options.position(new LatLng(latitude, longitude));
        return googleMap.addMarker(options);
    }

    public void LoadOnePlace(Place p)
    {
        place = p;
        Marker marker = addMarker(place.getTitle(),BitmapDescriptorFactory.HUE_BLUE,place.getLatitu(),place.getLongitu(),place.getAddress());
        marker.setTag(place);
    }

}
