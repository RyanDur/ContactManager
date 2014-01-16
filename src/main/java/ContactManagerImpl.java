import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.LinkedHashMap;

public class ContactManagerImpl implements ContactManager {
    private MeetingFactory meetingFactory;
    private ContactFactory contactFactory;
    private IdGenerator idGenerator;
    private Map<Integer, Meeting> meetings;
    private Map<Integer, FutureMeeting> futureMeetings;
    private Map<Integer, PastMeeting> pastMeetings;
    private Map<String, Contact> contactsByName;
    private Map<Integer, Contact> contactsById;

    public ContactManagerImpl(MeetingFactory meetingFactory, ContactFactory contactFactory, IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
        this.contactFactory = contactFactory;
        this.meetingFactory = meetingFactory;
        meetings = new LinkedHashMap<Integer, Meeting>();
        futureMeetings = new LinkedHashMap<Integer, FutureMeeting>();
        pastMeetings = new LinkedHashMap<Integer, PastMeeting>();
        contactsByName = new LinkedHashMap<String, Contact>();
        contactsById = new LinkedHashMap<Integer, Contact>();
    }

    @Override
    public int addFutureMeeting(Set<Contact> contacts, Calendar date) throws IllegalArgumentException {
        if(notValidFutureMeeting(contacts, date)) throw new IllegalArgumentException();

        int meetingId = idGenerator.getMeetingId();
        try {
            FutureMeeting meeting = meetingFactory.createFutureMeeting(meetingId, contacts, date);
            futureMeetings.put(meeting.getId(), meeting);
            meetings.put(meeting.getId(), meeting);
        }
        catch (InvalidMeetingException e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
        }
        return meetingId;
    }

    @Override
    public PastMeeting getPastMeeting(int id) throws IllegalArgumentException {
        PastMeeting meeting = pastMeetings.get(id);
        if(meeting.getDate().compareTo(new GregorianCalendar()) > 0) throw new IllegalArgumentException();
        return meeting;
    }

    @Override
    public FutureMeeting getFutureMeeting(int id) throws IllegalArgumentException {
        FutureMeeting meeting = futureMeetings.get(id);
        if(meeting.getDate().compareTo(new GregorianCalendar()) < 0) throw new IllegalArgumentException();
        return meeting;
    }

    @Override
    public Meeting getMeeting(int id) {
        return meetings.get(id);
    }

    @Override
    public List<Meeting> getFutureMeetingList(Contact contact) throws IllegalArgumentException {
        List<Meeting> futureMeetingList = new ArrayList<Meeting>();
        for (Meeting meeting : futureMeetings.values()){
            Set<Contact> contacts = meeting.getContacts();
            if(contacts.contains(contact)) {
                futureMeetingList.add(meeting);
            }
        }
        return futureMeetingList;
    }

    @Override
    public List<Meeting> getFutureMeetingList(Calendar date) {
        return null;
    }

    @Override
    public List<PastMeeting> getPastMeetingList(Contact contact) throws IllegalArgumentException {
        return null;
    }

    @Override
    public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) throws IllegalArgumentException, NullPointerException {
        if(contacts == null || date == null || text == null) throw new NullPointerException();
        if(notValidContacts(contacts)) throw new IllegalArgumentException();
        int meetingId = idGenerator.getMeetingId();
        try {
            PastMeeting meeting = meetingFactory.createPastMeeting(meetingId, contacts, date, text);
            pastMeetings.put(meeting.getId(), meeting);
            meetings.put(meeting.getId(), meeting);
        }
        catch (InvalidMeetingException e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void addMeetingNotes(int id, String text) throws IllegalArgumentException, IllegalStateException, NullPointerException {

    }

    @Override
    public void addNewContact(String name, String notes) throws NullPointerException {
        if(name == null || notes == null) throw new NullPointerException();
        Contact contact = contactFactory.createContact(idGenerator.getContactId(), name);
        contact.addNotes(notes);

        contactsByName.put(contact.getName(), contact);
        contactsById.put(contact.getId(), contact);
    }

    @Override
    public Set<Contact> getContacts(int... ids) throws IllegalArgumentException {
        Set<Contact> set = new HashSet();
        for(int id : ids) {
            if(contactsById.get(id) == null) throw new IllegalArgumentException();
            set.add(contactsById.get(id));
        }
        return set;
    }

    @Override
    public Set<Contact> getContacts(String name) throws NullPointerException {
        if(name == null) throw new NullPointerException();
        Set<Contact> set = new HashSet();
        set.add(contactsByName.get(name));
        return set;
    }

    @Override
    public void flush() {}

    private boolean notValidFutureMeeting(Set<Contact> contacts, Calendar date) {
        if(date.compareTo(new GregorianCalendar()) < 0) {
            return true;
        }
        return notValidContacts(contacts);
    }

    private boolean notValidContacts(Set<Contact> contacts) {
        if(contacts.isEmpty()) return true;

        for(Contact contact : contacts) {
            if(contactsById.get(contact.getId()) == null) {
                return true;
            }
        }
        return false;
    }
}
