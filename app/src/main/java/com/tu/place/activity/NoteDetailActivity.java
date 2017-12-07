package com.tu.place.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.tu.place.R;
import com.tu.place.model.Note;

/**
 * Created by SEV_USER on 4/28/2017.
 */

public class NoteDetailActivity extends NavigationActivity{
    public static final String KEY_NOTE = "key_note";
    private Note note;
    private EditText edtTitle;
    private EditText edtContent;
    private LinearLayout panelImageNote;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        initViews();
        displayData();
    }

    private void displayData() {
        Intent intent = getIntent();
        if (intent.getSerializableExtra(KEY_NOTE)!=null) {
            note = (Note) intent.getSerializableExtra(KEY_NOTE);
            edtTitle.setText(note.getTitle());
            edtContent.setText(note.getContent());
            LayoutInflater inflater = LayoutInflater.from(this);
            for (String s:note.getArrImage()){
                View v = inflater.inflate(R.layout.item_image_note,null);
                ImageView imNote = (ImageView) v.findViewById(R.id.imNote);
                Glide.with(v.getContext()).load(s).placeholder(R.drawable.ic_map).error(R.drawable.ic_map).into(imNote);
                panelImageNote.addView(v);
            }
        }
    }

    private void initViews() {
        edtTitle = (EditText) findViewById(R.id.edtTitle);
        edtContent = (EditText) findViewById(R.id.edtContent);
        panelImageNote = (LinearLayout) findViewById(R.id.panel_image);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note,menu);
        if (note == null){
            MenuItem menuItem = menu.findItem(R.id.note_delete);
            menuItem.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }
}
