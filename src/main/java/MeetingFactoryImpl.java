public class MeetingFactoryImpl implements MeetingFactory {

    @Override
    public static Meeting create(int id, Set<Contact> contacts, Calendar date) throws InvalidMeetingException {
	return new Meeting(int id, Set<Contact> contacts, Calendar date);
    }
}
