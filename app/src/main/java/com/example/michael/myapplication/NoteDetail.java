package com.example.michael.myapplication;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.michael.myapplication.Helpers.Database;
import com.example.michael.myapplication.models.Assessment;
import com.example.michael.myapplication.models.Note;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * Created by michael on 9/3/16.
 */
public class NoteDetail extends Fragment {

    View rootView;
    Database helper;
    EditText nameTextField;
    EditText contentTextField;
    String photoPath;

    Note note;
    int termId = -1;
    int courseId = -1;
    int assessmentId = -1;
    int noteId = -1;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    private static final int TAKE_PHOTO_CODE = 2;

    ImageView mImageView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            // We have access. Life is good.

        } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            // We've been denied once before. Explain why we need the permission, then ask again.
            //getActivity().showDialog(DIALOG_PERMISSION_REASON);
        } else {

            // We've never asked. Just do it.
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            // We have access. Life is good.

        } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {

            // We've been denied once before. Explain why we need the permission, then ask again.
            //getActivity().showDialog(DIALOG_PERMISSION_REASON);
        } else {

            // We've never asked. Just do it.
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }

//        ActivityCompat.requestPermissions((NoteDetail)getActivity(),
//                new String[] {
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                },
//                100);


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
                        helper.updateNote(noteId , courseId, contentTextField.getText().toString(),   nameTextField.getText().toString(), photoPath);
                    } else {
                        Note note = new Note( -1, courseId, contentTextField.getText().toString(), nameTextField.getText().toString(), photoPath);
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
        Button btn_addPhoto = (Button)rootView.findViewById(R.id.BaddPhotoNote);
        btn_addPhoto.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dispatchTakePictureIntent();
                    }
                });

        if ((courseId > 0) && (termId > 0) && (noteId > 0)) populateNoteDetail(noteId);
        else populateEmptyNote();




        return rootView;
    }

    private void populateEmptyNote() {
        helper = new Database(getActivity());
        nameTextField = (EditText) rootView.findViewById(R.id.TFnoteDetailName);
        contentTextField = (EditText) rootView.findViewById(R.id.TFnoteDetailContent);
        mImageView = (ImageView) rootView.findViewById(R.id.thumbnail_display);
    }

    private void populateNoteDetail(int noteId) {
        // Construct the data source
        //Get assessment by Id
        helper = new Database(getActivity());
        note = helper.GetNote(noteId);
        nameTextField = (EditText) rootView.findViewById(R.id.TFnoteDetailName);
        nameTextField.setText(note.getNoteName());
        mImageView = (ImageView) rootView.findViewById(R.id.thumbnail_display);
        contentTextField = (EditText) rootView.findViewById(R.id.TFnoteDetailContent);
        contentTextField.setText(note.getNoteContent());
    }

    private void dispatchTakePictureIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        intent.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri(imageFileName + ".jpg"));
        startActivityForResult(intent, TAKE_PHOTO_CODE);


        //Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        //if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            //File photoFile = null;
            //try {
                //photoFile = createImageFile();
            //} catch (IOException ex) {
                // Error occurred while creating the File
//System.out.println(ex);
            //}


            // Continue only if the File was successfully created
            //if (photoFile != null) {
                //Uri photoURI = FileProvider.getUriForFile(getActivity(), "com.example.michael.myapplication", photoFile);


                //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                //startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

        }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        File f = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Boolean dirExists;
        Boolean fileExists;
        if(f.isDirectory()) {dirExists = true;}
        if(image.isFile()) {fileExists = true;}

        Boolean externalWriteAvail = isExternalStorageWritable();
        Boolean externalReadAvail = isExternalStorageReadable();

        // Save a file: path for use with ACTION_VIEW intents
        photoPath = image.getAbsolutePath();
        return image;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().post(new Runnable() {
                           @Override
                           public void run() {
                               mImageView = (ImageView) rootView.findViewById(R.id.thumbnail_display);
                               if (note != null && note.getPhoto() != null && note.getPhoto() != "")
                               {
                                   photoPath = note.getPhoto();
                                   setPic();
                               }
                           }
                       }
        );
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //mImageView.setImageBitmap((BitmapFactory.decodeFile(file.getPath())));
            //set thumbnail
            setPic();
        }
    }


    private void setPic() {

        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        File file = new File(photoPath);
        if(file.exists()) {
            Log.d("DEBUG", "File exists");
        } else {
            Log.d("DEBUG", "File does not exist");
        }

        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;


        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, bmOptions);
        mImageView.setImageBitmap(bitmap);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //setupContactsPicker();
        } else {

            // We were not granted permission this time, so don't try to show the contact picker
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private Uri getImageUri(String fileName) {
        // Store image in dcim
        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM", fileName);
        Uri imgUri = Uri.fromFile(file);
        photoPath = file.getAbsolutePath();
        return imgUri;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
