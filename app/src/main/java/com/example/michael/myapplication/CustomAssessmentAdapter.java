package com.example.michael.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.michael.myapplication.models.Assessment;
import com.example.michael.myapplication.models.Term;

import java.util.ArrayList;

/**
 * Created by michael on 9/4/16.
 */
    public class CustomAssessmentAdapter extends ArrayAdapter<Assessment> {
    private ArrayList<Assessment> assessments;
    public CustomAssessmentAdapter(Context context, ArrayList<Assessment> assessments) {
            super(context, 0, assessments);
            this.assessments = assessments;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Assessment assessment = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.assessment, parent, false);
            }
            // Lookup view for data population
            TextView tvAssessmentName = (TextView) convertView.findViewById(R.id.tvAssessmentName);
            TextView tvAssessmentId = (TextView) convertView.findViewById(R.id.tvAssessmentId);
            // Populate the data into the template view using the data object
            tvAssessmentId.setText(String.valueOf(assessment.getAssessmentId()));
            tvAssessmentName.setText(assessment.getAssessmentName());
            // Return the completed view to render on screen
            return convertView;
        }
    }
