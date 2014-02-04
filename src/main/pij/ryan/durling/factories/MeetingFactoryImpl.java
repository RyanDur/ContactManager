package pij.ryan.durling.factories;

import pij.ryan.durling.exceptions.InvalidMeetingException;
import pij.ryan.durling.models.*;

import java.util.Calendar;
import java.util.Set;

public class MeetingFactoryImpl implements MeetingFactory {
  private static MeetingFactory meetingFactoryInstance = null;

  private MeetingFactoryImpl() {
  }

  public static MeetingFactory getInstance() {
    if (meetingFactoryInstance == null) {
      meetingFactoryInstance = new MeetingFactoryImpl();
    }
    return meetingFactoryInstance;
  }

  @Override
  public FutureMeeting createFutureMeeting(int id, Set<Contact> contacts, Calendar date) throws InvalidMeetingException {
    return new FutureMeetingImpl(id, contacts, date);
  }

  @Override
  public PastMeeting createPastMeeting(int id, Set<Contact> contacts, Calendar date, String notes) throws InvalidMeetingException {
    return new PastMeetingImpl(id, contacts, date, notes);
  }
}
