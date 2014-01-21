package controllers;

import exceptions.InvalidMeetingException;
import factories.MeetingFactory;
import generators.IdGenerator;
import models.Contact;
import models.FutureMeeting;
import models.Meeting;

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
    FutureMeeting futureMeeting = (FutureMeeting) meetings.get(id);
    if(isInThePast(futureMeeting.getDate())) throw new IllegalArgumentException();
    return futureMeeting;
  }

  private boolean isInThePast(Calendar date) {
    return date.compareTo(Calendar.getInstance()) < 0;
  }
}
