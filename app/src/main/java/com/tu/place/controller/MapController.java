package com.tu.place.controller;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tu.place.R;
import com.tu.place.activity.MainActivity;
import com.tu.place.activity.MapActivity;
import com.tu.place.activity.PlaceMgrActivity;
import com.tu.place.model.Place;

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
    public Location myLocation;
    private GoogleMap googleMap;
    private Context context;
    private Geocoder geocoder;
    private Marker mMarker;
    private MapActivity mainActivity;
    private Marker newPoint;

    public MapController(GoogleMap googleMap, Context context) {
        this.googleMap = googleMap;
        this.context = context;
        geocoder = new Geocoder(context);
        mainActivity = (MapActivity) context;
    }

    public void loadPlace() {
        arrPlace.clear();
//        arrPlace.add(new Place(0,"Quán gà nướng Út Hân","Ăn uống","none","161, Đường 154, Tân Phú, Quận 9, Hồ Chí Minh, Việt Nam","none", "none",5,10.866729,106.809573));
//        arrPlace.add(new Place(1,"Hope Hotel","Ăn uống","none","49 Đường số 154, Tân Phú, Quận 9, Hồ Chí Minh, Việt Nam","none", "none",5,10.867404,106.808307));
//        arrPlace.add(new Place(2,"Cafe Mộc 47","Ăn uống","none","47/1 Đường 120, Tân Phú, Quận 9, Hồ Chí Minh, Việt Nam","none", "none",5,10.865623,106.805228));
//        arrPlace.add(new Place(3,"Suối Tiên","Tham quan","none","Xa lộ Hà Nội, Tân Phú, Quận 9, Hồ Chí Minh, Việt Nam","none", "none",5,10.863821,106.802020));
//        arrPlace.add(new Place(4,"Nhà Thờ Minh Đức","Tôn giáo","none","154 Đường Số 10, Tân Phú, Hồ Chí Minh, Việt Nam","none", "none",5,10.867174,106.807011));
            arrPlace = MainActivity.arrPlaceResult;
        drawPlace();
    }


    public void initMap() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.setMyLocationEnabled(true);
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

    @Override
    public void onLocationChanged(Location location) {
        myLocation = location;
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latln = new LatLng(latitude, longitude);
        if (mMarker == null) {
            mMarker = addMarker("My location", BitmapDescriptorFactory.HUE_RED, latitude, longitude,"");
            CameraPosition cameraPosition = new CameraPosition(latln, 15, 0, 0);
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        } else {
            mMarker.setPosition(latln);
        }
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

    private void drawPlace(){
        for (Marker marker: arrMarker) {
            marker.remove();
        }
        arrMarker.clear();
        for (Place place:arrPlace) {
            Marker marker = addMarker(place.getTitle(),BitmapDescriptorFactory.HUE_BLUE,place.getLatitu(),place.getLongitu(),place.getAddress());
            marker.setTag(place);
            arrMarker.add(marker);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        myLocation = googleMap.getMyLocation();
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
        float distance = myLocation.distanceTo(location) / 1000;
        place.setDistance(distance);
        mainActivity.showFragment(place);
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mainActivity.hideFragment();
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
