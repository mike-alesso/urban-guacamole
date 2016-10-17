package com.example.michael.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.michael.myapplication.Helpers.Database;
import com.example.michael.myapplication.models.Assessment;
import com.example.michael.myapplication.models.Note;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by michael on 9/3/16.
 */
public class NoteDetail extends Fragment {

    View rootView;
    Database helper;
    EditText nameTextField;
    EditText contentTextField;
    String picturePath;

    Note note;
    int termId = -1;
    int courseId = -1;
    int assessmentId = -1;
    int noteId = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            courseId = bundle.getInt("courseIndex", -1);
            termId = bundle.getInt("termIndex", -1);
            noteId = bundle.getInt("noteIndex", -1);
        }

        rootView = inflater.inflate(R.layout.note_edit, container, false);

        Button btn_saveNote = (Button) rootView.findViewById(R.id.BsaveNote);
        btn_saveNote.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                {
                    if (note != null) {
                        helper.updateNote(noteId , courseId, contentTextField.getText().toString(),   nameTextField.getText().toString(), picturePath);
                    } else {
                        Note note = new Note( -1, courseId, contentTextField.getText().toString(), nameTextField.getText().toString(), picturePath);
                        helper.insertNote(note);
                    }
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, new Terms());
                    ft.commit();
                }
                }
            }
        );

        Button btn_deleteNote = (Button)rootView.findViewById(R.id.BdeleteNote);
        btn_deleteNote.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    helper.removeNote(noteId);
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, new Terms());
                    ft.commit();
                }
            });


        if ((courseId > 0) && (termId > 0) && (assessmentId > 0) && (noteId > 0)) populateNoteDetail(noteId);
        else populateEmptyNote();

        return rootView;
    }

    private void populateEmptyNote() {
        helper = new Database(getActivity());
        nameTextField = (EditText) rootView.findViewById(R.id.TFassessmentDetailName);
    }

    private void populateNoteDetail(int noteId) {
        // Construct the data source
        //Get assessment by Id
        helper = new Database(getActivity());
        note = helper.GetNote(noteId);
        nameTextField = (EditText) rootView.findViewById(R.id.TFnoteDetailName);
        nameTextField.setText(note.getNoteName());

    }
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
}
