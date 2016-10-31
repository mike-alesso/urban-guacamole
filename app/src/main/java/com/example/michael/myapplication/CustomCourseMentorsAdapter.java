package com.example.michael.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.michael.myapplication.models.Course;
import com.example.michael.myapplication.models.CourseMentor;

import java.util.ArrayList;

/**
 * Created by michael on 9/4/16.
 */
    public class CustomCourseMentorsAdapter extends ArrayAdapter<CourseMentor> {
    private ArrayList<CourseMentor> mentors;
        public CustomCourseMentorsAdapter(Context context, ArrayList<CourseMentor> mentors) {
            super(context, 0, mentors);
            this.mentors = mentors;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            CourseMentor mentor = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.course_mentor, parent, false);
            }
            // Lookup view for data population
            TextView tvCourseName = (TextView) convertView.findViewById(R.id.tvCourseMentorName);
            TextView tvCourseId = (TextView) convertView.findViewById(R.id.tvCourseMentorId);
            // Populate the data into the template view using the data object
            tvCourseId.setText(String.valueOf(mentor.getCourseId()));
            tvCourseName.setText(mentor.getName());
            // Return the completed view to render on screen
            return convertView;
        }
    }
