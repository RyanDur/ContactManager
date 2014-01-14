import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.HashSet;

import org.junit.*;
import org.junit.rules.ExpectedException;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ContactManagerTest {
    private ContactManager cm;
    private Set<Contact> mockContacts;
    private Contact mockContact;
    private Meeting mockMeeting;
    private FutureMeeting mockFutureMeeting;
    private PastMeeting mockPastMeeting;
    private Calendar mockDate = mockDate(1);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Set<Contact> getMockContacts(int numOfMocks) {
        Set<Contact> set = new HashSet<Contact>();
        for(int i = 0; i < numOfMocks; i++) {
            set.add(mock(Contact.class));
        }
        assertEquals(numOfMocks, set.size());
        return set;
    }

    @Before
    public void setup() throws InvalidMeetingException {
        mockContacts = new HashSet<Contact>();
        mockContact = mock(Contact.class);
        mockContacts.add(mockContact);
        mockDate = mockDate(1);

        mockMeeting = mock(Meeting.class);
        mockFutureMeeting = mock(FutureMeeting.class);
        mockPastMeeting = mock(PastMeeting.class);

        cm = new ContactManagerImpl(mockMeetingFactory(), mockContactFactory());
    }

    @Test
    public void shouldBeAbleToAddaMeetingForTheFuture() {
        when(mockFutureMeeting.getDate()).thenReturn(mockDate);
        int id = cm.addFutureMeeting(mockContacts, mockDate);
        Meeting meeting = cm.getFutureMeeting(id);

        assertThat(meeting, is(instanceOf(FutureMeeting.class)));
        assertThat(mockFutureMeeting, is(equalTo(meeting)));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionIfUserTryToAddPastMeetingToFutureDate() {
        thrown.expect(IllegalArgumentException.class);

        cm.addFutureMeeting(mockContacts, mockDate(-1));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionIfUserTyToGetPastMeetingFromFutureMeeting() {
        thrown.expect(IllegalArgumentException.class);
        when(mockFutureMeeting.getDate()).thenReturn(mockDate(-1));
        int id = cm.addFutureMeeting(mockContacts, mockDate);

        cm.getFutureMeeting(id);
    }

    @Test
    public void shouldBeAbleToAddANewContact() {
        String name = "Ryan";
        String note = "note";
        when(mockContact.getNotes()).thenReturn(note);
        when(mockContact.getName()).thenReturn(name);

        cm.addNewContact(name, note);
        Contact contact = cm.getContacts(name).toArray(new Contact[0])[0];

        assertThat(contact, is(instanceOf(Contact.class)));
        assertThat(mockContact, is(equalTo(contact)));
    }

    @Test
    public void shouldThrowNullPointerExceptionIfNoNameIsGiven() {
        thrown.expect(NullPointerException.class);
        String name = null;
        String note = "note";

        cm.addNewContact(name, note);
    }

    @Test
    public void shouldThrowNullPointerExceptionIfNoNotesAreGiven() {
        thrown.expect(NullPointerException.class);
        String name = "Ryan";
        String note = null;

        cm.addNewContact(name, note);
    }


    private Calendar mockDate(int days) {
        Calendar date = new GregorianCalendar();
        date.add(Calendar.DATE, days);
        return date;
    }

    private MeetingFactory mockMeetingFactory() throws InvalidMeetingException {
        MeetingFactory mockMeetingFactory = mock(MeetingFactory.class);
        when(mockMeetingFactory.createMeeting(anyInt(), eq(mockContacts), eq(mockDate))).thenReturn(mockMeeting);
        when(mockMeetingFactory.createFutureMeeting(anyInt(), eq(mockContacts), eq(mockDate))).thenReturn(mockFutureMeeting);
        when(mockMeetingFactory.createPastMeeting(eq(mockMeeting), anyString())).thenReturn(mockPastMeeting);
        return mockMeetingFactory;
    }

    private ContactFactory mockContactFactory() {
        ContactFactory mockContactFactory = mock(ContactFactory.class);
        when(mockContactFactory.createContact(anyInt(), anyString())).thenReturn(mockContact);
        return mockContactFactory;
    }
}
