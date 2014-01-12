public interface  MeetingFactory {

    Meeting create(int id, Set<Contact> contacts, Calendar date);

    PastMeeting create(Meeting meeting, String notes);
}
