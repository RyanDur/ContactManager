import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ContactManagerTest {
  private IdGenerator mockIdGenerator;
  private ContactFactory mockContactFactory;
  private MeetingFactory mockMeetingFactory;
  private ContactManager cm;
  private Set<Contact> mockContacts;
  private Contact mockContact;
  private FutureMeeting mockFutureMeeting;
  private PastMeeting mockPastMeeting;
  private Calendar mockDate = mockDate(1);
  private int knownId;
  private String knownName;
  private String knownNote;
  private int numberOfFutureMeetings;
  private int numberOfPastMeetings;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setup() throws InvalidMeetingException {
    knownId = 400;
    knownName = "Ryan";
    knownNote = "note";
    numberOfFutureMeetings = 100;
    numberOfPastMeetings = 100;

    setupMocks();
    cm = spy(new ContactManagerImpl(mockMeetingFactory, mockContactFactory, mockIdGenerator));
    addMockContact(mockContact, knownId, knownName);
    setupMeetings(numberOfFutureMeetings, numberOfPastMeetings);
  }

  @Test
  public void shouldBeAbleToAddaMeetingForTheFuture() {
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
  public void shouldThrowIllegalArgumentExceptionIfAContactIsUnknown() {
    thrown.expect(IllegalArgumentException.class);

    int unknownId = 42;
    Contact unknownContact = mock(Contact.class);
    mockContacts.add(unknownContact);
    when(unknownContact.getId()).thenReturn(unknownId);
    addMockFutureMeeting(mockFutureMeeting, mockContacts, mockDate(1), knownId);
  }

  @Test
  public void shouldThrowIllegalArgumentExceptionIfUserTyToGetPastMeetingFromFutureMeeting() {
    thrown.expect(IllegalArgumentException.class);

    int id = addMockFutureMeeting(mockFutureMeeting, mockContacts, mockDate(1), knownId);
    when(mockFutureMeeting.getDate()).thenReturn(mockDate(-1));
    cm.getFutureMeeting(id);
  }

  @Test
  public void shouldBeAbleToAddANewContactByName() {
    Set<Contact> contacts;
    contacts = cm.getContacts(knownName);

    assertTrue(contacts.contains(mockContact));
  }

  @Test
  public void shouldBeAbleToRetrieveContactsOfSamNameButDifferentId() {
    Contact contact1 = mock(Contact.class);
    Contact contact2 = mock(Contact.class);
    addMockContact(contact1, 0, knownName);
    addMockContact(contact2, 1, knownName);

    Set<Contact> expected = new HashSet<>();
    expected.add(contact1);
    expected.add(contact2);
    expected.add(mockContact);

    Set<Contact> actual = cm.getContacts(knownName);

    assertThat(expected, is(equalTo(actual)));
  }

  @Test
  public void shouldThrowNullPointerExceptionIfNoNameIsGivenToAdd() {
    thrown.expect(NullPointerException.class);

    cm.addNewContact(null, knownNote);
  }

  @Test
  public void shouldThrowNullPointerExceptionIfNoNotesAreGivenToAdd() {
    thrown.expect(NullPointerException.class);

    cm.addNewContact(knownName, null);
  }

  @Test
  public void shouldThrowNullPointerExceptionIfNoNameIsGivenToGet() {
    thrown.expect(NullPointerException.class);

    String name = null;
    cm.getContacts(name);
  }

  @Test
  public void shouldBeAbleToGetGetContactsById() {
    Set<Contact> contacts = cm.getContacts(knownId);
    Set<Contact> expected = new HashSet<>();
    expected.add(mockContact);

    assertThat(expected, is(equalTo(contacts)));
  }

  @Test
  public void shouldThrowIllegalArgumentExceptionIfAnyOfTheIdsDoNotCorrespondToARealContact() {
    thrown.expect(IllegalArgumentException.class);

    int unknownId = 50;
    cm.getContacts(knownId, unknownId);
  }

  @Test
  public void shouldBeAbleToGetMeeting() {
    int id = addMockFutureMeeting(mockFutureMeeting, mockContacts, mockDate, knownId);
    Meeting meeting = cm.getMeeting(id);

    int pastId = 9;
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
  public void shouldBeAbleToAddAPastMeeting() {
    addMockPastMeeting(mockPastMeeting, mockContacts, mockDate(-1), knownId, knownNote);
    PastMeeting meeting = cm.getPastMeeting(knownId);

    assertThat(meeting, is(equalTo(mockPastMeeting)));
  }

  @Test
  public void shouldThrowANullPointerExceptionIfContactsIsNull() {
    thrown.expect(NullPointerException.class);

    addMockPastMeeting(mockPastMeeting, null, mockDate, knownId, knownNote);
  }

  @Test
  public void shouldThrowANullPointerExceptionIfDateIsNull() throws InvalidMeetingException {
    thrown.expect(NullPointerException.class);

    addMockPastMeeting(mockPastMeeting, mockContacts, null, knownId, knownNote);
  }

  @Test
  public void shouldThrowANullPointerExceptionIfTextIsNull() {
    thrown.expect(NullPointerException.class);

    addMockPastMeeting(mockPastMeeting, mockContacts, mockDate, knownId, null);
  }

  @Test
  public void shouldThrowIllegalArgumentExceptionIfTheListOfContactsIsEmpty() {
    thrown.expect(IllegalArgumentException.class);

    Set<Contact> emptyContacts = new HashSet<>();
    addMockPastMeeting(mockPastMeeting, emptyContacts, mockDate, knownId, knownNote);
  }

  @Test
  public void shouldThrowIllegalArgumentExceptionIfAnyOfTheContactsDoNotExist() {
    thrown.expect(IllegalArgumentException.class);

    mockContacts.add(mock(Contact.class));
    addMockPastMeeting(mockPastMeeting, mockContacts, mockDate, knownId, knownNote);
  }

  @Test
  public void shouldThrowIllegalArgumentExceptionIfThereIsAMeetingHappeningInTheFuture() {
    thrown.expect(IllegalArgumentException.class);

    addMockPastMeeting(mockPastMeeting, mockContacts, mockDate(-1), knownId, knownNote);
    when(mockPastMeeting.getDate()).thenReturn(mockDate(1));
    cm.getPastMeeting(knownId);
  }

  @Test
  public void shouldBeAbleToGetTheFutureMeetingsOfAContact() {
    Contact knownContact = mock(Contact.class);
    int knownContactId = 191;
    addMockContact(knownContact, knownContactId, knownName);

    Set<Contact> moreMockedContacts = listOfContacts(10, null, 100);
    Set<Contact> listOfContactsWithKnown = listOfContacts(10, knownContact, 200);

    FutureMeeting mockFutureMeetingWithKnownContact = mock(FutureMeeting.class);
    FutureMeeting anotherMockFutureMeeting = mock(FutureMeeting.class);

    addMockFutureMeeting(mockFutureMeeting, mockContacts, mockDate(1), 0);
    addMockFutureMeeting(anotherMockFutureMeeting, moreMockedContacts, mockDate(1), 1);
    addMockFutureMeeting(mockFutureMeetingWithKnownContact, listOfContactsWithKnown, mockDate(1), 2);

    List<Meeting> expected = new ArrayList<>();
    expected.add(mockFutureMeetingWithKnownContact);

    assertThat(cm.getFutureMeetingList(knownContact), is(equalTo(expected)));
  }

  @Test
  public void shouldThrowIllegalArgumentExceptionIfTheContactDoesNotExist() {
    thrown.expect(IllegalArgumentException.class);

    addMockFutureMeeting(mockFutureMeeting, mockContacts, mockDate(1), 0);
    cm.getFutureMeetingList(mock(Contact.class));
  }

  @Test
  public void shouldBeAbleToGetTheFutureMeetingsByDate() throws InvalidMeetingException {
    FutureMeeting anotherMockFutureMeeting = mock(FutureMeeting.class);

    Calendar date = mockDate(2);
    addMockFutureMeeting(mockFutureMeeting, mockContacts, date, 0);
    addMockFutureMeeting(anotherMockFutureMeeting, mockContacts, mockDate, 1);

    List<Meeting> actual = cm.getFutureMeetingList(mockDate);

    assertTrue(actual.size() == numberOfFutureMeetings+1);
    assertTrue(actual.contains(anotherMockFutureMeeting));
  }

  @Test
  public void shouldReturnAListOfPastMeetingsInWhichAContactHasParticipated() {
    Contact knownContact = mock(Contact.class);
    int knownContactId = 191;
    addMockContact(knownContact, knownContactId, knownName);

    Set<Contact> moreMockedContacts = listOfContacts(10, null, 100);
    Set<Contact> listOfContactsWithKnown = listOfContacts(10, knownContact, 200);

    PastMeeting mockPastMeetingWithKnownContact = mock(PastMeeting.class);
    PastMeeting anotherMockPastMeeting = mock(PastMeeting.class);

    addMockPastMeeting(mockPastMeeting, mockContacts, mockDate(-1), 0, knownNote);
    addMockPastMeeting(anotherMockPastMeeting, moreMockedContacts, mockDate(-1), 1, knownNote);
    addMockPastMeeting(mockPastMeetingWithKnownContact, listOfContactsWithKnown, mockDate(-1), 2, knownNote);

    List<PastMeeting> expected = new ArrayList<>();
    expected.add(mockPastMeetingWithKnownContact);

    assertThat(cm.getPastMeetingList(knownContact), is(equalTo(expected)));
  }

  @Test
  public void shouldThrowIllegalArgumentExceptionIfAContactDoesNotExist() {
    thrown.expect(IllegalArgumentException.class);

    addMockPastMeeting(mockPastMeeting, mockContacts, mockDate(-1), 0, knownNote);
    cm.getPastMeetingList(mock(Contact.class));
  }

  @Test
  public void shouldTakAFutureMeetingAndMakeItAPastMeetingWithNotes() {
    Contact knownContact = mock(Contact.class);
    int knownContactId = 191;
    addMockContact(knownContact, knownContactId, knownName);

    Set<Contact> moreMockedContacts = listOfContacts(10, null, 100);
    Set<Contact> listOfContactsWithKnown = listOfContacts(10, knownContact, 200);

    FutureMeeting mockFutureMeetingWithKnownContact = mock(FutureMeeting.class);
    FutureMeeting anotherMockFutureMeeting = mock(FutureMeeting.class);

    addMockFutureMeeting(mockFutureMeeting, mockContacts, mockDate(1), 0);
    addMockFutureMeeting(anotherMockFutureMeeting, moreMockedContacts, mockDate(1), 1);
    addMockFutureMeeting(mockFutureMeetingWithKnownContact, listOfContactsWithKnown, mockDate(1), 2);

    List<Meeting> first = new ArrayList<>();
    first.add(mockFutureMeetingWithKnownContact);
    assertThat(first, is(equalTo(cm.getFutureMeetingList(knownContact))));

    List<PastMeeting> empty = new ArrayList<>();
    assertThat(empty, is(equalTo(cm.getPastMeetingList(mockContact))));

    addMockPastMeeting(mockPastMeeting, mockContacts, mockDate(1), knownId, knownNote);
    List<PastMeeting> actual = cm.getPastMeetingList(mockContact);

    assertThat(new ArrayList<PastMeeting>(), is(not(actual)));
    assertThat(knownId, is(equalTo(actual.get(0).getId())));
  }

  @Test
  public void shouldThrowAnIllegalArgumentExceptionIfAMeetingDoesNotExist() {
    thrown.expect(IllegalArgumentException.class);

    int unKnownId = 4500;
    cm.addMeetingNotes(unKnownId, knownNote);
  }

  @Test
  public void shouldThrowIllegalStateExceptionIfTheMeetingIsSetForADateInTheFuture() {
    thrown.expect(IllegalStateException.class);

    addMockFutureMeeting(mockFutureMeeting, mockContacts, mockDate(1), knownId);
    cm.addMeetingNotes(knownId, knownNote);
  }

  @Test
  public void shouldThrowANullPointerExceptionIfTheNotesAreNull() {
    thrown.expect(NullPointerException.class);

    cm.addMeetingNotes(knownId, null);
  }

  private void setupMeetings(int numberOfFutureMeetings, int numberOfPastMeetings) {
    setupFutureMeetings(numberOfFutureMeetings);
    setupPastMeetings(numberOfPastMeetings);
  }

  private void setupPastMeetings(int numberOfPastMeetings) {
    int drift = 2000;
    Set<Contact> mockContacts = listOfContacts(10, null, drift);
    for (int i = 0; i < numberOfPastMeetings; i++) {
      addMockPastMeeting(mock(PastMeeting.class), mockContacts, mockDate(-1), i + drift, knownNote);
    }
  }

  private void setupFutureMeetings(int numberOfFutreMeetings) {
    int drift = 1000;
    Set<Contact> mockContacts = listOfContacts(10, null, drift);
    for (int i = 0; i < numberOfFutreMeetings; i++) {
      addMockFutureMeeting(mock(FutureMeeting.class), mockContacts, mockDate, i + drift);
    }
  }

  private Set<Contact> listOfContacts(int numberOfUnknownContacts, Contact contact, int idDrift) {
    Set<Contact> list = new HashSet<>();
    for (int i = 0; i < numberOfUnknownContacts; i++) {
      Contact mockContact = mock(Contact.class);
      addMockContact(mockContact, i + idDrift, knownName+i);
      list.add(mockContact);
    }
    if (contact != null) {
      list.add(contact);
    }
    return list;
  }

  private int addMockFutureMeeting(FutureMeeting fm, Set<Contact> contacts, Calendar date, int id) {
    setFutureMeeting(fm);
    when(mockIdGenerator.getMeetingId()).thenReturn(id);
    when(fm.getId()).thenReturn(id);
    when(fm.getDate()).thenReturn(date);
    when(fm.getContacts()).thenReturn(contacts);
    return cm.addFutureMeeting(contacts, date);
  }

  private void addMockPastMeeting(PastMeeting pm, Set<Contact> contacts, Calendar date, int id, String note) {
    setPastMeeting(pm);
    when(mockIdGenerator.getMeetingId()).thenReturn(id);
    when(pm.getId()).thenReturn(id);
    when(pm.getDate()).thenReturn(date);
    when(pm.getContacts()).thenReturn(contacts);
    cm.addNewPastMeeting(contacts, date, note);
  }

  private void setFutureMeeting(FutureMeeting fm) {
    Set<Contact> anySet = any();
    Calendar anyDate = any();
    try {
      when(mockMeetingFactory.createFutureMeeting(anyInt(), anySet, anyDate)).thenReturn(fm);
    } catch (InvalidMeetingException e) {
      e.printStackTrace();
    }
  }

  private void setPastMeeting(PastMeeting pm) {
    Set<Contact> anySet = any();
    Calendar anyDate = any();
    try {
      when(mockMeetingFactory.createPastMeeting(anyInt(), anySet, anyDate, anyString())).thenReturn(pm);
    } catch (InvalidMeetingException e) {
      e.printStackTrace();
    }
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

  private void setupMocks() {
    mockContactFactory = mock(ContactFactory.class);
    mockMeetingFactory = mock(MeetingFactory.class);
    mockFutureMeeting = mock(FutureMeeting.class);
    mockContacts = new HashSet<>();
    mockContact = mock(Contact.class);
    mockContacts.add(mockContact);
    mockDate = mockDate(1);

    mockPastMeeting = mock(PastMeeting.class);
    mockIdGenerator = mock(IdGenerator.class);
  }
}