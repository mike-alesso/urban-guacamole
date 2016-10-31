package com.example.michael.myapplication;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.michael.myapplication.Helpers.Database;
import com.example.michael.myapplication.models.CourseMentor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by michael on 9/3/16.
 */
public class CourseMentorDetail extends Fragment {

    View rootView;
    Database helper;
    EditText nameTextField;
    EditText phoneNumberTextField;
    EditText emailAddressTextField;

    CourseMentor mentor;

    int termId = -1;
    int courseId = -1;
    int mentorId = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            courseId = bundle.getInt("courseIndex", -1);
            termId = bundle.getInt("termIndex", -1);
        }

        rootView = inflater.inflate(R.layout.course_mentor_edit, container, false);

        Button btn_saveMentor = (Button) rootView.findViewById(R.id.BsaveMentor);
        btn_saveMentor.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    {
                        if (mentor != null) {
                            helper.updateMentor(mentor.getMentorId(), mentor.getCourseId(), nameTextField.getText().toString(), phoneNumberTextField.getText().toString(), emailAddressTextField.getText().toString());
                        } else {
                            mentor = new CourseMentor( -1, courseId, nameTextField.getText().toString(), phoneNumberTextField.getText().toString(), emailAddressTextField.getText().toString());
                            helper.insertCourseMentor(mentor);
                        }
                        final FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, new Terms());
                        ft.commit();
                    }
                }
            }
        );

        Button btn_deleteMentor=(Button)rootView.findViewById(R.id.BdeleteMentor);
        btn_deleteMentor.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    helper.removeMentor(mentorId);
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, new Terms());
                    ft.commit();
                }
            });


        if ((courseId > 0) && (termId > 0) && (mentorId > 0)) populateMentorDetail(mentorId);
        else populateEmptyMentor();

        return rootView;
    }

    private void populateEmptyMentor() {
        helper = new Database(getActivity());
        nameTextField = (EditText) rootView.findViewById(R.id.TFcourseMentorDetailName);
        phoneNumberTextField = (EditText) rootView.findViewById(R.id.TFcourseMentorDetailPhone);
        emailAddressTextField = (EditText) rootView.findViewById(R.id.TFcourseMentorDetailEmail);
    }

    private void populateMentorDetail(int mentorId) {
        // Construct the data source
        //Get mentor by Id
        helper = new Database(getActivity());
        mentor = helper.GetMentor(mentorId);
        nameTextField = (EditText) rootView.findViewById(R.id.TFcourseMentorDetailName);
        phoneNumberTextField = (EditText) rootView.findViewById(R.id.TFcourseMentorDetailPhone);
        emailAddressTextField = (EditText) rootView.findViewById(R.id.TFcourseMentorDetailEmail);

        nameTextField.setText(mentor.getName());
        phoneNumberTextField.setText(mentor.getPhoneNumber());
        emailAddressTextField.setText(mentor.getEmail());
    }
}
