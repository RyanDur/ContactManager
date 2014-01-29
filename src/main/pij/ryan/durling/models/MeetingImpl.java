package ryan.durling.models;

import ryan.durling.exceptions.InvalidMeetingException;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

public class MeetingImpl implements Meeting, Serializable {
  private static final long serialVersionUID = 9206612321465304306L;
  private int id;
  private Set<Contact> contacts;
  private Calendar date;

  public MeetingImpl(int id, Set<Contact> contacts, Calendar date) throws InvalidMeetingException {
    if (contacts == null || contacts.isEmpty()) throw new InvalidMeetingException();
    this.id = id;
    this.contacts = contacts;
    this.date = date;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public Calendar getDate() {
    return date;
  }

  @Override
  public Set<Contact> getContacts() {
    return contacts;
  }
}
