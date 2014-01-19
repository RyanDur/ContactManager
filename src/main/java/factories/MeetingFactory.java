package factories;

import exceptions.InvalidMeetingException;
import models.Contact;
import models.FutureMeeting;
import models.Meeting;
import models.PastMeeting;

import java.util.Calendar;
import java.util.Set;

public interface MeetingFactory {

  /**
   * @param id          the id of the meeting
   * @param contacts    the contacts who attend the meeting
   * @param date        the date when the meeting will take place
   * @return an instance of a FutureMeeting
   * @throws InvalidMeetingException if the contacts are null / empty
   */
  FutureMeeting createFutureMeeting(int id, Set<Contact> contacts, Calendar date) throws InvalidMeetingException;

  /**
   * @param id           the id of the meeting
   * @param contacts     the contacts who attend the meeting
   * @param date         the date when the meeting will take place
   * @param notes        the notes about the meeting
   * @return an instance of a PastMeeting
   * @throws InvalidMeetingException if the contacts are null / empty
   */
  PastMeeting createPastMeeting(int id, Set<Contact> contacts, Calendar date, String notes) throws InvalidMeetingException;
}
