import java.util.Calendar;
import java.util.Set;

public class MeetingFactoryImpl implements MeetingFactory {
    private static MeetingFactory meetingFactoryInstance = null;

    private MeetingFactoryImpl() {}

    public static MeetingFactory getInstance() {
        if(meetingFactoryInstance == null) {
            meetingFactoryInstance = new MeetingFactoryImpl();
        }
        return meetingFactoryInstance;
    }

    @Override
    public Meeting createMeeting(int id, Set<Contact> contacts, Calendar date) throws InvalidMeetingException {
        return new MeetingImpl(id, contacts, date);
    }

    @Override
    public FutureMeeting createFutureMeeting(int id, Set<Contact> contacts, Calendar date) throws InvalidMeetingException {
        return new FutureMeetingImpl(id, contacts, date);
    }

    @Override
    public PastMeeting createPastMeeting(Meeting meeting, String notes) throws InvalidMeetingException {
        return new PastMeetingImpl(meeting, notes);
    }
}
