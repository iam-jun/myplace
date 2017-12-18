package com.tu.place.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tu.place.R;
import com.tu.place.adapter.AdapterNote;
import com.tu.place.model.Note;
import com.tu.place.model.Place;

import java.util.ArrayList;

/**
 * Created by SEV_USER on 4/28/2017.
 */

public class NoteActivity extends NavigationActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private GridView gvNote;
    private FloatingActionButton btnAdd;
    private AdapterNote adapterNote;
    private ArrayList<Note> arrNote = new ArrayList<>();
    public static Place placeChoose;

    private ImageView image;
    private TextView tvTitle;
    private TextView tvDistance;
    private TextView tvContent;
    private TextView tvScore;
    private TextView tvAddress;
    private TextView tvInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        int id = getIntent().getExtras().getInt("ID_PLACE");
        placeChoose = MainActivity2.arrPlace.get(id);
        initData();
        initViews();
    }

    private void initData() {
        arrNote.clear();

    }

    private void initViews() {
        //gvNote = (GridView) findViewById(R.id.gvNote);
        adapterNote = new AdapterNote(this, arrNote);
        //gvNote.setAdapter(adapterNote);
        //gvNote.setOnItemClickListener(this);
        btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        this.image = (ImageView) findViewById(R.id.imPlace);
        this.tvTitle = (TextView) findViewById(R.id.tvTitle);
        this.tvDistance = (TextView) findViewById(R.id.tvDistance);
        this.tvScore = (TextView) findViewById(R.id.tvScore);
        this.tvAddress = (TextView) findViewById(R.id.tvAddress);
        this.tvInfo = (TextView) findViewById(R.id.tvInfo);

        this.tvTitle.setText(placeChoose.getTitle());
//        this.tvDistance.setText(Float.toString(placeChoose.getDistance()) + " km");
        this.tvScore.setText(Integer.toString(placeChoose.getScore()) + " điểm");
        this.tvAddress.setText(placeChoose.getAddress());
        this.tvInfo.setText(placeChoose.getInfo());
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Note note = arrNote.get(i);
        Intent intent = new Intent(this, MapRouteActivity.class);
//        intent.putExtra("ID_PLACE",placeChoose.getId() );
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            LinearLayout layout = (LinearLayout) view.findViewById(R.id.panel_note);
//            ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, layout, "panel_note");
//            startActivity(intent, compat.toBundle());
//        } else {
            startActivity(intent);
        //}
    }

    @Override
    public void onClick(View view) {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra("ID_PLACE",placeChoose.getId() );
//        startActivity(intent);
        //Note note = arrNote.get(i);
        Intent intent = new Intent(this, MapRouteActivity.class);
//        intent.putExtra("ID_PLACE",placeChoose.getId() );
//        final Intent intent = new
//                Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?" +
//                "saddr=" + MainActivity2.myLocation.getLatitude() + "," + MainActivity2.myLocation.getLongitude() + "&daddr=" + placeChoose.getLatitu() + "," +
//                placeChoose.getLongitu()));
//        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);
    }
}
