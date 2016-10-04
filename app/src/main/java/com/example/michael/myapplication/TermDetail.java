package com.example.michael.myapplication;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.michael.myapplication.Helpers.Database;
import com.example.michael.myapplication.models.Course;
import com.example.michael.myapplication.models.Term;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.michael.myapplication.R.id.lvCourses;

/**
 * Created by michael on 9/3/16.
 */
public class TermDetail extends Fragment {

    View rootView;
    Database helper;
    EditText nameTextField;
    EditText beginDateTextField;
    EditText endDateTextField;
    Term term;
    int id = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getInt("termIndex", -1);
        }

        rootView = inflater.inflate(R.layout.term_edit, container, false);

        if (id > 0) populateTermDetail(id);
        else populateEmptyTerm();

        populateCoursesList(id);

        Button btn_saveTerm = (Button) rootView.findViewById(R.id.BsaveTerm);
        btn_saveTerm.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    {
                        String myFormat = "MM/dd/yy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        Date beginDate = new Date();
                        Date endDate = new Date();
                        try {
                            beginDate = sdf.parse(beginDateTextField.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        try {
                            endDate = sdf.parse(beginDateTextField.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (term != null) {
                            helper.updateTerm(id, nameTextField.getText().toString(), beginDateTextField.getText().toString(), endDateTextField.getText().toString());
                        } else {
                            Term term = new Term(-1, nameTextField.getText().toString(), beginDate, endDate);
                            helper.insertTerm(nameTextField.getText().toString(), beginDateTextField.getText().toString(), endDateTextField.getText().toString());
                        }
                        //Intent i = new Intent(TermDetail.this, Terms.class);
                        //startActivity(i);
                        final FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, new Terms());
                        ft.commit();
                    }
                }
            }
        );

        Button btn_deleteTerm=(Button)rootView.findViewById(R.id.BdeleteTerm);
        btn_deleteTerm.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    helper.removeTerm(id);
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, new Terms());
                    ft.commit();
                }
            });

        Button btn_newCourse=(Button)rootView.findViewById(R.id.BnewcourseButton);

        btn_newCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new CourseDetail());
                ft.commit();
            }
        });

        return rootView;
    }


    private void populateEmptyTerm() {
        // Construct the data source
        //Get term by Id
        helper = new Database(getActivity());
        nameTextField = (EditText) rootView.findViewById(R.id.TFtermDetailName);
        beginDateTextField = (EditText) rootView.findViewById(R.id.TFtermDetailBeginDate);
        endDateTextField = (EditText) rootView.findViewById(R.id.TFtermDetailEndDate);

        DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yy", Locale.US);

        DateFormat monthFormatter = new SimpleDateFormat("MM", Locale.US);
        DateFormat dayFormatter = new SimpleDateFormat("dd", Locale.US);
        DateFormat yearFormatter = new SimpleDateFormat("yyyy", Locale.US);
        beginDateTextField.setText(dateFormatter.format(new Date()));
        startDateCalendar.set(Calendar.YEAR, Integer.parseInt(yearFormatter.format(new Date())));
        startDateCalendar.set(Calendar.MONTH, Integer.parseInt(monthFormatter.format(new Date())) - 1);
        startDateCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dayFormatter.format(new Date())));
        beginDateTextField.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), beginDate, startDateCalendar
                        .get(startDateCalendar.YEAR), startDateCalendar.get(startDateCalendar.MONTH),
                        startDateCalendar.get(startDateCalendar.DAY_OF_MONTH)).show();
            }
        });
        endDateTextField.setText(dateFormatter.format(new Date()));
        endDateCalendar.set(Calendar.YEAR, Integer.parseInt(yearFormatter.format(new Date())));
        endDateCalendar.set(Calendar.MONTH, Integer.parseInt(monthFormatter.format(new Date())) - 1);
        endDateCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dayFormatter.format(new Date())));
        endDateTextField.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), endDate, endDateCalendar
                        .get(endDateCalendar.YEAR), endDateCalendar.get(endDateCalendar.MONTH),
                        endDateCalendar.get(endDateCalendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void populateTermDetail(int id) {
        // Construct the data source
        //Get term by Id
        helper = new Database(getActivity());
        term = helper.GetTerm(id);
        nameTextField = (EditText) rootView.findViewById(R.id.TFtermDetailName);
        beginDateTextField = (EditText) rootView.findViewById(R.id.TFtermDetailBeginDate);
        endDateTextField = (EditText) rootView.findViewById(R.id.TFtermDetailEndDate);

        nameTextField.setText(term.getTitle());


        DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yy", Locale.US);

        DateFormat monthFormatter = new SimpleDateFormat("MM", Locale.US);
        DateFormat dayFormatter = new SimpleDateFormat("dd", Locale.US);
        DateFormat yearFormatter = new SimpleDateFormat("yyyy", Locale.US);
        beginDateTextField.setText(dateFormatter.format(term.getStartDate()));
        startDateCalendar.set(Calendar.YEAR, Integer.parseInt(yearFormatter.format(term.getStartDate())));
        startDateCalendar.set(Calendar.MONTH, Integer.parseInt(monthFormatter.format(term.getStartDate())) - 1);
        startDateCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dayFormatter.format(term.getStartDate())));
        beginDateTextField.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), beginDate, startDateCalendar
                        .get(startDateCalendar.YEAR), startDateCalendar.get(startDateCalendar.MONTH),
                        startDateCalendar.get(startDateCalendar.DAY_OF_MONTH)).show();
            }
        });
        endDateTextField.setText(dateFormatter.format(term.getEndDate()));
        endDateCalendar.set(Calendar.YEAR, Integer.parseInt(yearFormatter.format(term.getEndDate())));
        endDateCalendar.set(Calendar.MONTH, Integer.parseInt(monthFormatter.format(term.getEndDate())) - 1);
        endDateCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dayFormatter.format(term.getEndDate())));
        endDateTextField.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), endDate, endDateCalendar
                        .get(endDateCalendar.YEAR), endDateCalendar.get(endDateCalendar.MONTH),
                        endDateCalendar.get(endDateCalendar.DAY_OF_MONTH)).show();
            }
        });
    }


    Calendar startDateCalendar = Calendar.getInstance();
    Calendar endDateCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener beginDate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            startDateCalendar.set(Calendar.YEAR, year);
            startDateCalendar.set(Calendar.MONTH, monthOfYear);
            startDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateBeginLabel();
        }
    };

    DatePickerDialog.OnDateSetListener endDate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            endDateCalendar.set(Calendar.YEAR, year);
            endDateCalendar.set(Calendar.MONTH, monthOfYear);
            endDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateEndLabel();
        }
    };

    private void updateBeginLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        beginDateTextField.setText(sdf.format(startDateCalendar.getTime()));
    }

    private void updateEndLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        endDateTextField.setText(sdf.format(endDateCalendar.getTime()));
    }

    private void populateCoursesList(final int termId) {
        // Construct the data source
        helper = new Database(getActivity());

        ArrayList<Course> arrayOfCourses = helper.getAllCourses(termId);
        // Create the adapter to convert the array to views
        CustomCoursesAdapter adapter = new CustomCoursesAdapter(getActivity(), arrayOfCourses);
        // Attach the adapter to a ListView
        ListView listView = (ListView) rootView.findViewById(lvCourses);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putInt("courseIndex", position + 1);
                bundle.putInt("termIndex", termId);
                //startActivity(newActivity);
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment courseDetailFragment = new CourseDetail();
                courseDetailFragment.setArguments(bundle);
                ft.replace(R.id.content_frame, courseDetailFragment);
                ft.commit();
            }
        });

        Button btn_newCourse = (Button)rootView.findViewById(R.id.BnewcourseButton);

        btn_newCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new CourseDetail());
                ft.commit();
            }
        });
    }
}
