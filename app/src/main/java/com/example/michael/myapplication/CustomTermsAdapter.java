package com.example.michael.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.michael.myapplication.models.Term;

import java.util.ArrayList;

/**
 * Created by michael on 9/4/16.
 */
    public class CustomTermsAdapter extends ArrayAdapter<Term> {
        public CustomTermsAdapter(Context context, ArrayList<Term> terms) {
            super(context, 0, terms);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Term term = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.term, parent, false);
            }
            // Lookup view for data population
            TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
            TextView tvID = (TextView) convertView.findViewById(R.id.tvId);
            // Populate the data into the template view using the data object
            tvID.setText(String.valueOf(term.getId()));
            tvName.setText(term.getTitle());
            // Return the completed view to render on screen
            return convertView;
        }
    }
