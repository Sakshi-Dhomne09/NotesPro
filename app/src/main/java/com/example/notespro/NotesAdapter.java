package com.example.notespro;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NotesAdapter extends FirestoreRecyclerAdapter<Note,NotesAdapter.NotesViewHolder> {
    Context context;
    public NotesAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NotesViewHolder holder, int position, @NonNull Note note) {
        holder.titleTextView.setText(note.title);
        holder.ContentTextView.setText(note.content);
        holder.TimestampTextView.setText(Utility.timeStampToString(note.timestamp));

        holder.itemView.setOnClickListener((v)->{
            Intent intent=new Intent(context,NoteDetailsActivity.class);
            intent.putExtra("title",note.title);
            intent.putExtra("content",note.content);
            String docID=this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docID",docID);
            context.startActivity(intent);
        });

    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item,parent,false);
        return new NotesViewHolder(view);
    }

    class NotesViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView,ContentTextView,TimestampTextView;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView=itemView.findViewById(R.id.note_title_text_view);
            ContentTextView=itemView.findViewById(R.id.note_content_text_view);
            TimestampTextView=itemView.findViewById(R.id.note_timestamp_text_view);
        }
    }
}
