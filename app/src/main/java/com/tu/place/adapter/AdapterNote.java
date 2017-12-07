package com.tu.place.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tu.place.R;
import com.tu.place.model.Friend;
import com.tu.place.model.Note;

import java.util.ArrayList;

/**
 * Created by SEV_USER on 4/25/2017.
 */

public class AdapterNote extends BaseAdapter{
    private ArrayList<Note> arrNote;

    public AdapterNote(Context context, ArrayList<Note> arrNote) {
        super(context, arrNote);
        this.arrNote = arrNote;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        v = super.getView(position, v, parent);
        TextView tvTitle = (TextView) v.findViewById(R.id.tvNoteTitle);
        TextView tvContent = (TextView) v.findViewById(R.id.tvNoteContent);
        Note note = arrNote.get(position);
        tvTitle.setText(note.getTitle());
        tvContent.setText(note.getContent());
        return v;
    }

    @Override
    protected int animationId() {
        return R.anim.scale;
    }

    @Override
    protected int getIdItem() {
        return R.layout.item_note;
    }
}
