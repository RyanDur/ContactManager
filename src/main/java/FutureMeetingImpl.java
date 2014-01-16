import java.util.Calendar;
import java.util.Set;

public class FutureMeetingImpl extends MeetingImpl implements FutureMeeting {

  public FutureMeetingImpl(int id, Set<Contact> contacts, Calendar date) throws InvalidMeetingException {
    super(id, contacts, date);
  }
}
