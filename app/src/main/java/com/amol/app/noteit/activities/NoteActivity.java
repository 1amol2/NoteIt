package com.amol.app.noteit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.amol.app.noteit.databinding.ActivityNoteBinding;
import com.amol.app.noteit.model.NoteItem;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.UUID;

public class NoteActivity extends AppCompatActivity {
  private ActivityNoteBinding binding;
  private FirebaseFirestore firebaseDb;
  private CollectionReference cRef;
  private DocumentReference dRef;
  private String key, uid, mTitle, mText, mUid, title, text, currentTitle, currentText;
  private Thread thread;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityNoteBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    init();
  }

  private void init() {
    // TODO: Implement this method
    firebaseDb = FirebaseFirestore.getInstance();
    cRef = firebaseDb.collection("Notes");
    key = UUID.randomUUID().toString();
    if (getIntent().getExtras().getString("Intent").equals("Intent_A")) {

      mUid = getIntent().getExtras().getString("Uid");
      mTitle = getIntent().getExtras().getString("Title");
      mText = getIntent().getExtras().getString("Text");

      binding.noteTitle.setText(mTitle);
      binding.noteText.setText(mText);

      currentTitle = mTitle;
      currentText = mText;

      binding.noteSaveBtn.setOnClickListener(
          p3 -> {
            updateNote(
                binding.noteTitle.getText().toString(), binding.noteText.getText().toString());
          });
    } else {
      binding.noteSaveBtn.setOnClickListener(
          p1 -> {
            addNote();
          });
    }
  }

  private void addNote() {
    title = binding.noteTitle.getText().toString();

    text = binding.noteText.getText().toString();

    if (!title.isEmpty() && !text.isEmpty()) {

      thread =
          new Thread(
              new Runnable() {
                @Override
                public void run() {

                  NoteItem noteItem = new NoteItem(key, title, text);
                  cRef.document(key)
                      .set(noteItem)
                      .addOnCompleteListener(
                          task -> {
                            if (task.isSuccessful()) {
                              Intent intent = new Intent(NoteActivity.this, MainActivity.class);

                              startActivity(intent);
                              finish();

                            } else {
                              Toast.makeText(
                                      NoteActivity.this,
                                      task.getException().getMessage().toString(),
                                      Toast.LENGTH_SHORT)
                                  .show();
                            }
                          });
                }
              });
      thread.start();
    }
  }

  private void updateNote(String title, String text) {
    if (!currentTitle.equals(title) || !currentText.equals(text)) {

      if (!title.isEmpty() && !text.isEmpty()) {

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
                    Toast.makeText(
                            this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT)
                        .show();
                  }

                  thread =
                      new Thread(
                          new Runnable() {
                            @Override
                            public void run() {

                              NoteItem item = new NoteItem(mUid, title, text);
                              cRef.document(mUid)
                                  .set(item)
                                  .addOnSuccessListener(
                                      p1 -> {
                                        Toast.makeText(
                                                NoteActivity.this,
                                                "Note Updated Successfully",
                                                Toast.LENGTH_SHORT)
                                            .show();
                                        Intent intent =
                                            new Intent(NoteActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                      })
                                  .addOnFailureListener(
                                      p2 -> {
                                        Toast.makeText(
                                                NoteActivity.this,
                                                p2.getMessage().toString(),
                                                Toast.LENGTH_SHORT)
                                            .show();
                                      });
                            }
                          });
                  thread.start();
                });
      }else{
          finish();
      }
    }
  }
}
