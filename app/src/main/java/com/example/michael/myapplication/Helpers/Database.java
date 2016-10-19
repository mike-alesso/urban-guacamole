package com.example.michael.myapplication.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Editable;
import android.util.Log;
import com.example.michael.myapplication.models.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by michael on 9/3/16.
 */
public class Database extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 11;
    private static final String DATABASE_NAME = "schoolStore.db";
    private static final String TAG = "storeApp";
    SQLiteDatabase db;

    //Students Table
    private static final String STUDENTS_TABLE_NAME = "students";
    private static final String STUDENTS_COLUMN_NAME = "name";
    private static final String STUDENTS_COLUMN_EMAIL = "email";
    private static final String STUDENTS_COLUMN_USERNAME = "username";
    private static final String STUDENTS_COLUMN_PASSWORD = "password";

    private static final String STUDENTS_TABLE_CREATE = "create table " + STUDENTS_TABLE_NAME +
            "( _id INTEGER PRIMARY KEY, name test not null, email test not null, username test not null, " +
            "password test not null);";

    //public enum AssessmentType {OBJECTIVE, PERFORMANCE}

    //Assessment Table
    private static final String ASSESSMENT_TABLE_NAME = "assessment";
    private static final String ASSESSMENT_COLUMN_COURSE_ID = "course_id";
    private static final String ASSESSMENT_COLUMN_NAME = "name";
    private static final String ASSESSMENT_COLUMN_TYPE = "type";
    private static final String ASSESSMENT_COLUMN_DATE = "goal_date";
    private static final String ASSESSMENT_COLUMN_REMINDER = "reminder";


    private static final String ASSESSMENT_TABLE_CREATE =
            "create table " + ASSESSMENT_TABLE_NAME +
            "( _id INTEGER PRIMARY KEY, "
            + ASSESSMENT_COLUMN_COURSE_ID + " INTEGER not null, "
            + ASSESSMENT_COLUMN_NAME + " text not null, "
            + ASSESSMENT_COLUMN_TYPE + " INTEGER not null, "
            + ASSESSMENT_COLUMN_DATE + " text not null, "
            + ASSESSMENT_COLUMN_REMINDER + " INTEGER text not null);";

    public void insertAssessment(Assessment assessment) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ASSESSMENT_COLUMN_COURSE_ID, assessment.getCourseId());
        values.put(ASSESSMENT_COLUMN_NAME, assessment.getAssessmentName());
        values.put(ASSESSMENT_COLUMN_TYPE, assessment.getType().toString());
        values.put(ASSESSMENT_COLUMN_DATE, assessment.getGoalDate().toString());
        values.put(ASSESSMENT_COLUMN_REMINDER, assessment.getReminder());

        db.insert(ASSESSMENT_TABLE_NAME, null, values);
    }

    public void updateAssessment(int assessmentId, String name, String goalDate, boolean goalReminder, Assessment.AssessmentType type) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ASSESSMENT_COLUMN_NAME, name); //These Fields should be your String values of actual column names
        cv.put(ASSESSMENT_COLUMN_TYPE, type.toString());
        cv.put(ASSESSMENT_COLUMN_DATE, goalDate); //startDate
        cv.put(ASSESSMENT_COLUMN_REMINDER, goalReminder);

        int update = db.update(ASSESSMENT_TABLE_NAME, cv, "_id=" + assessmentId, null);
    }

    public void removeAssessment(int i) {
        db = this.getWritableDatabase();
        int delete = db.delete(ASSESSMENT_TABLE_NAME, "_id=" + i, null);
    }

    public ArrayList<Assessment> getAllAssessments(int courseId) {
        db = this.getReadableDatabase();
        String query = "select _id, " + ASSESSMENT_COLUMN_COURSE_ID + ", " + ASSESSMENT_COLUMN_NAME + ", " + ASSESSMENT_COLUMN_DATE + ", " + ASSESSMENT_COLUMN_REMINDER  +
                ", " + ASSESSMENT_COLUMN_TYPE + " from " + ASSESSMENT_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Assessment> resultList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {

                Assessment tempAssessment = new Assessment();
                DateFormat format = new SimpleDateFormat("MM/dd/yy", Locale.US);
                tempAssessment.setAssessmentId(cursor.getInt(0));
                tempAssessment.setCourseId(cursor.getInt(1));
                tempAssessment.setAssessmentName(cursor.getString(2));
                String goalDate = cursor.getString(3);
                tempAssessment.setReminder(cursor.getInt(4) == 1);
                tempAssessment.setType(Assessment.AssessmentType.valueOf(cursor.getString(5)));
                        //Course.Status.valueOf(cursor.getString(5)));
                //tempCourse.setStartDateReminder(cursor.getInt(5) == 1);
                //tempCourse.setEndDateReminder(cursor.getInt(6) == 1);
                Date goalDateParsed = new Date();
                try {
                    goalDateParsed = format.parse(goalDate);
                } catch (ParseException e) {
                    Log.e(TAG, "Parsing datetime failed", e);
                }
                tempAssessment.setGoalDate(goalDateParsed);
                if (tempAssessment.getCourseId() == courseId) {
                    resultList.add(tempAssessment);
                }
            }
            while (cursor.moveToNext());
        }
        db.close();
        return resultList;
    }

    public Assessment GetAssessment(int id) {
        db = this.getReadableDatabase();
        String query = "select _id, " + ASSESSMENT_COLUMN_NAME + ", " + ASSESSMENT_COLUMN_COURSE_ID + ", " + ASSESSMENT_COLUMN_DATE + ", " + ASSESSMENT_COLUMN_REMINDER + ", " + ASSESSMENT_COLUMN_TYPE + " from " + ASSESSMENT_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        Assessment tempAssessment = new Assessment();
        do{
            DateFormat format = new SimpleDateFormat("MM/dd/yy", Locale.US);
            tempAssessment.setAssessmentId(cursor.getInt(0));
            tempAssessment.setAssessmentName(cursor.getString(1));
            tempAssessment.setCourseId(cursor.getInt(2));
            String goalDate = cursor.getString(3);
            tempAssessment.setReminder(cursor.getInt(4) == 1);
            tempAssessment.setType(Assessment.AssessmentType.valueOf(cursor.getString(5)));
            Date goalDateParsed = new Date();
            try{
                goalDateParsed = format.parse(goalDate);
            } catch (ParseException e) {
                Log.e(TAG, "Parsing datetime failed", e);
            }
            tempAssessment.setGoalDate(goalDateParsed);

            if(cursor.getInt(0) == id){
                break;
            }
        }
        while (cursor.moveToNext());
        db.close();
        return tempAssessment;
    }

    //Course Table
    private static final String COURSE_TABLE_NAME = "course";
    private static final String COURSE_COLUMN_TITLE = "title";
    private static final String COURSE_COLUMN_START_DATE = "start_date";
    private static final String COURSE_COLUMN_END_DATE = "end_date";
    private static final String COURSE_COLUMN_STATUS = "status";
    private static final String COURSE_COLUMN_START_DATE_REMINDER = "start_date_reminder";
    private static final String COURSE_COLUMN_END_DATE_REMINDER = "end_date_reminder";
    private static final String COURSE_COLUMN_TERM_ID = "term_id";


    private static final String COURSE_TABLE_CREATE = "create table " + COURSE_TABLE_NAME +
            "( _id INTEGER PRIMARY KEY, " + COURSE_COLUMN_TERM_ID + " INTEGER not null, " + COURSE_COLUMN_TITLE + " text not null, " + COURSE_COLUMN_START_DATE
            + " text not null, " + COURSE_COLUMN_END_DATE + " text not null, " + COURSE_COLUMN_STATUS + " text not null, " +
            COURSE_COLUMN_START_DATE_REMINDER + " INTEGER not null, " + COURSE_COLUMN_END_DATE_REMINDER + " INTEGER not null);";

    public void insertCourse(Course course) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COURSE_COLUMN_TERM_ID, course.getTermId());
        values.put(COURSE_COLUMN_TITLE, course.getTitle());
        values.put(COURSE_COLUMN_START_DATE, course.getStartDate().toString());
        values.put(COURSE_COLUMN_END_DATE, course.getEndDate().toString());
        values.put(COURSE_COLUMN_STATUS, course.getStatus().toString());
        values.put(COURSE_COLUMN_START_DATE_REMINDER, course.getStartDateReminder());
        values.put(COURSE_COLUMN_END_DATE_REMINDER, course.getEndDateReminder());

        db.insert(COURSE_TABLE_NAME, null, values);
    }

    public void updateCourse(int courseId, String name, String startDate, String endDate, Course.Status status, boolean startReminder, boolean endReminder) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COURSE_COLUMN_TITLE, name); //These Fields should be your String values of actual column names
        cv.put(COURSE_COLUMN_START_DATE, startDate); //startDate
        cv.put(COURSE_COLUMN_END_DATE, endDate);
        cv.put(COURSE_COLUMN_STATUS, status.toString());
        cv.put(COURSE_COLUMN_START_DATE_REMINDER, startReminder);
        cv.put(COURSE_COLUMN_END_DATE_REMINDER, endReminder);

        int update = db.update(COURSE_TABLE_NAME, cv, "_id=" + courseId, null);
    }

    public void removeCourse(int i) {
        db = this.getWritableDatabase();
        int delete = db.delete(COURSE_TABLE_NAME, "_id=" + i, null);
    }

    public ArrayList<Course> getAllCourses(int termId) {
        db = this.getReadableDatabase();
        String query = "select _id, " + COURSE_COLUMN_TERM_ID + ", " + COURSE_COLUMN_TITLE + ", " + COURSE_COLUMN_START_DATE + ", " + COURSE_COLUMN_END_DATE  +
                ", " + COURSE_COLUMN_STATUS  + ", " + COURSE_COLUMN_START_DATE_REMINDER  + ", " + COURSE_COLUMN_END_DATE_REMINDER + " from " + COURSE_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Course> resultList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Course tempCourse = new Course();
                DateFormat format = new SimpleDateFormat("MM/dd/yy", Locale.US);
                tempCourse.setCourseId(cursor.getInt(0));
                tempCourse.setTermId(cursor.getInt(1));
                tempCourse.setTitle(cursor.getString(2));
                String startTime = cursor.getString(3);
                String endTime = cursor.getString(4);
                tempCourse.setStatus(Course.Status.valueOf(cursor.getString(5)));
                tempCourse.setStartDateReminder(cursor.getInt(5) == 1);
                tempCourse.setEndDateReminder(cursor.getInt(6) == 1);
                Date startTimeParsed = new Date();
                try {
                    startTimeParsed = format.parse(startTime);
                } catch (ParseException e) {
                    Log.e(TAG, "Parsing datetime failed", e);
                }
                tempCourse.setStartDate(startTimeParsed);

                Date endTimeParsed = new Date();
                try {
                    endTimeParsed = format.parse(endTime);
                } catch (ParseException e) {
                    Log.e(TAG, "Parsing datetime failed", e);
                }
                tempCourse.setEndDate(endTimeParsed);
                if (tempCourse.getTermId() == termId) {
                    resultList.add(tempCourse);
                }
            }
            while (cursor.moveToNext());
        }
        db.close();
        return resultList;
    }

    public Course GetCourse(int id) {
        db = this.getReadableDatabase();
        String query = "select _id, " + COURSE_COLUMN_TERM_ID + ", " + COURSE_COLUMN_TITLE + ", " + COURSE_COLUMN_START_DATE + ", " + COURSE_COLUMN_END_DATE + ", " + COURSE_COLUMN_STATUS  + ", " + COURSE_COLUMN_START_DATE_REMINDER  + ", " + COURSE_COLUMN_END_DATE_REMINDER + " from " + COURSE_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        Course tempCourse = new Course();
        do{
            DateFormat format = new SimpleDateFormat("MM/dd/yy", Locale.US);
            tempCourse.setCourseId(cursor.getInt(0));
            tempCourse.setTermId(cursor.getInt(1));
            tempCourse.setTitle(cursor.getString(2));
            String startTime = cursor.getString(3);
            String endTime = cursor.getString(4);
            tempCourse.setStatus(Course.Status.valueOf(cursor.getString(5)));
            tempCourse.setStartDateReminder(cursor.getInt(6) == 1);
            tempCourse.setEndDateReminder(cursor.getInt(7) == 1);

            Date startTimeParsed = new Date();
            try{
                startTimeParsed = format.parse(startTime);
            } catch (ParseException e) {
                Log.e(TAG, "Parsing datetime failed", e);
            }
            tempCourse.setStartDate(startTimeParsed);

            Date endTimeParsed = new Date();
            try{
                endTimeParsed = format.parse(endTime);
            } catch (ParseException e) {
                Log.e(TAG, "Parsing datetime failed", e);
            }
            tempCourse.setEndDate(endTimeParsed);

            if(cursor.getInt(0) == id){
                break;
            }

        }
        while (cursor.moveToNext());
        db.close();
        return tempCourse;
    }


    //Course Mentor Table
    private static final String CM_TABLE_NAME = "course_mentor";
    private static final String CM_COLUMN_NAME = "name";
    private static final String CM_COLUMN_EMAIL = "email";
    private static final String CM_COLUMN_PHONE_NUMBER = "phone_number";
    private static final String CM_COLUMN_COURSE_NAME = "course_name";

    private static final String CM_TABLE_CREATE = "create table " + CM_TABLE_NAME +
            "( _id INTEGER PRIMARY KEY, " + CM_COLUMN_NAME + " text not null, " + CM_COLUMN_EMAIL
            + " text not null, " + CM_COLUMN_PHONE_NUMBER + " text not null, " + CM_COLUMN_COURSE_NAME +
            " text not null);";

    public void insertCourseMentor(CourseMentor cm) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CM_COLUMN_NAME, cm.getName());
        values.put(CM_COLUMN_EMAIL, cm.getEmail());
        values.put(CM_COLUMN_PHONE_NUMBER, cm.getPhoneNumber());
        values.put(CM_COLUMN_COURSE_NAME, cm.getCourseName());

        db.insert(CM_TABLE_NAME, null, values);
    }

    //Note Table
    private static final String NOTE_TABLE_NAME = "note";
    private static final String NOTE_COLUMN_ID = "note_id";
    private static final String NOTE_COLUMN_COURSE_ID = "course_id";
    private static final String NOTE_COLUMN_CONTENT = "content";
    private static final String NOTE_COLUMN_NAME = "note_name";
    private static final String NOTE_COLUMN_PHOTO = "note_photo";

    private static final String NOTE_TABLE_CREATE = "create table " + NOTE_TABLE_NAME +
            "( _id INTEGER PRIMARY KEY, " + NOTE_COLUMN_ID + " INTEGER not null, "
            + NOTE_COLUMN_COURSE_ID + " INTEGER not null, "
            + NOTE_COLUMN_CONTENT + " text, "
            + NOTE_COLUMN_NAME + " text,"
            + NOTE_COLUMN_PHOTO + " text);";

    public void insertNote(Note note) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NOTE_COLUMN_ID, note.getNoteId());
        values.put(NOTE_COLUMN_COURSE_ID, note.getCourseId());
        values.put(NOTE_COLUMN_CONTENT, note.getNoteContent());
        values.put(NOTE_COLUMN_NAME, note.getNoteName());
        values.put(NOTE_COLUMN_PHOTO, note.getPhoto());
        db.insert(NOTE_TABLE_NAME, null, values);
    }

    public void updateNote(int noteId, int courseId, String content, String name, String photo) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NOTE_COLUMN_COURSE_ID, courseId); //These Fields should be your String values of actual column names
        cv.put(NOTE_COLUMN_CONTENT, content); //startDate
        cv.put(NOTE_COLUMN_NAME, name);
        cv.put(NOTE_COLUMN_PHOTO, photo);

        int update = db.update(COURSE_TABLE_NAME, cv, "_id=" + noteId, null);
    }

    public void removeNote(int i) {
        db = this.getWritableDatabase();
        int delete = db.delete(NOTE_TABLE_NAME, "_id=" + i, null);
    }

    public ArrayList<Note> getAllNotes(int courseId) {
        db = this.getReadableDatabase();
        String query = "select _id, " + NOTE_COLUMN_COURSE_ID + ", " + NOTE_COLUMN_NAME + ", " + NOTE_COLUMN_CONTENT + ", " + NOTE_COLUMN_PHOTO  + " from " + NOTE_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Note> resultList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Note tempNote = new Note();
                DateFormat format = new SimpleDateFormat("MM/dd/yy", Locale.US);
                tempNote.setNoteId(cursor.getInt(0));
                tempNote.setCourseId(cursor.getInt(1));
                tempNote.setNoteName(cursor.getString(2));
                String noteContent = cursor.getString(3);
                String notePhoto = cursor.getString(4);
                if (tempNote.getCourseId() == courseId) {
                    resultList.add(tempNote);
                }
            }
            while (cursor.moveToNext());
        }
        db.close();
        return resultList;
    }

    public Note GetNote(int id) {
        db = this.getReadableDatabase();
        String query = "select _id, " + NOTE_COLUMN_COURSE_ID + ", " + NOTE_COLUMN_CONTENT + ", " + NOTE_COLUMN_NAME + ", " + NOTE_COLUMN_PHOTO + " from " + NOTE_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        Note tempNote = new Note();
        do{
            tempNote.setNoteId(cursor.getInt(0));
            tempNote.setCourseId(cursor.getInt(1));
            tempNote.setNoteContent(cursor.getString(2));
            tempNote.setNoteName(cursor.getString(3));
            tempNote.setPhotos(cursor.getString(4));
            if(cursor.getInt(0) == id){
                break;
            }
        }
        while (cursor.moveToNext());
        db.close();
        return tempNote;
    }


    //Photo Table
