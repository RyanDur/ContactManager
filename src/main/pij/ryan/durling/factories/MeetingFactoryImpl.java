package pij.ryan.durling.factories;

import com.google.inject.Singleton;
import pij.ryan.durling.exceptions.InvalidMeetingException;
import pij.ryan.durling.models.*;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

@Singleton
public class MeetingFactoryImpl implements MeetingFactory, Serializable {
  private static final long serialVersionUID = -3997460871547952793L;

  @Override
  public FutureMeeting createFutureMeeting(int id, Set<Contact> contacts, Calendar date) throws InvalidMeetingException {
    return new FutureMeetingImpl(id, contacts, date);
  }

  @Override
  public PastMeeting createPastMeeting(int id, Set<Contact> contacts, Calendar date, String notes) throws InvalidMeetingException {
    return new PastMeetingImpl(id, contacts, date, notes);
  }
}
