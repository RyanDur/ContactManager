package pij.ryan.durling.controllers;

import pij.ryan.durling.models.Contact;
import pij.ryan.durling.models.FutureMeeting;
import pij.ryan.durling.models.Meeting;
import pij.ryan.durling.models.PastMeeting;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContactManagerImpl implements ContactManager {
  Contacts contactController;
  Meetings meetingController;

  public ContactManagerImpl(Contacts contacts, Meetings meetings) {
    contactController = contacts;
    meetingController = meetings;
  }

  @Override
  public int addFutureMeeting(Set<Contact> contacts, Calendar date) throws IllegalArgumentException {
    if(contactController.notValidContactSet(contacts) || dateIsInThePast(date)) throw new IllegalArgumentException();
    return meetingController.addFutureMeeting(contacts, date);
  }

  @Override
  public PastMeeting getPastMeeting(int id) {
    return null;  //TODO
  }

  @Override
  public FutureMeeting getFutureMeeting(int id) {
    return null;  //TODO
  }

  @Override
  public Meeting getMeeting(int id) {
    return null;  //TODO
  }

  @Override
  public List<Meeting> getFutureMeetingList(Contact contact) {
    return null;  //TODO
  }

  @Override
  public List<Meeting> getFutureMeetingList(Calendar date) {
    return null;  //TODO
  }

  @Override
  public List<PastMeeting> getPastMeetingList(Contact contact) {
    return null;  //TODO
  }

  @Override
  public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) throws IllegalArgumentException {
    if (contacts == null || date == null || text == null) throw new NullPointerException();
    if (contactController.notValidContactSet(contacts)) throw new IllegalArgumentException();
    meetingController.addNewPastMeeting(contacts, date, text);
  }

  @Override
  public void addMeetingNotes(int id, String text) {
    //TODO
  }

  @Override
  public void addNewContact(String name, String notes) throws NullPointerException {
    if (name == null || notes == null) throw new NullPointerException();
    contactController.add(name, notes);
  }

  @Override
  public Set<Contact> getContacts(int... ids) throws IllegalArgumentException {
    if (contactController.notValidContactId(ids)) throw new IllegalArgumentException();
    Set<Contact> contactSet = new HashSet<>();
    for (int id : ids) {
      contactSet.add(contactController.get(id));
    }
    return contactSet;
  }

  @Override
  public Set<Contact> getContacts(String name) {
    if (name == null) throw new NullPointerException();
    return contactController.get(name);
  }

  @Override
  public void flush() {
    //TODO
  }

  private boolean dateIsInThePast(Calendar date) {
    return date.compareTo(Calendar.getInstance()) < 0;
  }
}
