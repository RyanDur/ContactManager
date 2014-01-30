package pij.ryan.durling.controllers;

import pij.ryan.durling.models.Contact;
import pij.ryan.durling.models.FutureMeeting;
import pij.ryan.durling.models.Meeting;
import pij.ryan.durling.models.PastMeeting;

import java.util.*;

public class ContactManagerImpl implements ContactManager {
  Contacts contactController;
  Meetings meetingController;

  public ContactManagerImpl(Contacts contacts, Meetings meetings) {
    contactController = contacts;
    meetingController = meetings;
  }

  @Override
  public int addFutureMeeting(Set<Contact> contacts, Calendar date) throws IllegalArgumentException {
    if (contactController.notValidContactSet(contacts) || dateIsInThePast(date)) throw new IllegalArgumentException();
    return meetingController.addFutureMeeting(contacts, date);
  }

  @Override
  public PastMeeting getPastMeeting(int id) throws IllegalArgumentException {
    Meeting meeting = getMeeting(id);
    if (meeting != null && dateIsInTheFuture(meeting.getDate())) throw new IllegalArgumentException();
    return (PastMeeting) meeting;
  }

  @Override
  public FutureMeeting getFutureMeeting(int id) throws IllegalArgumentException {
    Meeting meeting = getMeeting(id);
    if (meeting != null && dateIsInThePast(meeting.getDate())) throw new IllegalArgumentException();
    return (FutureMeeting) meeting;
  }

  @Override
  public Meeting getMeeting(int id) {
    return meetingController.get(id);
  }

  @Override
  public List<Meeting> getFutureMeetingList(Contact contact) throws IllegalArgumentException {
    if (contactController.notValidContactId(contact.getId())) throw new IllegalArgumentException();
    List<Meeting> meetingList = new ArrayList<>();
    List<Meeting> contactsMeetings = meetingController.get(contact);
    if (contactsMeetings != null) {
      for (Meeting meeting : contactsMeetings) {
        if (meeting instanceof FutureMeeting) {
          meetingList.add(meeting);
        }
      }
    }
    return meetingList;
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
  public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) throws IllegalArgumentException, NullPointerException {
    if (contacts == null || date == null || text == null) throw new NullPointerException();
    if (contactController.notValidContactSet(contacts)) throw new IllegalArgumentException();
    meetingController.addNewPastMeeting(contacts, date, text);
  }

  @Override
  public void addMeetingNotes(int id, String text) throws IllegalArgumentException, IllegalStateException, NullPointerException {
    if (text == null) throw new NullPointerException();
    Meeting meeting = meetingController.get(id);
    if (meeting == null) throw new IllegalArgumentException();
    if (dateIsInTheFuture(meeting.getDate())) throw new IllegalStateException();
    meetingController.convertToPastMeeting(meeting, text);
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
  public Set<Contact> getContacts(String name) throws NullPointerException {
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

  private boolean dateIsInTheFuture(Calendar date) {
    return date.compareTo(Calendar.getInstance()) > 0;
  }
}
