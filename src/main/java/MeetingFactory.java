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
    PastMeeting createPastMeeting(Meeting meeting, String notes) throws InvalidMeetingException;
}