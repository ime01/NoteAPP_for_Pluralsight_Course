package com.example.flowz.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

//import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
//    Spinner spinner;
//    ArrayAdapter<CharSequence> adapter;

    public static final String NOTE_POSITION = "com.example.flowz.myapplication.NOTE_POSITION";
    public static final String ORIGINAL_NOTE_COURSE_ID = "com.example.flowz.myapplication.ORIGINAL_NOTE_COURSE_ID";
    public static final String ORIGINAL_NOTE_TITLE = "com.example.flowz.myapplication.ORIGINAL_NOTE_TITLE ";
    public static final String ORIGINAL_NOTE_TEXT  = "com.example.flowz.myapplication.ORIGINAL_NOTE_TEXT ";
    public static final int POSITION_NOT_SET = -1;
    private NoteInfo mNote;
    private boolean misNewNote;
    private Spinner mspinnerCourses;
    private EditText mtextNoteTitle;
    private EditText mtextNoteText;
    private int mnotePosition;
    private int mNotePosition1;
    private int mNotePosition;
    private boolean misCancelling;
    private String mOriginalNoteCourseId;
    private String mOriginalNoteTitle;
    private String mOriginalNoteText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mspinnerCourses = (Spinner) findViewById(R.id.spinner_courses);
        List<CourseInfo> course = DataManager.getInstance().getCourses();
        ArrayAdapter<CourseInfo> adapterCourses = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, course);
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mspinnerCourses.setAdapter(adapterCourses);

        readDisplayStateValues();
        if (savedInstanceState ==null) {
            saveOriginalNoteValue();
        } else {
            restoreOriginalNoteValue(savedInstanceState);
        }

        mtextNoteTitle = (EditText) findViewById(R.id.text_note_text);
        mtextNoteText = (EditText) findViewById(R.id.text_note_text);

        if (!misNewNote)
            displayNote(mspinnerCourses, mtextNoteTitle, mtextNoteText);









//        adapter = ArrayAdapter.createFromResource(this, R.array.List_of_courses, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
//
//                Toast.makeText(getBaseContext(), parent.getItemIdAtPosition(i) + "is selected", Toast.LENGTH_LONG).show();
//            }
//
}

    private void restoreOriginalNoteValue(Bundle savedInstanceState) {
       mOriginalNoteCourseId = savedInstanceState.getString(ORIGINAL_NOTE_COURSE_ID);
       mOriginalNoteTitle = savedInstanceState.getString(ORIGINAL_NOTE_TITLE);
       mOriginalNoteText = savedInstanceState.getString(ORIGINAL_NOTE_TEXT);

    }

    private void saveOriginalNoteValue() {
        if (misNewNote)
            return;
        mOriginalNoteCourseId = mNote.getCourse().getCourseId();
        mOriginalNoteTitle = mNote.getTitle();
        mOriginalNoteText = mNote.getTitle();




    }

    @Override
    protected void onPause() {
        super.onPause();
        if (misCancelling) {
            if  (misNewNote){
                DataManager.getInstance().removeNote(mNotePosition);
            } else {
                storePreviosNoteValues();
            }

        }else
            saveNote();
    }

    private void storePreviosNoteValues() {
        CourseInfo course = DataManager.getInstance().getCourse(mOriginalNoteCourseId);
        mNote.setCourse(course);
        mNote.setTitle(mOriginalNoteTitle);
        mNote.setText(mOriginalNoteText);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ORIGINAL_NOTE_COURSE_ID, mOriginalNoteCourseId);
        outState.putString(ORIGINAL_NOTE_TITLE, mOriginalNoteTitle);
        outState.putString(ORIGINAL_NOTE_TEXT, mOriginalNoteText);
    }

    private void saveNote() {
        mNote.setCourse((CourseInfo) mspinnerCourses.getSelectedItem());
        mNote.setTitle(mtextNoteTitle.getText().toString());
        mNote.setText(mtextNoteText.getText().toString());
    }

    private void displayNote(Spinner spinnerCourses, EditText textNoteTitle, EditText textNoteText) {
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        int coursesIndex = courses.indexOf(mNote.getCourse());
        spinnerCourses.setSelection(coursesIndex);
        textNoteTitle.setText(mNote.getTitle());
        textNoteText.setText(mNote.getText());

    }

    private void readDisplayStateValues() {
        Intent intent = getIntent();
        int position = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);
        misNewNote = position == POSITION_NOT_SET;
        if (misNewNote) {
            createNewNote();


        }else {
            mNote = DataManager.getInstance().getNotes().get(position);
        }
    }

    private void createNewNote() {
           DataManager dm = DataManager.getInstance();
        mNotePosition = dm.createNewNote();
        mNote =dm.getNotes().get(mNotePosition);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_send_mail) {
            sendEmail();
            return true;
        } else if (id == R.id.action_cancel) {

            misCancelling = true;
            finish();
        }


        return super.onOptionsItemSelected(item);

    }

    public void sendEmail() {
        CourseInfo course = (CourseInfo) mspinnerCourses.getSelectedItem();
        String subject = mtextNoteTitle.getText().toString();
        String text = "Checkout what i learned in the Pluralsight course \"" +
                course.getTitle() + "\"\n" + mtextNoteText.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(intent);

    }


//
//    public void ImplicitIntent (View view ) {
//        Intent sendmail = new Intent(Intent.ACTION_SENDTO);
//        sendmail.setData(Uri.parse("send mail to"));
//        startActivity(sendmail);

}



