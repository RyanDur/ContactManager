package pij.ryan.durling.controllers;

import pij.ryan.durling.models.Contact;
import pij.ryan.durling.models.Meeting;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

public interface Meetings {

  /**
   * add a future meeting to the collection
   *
   * @param contacts
   * @param date
   * @return the id of the created meeting
   * @throws IllegalArgumentException if date is in the past
   */
  int addFutureMeeting(Set<Contact> contacts, Calendar date);

  /**
   * adds a  new past meeting to th collection
   *
   * @param contacts
   * @param date
   * @param text
   * @throws NullPointerException if any params are null
   */
  void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text);

  /**
   * get a meeting based on id
   *
   * @param id
   * @return a meeting
   */
  Meeting get(int id);

  /**
   * returns a list of meetings for the date specified
   *
   * @param date
   * @return a list of meetings
   */
  List<Meeting> get(Calendar date);

  /**
   * returns a list of meetings for a contact
   *
   * @param contact
   * @return a list of meetings
   */
  List<Meeting> get(Contact contact);

  /**
   * converts a meeting into a past meeting with notes
   *
   * @param meeting
   * @param notes
   * @throws IllegalArgumentException if meeting is not in the collection
   * @throws IllegalStateException if meeting is happening in the future
   * @throws NullPointerException if notes are null
   */
  void convertToPastMeeting(Meeting meeting, String notes);

  /**
   * check if date is before today
   *
   * @param date
   * @return true if date is before today and false otherwise
   */
  boolean beforeToday(Calendar date);

  /**
   * check if date is after today
   *
   * @param date
   * @return true if after today and false otherwise
   */
  boolean afterToday(Calendar date);
}
