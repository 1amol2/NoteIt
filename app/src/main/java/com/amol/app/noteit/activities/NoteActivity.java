package com.amol.app.noteit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;
import com.amol.app.noteit.databinding.ActivityNoteBinding;
import com.amol.app.noteit.model.NoteItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.UUID;

public class NoteActivity extends AppCompatActivity {
  private ActivityNoteBinding binding;
  private FirebaseFirestore firebaseDb;
  private CollectionReference cRef;
  private DocumentReference dRef;
  private String key, uid, mTitle, mText, mUid, title, text;
  private Handler handler;
  private Thread thread;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityNoteBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    init();

          

    binding.noteSaveBtn.setOnClickListener(
        p1 -> {
          addNote();
        });
  }

  private void init() {
    // TODO: Implement this method
    firebaseDb = FirebaseFirestore.getInstance();
    cRef = firebaseDb.collection("Notes");
    key = UUID.randomUUID().toString();
    if(getIntent().getExtras().getString("Intent").equals("Intent_A")){
        
    
    mUid = getIntent().getExtras().getString("Uid");
    mTitle = getIntent().getExtras().getString("Title");
    mText = getIntent().getExtras().getString("Text");
            handler = new Handler();

      thread =
          new Thread(
              new Runnable() {
                @Override
                public void run() {
                  binding.noteTitle.setText(mTitle);
                  binding.noteText.setText(mText);
                }
              });
      thread.start();
    
    }        
  }

  private void addNote() {
    title = binding.noteTitle.getText().toString();

    text = binding.noteText.getText().toString();
    Toast.makeText(this, key, Toast.LENGTH_SHORT).show();

    if (!title.isEmpty() && !text.isEmpty()) {
      Toast.makeText(this, "not empty", Toast.LENGTH_SHORT).show();

      NoteItem noteItem = new NoteItem(key, title, text);
      cRef.document(key)
          .set(noteItem)
          .addOnCompleteListener(
              task -> {
                if (task.isSuccessful()) {
                  Intent intent = new Intent(NoteActivity.this, MainActivity.class);
                  intent.putExtra("uid", key);
                  intent.putExtra("title", title);
                  intent.putExtra("text", text);
                  startActivity(intent);
                  finish();

                } else {
                  Toast.makeText(this, "noOOO", Toast.LENGTH_SHORT).show();
                }
              });
    }
  }

  @Override
  @MainThread
  public void onBackPressed() {
    if (!title.isEmpty() && !text.isEmpty()) {

      addNote();

    
  }
}
}
