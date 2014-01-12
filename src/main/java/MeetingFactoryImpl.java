public class MeetingFactoryImpl implements MeetingFactory {

    @Override
    public static Meeting create(int id, Set<Contact> contacts, Calendar date) throws InvalidMeetingException {
	return new Meeting(int id, Set<Contact> contacts, Calendar date);
    }

    @Override
    public static PastMeeting create(Meeting meeting, String notes) throws InvalidMeetingException {
	return new PastMeeting(Meeting meeting, String notes);
    }
}
