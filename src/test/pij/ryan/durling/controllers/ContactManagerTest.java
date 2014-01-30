package pij.ryan.durling.controllers;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pij.ryan.durling.models.Contact;
import pij.ryan.durling.models.FutureMeeting;
import pij.ryan.durling.models.Meeting;
import pij.ryan.durling.models.PastMeeting;

import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class ContactManagerTest {
  Contacts contacts = mock(Contacts.class);
  Calendar date;
  int id = 0;
  String notes = "notes";
  Meetings meetings = mock(Meetings.class);
  Set<Contact> contactSet = new HashSet<>();
  ContactManager cm;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setup() {
    cm = new ContactManagerImpl(contacts, meetings);
    contactSet.add(mock(Contact.class));
    date = Calendar.getInstance();
  }

  @Test
  public void shouldBeAbleToAddAContact() {
    cm.addNewContact("Ryan", "notes");
    verify(contacts).add(anyString(), anyString());
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
    cm.getContacts("Ryan");
    verify(contacts).get(anyString());
  }

  @Test
  public void shouldThrowNullPointerExceptionIfGetContactsNameIsNull() {
    thrown.expect(NullPointerException.class);
    String name = null;
    cm.getContacts(name);
  }

  @Test
  public void shouldBeAbleToGetAContactById() {
    cm.getContacts(0, 1, 4);
    verify(contacts, times(3)).get(anyInt());
  }

  @Test
  public void shouldCheckIfIdsExistInTheContacts() {
    cm.getContacts(0, 1, 4);
    verify(contacts).notValidContactId((int[]) anyVararg());
  }

  @Test
  public void shouldThrowIllegalArgumentExceptionIfAnyOfTheIDsDoNotCorrespondToARealContact() {
    thrown.expect(IllegalArgumentException.class);
    when(contacts.notValidContactId((int[]) anyVararg())).thenReturn(true);
    cm.getContacts(10);
  }

  @Test
  public void shouldBeAbleToAddAFutureMeeting() {
    cm.addFutureMeeting(contactSet, date);
    verify(meetings).addFutureMeeting(contactSet, date);
  }

  @Test
  public void shouldThrowAnIllegalArgumentExceptionIfAContactDoesNotExistWhenAddingAFutureMeeting() {
    thrown.expect(IllegalArgumentException.class);
    when(contacts.notValidContactSet(contactSet)).thenReturn(true);
    cm.addFutureMeeting(contactSet, date);
  }

  @Test
  public void shouldThrowAnIllegalArgumentExceptionIfATheDateIsInThePastWhenAddingAFutureMeeting() {
    thrown.expect(IllegalArgumentException.class);
    date.add(Calendar.DATE, -1);
    when(contacts.notValidContactSet(contactSet)).thenReturn(false);
    cm.addFutureMeeting(contactSet, date);
  }

  @Test
  public void shouldBeAbleToAddAPastMeeting() {
    cm.addNewPastMeeting(contactSet, date, notes);
    verify(meetings).addNewPastMeeting(contactSet, date, notes);
  }

  @Test
  public void shouldThrowIllegalArgumentExceptionIfTheListOfContactsIsEmptyWhenAddingAPastMeeting() {
    thrown.expect(IllegalArgumentException.class);
    Set<Contact> emptyContactSet = new HashSet<>();
    when(contacts.notValidContactSet(emptyContactSet)).thenReturn(true);
    cm.addNewPastMeeting(emptyContactSet, date, notes);
  }

  @Test
  public void shouldThrowIllegalArgumentExceptionIfAnyOfTheContactsDoesNotExistWhenAddingAPastMeeting() {
    thrown.expect(IllegalArgumentException.class);
    when(contacts.notValidContactSet(contactSet)).thenReturn(true);
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
    when(meetings.get(id)).thenReturn(mockFutureMeeting);
    cm.addMeetingNotes(id, notes);
    verify(meetings).convertToPastMeeting(mockFutureMeeting, notes);
  }

  @Test
  public void shouldThrowAnIllegalArgumentExceptionIfTheMeetingDoesNotExistWhenAddingNotesToAMeeting() {
    thrown.expect(IllegalArgumentException.class);
    when(meetings.get(id)).thenReturn(null);
    cm.addMeetingNotes(id, notes);
  }

  @Test
  public void shouldThrowAnIllegalStateExceptionIfTheMeetingIsSetForADateInTheFutureWhenAddingNotesToAMeeting() {
    thrown.expect(IllegalStateException.class);
    FutureMeeting mockFutureMeeting = mock(FutureMeeting.class);
    date.add(Calendar.DATE, 1);
    when(mockFutureMeeting.getDate()).thenReturn(date);
    when(meetings.get(id)).thenReturn(mockFutureMeeting);
    cm.addMeetingNotes(id, notes);
  }

  @Test
  public void shouldThrowANullPointerExceptionIfTheNotesAreNull() {
    thrown.expect(NullPointerException.class);
    FutureMeeting mockFutureMeeting = mock(FutureMeeting.class);
    date.add(Calendar.DATE, -1);
    when(mockFutureMeeting.getDate()).thenReturn(date);
    when(meetings.get(id)).thenReturn(mockFutureMeeting);
    cm.addMeetingNotes(id, null);
  }

  @Test
  public void shouldBeAbleToGetAPastMeeting() {
    Meeting mockMeeting = mock(PastMeeting.class);
    date.add(Calendar.DATE, -1);
    when(mockMeeting.getDate()).thenReturn(date);
    when(meetings.get(id)).thenReturn(mockMeeting);
    cm.getPastMeeting(id);
    verify(meetings).get(id);
  }

  @Test
  public void shouldBeGetNullThereIsNoPastMeeting() {
    when(meetings.get(id)).thenReturn(null);
    Meeting actual = cm.getPastMeeting(id);
    verify(meetings).get(id);
    assertThat(null, is(equalTo(actual)));
  }

  @Test
  public void ShouldThrowAnIllegalArgumentExceptionIfThereIsAMeetingWithThatIDHappeningInTheFutureWhenGettingAPastMeeting() {
    thrown.expect(IllegalArgumentException.class);
    Meeting mockMeeting = mock(PastMeeting.class);
    date.add(Calendar.DATE, 1);
    when(mockMeeting.getDate()).thenReturn(date);
    when(meetings.get(id)).thenReturn(mockMeeting);
    cm.getPastMeeting(id);
  }

  @Test
  public void shouldBeAbleToGetAFutureMeeting() {
    Meeting mockMeeting = mock(FutureMeeting.class);
    date.add(Calendar.DATE, 1);
    when(mockMeeting.getDate()).thenReturn(date);
    when(meetings.get(id)).thenReturn(mockMeeting);
    cm.getFutureMeeting(id);
    verify(meetings).get(id);
  }

  @Test
  public void shouldBeGetNullThereIsNoFutureMeeting() {
    when(meetings.get(id)).thenReturn(null);
    Meeting actual = cm.getFutureMeeting(id);
    verify(meetings).get(id);
    assertThat(null, is(equalTo(actual)));
  }

  @Test
  public void ShouldThrowAnIllegalArgumentExceptionIfThereIsAMeetingWithThatIDHappeningInThePastWhenGettingAFutureMeeting() {
    thrown.expect(IllegalArgumentException.class);
    Meeting mockMeeting = mock(PastMeeting.class);
    date.add(Calendar.DATE, -1);
    when(mockMeeting.getDate()).thenReturn(date);
    when(meetings.get(id)).thenReturn(mockMeeting);
    cm.getFutureMeeting(id);
  }

  @Test
  public void shouldBeAbleToGetAMeeting() {
    Meeting mockMeeting = mock(FutureMeeting.class);
    when(meetings.get(id)).thenReturn(mockMeeting);
    cm.getMeeting(id);
    verify(meetings).get(id);
  }

  @Test
  public void shouldGetNullThereIsNoMeeting() {
    when(meetings.get(id)).thenReturn(null);
    Meeting actual = cm.getMeeting(id);
    verify(meetings).get(id);
    assertThat(null, is(equalTo(actual)));
  }

  @Test
  public void shouldBeAbleToGetAListOfFutureMeetingsForAContact() {
    Contact contact = mock(Contact.class);
    List<Meeting> meetingsList = new ArrayList<>();
    FutureMeeting futureMeeting1 = mock(FutureMeeting.class);
    FutureMeeting futureMeeting2 = mock(FutureMeeting.class);
    PastMeeting pastMeeting = mock(PastMeeting.class);
    meetingsList.add(futureMeeting1);
    meetingsList.add(pastMeeting);
    meetingsList.add(futureMeeting2);
    List<Meeting> expected = new ArrayList<>();
    expected.add(futureMeeting1);
    expected.add(futureMeeting2);

    when(meetings.get(contact)).thenReturn(meetingsList);
    List<Meeting> actual = cm.getFutureMeetingList(contact);
    verify(meetings).get(contact);

    assertThat(expected, is(equalTo(actual)));
  }

  @Test
  public void shouldThrowIllegalArgumentExceptionIfTheContactDoesNotExist() {
    thrown.expect(IllegalArgumentException.class);
    Contact contact = mock(Contact.class);
    when(contacts.notValidContactId(contact.getId())).thenReturn(true);
    cm.getFutureMeetingList(contact);
  }

  @Test
  public void shouldBeAbleToGetAAnEmptyListOfFutureMeetingsForAContact() {
    Contact contact = mock(Contact.class);
    List<Meeting> expected = new ArrayList<>();

    when(meetings.get(contact)).thenReturn(null);
    List<Meeting> actual = cm.getFutureMeetingList(contact);
    verify(meetings).get(contact);

    assertThat(expected, is(equalTo(actual)));
  }
}