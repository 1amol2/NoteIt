package com.amol.app.noteit.activities;

import android.content.Intent;
import android.os.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.amol.app.noteit.adapter.MyNotesAdapter;
import com.amol.app.noteit.databinding.ActivityMainBinding;
import com.amol.app.noteit.model.NoteItem;
import com.amol.app.noteit.utils.OnItemClickListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

  private ActivityMainBinding binding;
  private ArrayList<NoteItem> noteList;
  private MyNotesAdapter mAdapter;
  private Thread thread;
  private CollectionReference cRef;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    setSupportActionBar(binding.toolbar);
    // Initialise Views
    init();
  }

  private void init() {

    cRef = FirebaseFirestore.getInstance().collection("Notes");

    noteList = new ArrayList<NoteItem>();

    LinearLayoutManager llm = new LinearLayoutManager(this);
    llm.setOrientation(LinearLayoutManager.VERTICAL);

    mAdapter = new MyNotesAdapter(noteList, this);

    binding.noteRecyclerView.setLayoutManager(llm);
    binding.noteRecyclerView.setAdapter(mAdapter);

    binding.addBtn.setOnClickListener(
        p1 -> {
          Intent intent = new Intent(MainActivity.this, NoteActivity.class);
          intent.putExtra("Intent", "Intent_B");
          startActivity(intent);
        });

    mAdapter.setClickListener(
        new OnItemClickListener() {
          @Override
          public void onItemClick(int position, String uid) {
            // View note
            String mTitle = noteList.get(position).getTitle().toString();
            String mText = noteList.get(position).getText().toString();

            Intent intent = new Intent(MainActivity.this, NoteActivity.class);
            intent.putExtra("Intent", "Intent_A");
            intent.putExtra("Uid", uid);
            intent.putExtra("Title", mTitle);
            intent.putExtra("Text", mText);
            startActivity(intent);
          }
        });
    thread =
        new Thread(
            new Runnable() {
              @Override
              public void run() {

                cRef.get()
                    .addOnSuccessListener(
                        dSnap -> {
                          for (QueryDocumentSnapshot snap : dSnap) {
                            NoteItem note = snap.toObject(NoteItem.class);

                            String id = note.getUid();
                            String title = note.getTitle();
                            String text = note.getText();

                            noteList.add(note);

                            mAdapter.notifyItemInserted(noteList.size() - 1);
                          }
                        });
              }
            });

    thread.start();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

    thread.interrupt();
  }
}
