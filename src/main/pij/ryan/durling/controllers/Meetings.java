package ryan.durling.controllers;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

public interface Meetings {
  int addFutureMeeting(Set<Contact> contacts, Calendar date);

  void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text);

  Meeting get(int id);

  List<Meeting> get(Calendar date);

  List<Meeting> get(Contact contact);

  void convert(FutureMeeting meeting, String notes);
}
