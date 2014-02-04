package pij.ryan.durling.controllers;

import pij.ryan.durling.models.Contact;
import pij.ryan.durling.models.FutureMeeting;
import pij.ryan.durling.models.Meeting;
import pij.ryan.durling.models.PastMeeting;

import java.util.*;

public class ContactManagerImpl implements ContactManager {
  Contacts contactsCtrl;
  Meetings meetingsCtrl;
  CalendarDates dates;

  public ContactManagerImpl(Contacts contacts, Meetings meetings, CalendarDates dates) {
    contactsCtrl = contacts;
    meetingsCtrl = meetings;
    this.dates = dates;
    onExitHook();
  }

  @Override
  public int addFutureMeeting(Set<Contact> contacts, Calendar date) throws IllegalArgumentException {
    if (contactsCtrl.notValidContactSet(contacts) || dates.beforeToday(date)) throw new IllegalArgumentException();
    return meetingsCtrl.addFutureMeeting(contacts, date);
  }

  @Override
  public PastMeeting getPastMeeting(int id) throws IllegalArgumentException {
    Meeting meeting = getMeeting(id);
    if (meeting != null && dates.afterToday(meeting.getDate())) throw new IllegalArgumentException();
    return (PastMeeting) meeting;
  }

  @Override
  public FutureMeeting getFutureMeeting(int id) throws IllegalArgumentException {
    Meeting meeting = getMeeting(id);
    if (meeting != null && dates.beforeToday(meeting.getDate())) throw new IllegalArgumentException();
    return (FutureMeeting) meeting;
  }

  @Override
  public Meeting getMeeting(int id) {
    return meetingsCtrl.get(id);
  }

  @Override
  public List<Meeting> getFutureMeetingList(Contact contact) throws IllegalArgumentException {
    if (contactsCtrl.notValidContactId(contact.getId())) throw new IllegalArgumentException();
    return extractFutureMeetings(meetingsCtrl.get(contact));
  }

  @Override
  public List<Meeting> getFutureMeetingList(Calendar date) {
    List<Meeting> meetingList = extractFutureMeetings(meetingsCtrl.get(date));
    sort(meetingList);
    return meetingList;
  }

  @Override
  public List<PastMeeting> getPastMeetingList(Contact contact) {
    if (contactsCtrl.notValidContactId(contact.getId())) throw new IllegalArgumentException();
    List<PastMeeting> meetingList = new ArrayList<>();
    List<Meeting> contactsMeetings = meetingsCtrl.get(contact);
    if (contactsMeetings != null) {
      for (Meeting meeting : contactsMeetings) {
        if (meeting instanceof PastMeeting) {
          meetingList.add((PastMeeting) meeting);
        }
      }
    }
    return meetingList;
  }

  @Override
  public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) throws IllegalArgumentException, NullPointerException {
    if (contacts == null || date == null || text == null) throw new NullPointerException();
    if (contactsCtrl.notValidContactSet(contacts)) throw new IllegalArgumentException();
    meetingsCtrl.addNewPastMeeting(contacts, date, text);
  }

  @Override
  public void addMeetingNotes(int id, String text) throws IllegalArgumentException, IllegalStateException, NullPointerException {
    if (text == null) throw new NullPointerException();
    Meeting meeting = meetingsCtrl.get(id);
    if (meeting == null) throw new IllegalArgumentException();
    if (dates.afterToday(meeting.getDate())) throw new IllegalStateException();
    meetingsCtrl.convertToPastMeeting(meeting, text);
  }

  @Override
  public void addNewContact(String name, String notes) throws NullPointerException {
    if (name == null || notes == null) throw new NullPointerException();
    contactsCtrl.add(name, notes);
  }

  @Override
  public Set<Contact> getContacts(int... ids) throws IllegalArgumentException {
    if (contactsCtrl.notValidContactId(ids)) throw new IllegalArgumentException();
    Set<Contact> contactSet = new HashSet<>();
    for (int id : ids) {
      contactSet.add(contactsCtrl.get(id));
    }
    return contactSet;
  }

  @Override
  public Set<Contact> getContacts(String name) throws NullPointerException {
    if (name == null) throw new NullPointerException();
    return contactsCtrl.get(name);
  }

  @Override
  public void flush() {
    //TODO
  }

  private void onExitHook() {
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        flush();
      }
    });
  }

  private void sort(List<Meeting> meetings) {
    if (meetings != null) {
      Collections.sort(meetings, dateComparator());
    }
  }

  private Comparator dateComparator() {
    return new Comparator<Meeting>() {
      @Override
      public int compare(Meeting o1, Meeting o2) {
        return o1.getDate().compareTo(o2.getDate());
      }
    };
  }

  private List<Meeting> extractFutureMeetings(List<Meeting> meetings) {
    List<Meeting> meetingList = new ArrayList<>();
    if (meetings != null) {
      for (Meeting meeting : meetings) {
        if (meeting instanceof FutureMeeting) {
          meetingList.add(meeting);
        }
      }
    }
    return meetingList;
  }
}