package com.tu.place.fragment;

import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.tu.place.R;
import com.tu.place.activity.MainActivity;
import com.tu.place.controller.GMapV2Direction;
import com.tu.place.controller.GMapV2DirectionAsyncTask;
import com.tu.place.controller.MapController;
import com.tu.place.model.Place;
import com.tu.place.utils.AppContants;

import org.w3c.dom.Document;

import java.util.ArrayList;


public class MapFragment extends Fragment implements OnMapReadyCallback, PlaceSelectionListener {


    private OnFragmentInteractionListener mListener;
    public static GoogleMap mGoogleMap;
    public MapController mapController;
    MapView mMapView;
    private GoogleMap googleMap;

    public MapFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);
        ((MainActivity)getActivity()).autocompleteFragment.setOnPlaceSelectedListener(this);
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    return true;
                }
                return false;
            }
        } );
        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    public void showPlace(Place place) {

        mGoogleMap.clear();
        route(new LatLng(((MainActivity)getActivity()).myLocation.getLatitude(), ((MainActivity)getActivity()).myLocation.getLongitude()), new LatLng(place.getLatitu(),place.getLongitu()),
                GMapV2Direction.MODE_WALKING);
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
                    Polyline polylin = mGoogleMap.addPolyline(rectLine);
                    md.getDurationText(doc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ;
        };

        new GMapV2DirectionAsyncTask(handler, sourcePosition, destPosition, mode).execute();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mapController = new MapController(googleMap, getActivity());
        mapController.initMap();
        mapController.listionLocationChange();
        mapController.loadPlace();
//        if(((MainActivity)getActivity()).filterTypeList!=null)
//        ((MainActivity)getActivity()).filterPlace();
        Log.d(AppContants.TAG, "Map ready");
    }

    @Override
    public void onPlaceSelected(com.google.android.gms.location.places.Place place) {
        if(mapController!=null){
            Location location = new Location(place.getId());
            location.setLatitude(place.getLatLng().latitude);
            location.setLongitude(place.getLatLng().longitude);
            mapController.moveToPlace(location);
            mapController.loadPlace();
//            if(((MainActivity)getActivity()).filterTypeList!=null)
//            ((MainActivity)getActivity()).filterPlace();
        }
    }

    @Override
    public void onError(Status status) {

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).lnControl.setVisibility(View.VISIBLE);
        Log.d(AppContants.TAG, "onResume");
//        if(mapController!=null)
//        mapController.drawPlace();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
