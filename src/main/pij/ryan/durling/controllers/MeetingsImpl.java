package pij.ryan.durling.controllers;

import pij.ryan.durling.exceptions.InvalidMeetingException;
import pij.ryan.durling.factories.MeetingFactory;
import pij.ryan.durling.generators.IdGenerator;
import pij.ryan.durling.models.Contact;
import pij.ryan.durling.models.Meeting;

import java.util.*;

public class MeetingsImpl implements Meetings {
  MeetingFactory meetingFactory;
  private IdGenerator idGenerator;
  private CalendarDates dates;
  private HashMap<Integer, Meeting> meetings = new HashMap<>();
  private HashMap<Integer, Set<Integer>> meetingsByDate = new HashMap<>();
  private HashMap<Integer, Set<Integer>> meetingsByContact = new HashMap<>();

  public MeetingsImpl(MeetingFactory meetingFactory, IdGenerator idGenerator, CalendarDates dates) {
    this.meetingFactory = meetingFactory;
    this.idGenerator = idGenerator;
    this.dates = dates;
  }

  @Override
  public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
    if (dates.beforeToday(date)) throw new IllegalArgumentException();
    int meetingId = idGenerator.getMeetingId();
    try {
      addMeeting(meetingFactory.createFutureMeeting(meetingId, contacts, date));
    } catch (InvalidMeetingException e) {
      System.out.println("Error " + e.getMessage());
      e.printStackTrace();
    }
    return meetingId;
  }

  @Override
  public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
    if (contacts == null || date == null || text == null) throw new NullPointerException();
    int id = idGenerator.getMeetingId();
    try {
      addMeeting(meetingFactory.createPastMeeting(id, contacts, date, text));
    } catch (InvalidMeetingException e) {
      System.out.println("Error " + e.getMessage());
      e.printStackTrace();
    }
  }

  @Override
  public void convertToPastMeeting(Meeting meeting, String notes) throws IllegalStateException, NullPointerException, IllegalArgumentException {
    if (meetings.get(meeting.getId()) == null) throw new IllegalArgumentException();
    if (dates.afterToday(meeting.getDate())) throw new IllegalStateException();
    if (notes == null) throw new NullPointerException();
    try {
      addMeeting(meetingFactory.createPastMeeting(meeting.getId(), meeting.getContacts(), meeting.getDate(), notes));
    } catch (InvalidMeetingException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Meeting get(int id) {
    return meetings.get(id);
  }

  @Override
  public List<Meeting> get(Calendar date) {
    Set<Integer> meetingIds = meetingsByDate.get(dates.key(date));
    List<Meeting> resultList = new ArrayList<>();
    if (meetingIds != null) {
      for (Integer id : meetingIds) {
        resultList.add(meetings.get(id));
      }
    }
    return resultList;
  }

  @Override
  public List<Meeting> get(Contact contact) {
    Set<Integer> meetingIds = meetingsByContact.get(contact.getId());
    List<Meeting> resultList = new ArrayList<>();
    if (meetingIds != null) {
      for (Integer id : meetingIds) {
        resultList.add(meetings.get(id));
      }
    }
    return resultList;
  }

  private void addMeeting(Meeting meeting) {
    addMeetingById(meeting);
    addMeetingByDate(meeting);
    addMeetingByContacts(meeting);
  }

  private void addMeetingById(Meeting meeting) {
    meetings.put(meeting.getId(), meeting);
  }

  private void addMeetingByContacts(Meeting meeting) {
    Set<Contact> contacts = meeting.getContacts();
    for (Contact contact : contacts) {
      addMeetingByContact(contact, meeting);
    }
  }

  private void addMeetingByContact(Contact contact, Meeting meeting) {
    Set<Integer> meetings = meetingsByContact.get(contact.getId());
    if (meetings == null) {
      Set<Integer> meetingIds = new HashSet<>();
      meetingIds.add(meeting.getId());
      meetingsByContact.put(contact.getId(), meetingIds);
    } else {
      meetings.add(meeting.getId());
    }
  }

  private void addMeetingByDate(Meeting meeting) {
    Integer dateKey = dates.key(meeting.getDate());
    Set<Integer> meetings = meetingsByDate.get(dateKey);
    if (meetings == null) {
      Set<Integer> meetingIds = new HashSet<>();
      meetingIds.add(meeting.getId());
      meetingsByDate.put(dateKey, meetingIds);
    } else {
      meetings.add(meeting.getId());
    }
  }
}