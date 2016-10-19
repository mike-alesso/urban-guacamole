package com.example.michael.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.michael.myapplication.models.Assessment;
import com.example.michael.myapplication.models.Note;

import java.util.ArrayList;

/**
 * Created by michael on 9/4/16.
 */
    public class CustomNoteAdapter extends ArrayAdapter<Note> {
    private ArrayList<Note> note;
    public CustomNoteAdapter(Context context, ArrayList<Note> note) {
            super(context, 0, note);
            this.note = note;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Note note = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.note, parent, false);
            }
            // Lookup view for data population
            TextView tvNoteName = (TextView) convertView.findViewById(R.id.tvNoteName);
            // Populate the data into the template view using the data object
            tvNoteName.setText(note.getNoteName());
            // Return the completed view to render on screen
            return convertView;
        }
    }