package pij.ryan.durling.controllers;

import pij.ryan.durling.models.Contact;
import pij.ryan.durling.models.FutureMeeting;
import pij.ryan.durling.models.Meeting;

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