//    private static final String PHOTO_TABLE_NAME = "photo";
//    private static final String PHOTO_COLUMN_ID = "note_id";
//    private static final String PHOTO_COLUMN_FILENAME = "filename";
//
//    private static final String PHOTO_TABLE_CREATE = "create table " + PHOTO_TABLE_NAME +
//            "( _id INTEGER PRIMARY KEY, " + PHOTO_COLUMN_ID + " INTEGER not null, " + PHOTO_COLUMN_FILENAME
//            + " text not null);";
//
//    public void insertPhoto(Photo photo) {
//        db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(PHOTO_COLUMN_ID, photo.getNoteId());
//        values.put(PHOTO_COLUMN_FILENAME, photo.getFileName());
//
//        db.insert(PHOTO_TABLE_NAME, null, values);
//    }

    //Term Table
    private static final String TERM_TABLE_NAME = "term";
    private static final String TERM_COLUMN_NAME = "name";
    private static final String TERM_COLUMN_START_DATE = "start_date"; //Store in sqlite as TEXT
    private static final String TERM_COLUMN_END_DATE = "end_date";

    private static final String TERM_TABLE_CREATE = "create table " + TERM_TABLE_NAME +
            "( _id INTEGER PRIMARY KEY, " + TERM_COLUMN_NAME + " text not null, " + TERM_COLUMN_START_DATE
            + " text not null, " + TERM_COLUMN_END_DATE + " text not null);";

    public void insertTerm(String name, String startDate, String endDate) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TERM_COLUMN_NAME, name);
        values.put(TERM_COLUMN_START_DATE, startDate);
        values.put(TERM_COLUMN_END_DATE, endDate);

        db.insert(TERM_TABLE_NAME, null, values);
    }



    public ArrayList<Term> getAllTerms() {
        db = this.getReadableDatabase();
        String query = "select _id, " + TERM_COLUMN_NAME + ", " + TERM_COLUMN_START_DATE + ", " + TERM_COLUMN_END_DATE + " from " + TERM_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Term> resultList = new ArrayList<Term>();
        if (cursor.moveToFirst()) {
            do {

                Term tempTerm = new Term();
                DateFormat format = new SimpleDateFormat("MM/dd/yy", Locale.US);
                tempTerm.setId(cursor.getInt(0));
                tempTerm.setTitle(cursor.getString(1));
                //tempTerm.setTitle(cursor.getString(1));
                String startTime = cursor.getString(2);

                Date startTimeParsed = new Date();
                try {
                    startTimeParsed = format.parse(startTime);
                } catch (ParseException e) {
                    Log.e(TAG, "Parsing datetime failed", e);
                }
                tempTerm.setStartDate(startTimeParsed);

                String endTime = cursor.getString(3);
                Date endTimeParsed = new Date();
                try {
                    endTimeParsed = format.parse(endTime);
                } catch (ParseException e) {
                    Log.e(TAG, "Parsing datetime failed", e);
                }
                tempTerm.setEndDate(endTimeParsed);


                resultList.add(tempTerm);
//            if(a.equals(username)){
//                b = cursor.getString(0);
//                break;
//            }

            }
            while (cursor.moveToNext());
        }
        db.close();
        return resultList;
    }

    public Database(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(STUDENTS_TABLE_CREATE);
        db.execSQL(ASSESSMENT_TABLE_CREATE);
        db.execSQL(COURSE_TABLE_CREATE);
        db.execSQL(CM_TABLE_CREATE);
        db.execSQL(NOTE_TABLE_CREATE);
        //db.execSQL(PHOTO_TABLE_CREATE);
        db.execSQL(TERM_TABLE_CREATE);
        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query1 = "DROP TABLE IF EXISTS " + STUDENTS_TABLE_NAME;
        db.execSQL(query1);
        String query2 = "DROP TABLE IF EXISTS " + ASSESSMENT_TABLE_NAME;
        db.execSQL(query2);
        String query3 = "DROP TABLE IF EXISTS " + COURSE_TABLE_NAME;
        db.execSQL(query3);
        String query4 = "DROP TABLE IF EXISTS " + CM_TABLE_NAME;
        db.execSQL(query4);
        String query5 = "DROP TABLE IF EXISTS " + NOTE_TABLE_NAME;
        db.execSQL(query5);
        //String query6 = "DROP TABLE IF EXISTS " + PHOTO_TABLE_NAME;
        //db.execSQL(query6);
        String query7 = "DROP TABLE IF EXISTS " + TERM_TABLE_NAME;
        db.execSQL(query7);
        this.onCreate(db);
    }

    public void insertStudent(Student student) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STUDENTS_COLUMN_NAME, student.getName());
        values.put(STUDENTS_COLUMN_EMAIL, student.getEmail());
        values.put(STUDENTS_COLUMN_USERNAME, student.getUsername());
        values.put(STUDENTS_COLUMN_PASSWORD, student.getPassword());

        db.insert(STUDENTS_TABLE_NAME, null, values);
    }

    public String searchPass(String username) {
        db = this.getReadableDatabase();
        String query = "select " + STUDENTS_COLUMN_USERNAME + ", " + STUDENTS_COLUMN_PASSWORD +
                " from " + STUDENTS_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        String a, b;
        b = "Not found";
        do{
            cursor.moveToNext();
            a = cursor.getString(0);

            if(a.equals(username)){
                b = cursor.getString(1);
                break;
            }
        }
        while (cursor.moveToNext());
        db.close();
        return b;
    }

    public Term GetTerm(int id) {
        db = this.getReadableDatabase();
        String query = "select _id, " + TERM_COLUMN_NAME + ", " + TERM_COLUMN_START_DATE + ", " + TERM_COLUMN_END_DATE + " from " + TERM_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        Term tempTerm = new Term();
        do{
            DateFormat format = new SimpleDateFormat("MM/dd/yy", Locale.US);
            tempTerm.setId(cursor.getInt(0));
            tempTerm.setTitle(cursor.getString(1));
            String startTime = cursor.getString(2);

            Date startTimeParsed = new Date();
            try{
                startTimeParsed = format.parse(startTime);
            } catch (ParseException e) {
                Log.e(TAG, "Parsing datetime failed", e);
            }
            tempTerm.setStartDate(startTimeParsed);

            String endTime = cursor.getString(3);
            Date endTimeParsed = new Date();
            try{
                endTimeParsed = format.parse(endTime);
            } catch (ParseException e) {
                Log.e(TAG, "Parsing datetime failed", e);
            }
            tempTerm.setEndDate(endTimeParsed);

            if(cursor.getInt(0) == id){
                break;
            }

        }
        while (cursor.moveToNext());
        db.close();
        return tempTerm;

    }

    public void updateTerm(int id, String name, String startDate, String endDate) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TERM_COLUMN_NAME, name); //These Fields should be your String values of actual column names
        cv.put(TERM_COLUMN_START_DATE, startDate); //startDate
        cv.put(TERM_COLUMN_END_DATE, endDate);

        int update = db.update(TERM_TABLE_NAME, cv, "_id=" + id, null);
    }

    public void removeTerm(int i) {
        db = this.getWritableDatabase();
        int delete = db.delete(TERM_TABLE_NAME, "_id=" + i, null);
    }
}
