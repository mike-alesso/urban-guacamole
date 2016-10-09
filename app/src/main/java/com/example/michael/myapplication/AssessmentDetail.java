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
import com.example.michael.myapplication.models.Assessment;
import com.example.michael.myapplication.models.Course;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by michael on 9/3/16.
 */
public class AssessmentDetail extends Fragment {

    View rootView;
    Database helper;
    EditText nameTextField;
    EditText goalDateTextField;
    Switch assessmentDateReminder;
    //Spinner courseStatusSpinner;

    Assessment assessment;
    int termId = -1;
    int courseId = -1;
    int assessmentId = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            assessmentId = bundle.getInt("assessmentIndex", -1);
            courseId = bundle.getInt("courseIndex", -1);
            termId = bundle.getInt("termIndex", -1);
        }

        rootView = inflater.inflate(R.layout.assessment_edit, container, false);

        Button btn_saveAssessment = (Button) rootView.findViewById(R.id.BsaveAssessment);
        btn_saveAssessment.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    {
                        String myFormat = "MM/dd/yy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        Date goalDate = new Date();
                        try {
                            goalDate = sdf.parse(goalDateTextField.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Switch reminderSwitch = (Switch) rootView.findViewById(R.id.goalReminderSwitch);
                        boolean assessmentReminder = reminderSwitch.isChecked();

                        Switch typeSwitch = (Switch) rootView.findViewById(R.id.assessmentTypeSwitch);
                        boolean assessmentTypeBool = typeSwitch.isChecked();
                        Assessment.AssessmentType assessmentType;
                        if (assessmentTypeBool)
                        {
                            assessmentType = Assessment.AssessmentType.OBJECTIVE;
                        } else
                        {
                            assessmentType = Assessment.AssessmentType.PERFORMANCE;
                        }
                        if (assessment != null) {
                            helper.updateAssessment(assessmentId , nameTextField.getText().toString(), goalDateTextField.getText().toString(), assessmentReminder, assessmentType);
                        } else {
                            Assessment assessment = new Assessment( -1, courseId, nameTextField.getText().toString(), goalDate, assessmentReminder, assessmentType);
                            helper.insertAssessment(assessment);
                        }
                        final FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, new Terms());
                        ft.commit();
                    }
                }
            }
        );

        Button btn_deleteAssessment=(Button)rootView.findViewById(R.id.BdeleteAssessment);
        btn_deleteAssessment.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    helper.removeAssessment(assessmentId);
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, new Terms());
                    ft.commit();
                }
            });


        if ((courseId > 0) && (termId > 0) && (assessmentId > 0)) populateAssessmentDetail(assessmentId);
        else populateEmptyAssessment();

        return rootView;
    }

    private void populateEmptyAssessment() {
        helper = new Database(getActivity());
        nameTextField = (EditText) rootView.findViewById(R.id.TFassessmentDetailName);
        goalDateTextField = (EditText) rootView.findViewById(R.id.TFassessmentGoalDate);

        DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yy", Locale.US);

        DateFormat monthFormatter = new SimpleDateFormat("MM", Locale.US);
        DateFormat dayFormatter = new SimpleDateFormat("dd", Locale.US);
        DateFormat yearFormatter = new SimpleDateFormat("yyyy", Locale.US);
        goalDateTextField.setText(dateFormatter.format(new Date()));
        goalDateCalendar.set(Calendar.YEAR, Integer.parseInt(yearFormatter.format(new Date())));
        goalDateCalendar.set(Calendar.MONTH, Integer.parseInt(monthFormatter.format(new Date())) - 1);
        goalDateCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dayFormatter.format(new Date())));
        goalDateTextField.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), goalDate, goalDateCalendar
                        .get(goalDateCalendar.YEAR), goalDateCalendar.get(goalDateCalendar.MONTH),
                        goalDateCalendar.get(goalDateCalendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void populateAssessmentDetail(int assessmentId) {
        // Construct the data source
        //Get assessment by Id
        helper = new Database(getActivity());
        assessment = helper.GetAssessment(assessmentId);
        nameTextField = (EditText) rootView.findViewById(R.id.TFassessmentDetailName);
        goalDateTextField = (EditText) rootView.findViewById(R.id.TFassessmentGoalDate);
        assessmentDateReminder = (Switch) rootView.findViewById(R.id.goalReminderSwitch);

        nameTextField.setText(assessment.getAssessmentName());


        DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yy", Locale.US);

        DateFormat monthFormatter = new SimpleDateFormat("MM", Locale.US);
        DateFormat dayFormatter = new SimpleDateFormat("dd", Locale.US);
        DateFormat yearFormatter = new SimpleDateFormat("yyyy", Locale.US);
        goalDateTextField.setText(dateFormatter.format(assessment.getGoalDate()));
        goalDateCalendar.set(Calendar.YEAR, Integer.parseInt(yearFormatter.format(assessment.getGoalDate())));
        goalDateCalendar.set(Calendar.MONTH, Integer.parseInt(monthFormatter.format(assessment.getGoalDate())) - 1);
        goalDateCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dayFormatter.format(assessment.getGoalDate())));
        goalDateTextField.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), goalDate, goalDateCalendar
                        .get(goalDateCalendar.YEAR), goalDateCalendar.get(goalDateCalendar.MONTH),
                        goalDateCalendar.get(goalDateCalendar.DAY_OF_MONTH)).show();
            }
        });

        assessmentDateReminder.setChecked(assessment.getReminder());
    }


    Calendar goalDateCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener goalDate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            goalDateCalendar.set(Calendar.YEAR, year);
            goalDateCalendar.set(Calendar.MONTH, monthOfYear);
            goalDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateGoalLabel();
        }
    };

    private void updateGoalLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        goalDateTextField.setText(sdf.format(goalDateCalendar.getTime()));
    }
}
