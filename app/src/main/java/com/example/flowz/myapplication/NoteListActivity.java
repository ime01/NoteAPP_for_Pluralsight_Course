package com.example.flowz.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class NoteListActivity extends AppCompatActivity {

    private ArrayAdapter<NoteInfo> mAdapterNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        FloatingActionButton fab =(FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity (new Intent(NoteListActivity.this,MainActivity.class));


            }
        }) ;



         initializeDisplayContent();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapterNotes.notifyDataSetChanged();
    }

    private void initializeDisplayContent() {
        final ListView ListNotes = (ListView) findViewById(R.id.List_notes);

        List<NoteInfo> notes = DataManager.getInstance().getNotes();
        mAdapterNotes = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);

        ListNotes.setAdapter(mAdapterNotes);

        ListNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent (NoteListActivity.this, MainActivity.class );
//                NoteInfo note = (NoteInfo) ListNotes.getItemAtPosition(i);
                intent.putExtra(MainActivity.NOTE_POSITION, i);


                startActivity(intent);

            }
        });


    }

}
