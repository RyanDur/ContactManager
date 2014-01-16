import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;

import org.junit.*;
import org.junit.rules.ExpectedException;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ContactManagerTest {
    private IdGenerator mockIdGenerator;
    ContactFactory mockContactFactory;
    private MeetingFactory mockMeetingFactory;
    private ContactManager cm;
    private Set<Contact> mockContacts;
    private Contact mockContact;
    private Meeting mockMeeting;
    private FutureMeeting mockFutureMeeting;
    private PastMeeting mockPastMeeting;
    private Calendar mockDate = mockDate(1);
    private int knownId;
    private String knownName;
    private String knownNote;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() throws InvalidMeetingException {
        knownId = 400;
        knownName = "Ryan";
        knownNote = "note";

        setupMocks();
        cm = spy(new ContactManagerImpl(mockMeetingFactory, mockContactFactory, mockIdGenerator));
        addMockContact(mockContact, knownId, knownName);
    }

    @Test
    public void shouldBeAbleToAddaMeetingForTheFuture() throws InvalidMeetingException {
        int id = addMockFutureMeeting(mockFutureMeeting, mockContacts, mockDate, 0);
        FutureMeeting meeting = cm.getFutureMeeting(id);

        assertThat(meeting, is(instanceOf(FutureMeeting.class)));
        assertThat(mockFutureMeeting, is(equalTo(meeting)));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionIfUserTryToAddPastMeetingToFutureDate() {
        thrown.expect(IllegalArgumentException.class);

        cm.addFutureMeeting(mockContacts, mockDate(-1));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionIfAContactIsUnknown() throws InvalidMeetingException {
        thrown.expect(IllegalArgumentException.class);

        int unknownId = 42;
        Contact unknownContact = mock(Contact.class);
        mockContacts.add(unknownContact);
        when(unknownContact.getId()).thenReturn(unknownId);
        addMockFutureMeeting(mockFutureMeeting, mockContacts, mockDate(1), knownId);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionIfUserTyToGetPastMeetingFromFutureMeeting() throws InvalidMeetingException {
        thrown.expect(IllegalArgumentException.class);

        int id = addMockFutureMeeting(mockFutureMeeting, mockContacts, mockDate(1), knownId);
        when(mockFutureMeeting.getDate()).thenReturn(mockDate(-1));
        cm.getFutureMeeting(id);
    }

    @Test
    public void shouldBeAbleToAddANewContact() {
        Contact contact = cm.getContacts(knownName).toArray(new Contact[0])[0];

        assertThat(contact, is(instanceOf(Contact.class)));
        assertThat(mockContact, is(equalTo(contact)));
    }

    @Test
    public void shouldThrowNullPointerExceptionIfNoNameIsGivenToAdd() {
        thrown.expect(NullPointerException.class);

        String name = null;
        cm.addNewContact(name, knownNote);
    }

    @Test
    public void shouldThrowNullPointerExceptionIfNoNotesAreGivenToAdd() {
        thrown.expect(NullPointerException.class);

        String note = null;
        cm.addNewContact(knownName, note);
    }

    @Test
    public void shouldThrowNullPointerExceptionIfNoNameIsGivenToGet() {
        thrown.expect(NullPointerException.class);

        String name = null;
        cm.getContacts(name);
    }

    @Test
    public void shouldBeAbleToGetGetContactsById() {
        Contact contact = cm.getContacts(knownId).toArray(new Contact[0])[0];

        assertThat(contact, is(instanceOf(Contact.class)));
        assertThat(mockContact, is(equalTo(contact)));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionIfAnyOfTheIdsDoNotCorrespondToARealContact() {
        thrown.expect(IllegalArgumentException.class);

        int unknownId = 50;
        cm.getContacts(knownId, unknownId);
    }

    @Test
    public void shouldBeAbleToGetMeeting() throws InvalidMeetingException {
        int id = addMockFutureMeeting(mockFutureMeeting, mockContacts, mockDate, knownId);
        Meeting meeting = cm.getMeeting(id);

        int pastId = 9;
        when(mockPastMeeting.getId()).thenReturn(pastId);
        addMockPastMeeting(mockPastMeeting, mockContacts, mockDate, pastId, knownNote);
        Meeting meeting1 = cm.getMeeting(pastId);

        assertThat(meeting, is(equalTo((Meeting) mockFutureMeeting)));
        assertThat(meeting1, is(equalTo((Meeting) mockPastMeeting)));
    }

    @Test
    public void shouldGetNullIfMeetingDoesNotExist() {
        Meeting meeting = cm.getMeeting(8);

        assertThat(meeting, is(equalTo(null)));
    }

    @Test
    public void shouldBeAbleToCreateAPastMeeting() throws InvalidMeetingException {
        addMockPastMeeting(mockPastMeeting, mockContacts, mockDate(-1), knownId, knownNote);
        PastMeeting meeting = cm.getPastMeeting(knownId);

        assertThat(meeting, is(equalTo(mockPastMeeting)));
    }

    @Test
    public void shouldThrowANullPointerExceptionIfContactsIsNull() throws InvalidMeetingException {
        thrown.expect(NullPointerException.class);

        addMockPastMeeting(mockPastMeeting, null, mockDate, knownId, knownNote);
    }

    @Test
    public void shouldThrowANullPointerExceptionIfDateIsNull() throws InvalidMeetingException {
        thrown.expect(NullPointerException.class);

        addMockPastMeeting(mockPastMeeting, mockContacts, null, knownId, knownNote);
    }

    @Test
    public void shouldThrowANullPointerExceptionIftextIsNull() throws InvalidMeetingException {
        thrown.expect(NullPointerException.class);

        addMockPastMeeting(mockPastMeeting, mockContacts, mockDate, knownId, null);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionifTheListOfContactsIsEmpty() throws InvalidMeetingException {
        thrown.expect(IllegalArgumentException.class);

        Set<Contact> emptyContacts = new HashSet<Contact>();
        addMockPastMeeting(mockPastMeeting, emptyContacts, mockDate, knownId, knownNote);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionifAnyOfTheContactsDoNotExist() throws InvalidMeetingException {
        thrown.expect(IllegalArgumentException.class);

        mockContacts.add(mock(Contact.class));
        addMockPastMeeting(mockPastMeeting, mockContacts, mockDate, knownId, knownNote);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionIfThereIsAMeetingHappeningInTheFuture() throws InvalidMeetingException {
        thrown.expect(IllegalArgumentException.class);


        addMockPastMeeting(mockPastMeeting, mockContacts, mockDate(-1), knownId, knownNote);
        when(mockPastMeeting.getDate()).thenReturn(mockDate(1));
        cm.getPastMeeting(knownId);
    }

    @Test
    public void shouldBeAbleToGetTheFutureMeetingsOfAContact() throws InvalidMeetingException {
        Set<Contact> set = new HashSet<Contact>();
        Set<Contact> listOfContactsWithKnown = mockContacts;
        listOfContactsWithKnown.add(mockContact);

	FutureMeeting mockFutureMeetingWithKnownContact = mock(FutureMeeting.class);
        addMockFutureMeeting(mockFutureMeeting, mockContacts, mockDate(1), 0);
        addMockFutureMeeting(mockFutureMeetingWithKnownContact, listOfContactsWithKnown, mockDate(1), 0);

        List<Meeting> expected = new ArrayList<Meeting>();
	expected.add(mockFutureMeetingWithKnownContact);

        assertThat(expected, is(equalTo(cm.getFutureMeetingList(mockContact))));
    }

    private int addMockFutureMeeting(FutureMeeting fm, Set<Contact> contacts, Calendar date, int id) throws InvalidMeetingException {
        setFutureMeeting(fm);
        when(mockIdGenerator.getMeetingId()).thenReturn(id);
        when(fm.getId()).thenReturn(id);
        when(fm.getDate()).thenReturn(date);
        when(fm.getContacts()).thenReturn(contacts);
        return cm.addFutureMeeting(contacts, date);
    }

    private void addMockPastMeeting(PastMeeting pm, Set<Contact> contacts, Calendar date, int id, String note) throws InvalidMeetingException {
        setPastMeeting(pm);
        when(mockIdGenerator.getMeetingId()).thenReturn(id);
        when(pm.getId()).thenReturn(id);
        when(pm.getDate()).thenReturn(date);
        when(pm.getContacts()).thenReturn(contacts);
        cm.addNewPastMeeting(contacts, date, note);
    }

    private void setFutureMeeting(FutureMeeting fm) throws InvalidMeetingException {
        Set<Contact> anySet = any();
        Calendar anyDate = any();
        when(mockMeetingFactory.createFutureMeeting(anyInt(), anySet, anyDate)).thenReturn(fm);
    }

    private void setPastMeeting(PastMeeting pm) throws InvalidMeetingException {
        Set<Contact> anySet = any();
        Calendar anyDate = any();
        when(mockMeetingFactory.createPastMeeting(anyInt(), anySet, anyDate, anyString())).thenReturn(pm);
    }

    private void setMeeting(Meeting m) throws InvalidMeetingException {
        Set<Contact> anySet = any();
        Calendar anyDate = any();
        when(mockMeetingFactory.createMeeting(anyInt(), anySet, anyDate)).thenReturn(m);
    }

    private void addMockContact(Contact mockContact, int id, String name) {
        when(mockContactFactory.createContact(anyInt(), anyString())).thenReturn(mockContact);
        when(mockContact.getName()).thenReturn(name);
        when(mockContact.getId()).thenReturn(id);
        cm.addNewContact(knownName, knownNote);
    }

    private Calendar mockDate(int days) {
        Calendar date = new GregorianCalendar();
        date.add(Calendar.DATE, days);
        return date;
    }

    private void mockMeetingFactory() throws InvalidMeetingException {
        setMeeting(mockMeeting);
        setFutureMeeting(mockFutureMeeting);
        setPastMeeting(mockPastMeeting);
    }

    private void setupMocks() {
        mockContactFactory = mock(ContactFactory.class);
        mockMeetingFactory = mock(MeetingFactory.class);
        mockFutureMeeting = mock(FutureMeeting.class);
        mockContacts = new HashSet<Contact>();
        mockContact = mock(Contact.class);
        mockContacts.add(mockContact);
        mockDate = mockDate(1);

        mockMeeting = mock(Meeting.class);
        mockPastMeeting = mock(PastMeeting.class);
        mockIdGenerator = mock(IdGenerator.class);
    }
}
