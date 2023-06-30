package com.amol.app.noteit.model;

public class NoteItem {
  private String uid, title, text;

  public NoteItem(String uid, String title, String text) {
    this.uid = uid;
    this.title = title;
    this.text = text;
  }

  public NoteItem() {}

  public String getText() {
    return this.text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getUid() {
    return this.uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }
}
