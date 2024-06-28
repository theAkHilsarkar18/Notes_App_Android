package com.example.notesappandroid;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private ArrayList<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        notes = new ArrayList<>();
        adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddNoteDialog();
            }
        });

        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                showUpdateDeleteDialog(note);
            }
        });

        loadDummyData();
    }

    private void loadDummyData() {
        notes.add(new Note("Sample Note 1", "This is a description for note 1"));
        adapter.setNotes(notes);
    }

    private void showAddNoteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Note");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_add_note, null, false);
        final EditText inputTitle = viewInflated.findViewById(R.id.edit_text_title);
        final EditText inputDescription = viewInflated.findViewById(R.id.edit_text_description);

        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = inputTitle.getText().toString().trim();
                String description = inputDescription.getText().toString().trim();
                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
                    Toast.makeText(MainActivity.this, "Please enter both title and description", Toast.LENGTH_SHORT).show();
                } else {
                    notes.add(new Note(title, description));
                    adapter.setNotes(notes);
                }
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void showUpdateDeleteDialog(final Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update/Delete Note");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_add_note, null, false);
        final EditText inputTitle = viewInflated.findViewById(R.id.edit_text_title);
        final EditText inputDescription = viewInflated.findViewById(R.id.edit_text_description);

        inputTitle.setText(note.getTitle());
        inputDescription.setText(note.getDescription());

        builder.setView(viewInflated);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = inputTitle.getText().toString().trim();
                String description = inputDescription.getText().toString().trim();
                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
                    Toast.makeText(MainActivity.this, "Please enter both title and description", Toast.LENGTH_SHORT).show();
                } else {
                    note.setTitle(title);
                    note.setDescription(description);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                notes.remove(note);
                adapter.setNotes(notes);
            }
        });

        builder.setNeutralButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
