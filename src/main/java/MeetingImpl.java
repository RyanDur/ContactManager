import java.util.Calendar;
import java.util.Set;

public class MeetingImpl implements Meeting {
    private int id;
    private Set<Contact> contacts;
    private Calendar date;

    public MeetingImpl(int id, Set<Contact> contacts, Calendar date) throws InvalidMeetingException {
	if(contacts == null || contacts.isEmpty()) throw new InvalidMeetingException();
	this.id = id;
	this.contacts = contacts;
	this.date = date;
    }

    public int getId() {
	return id;
    }

    public Calendar getDate() {
	return date;
    }

    public Set<Contact> getContacts() {
	return contacts;
    }
}
