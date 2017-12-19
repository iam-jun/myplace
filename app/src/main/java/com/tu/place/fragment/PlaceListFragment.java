package com.tu.place.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tu.place.R;
import com.tu.place.activity.MainActivity;
import com.tu.place.adapter.AdapterPlace;
import com.tu.place.model.Place;
import com.tu.place.utils.AppUtils;

public class PlaceListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private AdapterPlace adapter;

    public PlaceListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_place_list, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
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
        loadPlace(((MainActivity)getActivity()).filterListPlace);

        return rootView;
    }

    public void showPlace(){
        if(adapter!=null)
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).lnControl.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }

    public void loadPlace(java.util.ArrayList<Place> list){
        adapter = new AdapterPlace(list, new AdapterPlace.ItemListener() {
            @Override
            public void onItemClick(int position) {
//                AppDialogManager.onShowPlaceDialog(getActivity(), ((MainActivity)getActivity()).filterListPlace.get(position));
                AppUtils.replaceFragmentWithAnimation(getActivity().getSupportFragmentManager(), new PlaceDetailFragment(((MainActivity)getActivity()).filterListPlace.get(position)));
            }

            @Override
            public void onItemLongClick(int position) {

            }
        });
        recyclerView.setAdapter(adapter);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
