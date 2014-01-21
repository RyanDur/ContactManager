package controllers;

import models.Contact;
import models.FutureMeeting;
import models.PastMeeting;

import java.util.Calendar;
import java.util.Set;

public interface Meetings {
  int addFutureMeeting(Set<Contact> contacts, Calendar date);

  FutureMeeting getFutureMeeting(int id);

  void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text);

  PastMeeting getPastMeeting(int id);
}
