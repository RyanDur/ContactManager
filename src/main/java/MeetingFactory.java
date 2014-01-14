import java.util.Calendar;
import java.util.Set;

public interface  MeetingFactory {

    /**
     * @throws InvalidMeetingException
     */
    Meeting createMeeting(int id, Set<Contact> contacts, Calendar date) throws InvalidMeetingException;

    /**
     * @throws InvalidMeetingException
     */
    FutureMeeting createFutureMeeting(int id, Set<Contact> contacts, Calendar date) throws InvalidMeetingException;

    /**
     * @throws InvalidMeetingException
     */
    PastMeeting createPastMeeting(int id, Set<Contact> contacts, Calendar date, String notes) throws InvalidMeetingException;
}
