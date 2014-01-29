package pij.ryan.durling.models;

import java.util.Calendar;
import java.util.Set;

public class PastMeetingImpl extends MeetingImpl implements PastMeeting {
  private static final long serialVersionUID = 2253001125503946986L;
  private String notes;

  public PastMeetingImpl(int id, Set<ryan.durling.models.Contact> contacts, Calendar date, String notes) throws InvalidMeetingException {
    super(id, contacts, date);
    this.notes = notes == null ? "" : notes;
  }

  @Override
  public String getNotes() {
    return notes;
  }
}
