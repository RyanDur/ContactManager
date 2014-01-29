package pij.ryan.durling.models;

import java.io.Serializable;

public class ContactImpl implements Contact, Serializable{
  private static final long serialVersionUID = -6076612147734101521L;
  private int id;
  private String name;
  private String notes;

  public ContactImpl(int id, String name) {
    this.id = id;
    this.name = name;
    notes = "";
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getNotes() {
    return notes;
  }

  @Override
  public void addNotes(String note) {
    String newline = System.lineSeparator();
    note += newline + newline;
    if (notes == null) {
      notes = note;
    } else {
      notes += note;
    }
  }
}
