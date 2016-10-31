package com.example.michael.myapplication;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.michael.myapplication.Helpers.Database;
import com.example.michael.myapplication.Helpers.Utility;
import com.example.michael.myapplication.models.Assessment;
import com.example.michael.myapplication.models.Course;
import com.example.michael.myapplication.models.CourseMentor;
import com.example.michael.myapplication.models.Note;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.ALARM_SERVICE;
import static com.example.michael.myapplication.R.id.lvAssessment;
import static com.example.michael.myapplication.R.id.lvMentor;
import static com.example.michael.myapplication.R.id.lvNote;

/**
 * Created by michael on 9/3/16.
 */
public class CourseDetail extends Fragment {

    View rootView;
    Database helper;
    EditText nameTextField;
    EditText beginDateTextField;
    EditText endDateTextField;
    Switch beginDateReminder;
    Switch endDateReminder;
    Spinner courseStatusSpinner;

    Course course;
    int termId = -1;
    int courseId = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            courseId = bundle.getInt("courseIndex", -1);
            termId = bundle.getInt("termIndex", -1);
        }

        rootView = inflater.inflate(R.layout.course_edit, container, false);

        Button btn_saveCourse = (Button) rootView.findViewById(R.id.BsaveCourse);
        btn_saveCourse.setOnClickListener(
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
                            endDate = sdf.parse(endDateTextField.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        courseStatusSpinner = (Spinner) rootView.findViewById(R.id.SPcourseStatus);
                        int statusIndex = courseStatusSpinner.getSelectedItemPosition();

                        Switch beginSwitch = (Switch) rootView.findViewById(R.id.beginReminderSwitch);
                        boolean startReminder = beginSwitch.isChecked();

                        Switch endSwitch = (Switch) rootView.findViewById(R.id.endReminderSwitch);
                        boolean endReminder = endSwitch.isChecked();

                        if (beginSwitch.isChecked()) {
                            setBeginReminder(startDateCalendar); }
                        else {
                            unSetBeginReminder(startDateCalendar);
                        }

                        if (endSwitch.isChecked()) {
                            setEndReminder(endDateCalendar); }
                        else {
                            unSetEndReminder(endDateCalendar);
                        }

                        if (course != null) {
                            helper.updateCourse(courseId, nameTextField.getText().toString(), beginDateTextField.getText().toString(), endDateTextField.getText().toString(), Course.Status.values()[statusIndex], startReminder, endReminder);
                        } else {
                            Course course = new Course(-1, termId, nameTextField.getText().toString(), beginDate, endDate, Course.Status.values()[statusIndex], startReminder, endReminder);
                            helper.insertCourse(course);
                        }
                        final FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, new Terms());
                        ft.commit();
                    }
                }
            }
        );

        Button btn_deleteCourse=(Button)rootView.findViewById(R.id.BdeleteCourse);
        btn_deleteCourse.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    helper.removeCourse(courseId);
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, new Terms());
                    ft.commit();
                }
            });

        Spinner staticSpinner = (Spinner) rootView.findViewById(R.id.SPcourseStatus);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(getActivity(), R.array.course_status_array,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        staticSpinner.setAdapter(staticAdapter);

        if ((courseId > 0) && (termId > 0)) populateCourseDetail(courseId);
        else populateEmptyCourse();

        populateAssessmentList(courseId);
        populateNoteList(courseId);

        return rootView;
    }

    private void populateEmptyCourse() {
        // Construct the data source
        //Get course by Id
        helper = new Database(getActivity());
        nameTextField = (EditText) rootView.findViewById(R.id.TFcourseDetailName);
        beginDateTextField = (EditText) rootView.findViewById(R.id.TFcourseDetailBeginDate);
        endDateTextField = (EditText) rootView.findViewById(R.id.TFcourseDetailEndDate);

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

    private void populateCourseDetail(int courseId) {
        // Construct the data source
        //Get course by Id
        helper = new Database(getActivity());
        course = helper.GetCourse(courseId);
        nameTextField = (EditText) rootView.findViewById(R.id.TFcourseDetailName);
        beginDateTextField = (EditText) rootView.findViewById(R.id.TFcourseDetailBeginDate);
        endDateTextField = (EditText) rootView.findViewById(R.id.TFcourseDetailEndDate);
        beginDateReminder = (Switch) rootView.findViewById(R.id.beginReminderSwitch);
        endDateReminder = (Switch) rootView.findViewById(R.id.endReminderSwitch);
        courseStatusSpinner = (Spinner) rootView.findViewById(R.id.SPcourseStatus);

        nameTextField.setText(course.getTitle());


        DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yy", Locale.US);

        DateFormat monthFormatter = new SimpleDateFormat("MM", Locale.US);
        DateFormat dayFormatter = new SimpleDateFormat("dd", Locale.US);
        DateFormat yearFormatter = new SimpleDateFormat("yyyy", Locale.US);
        beginDateTextField.setText(dateFormatter.format(course.getStartDate()));
        startDateCalendar.set(Calendar.YEAR, Integer.parseInt(yearFormatter.format(course.getStartDate())));
        startDateCalendar.set(Calendar.MONTH, Integer.parseInt(monthFormatter.format(course.getStartDate())) - 1);
        startDateCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dayFormatter.format(course.getStartDate())));
        beginDateTextField.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), beginDate, startDateCalendar
                        .get(startDateCalendar.YEAR), startDateCalendar.get(startDateCalendar.MONTH),
                        startDateCalendar.get(startDateCalendar.DAY_OF_MONTH)).show();
            }
        });
        endDateTextField.setText(dateFormatter.format(course.getEndDate()));
        endDateCalendar.set(Calendar.YEAR, Integer.parseInt(yearFormatter.format(course.getEndDate())));
        endDateCalendar.set(Calendar.MONTH, Integer.parseInt(monthFormatter.format(course.getEndDate())) - 1);
        endDateCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dayFormatter.format(course.getEndDate())));
        endDateTextField.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), endDate, endDateCalendar
                        .get(endDateCalendar.YEAR), endDateCalendar.get(endDateCalendar.MONTH),
                        endDateCalendar.get(endDateCalendar.DAY_OF_MONTH)).show();
            }
        });
        beginDateReminder.setChecked(course.getStartDateReminder());
        endDateReminder.setChecked(course.getEndDateReminder());

        courseStatusSpinner.setSelection(course.getStatus().ordinal(), true);
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

    private void populateAssessmentList(final int courseId) {
        // Construct the data source
        helper = new Database(getActivity());

        ArrayList<Assessment> arrayOfAssessments = helper.getAllAssessments(courseId);
        // Create the adapter to convert the array to views
        CustomAssessmentAdapter adapter = new CustomAssessmentAdapter(getActivity(), arrayOfAssessments);
        // Attach the adapter to a ListView
        ListView listView = (ListView) rootView.findViewById(lvAssessment);
        listView.setAdapter(adapter);
        listView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putInt("assessmentIndex", position + 1);
                bundle.putInt("courseIndex", courseId);
                bundle.putInt("termIndex", termId);
                //startActivity(newActivity);
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment assessmentDetailFragment = new AssessmentDetail();
                assessmentDetailFragment.setArguments(bundle);
                ft.replace(R.id.content_frame, assessmentDetailFragment);
                ft.commit();
            }
        });

        Utility.setListViewHeightBasedOnChildren(listView);

        Button btn_newAssessment = (Button)rootView.findViewById(R.id.BnewAssessmentButton);

        btn_newAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putInt("courseIndex", courseId);
                bundle.putInt("termIndex", termId);
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment assessmentDetailFragment = new AssessmentDetail();
                assessmentDetailFragment.setArguments(bundle);
                ft.replace(R.id.content_frame, assessmentDetailFragment);
                ft.commit();
            }
        });
    }

    private void populateNoteList(final int courseId) {
        // Construct the data source
        helper = new Database(getActivity());

        ArrayList<Note> arrayOfNotes = helper.getAllNotes(courseId);
        // Create the adapter to convert the array to views
        CustomNoteAdapter adapter = new CustomNoteAdapter(getActivity(), arrayOfNotes);
        // Attach the adapter to a ListView
        ListView listView = (ListView) rootView.findViewById(lvNote);
        listView.setAdapter(adapter);
        listView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putInt("noteIndex", position + 1);
                bundle.putInt("courseIndex", courseId);
                bundle.putInt("termIndex", termId);
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment noteDetailFragment = new NoteDetail();
                noteDetailFragment.setArguments(bundle);
                ft.replace(R.id.content_frame, noteDetailFragment);
                ft.commit();
            }
        });

        Utility.setListViewHeightBasedOnChildren(listView);

        Button btn_newNote = (Button)rootView.findViewById(R.id.BnewNoteButton);

        btn_newNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putInt("courseIndex", courseId);
                bundle.putInt("termIndex", termId);
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment noteDetailFragment = new NoteDetail();
                noteDetailFragment.setArguments(bundle);
                ft.replace(R.id.content_frame, noteDetailFragment);
                ft.commit();
            }
        });
    }

    private void populateCourseMentorList(final int courseId) {
        // Construct the data source
        helper = new Database(getActivity());
        ArrayList<CourseMentor> mentors = new ArrayList<>();
        CourseMentor mentor = helper.GetMentorByCourseId(courseId);
        mentors.add(mentor);
        // Create the adapter to convert the array to views
        CustomCourseMentorsAdapter adapter = new CustomCourseMentorsAdapter(getActivity(), mentors);
        // Attach the adapter to a ListView
        ListView listView = (ListView) rootView.findViewById(lvMentor);
        listView.setAdapter(adapter);
        listView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putInt("courseIndex", courseId);
                bundle.putInt("termIndex", termId);
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment courseMentorDetailFragment = new CourseMentorDetail();
                courseMentorDetailFragment.setArguments(bundle);
                ft.replace(R.id.content_frame, courseMentorDetailFragment);
                ft.commit();
            }
        });

        Utility.setListViewHeightBasedOnChildren(listView);

        Button btn_newCourseMentor = (Button)rootView.findViewById(R.id.BnewMentorButton);

        btn_newCourseMentor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putInt("courseIndex", courseId);
                bundle.putInt("termIndex", termId);
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment courseMentorDetailFragment = new AssessmentDetail();
                courseMentorDetailFragment.setArguments(bundle);
                ft.replace(R.id.content_frame, courseMentorDetailFragment);
                ft.commit();
            }
        });
    }

    private void setBeginReminder(Calendar calendar) {
        Intent myIntent = new Intent(getActivity().getApplicationContext(), CourseNotifyService.class);
        myIntent.putExtra("title", course.getTitle() + "Starts today");
        AlarmManager alarmManager = (AlarmManager)getActivity().getApplicationContext().getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(getActivity().getApplicationContext(), (int) (calendar.getTimeInMillis() / 1000), myIntent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void unSetBeginReminder(Calendar calendar) {
        Intent myIntent = new Intent(getActivity().getApplicationContext(), CourseNotifyService.class);
        AlarmManager alarmManager = (AlarmManager)getActivity().getApplicationContext().getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(getActivity().getApplicationContext(), (int) (calendar.getTimeInMillis() / 1000), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }

    private void setEndReminder(Calendar calendar) {
        Intent myIntent = new Intent(getActivity().getApplicationContext(), CourseNotifyService.class);
        myIntent.putExtra("title", course.getTitle() + "Ends today");
        AlarmManager alarmManager = (AlarmManager)getActivity().getApplicationContext().getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(getActivity().getApplicationContext(), (int) (calendar.getTimeInMillis() / 1000), myIntent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void unSetEndReminder(Calendar calendar) {
        Intent myIntent = new Intent(getActivity().getApplicationContext(), CourseNotifyService.class);
        AlarmManager alarmManager = (AlarmManager)getActivity().getApplicationContext().getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(getActivity().getApplicationContext(), (int) (calendar.getTimeInMillis() / 1000), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }
}
