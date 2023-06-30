package com.amol.app.noteit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.amol.app.noteit.R;
import com.amol.app.noteit.model.NoteItem;
import com.amol.app.noteit.utils.OnItemClickListener;
import java.util.ArrayList;
public class MyNotesAdapter extends RecyclerView.Adapter<MyNotesAdapter.NotesViewHolder> {

  private ArrayList<NoteItem> notesList;
  private Context context;
  private OnItemClickListener clickListener;
  private String uid, title, text;

  public MyNotesAdapter(ArrayList<NoteItem> notesList, Context context) {
    this.notesList = notesList;
    this.context = context;
  }

  @Override
  public MyNotesAdapter.NotesViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
    View view = LayoutInflater.from(context).inflate(R.layout.layout_note_item, arg0, false);
    return new NotesViewHolder(view);
  }

  @Override
  public void onBindViewHolder(MyNotesAdapter.NotesViewHolder arg0, int arg1) {

    title = notesList.get(arg1).getTitle();
    text = notesList.get(arg1).getText();

    arg0.noteTitle.setText(title);
    arg0.noteText.setText(text);
  }

  public void setClickListener(OnItemClickListener listener) {
    this.clickListener = listener;
  }

  @Override
  public int getItemCount() {
    return notesList.size();
  }

  public class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView noteText, noteTitle;

    public NotesViewHolder(View v) {
      super(v);
            
      
      noteText = v.findViewById(R.id.noteText);
      noteTitle = v.findViewById(R.id.noteTitle);
            
            v.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      if (clickListener != null) {
        int position = getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
           uid = notesList.get(position).getUid();
          clickListener.onItemClick(position, uid);
        }
      }
    }
  }
}
