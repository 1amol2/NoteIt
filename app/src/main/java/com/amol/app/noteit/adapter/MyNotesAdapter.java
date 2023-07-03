package com.amol.app.noteit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import com.amol.app.noteit.R;
import com.amol.app.noteit.model.NoteItem;
import com.amol.app.noteit.utils.OnItemClickListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
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
    NoteItem item = notesList.get(arg1);

    title = notesList.get(arg1).getTitle();
    text = notesList.get(arg1).getText();

    arg0.noteTitle.setText(title);
    arg0.noteText.setText(text);

    arg0.optionBtn.setOnClickListener(
        new View.OnClickListener() {

          @Override
          public void onClick(View arg3) {
            PopupMenu menu = new PopupMenu(arg3.getContext(), arg0.optionBtn);
            menu.getMenuInflater().inflate(R.menu.popup_menu, menu.getMenu());
            menu.setOnMenuItemClickListener(
                new PopupMenu.OnMenuItemClickListener() {

                  @Override
                  public boolean onMenuItemClick(MenuItem arg0) {
                    String id = item.getUid();
                    switch (arg0.getItemId()) {
                      case R.id.item_delete:
                        Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
                        notesList.remove(item);
                        FirebaseFirestore.getInstance()
                            .collection("Notes")
                            .document(item.getUid())
                            .delete();
                        notifyItemRemoved(arg1);
                        return true;
                    }
                    return false;
                  }
                });
            menu.show();
          }
        });
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
    private MaterialButton optionBtn;

    public NotesViewHolder(View v) {
      super(v);

      noteText = v.findViewById(R.id.noteText);
      noteTitle = v.findViewById(R.id.noteTitle);
      optionBtn = v.findViewById(R.id.noteOptionBtn);
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
