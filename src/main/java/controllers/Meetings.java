package controllers;

import models.Contact;
import models.FutureMeeting;

import java.util.Calendar;
import java.util.Set;

public interface Meetings {
  int addFutureMeeting(Set<Contact> contacts, Calendar date);

  FutureMeeting getFutureMeeting(int id);
}
