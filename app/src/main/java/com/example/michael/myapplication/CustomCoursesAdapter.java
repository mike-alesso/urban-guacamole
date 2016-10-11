package com.example.michael.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.michael.myapplication.models.Course;

import java.util.ArrayList;

/**
 * Created by michael on 9/4/16.
 */
    public class CustomCoursesAdapter extends ArrayAdapter<Course> {
    private ArrayList<Course> courses;
        public CustomCoursesAdapter(Context context, ArrayList<Course> courses) {
            super(context, 0, courses);
            this.courses = courses;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Course course = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.course, parent, false);
            }
            // Lookup view for data population
            TextView tvCourseName = (TextView) convertView.findViewById(R.id.tvCourseName);
            TextView tvCourseId = (TextView) convertView.findViewById(R.id.tvCourseId);
            // Populate the data into the template view using the data object
            tvCourseId.setText(String.valueOf(course.getCourseId()));
            tvCourseName.setText(course.getTitle());
            // Return the completed view to render on screen
            return convertView;
        }
    }
