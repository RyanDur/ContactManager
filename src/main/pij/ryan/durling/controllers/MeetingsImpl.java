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
  private HashMap<Integer, Set<Integer>> meetingsByDate = new HashMap<>();

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
      meetings.put(meeting.getId(), meeting);
      addMeetingByDate(meeting);
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
      meetings.put(meeting.getId(), meeting);
      addMeetingByDate(meeting);
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
    Set<Integer> meetingIds = meetingsByDate.get(dateKey(date));
    List<Meeting> meetingList = new ArrayList<>();
    for (Integer id : meetingIds) {
        meetingList.add(meetings.get(id));
    }
    sort(meetingList);
    return meetingList;
  }

  @Override
  public List<Meeting> get(Contact contact) {
    List<Meeting> meetingList = new ArrayList<>();
    for(Meeting meeting : meetings.values()) {
      if(meeting.getContacts().contains(contact)) {
        meetingList.add(meeting);
      }
    }
    return meetingList;
  }

  @Override
  public void convert(FutureMeeting meeting, String notes) {

  }

  private void addMeetingByDate(Meeting meeting) {
    Integer dateKey = dateKey(meeting.getDate());
    Set<Integer> meetings = meetingsByDate.get(dateKey);
    if(meetings == null) {
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
