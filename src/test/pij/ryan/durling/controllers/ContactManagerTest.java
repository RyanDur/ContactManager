package pij.ryan.durling.controllers;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pij.ryan.durling.models.Contact;
import pij.ryan.durling.models.FutureMeeting;
import pij.ryan.durling.models.Meeting;
import pij.ryan.durling.models.PastMeeting;
import pij.ryan.durling.serializers.Serializers;

import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import org.mockito.Mock;
import static org.mockito.MockitoAnnotations.initMocks;

public class ContactManagerTest {

    @Mock
    private Contacts mockContacts;
    @Mock
    private Meetings mockMeetings;
    @Mock
    private Serializers mockSerializers;

    private ContactManager cm;
    private Set<Contact> contactSet = new HashSet<>();
    private String notes = "notes";
    private Calendar date;
    private int id = 0;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        initMocks(this);
        when(mockSerializers.dataExists()).thenReturn(false);
        cm = new ContactManagerImpl(mockContacts, mockMeetings, mockSerializers);
        contactSet.add(mock(Contact.class));
        date = Calendar.getInstance();
    }

    @Test
    public void shouldBeAbleToAddAContact() {
        cm.addNewContact("Ryan", "notes");

        verify(mockContacts).add(anyString(), anyString());
    }

    @Test
    public void shouldThrowNullPointerExceptionIfAddNewContactsNameIsNull() {
        thrown.expect(NullPointerException.class);

        cm.addNewContact(null, "notes");
    }

    @Test
    public void shouldThrowNullPointerExceptionIfAddNewContactsNotesAreNull() {
        thrown.expect(NullPointerException.class);

        cm.addNewContact("Ryan", null);
    }

    @Test
    public void shouldBeAbleToGetAContactByName() {
        Set<Contact> expected = new HashSet<>();
        when(mockContacts.get(anyString())).thenReturn(expected);
        Set<Contact> actual = cm.getContacts("Ryan");

        verify(mockContacts).get(anyString());
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void shouldThrowNullPointerExceptionIfGetContactsNameIsNull() {
        thrown.expect(NullPointerException.class);

        String name = null;
        cm.getContacts(name);
    }

    @Test
    public void shouldBeAbleToGetAContactById() {
        Contact contact0 = mock(Contact.class);
        Contact contact1 = mock(Contact.class);
        Contact contact4 = mock(Contact.class);
        Set<Contact> expected = new HashSet<>();
        expected.add(contact0);
        expected.add(contact1);
        expected.add(contact4);
        when(mockContacts.get(anyInt())).thenReturn(contact0, contact1, contact4);
        Set<Contact> actual = cm.getContacts(0, 1, 4);

        verify(mockContacts, times(3)).get(anyInt());
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void shouldCheckIfIdsExistInTheContacts() {
        cm.getContacts(0, 1, 4);

        verify(mockContacts).notValidContactId((int[]) anyVararg());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionIfAnyOfTheIDsDoNotCorrespondToARealContact() {
        thrown.expect(IllegalArgumentException.class);

        when(mockContacts.notValidContactId((int[]) anyVararg())).thenReturn(true);
        cm.getContacts(10);
    }

    @Test
    public void shouldBeAbleToAddAFutureMeeting() {
        when(mockContacts.notValidContactSet(contactSet)).thenReturn(false);
        date.add(Calendar.DATE, 1);
        cm.addFutureMeeting(contactSet, date);

        verify(mockMeetings).addFutureMeeting(contactSet, date);
    }

    @Test
    public void shouldGetAnIDOfTheMeetingWhenAddingAFutureMeeting() {
        when(mockContacts.notValidContactSet(contactSet)).thenReturn(false);
        date.add(Calendar.DATE, 1);
        int expected = 1;
        when(mockMeetings.addFutureMeeting(contactSet, date)).thenReturn(expected);
        int actual = cm.addFutureMeeting(contactSet, date);

        verify(mockMeetings).addFutureMeeting(contactSet, date);
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void shouldThrowAnIllegalArgumentExceptionIfAContactDoesNotExistWhenAddingAFutureMeeting() {
        thrown.expect(IllegalArgumentException.class);

        when(mockContacts.notValidContactSet(contactSet)).thenReturn(true);
        cm.addFutureMeeting(contactSet, date);
    }

    @Test
    public void shouldThrowAnIllegalArgumentExceptionIfATheDateIsInThePastWhenAddingAFutureMeeting() {
        thrown.expect(IllegalArgumentException.class);

        date.add(Calendar.DATE, -1);
        when(mockMeetings.beforeToday(date)).thenReturn(true);
        when(mockContacts.notValidContactSet(contactSet)).thenReturn(false);
        cm.addFutureMeeting(contactSet, date);
    }

    @Test
    public void shouldBeAbleToAddAPastMeeting() {
        cm.addNewPastMeeting(contactSet, date, notes);

        verify(mockMeetings).addNewPastMeeting(contactSet, date, notes);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionIfTheListOfContactsIsEmptyWhenAddingAPastMeeting() {
        thrown.expect(IllegalArgumentException.class);

        Set<Contact> emptyContactSet = new HashSet<>();
        when(mockContacts.notValidContactSet(emptyContactSet)).thenReturn(true);
        cm.addNewPastMeeting(emptyContactSet, date, notes);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionIfAnyOfTheContactsDoesNotExistWhenAddingAPastMeeting() {
        thrown.expect(IllegalArgumentException.class);

        when(mockContacts.notValidContactSet(contactSet)).thenReturn(true);
        cm.addNewPastMeeting(contactSet, date, notes);
    }

    @Test
    public void shouldThrowNullPointerExceptionIfContactsAreNullWhenAddingAPastMeeting() {
        thrown.expect(NullPointerException.class);

        cm.addNewPastMeeting(null, date, notes);
    }

    @Test
    public void shouldThrowNullPointerExceptionIfTheDateIsNullWhenAddingAPastMeeting() {
        thrown.expect(NullPointerException.class);

        cm.addNewPastMeeting(contactSet, null, notes);
    }

    @Test
    public void shouldThrowNullPointerExceptionIfTheNotesAreNullWhenAddingAPastMeeting() {
        thrown.expect(NullPointerException.class);

        cm.addNewPastMeeting(contactSet, date, null);
    }

    @Test
    public void shouldBeAbleToAddNotesToAMeetingThatHasTakenPlace() {
        FutureMeeting mockFutureMeeting = mock(FutureMeeting.class);
        date.add(Calendar.DATE, -1);
        when(mockFutureMeeting.getDate()).thenReturn(date);
        when(mockMeetings.get(id)).thenReturn(mockFutureMeeting);
        cm.addMeetingNotes(id, notes);

        verify(mockMeetings).convertToPastMeeting(mockFutureMeeting, notes);
    }

    @Test
    public void shouldThrowAnIllegalArgumentExceptionIfTheMeetingDoesNotExistWhenAddingNotesToAMeeting() {
        thrown.expect(IllegalArgumentException.class);

        when(mockMeetings.get(id)).thenReturn(null);
        cm.addMeetingNotes(id, notes);
    }

    @Test
    public void shouldThrowAnIllegalStateExceptionIfTheMeetingIsSetForADateInTheFutureWhenAddingNotesToAMeeting() {
        thrown.expect(IllegalStateException.class);

        FutureMeeting mockFutureMeeting = mock(FutureMeeting.class);
        date.add(Calendar.DATE, 1);
        when(mockMeetings.afterToday(date)).thenReturn(true);
        when(mockFutureMeeting.getDate()).thenReturn(date);
        when(mockMeetings.get(id)).thenReturn(mockFutureMeeting);
        cm.addMeetingNotes(id, notes);
    }

    @Test
    public void shouldThrowANullPointerExceptionIfTheNotesAreNull() {
        thrown.expect(NullPointerException.class);

        FutureMeeting mockFutureMeeting = mock(FutureMeeting.class);
        date.add(Calendar.DATE, -1);
        when(mockFutureMeeting.getDate()).thenReturn(date);
        when(mockMeetings.get(id)).thenReturn(mockFutureMeeting);
        cm.addMeetingNotes(id, null);
    }

    @Test
    public void shouldBeAbleToGetAPastMeeting() {
        Meeting expected = mock(PastMeeting.class);
        date.add(Calendar.DATE, -1);
        when(expected.getDate()).thenReturn(date);
        when(mockMeetings.get(id)).thenReturn(expected);
        PastMeeting actual = cm.getPastMeeting(id);

        verify(mockMeetings).get(id);
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void shouldGetNullWhenThereIsNoPastMeeting() {
        when(mockMeetings.get(id)).thenReturn(null);
        Meeting actual = cm.getPastMeeting(id);

        verify(mockMeetings).get(id);
        assertThat(null, is(equalTo(actual)));
    }

    @Test
    public void ShouldThrowAnIllegalArgumentExceptionIfThereIsAMeetingWithThatIDHappeningInTheFutureWhenGettingAPastMeeting() {
        thrown.expect(IllegalArgumentException.class);

        Meeting mockMeeting = mock(PastMeeting.class);
        date.add(Calendar.DATE, 1);
        when(mockMeetings.afterToday(date)).thenReturn(true);
        when(mockMeeting.getDate()).thenReturn(date);
        when(mockMeetings.get(id)).thenReturn(mockMeeting);
        cm.getPastMeeting(id);
    }

    @Test
    public void shouldBeAbleToGetAFutureMeeting() {
        Meeting expected = mock(FutureMeeting.class);
        date.add(Calendar.DATE, 1);
        when(expected.getDate()).thenReturn(date);
        when(mockMeetings.get(id)).thenReturn(expected);
        FutureMeeting actual = cm.getFutureMeeting(id);

        verify(mockMeetings).get(id);
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void shouldBeGetNullThereIsNoFutureMeeting() {
        when(mockMeetings.get(id)).thenReturn(null);
        Meeting actual = cm.getFutureMeeting(id);

        verify(mockMeetings).get(id);
        assertThat(null, is(equalTo(actual)));
    }

    @Test
    public void ShouldThrowAnIllegalArgumentExceptionIfThereIsAMeetingWithThatIDHappeningInThePastWhenGettingAFutureMeeting() {
        thrown.expect(IllegalArgumentException.class);

        Meeting mockMeeting = mock(PastMeeting.class);
        date.add(Calendar.DATE, -2);
        when(mockMeeting.getDate()).thenReturn(date);
        when(mockMeetings.beforeToday(date)).thenReturn(true);
        when(mockMeetings.get(id)).thenReturn(mockMeeting);
        cm.getFutureMeeting(id);
    }

    @Test
    public void shouldBeAbleToGetAMeeting() {
        Meeting expected = mock(FutureMeeting.class);
        when(mockMeetings.get(id)).thenReturn(expected);
        Meeting actual = cm.getMeeting(id);

        verify(mockMeetings).get(id);
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void shouldGetNullThereIsNoMeeting() {
        when(mockMeetings.get(id)).thenReturn(null);
        Meeting actual = cm.getMeeting(id);

        verify(mockMeetings).get(id);
        assertThat(null, is(equalTo(actual)));
    }

    @Test
    public void shouldBeAbleToGetAListOfFutureMeetingsForAContactInChronologicalOrder() {
        Contact contact = mock(Contact.class);
        Calendar date1 = Calendar.getInstance();
        date1.add(Calendar.DATE, 2);
        Calendar date2 = Calendar.getInstance();
        date2.add(Calendar.DATE, 1);
        List<Meeting> meetingsList = new ArrayList<>();
        FutureMeeting futureMeeting1 = mock(FutureMeeting.class);
        when(futureMeeting1.getDate()).thenReturn(date1);
        FutureMeeting futureMeeting2 = mock(FutureMeeting.class);
        when(futureMeeting2.getDate()).thenReturn(date2);
        PastMeeting pastMeeting = mock(PastMeeting.class);
        meetingsList.add(futureMeeting1);
        meetingsList.add(pastMeeting);
        meetingsList.add(futureMeeting2);
        List<Meeting> expected = new ArrayList<>();
        expected.add(futureMeeting2);
        expected.add(futureMeeting1);
        when(mockMeetings.get(contact)).thenReturn(meetingsList);
        List<Meeting> actual = cm.getFutureMeetingList(contact);

        verify(mockMeetings).get(contact);
        assertThat(expected, is(equalTo(actual)));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionIfTheContactDoesNotExist() {
        thrown.expect(IllegalArgumentException.class);

        Contact contact = mock(Contact.class);
        when(mockContacts.notValidContactId(contact.getId())).thenReturn(true);
        cm.getFutureMeetingList(contact);
    }

    @Test
    public void shouldBeAbleToGetAAnEmptyListOfFutureMeetingsForAContact() {
        Contact contact = mock(Contact.class);
        List<Meeting> expected = new ArrayList<>();
        when(mockMeetings.get(contact)).thenReturn(null);
        List<Meeting> actual = cm.getFutureMeetingList(contact);

        verify(mockMeetings).get(contact);
        assertThat(expected, is(equalTo(actual)));
    }

    @Test
    public void shouldBeAbleToGetAListOfFutureMeetingsForADate() {
        List<Meeting> meetingsList = new ArrayList<>();
        FutureMeeting futureMeeting1 = mock(FutureMeeting.class);
        when(futureMeeting1.getDate()).thenReturn(mockDay(1));
        FutureMeeting futureMeeting2 = mock(FutureMeeting.class);
        when(futureMeeting2.getDate()).thenReturn(mockDay(2));
        PastMeeting pastMeeting = mock(PastMeeting.class);
        meetingsList.add(futureMeeting1);
        meetingsList.add(pastMeeting);
        meetingsList.add(futureMeeting2);
        List<Meeting> expected = new ArrayList<>();
        expected.add(futureMeeting1);
        expected.add(futureMeeting2);
        when(mockMeetings.get(date)).thenReturn(meetingsList);
        List<Meeting> actual = cm.getFutureMeetingList(date);

        verify(mockMeetings).get(date);
        assertThat(expected, is(equalTo(actual)));
    }

    @Test
    public void shouldBeAbleToGetAListOfFutureMeetingsForADateInChronologicalOrder() {
        List<Meeting> meetingsList = new ArrayList<>();
        FutureMeeting futureMeeting1 = mock(FutureMeeting.class);
        when(futureMeeting1.getDate()).thenReturn(mockDay(3));
        FutureMeeting futureMeeting2 = mock(FutureMeeting.class);
        when(futureMeeting2.getDate()).thenReturn(mockDay(1));
        FutureMeeting futureMeeting3 = mock(FutureMeeting.class);
        when(futureMeeting3.getDate()).thenReturn(mockDay(2));
        meetingsList.add(futureMeeting1);
        meetingsList.add(futureMeeting2);
        meetingsList.add(futureMeeting3);
        List<Meeting> expected = new ArrayList<>();
        expected.add(futureMeeting2);
        expected.add(futureMeeting3);
        expected.add(futureMeeting1);
        when(mockMeetings.get(date)).thenReturn(meetingsList);
        List<Meeting> actual = cm.getFutureMeetingList(date);

        verify(mockMeetings).get(date);
        assertThat(expected, is(equalTo(actual)));
    }

    @Test
    public void shouldBeAbleToGetAListOfPastMeetingsForAContactInChronologicalOrder() {
        Contact contact = mock(Contact.class);
        Calendar date1 = Calendar.getInstance();
        date1.add(Calendar.DATE, -1);
        Calendar date2 = Calendar.getInstance();
        date2.add(Calendar.DATE, -2);
        List<Meeting> meetingsList = new ArrayList<>();
        PastMeeting pastMeeting1 = mock(PastMeeting.class);
        when(pastMeeting1.getDate()).thenReturn(date1);
        PastMeeting pastMeeting2 = mock(PastMeeting.class);
        when(pastMeeting2.getDate()).thenReturn(date2);
        FutureMeeting futureMeeting = mock(FutureMeeting.class);
        meetingsList.add(pastMeeting1);
        meetingsList.add(futureMeeting);
        meetingsList.add(pastMeeting2);
        List<PastMeeting> expected = new ArrayList<>();
        expected.add(pastMeeting2);
        expected.add(pastMeeting1);
        when(mockMeetings.get(contact)).thenReturn(meetingsList);
        List<PastMeeting> actual = cm.getPastMeetingList(contact);

        verify(mockMeetings).get(contact);
        assertThat(expected, is(equalTo(actual)));
    }

    @Test
    public void shouldThrowAnIllegalArgumentExceptionIfTheContactDoesNotExistWhenGettingAListOfPastMeetings() {
        thrown.expect(IllegalArgumentException.class);

        Contact contact = mock(Contact.class);
        when(mockContacts.notValidContactId(contact.getId())).thenReturn(true);
        cm.getPastMeetingList(contact);
    }

    @Test
    public void shouldBeAbleToGetAnEmptyListOfPastMeetingsForAContact() {
        Contact contact = mock(Contact.class);
        List<PastMeeting> expected = new ArrayList<>();
        when(mockMeetings.get(contact)).thenReturn(null);
        List<PastMeeting> actual = cm.getPastMeetingList(contact);

        verify(mockMeetings).get(contact);
        assertThat(expected, is(equalTo(actual)));
    }

    @Test
    public void shouldSerializeOnFlush() {
        cm.flush();
        verify(mockSerializers).serialize(eq(mockMeetings), eq(mockContacts));
    }

    @Test
    public void shouldLoadPersistedDataOnCreation() {
        when(mockSerializers.dataExists()).thenReturn(true);
        Object[] objects = {mockMeetings, mockContacts};
        when(mockSerializers.deserialize()).thenReturn(objects);
        new ContactManagerImpl(mockContacts, mockMeetings, mockSerializers);
        verify(mockSerializers).deserialize();
    }

    private Calendar mockDay(int hour) {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        date.set(Calendar.HOUR_OF_DAY, hour);
        return date;
    }
}