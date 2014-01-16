import java.util.Calendar;
import java.util.Set;

public class PastMeetingImpl extends MeetingImpl implements PastMeeting {
  private String notes;

  public PastMeetingImpl(int id, Set<Contact> contacts, Calendar date, String notes) throws InvalidMeetingException {
    super(id, contacts, date);
    this.notes = notes == null ? "" : notes;
  }

  @Override
  public String getNotes() {
    return notes;
  }
}
