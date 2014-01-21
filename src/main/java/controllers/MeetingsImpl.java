package controllers;

import exceptions.InvalidMeetingException;
import factories.MeetingFactory;
import generators.IdGenerator;
import models.Contact;
import models.FutureMeeting;
import models.Meeting;
import models.PastMeeting;

import java.util.*;

public class MeetingsImpl implements Meetings {
  MeetingFactory meetingFactory;
  private IdGenerator idGenerator;
  private TreeMap<Integer, Meeting> meetings = new TreeMap<>();

  public MeetingsImpl(MeetingFactory meetingFactory, IdGenerator idGenerator) {
    this.meetingFactory = meetingFactory;
    this.idGenerator = idGenerator;
  }

  @Override
  public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
    if(isInThePast(date)) throw new IllegalArgumentException();
    int meetingId = idGenerator.getMeetingId();
    try {
      FutureMeeting meeting = meetingFactory.createFutureMeeting(meetingId, contacts, date);
      meetings.put(meeting.getId(), meeting);
    } catch (InvalidMeetingException e) {
      System.out.println("Error " + e.getMessage());
      e.printStackTrace();
    }
    return meetingId;
  }

  @Override
  public FutureMeeting getFutureMeeting(int id) {
    Meeting futureMeeting = meetings.get(id);
    if(isInThePast(futureMeeting.getDate())) throw new IllegalArgumentException();
    return (FutureMeeting) futureMeeting;
  }

  @Override
  public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
    if(contacts == null || date == null || text == null) throw new NullPointerException();
    int id = idGenerator.getMeetingId();
    try {
      PastMeeting meeting = meetingFactory.createPastMeeting(id, contacts, date, text);
      meetings.put(meeting.getId(), meeting);
    } catch (InvalidMeetingException e) {
      e.printStackTrace();
    }
  }

  @Override
  public PastMeeting getPastMeeting(int id) {
    Meeting pastMeeting =  meetings.get(id);
    if(isInTheFuture(pastMeeting.getDate())) throw new IllegalArgumentException();
    return (PastMeeting) pastMeeting;
  }

  private boolean isInThePast(Calendar date) {
    return date.compareTo(Calendar.getInstance()) < 0;
  }

  private boolean isInTheFuture(Calendar date) {
    return date.compareTo(new GregorianCalendar()) > 0;
  }
}
