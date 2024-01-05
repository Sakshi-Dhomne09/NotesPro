
package com.example.notespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailsActivity extends AppCompatActivity {
    EditText titleEditText,ContentEditText;
    ImageButton saveNotebtn;
    TextView pageTitleTextView,deletenoteTextViewbutton;
    String title,content,docID;
    boolean isEditMode=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        titleEditText=findViewById(R.id.notes_title_text);
        ContentEditText=findViewById(R.id.notes_content_text);
        saveNotebtn=findViewById(R.id.save_note_btn);
        pageTitleTextView=findViewById(R.id.page_title);
        deletenoteTextViewbutton=findViewById(R.id.delete_note_textview_btn);
        //receive data
        title=getIntent().getStringExtra("title");
        content=getIntent().getStringExtra("content");
        docID=getIntent().getStringExtra("docID");

        if (docID != null && !docID.isEmpty() || (title != null && !title.isEmpty()) || (content != null && !content.isEmpty())) {
            isEditMode = true;
        }


        titleEditText.setText(title);
        ContentEditText.setText(content);
        if(isEditMode)
        {
            pageTitleTextView.setText("Edit your note");
            deletenoteTextViewbutton.setVisibility(View.VISIBLE);
        }
deletenoteTextViewbutton.setOnClickListener((v)->deletenotefromfirebase());
        saveNotebtn.setOnClickListener((v)->saveNote());
    }
    void saveNote()
    {
        String noteTitle=titleEditText.getText().toString();
        String noteContent=ContentEditText.getText().toString();
        if(noteTitle==null || noteTitle.isEmpty())
        {
            titleEditText.setError("Title is required");
            return;
        }
        Note note=new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());

        saveNoteToFirebase(note);
    }
    void saveNoteToFirebase(Note note)
    {
        DocumentReference documentreference;
        if(isEditMode)
        {
            documentreference=Utility.getcollectionreferenceFornotes().document(docID);
        }
        else{
            documentreference=Utility.getcollectionreferenceFornotes().document();
        }



        documentreference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    //note is added
                    Utility.showtoast(NoteDetailsActivity.this,"Notes added successfully");
                    finish();
                }
                else{
                    //note not added
                    Utility.showtoast(NoteDetailsActivity.this,"Failed adding notes");
                }
            }
        });
    }
    void deletenotefromfirebase()
    {
        DocumentReference documentreference;

            documentreference=Utility.getcollectionreferenceFornotes().document(docID);




        documentreference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    //note is deleted
                    Utility.showtoast(NoteDetailsActivity.this,"Notes deleted successfully");
                    finish();
                }
                else{
                    //note not added
                    Utility.showtoast(NoteDetailsActivity.this,"Failed deleting notes");
                }
            }
        });
    }
}