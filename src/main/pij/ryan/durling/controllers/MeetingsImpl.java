package pij.ryan.durling.controllers;

import pij.ryan.durling.exceptions.InvalidMeetingException;
import pij.ryan.durling.factories.MeetingFactory;
import pij.ryan.durling.generators.IdGenerator;
import pij.ryan.durling.models.Contact;
import pij.ryan.durling.models.Meeting;

import java.io.Serializable;
import java.util.*;

public class MeetingsImpl implements Meetings, Serializable {
  private static final long serialVersionUID = 6341603471619002879L;

  private MeetingFactory meetingFactory;
  private IdGenerator idGenerator;
  private HashMap<Integer, Meeting> meetings = new HashMap<>();
  private HashMap<Integer, Set<Integer>> meetingsByDate = new HashMap<>();
  private HashMap<Integer, Set<Integer>> meetingsByContact = new HashMap<>();

  public MeetingsImpl(MeetingFactory meetingFactory, IdGenerator idGenerator) {
    this.meetingFactory = meetingFactory;
    this.idGenerator = idGenerator;
  }

  @Override
  public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
    if (beforeToday(date)) throw new IllegalArgumentException();
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
    if (afterToday(meeting.getDate())) throw new IllegalStateException();
    if (notes == null) throw new NullPointerException();
    try {
      addMeeting(meetingFactory.createPastMeeting(meeting.getId(), meeting.getContacts(), meeting.getDate(), notes));
    } catch (InvalidMeetingException e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean beforeToday(Calendar date) {
    int today = dateKey(Calendar.getInstance());
    int otherDay = dateKey(date);
    return otherDay < today;
  }

  @Override
  public boolean afterToday(Calendar date) {
    int today = dateKey(Calendar.getInstance());
    int otherDay = dateKey(date);
    return otherDay > today;
  }

  @Override
  public Meeting get(int id) {
    return meetings.get(id);
  }

  @Override
  public List<Meeting> get(Calendar date) {
    Set<Integer> meetingIds = meetingsByDate.get(dateKey(date));
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
    Integer dateKey = dateKey(meeting.getDate());
    Set<Integer> meetings = meetingsByDate.get(dateKey);
    if (meetings == null) {
      Set<Integer> meetingIds = new HashSet<>();
      meetingIds.add(meeting.getId());
      meetingsByDate.put(dateKey, meetingIds);
    } else {
      meetings.add(meeting.getId());
    }
  }

  private int dateKey(Calendar date) {
    int year = date.get(Calendar.YEAR);
    int month = date.get(Calendar.MONTH);
    int week = date.get(Calendar.WEEK_OF_MONTH);
    int day = date.get(Calendar.DAY_OF_WEEK);
    String sDate = "" + year + month + week + day;
    return Integer.parseInt(sDate);
  }
}