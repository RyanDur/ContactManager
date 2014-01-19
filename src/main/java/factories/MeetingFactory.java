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
   * @param id
   * @param contacts
   * @param date
   * @return
   * @throws InvalidMeetingException
   */
  Meeting createMeeting(int id, Set<Contact> contacts, Calendar date) throws InvalidMeetingException;

  /**
   * @param id
   * @param contacts
   * @param date
   * @return
   * @throws InvalidMeetingException
   */
  FutureMeeting createFutureMeeting(int id, Set<Contact> contacts, Calendar date) throws InvalidMeetingException;

  /**
   * @param id
   * @param contacts
   * @param date
   * @param notes
   * @return
   * @throws InvalidMeetingException
   */
  PastMeeting createPastMeeting(int id, Set<Contact> contacts, Calendar date, String notes) throws InvalidMeetingException;
}
