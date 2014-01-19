package factories;

import exceptions.InvalidMeetingException;
import models.*;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

public class MeetingFactoryImpl implements MeetingFactory, Serializable {
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
