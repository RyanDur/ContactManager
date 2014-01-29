package pij.ryan.durling.controllers;

import pij.ryan.durling.exceptions.InvalidMeetingException;
import pij.ryan.durling.factories.MeetingFactory;
import pij.ryan.durling.generators.IdGenerator;
import pij.ryan.durling.models.Contact;
import pij.ryan.durling.models.FutureMeeting;
import pij.ryan.durling.models.Meeting;
import pij.ryan.durling.models.PastMeeting;

import java.util.*;

public class MeetingsImpl implements Meetings {
  MeetingFactory meetingFactory;
  private IdGenerator idGenerator;
  private HashMap<Integer, Meeting> meetings = new HashMap<>();
  private HashMap<Integer, List<Integer>> meetingsByDate = new HashMap<>();
  private HashMap<Integer, List<Integer>> meetingsByContact = new HashMap<>();

  public MeetingsImpl(MeetingFactory meetingFactory, IdGenerator idGenerator) {
    this.meetingFactory = meetingFactory;
    this.idGenerator = idGenerator;
  }

  @Override
  public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
    if (isInThePast(date)) throw new IllegalArgumentException();
    int meetingId = idGenerator.getMeetingId();
    try {
      FutureMeeting meeting = meetingFactory.createFutureMeeting(meetingId, contacts, date);
      addMeeting(meeting);
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
      PastMeeting meeting = meetingFactory.createPastMeeting(id, contacts, date, text);
      addMeeting(meeting);
    } catch (InvalidMeetingException e) {
      System.out.println("Error " + e.getMessage());
      e.printStackTrace();
    }
  }

  @Override
  public Meeting get(int id) {
    return meetings.get(id);
  }

  @Override
  public List<Meeting> get(Calendar date) {
    List<Integer> meetingIds = meetingsByDate.get(dateKey(date));
    List<Meeting> resultList = new ArrayList<>();
    if (meetingIds != null) {
      for (Integer id : meetingIds) {
        resultList.add(meetings.get(id));
      }
      sort(resultList);
    }
    return resultList;
  }

  @Override
  public List<Meeting> get(Contact contact) {
    List<Integer> meetingIds = meetingsByContact.get(contact.getId());
    List<Meeting> resultList = new ArrayList<>();
    if (meetingIds != null) {
      for (Integer id : meetingIds) {
        resultList.add(meetings.get(id));
      }
    }
    return resultList;
  }


  @Override
  public void convert(FutureMeeting meeting, String notes) {

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
    List<Integer> meetings = meetingsByContact.get(contact.getId());
    if(meetings == null) {
      List<Integer> meetingIds = new ArrayList<>();
      meetingIds.add(meeting.getId());
      meetingsByContact.put(contact.getId(), meetingIds);
    } else {
      meetings.add(meeting.getId());
    }
  }

  private void addMeetingByDate(Meeting meeting) {
    Integer dateKey = dateKey(meeting.getDate());
    List<Integer> meetings = meetingsByDate.get(dateKey);
    if(meetings == null) {
      List<Integer> meetingIds = new ArrayList<>();
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
    String sDate = year+ "" +month+ "" +week+ "" +day;
    return Integer.parseInt(sDate);
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

  private boolean isInThePast(Calendar date) {
    return date.compareTo(Calendar.getInstance()) < 0;
  }
}
