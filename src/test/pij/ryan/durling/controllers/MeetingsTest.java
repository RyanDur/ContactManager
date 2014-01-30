package pij.ryan.durling.controllers;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pij.ryan.durling.exceptions.InvalidMeetingException;
import pij.ryan.durling.factories.MeetingFactory;
import pij.ryan.durling.generators.IdGenerator;
import pij.ryan.durling.models.Contact;
import pij.ryan.durling.models.FutureMeeting;
import pij.ryan.durling.models.Meeting;
import pij.ryan.durling.models.PastMeeting;

import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MeetingsTest {
  Meetings meetings;
  MeetingFactory mockMeetingFactory;

  @Rule
  public ExpectedException thrown = ExpectedException.none();
  private IdGenerator mockIdGenerator;

  @Before
  public void setup() {
    mockMeetingFactory = mock(MeetingFactory.class);
    mockIdGenerator = mock(IdGenerator.class);
    meetings = new MeetingsImpl(mockMeetingFactory, mockIdGenerator);
  }

  @Test
  public void shouldBeAbleToAddAMeetingForTheFuture() {
    final int mockId = 1;
    Calendar date = mockDate(1);
    Set<Contact> contacts = new HashSet<>();
    FutureMeeting mockFutureMeeting = mock(FutureMeeting.class);

    int id = addFutureMeeting(contacts, date, mockId, mockFutureMeeting);
    FutureMeeting futureMeeting = (FutureMeeting) meetings.get(id);

    assertThat(futureMeeting, is(equalTo(mockFutureMeeting)));
  }

  @Test
  public void shouldThrowAnIllegalArgumentExceptionIfDateIsInThePast() {
    thrown.expect(IllegalArgumentException.class);

    Set<Contact> contacts = new HashSet<>();
    meetings.addFutureMeeting(contacts, mockDate(-1));
  }

  @Test
  public void shouldBeAbleToAddAPastMeeting() {
    PastMeeting mockPastMeeting = mock(PastMeeting.class);
    setPastMeeting(mockPastMeeting);

    int id = 0;
    addPastMeeting(mockContacts(3), mockDate(-1), "note", mockPastMeeting, id);
    PastMeeting meeting = (PastMeeting) meetings.get(id);

    assertThat(meeting, is(equalTo(mockPastMeeting)));
  }

  @Test
  public void shouldThrowANullPointerExceptionIfContactsIsNull() {
    thrown.expect(NullPointerException.class);

    Calendar date = mockDate(-1);
    String text = "note";
    meetings.addNewPastMeeting(null, date, text);
  }

  @Test
  public void shouldThrowANullPointerExceptionIfDateIsNull() {
    thrown.expect(NullPointerException.class);

    Set<Contact> contacts = mockContacts(3);
    String text = "note";
    meetings.addNewPastMeeting(contacts, null, text);
  }

  @Test
  public void shouldThrowANullPointerExceptionIfTheNotesAreNullWhenAddingAPastMeeting() {
    thrown.expect(NullPointerException.class);

    Set<Contact> contacts = mockContacts(3);
    Calendar date = mockDate(-1);
    meetings.addNewPastMeeting(contacts, date, null);
  }

  @Test
  public void shouldGetACollectionOfMeetingsByDate() {
    Calendar date = mockDay(6);
    Set<Contact> contacts = mockContacts(3);

    Calendar date1 = mockDay(4);
    FutureMeeting futureMeeting1 = mock(FutureMeeting.class);
    addFutureMeeting(contacts, date1, 5, futureMeeting1);

    Calendar date2 = mockDay(2);
    FutureMeeting futureMeeting2 = mock(FutureMeeting.class);
    addFutureMeeting(contacts, date2, 6, futureMeeting2);

    Calendar date3 = mockDay(3);
    FutureMeeting futureMeeting3 = mock(FutureMeeting.class);
    addFutureMeeting(contacts, date3, 7, futureMeeting3);

    List<Meeting> futureMeetings = new ArrayList<>();
    futureMeetings.add(futureMeeting2);
    futureMeetings.add(futureMeeting3);
    futureMeetings.add(futureMeeting1);

    assertThat(meetings.get(date), is(equalTo(futureMeetings)));
  }

  @Test
  public void shouldGetMeetingsByContact() {
    Contact knownContact = mock(Contact.class);
    when(knownContact.getId()).thenReturn(500);

    Set<Contact> contacts = mockContacts(3);
    contacts.add(knownContact);
    Set<Contact> contacts1 = mockContacts(3);
    contacts1.add(knownContact);

    FutureMeeting futureMeeting1 = mock(FutureMeeting.class);
    addFutureMeeting(contacts, mockDate(2), 0, futureMeeting1);

    FutureMeeting futureMeeting2 = mock(FutureMeeting.class);
    addFutureMeeting(mockContacts(3), mockDate(1), 1, futureMeeting2);

    FutureMeeting futureMeeting3 = mock(FutureMeeting.class);
    addFutureMeeting(contacts1, mockDate(3), 2, futureMeeting3);

    List<Meeting> futureMeetings = new ArrayList<>();
    futureMeetings.add(futureMeeting1);
    futureMeetings.add(futureMeeting3);

    assertThat(meetings.get(knownContact), is(equalTo(futureMeetings)));
  }

  @Test
  public void shouldBeAbleToConvertAFutureMeetingToAPastMeeting() {
    String notes = "note";
    int id = 0;
    FutureMeeting futureMeeting = mock(FutureMeeting.class);
    addFutureMeeting(mockContacts(3), mockDate(2), id, futureMeeting);
    when(futureMeeting.getDate()).thenReturn(mockDate(-1));

    PastMeeting mockPastMeeting = mock(PastMeeting.class);
    when(mockPastMeeting.getId()).thenReturn(id);
    when(mockPastMeeting.getNotes()).thenReturn(notes);
    when(mockPastMeeting.getDate()).thenReturn(mockDate(-1));
    setPastMeeting(mockPastMeeting);

    meetings.convertToPastMeeting(futureMeeting, notes);
    PastMeeting actual = (PastMeeting) meetings.get(id);

    assertThat(futureMeeting.getId(), is(equalTo(actual.getId())));
    assertThat(notes, is(equalTo(actual.getNotes())));
  }

  @Test
  public void shouldThrowIllegalStateExceptionIfTheMeetingIsSetForADateInTheFuture() {
    thrown.expect(IllegalStateException.class);

    String notes = "note";
    int id = 0;
    FutureMeeting futureMeeting = mock(FutureMeeting.class);
    addFutureMeeting(mockContacts(3), mockDate(2), id, futureMeeting);
    meetings.convertToPastMeeting(futureMeeting, notes);
  }

  @Test
  public void shouldThrowANullPointerExceptionIfTheNotesAreNullWhenConvertingToAPastMeeting() {
    thrown.expect(NullPointerException.class);

    String notes = null;
    int id = 0;
    FutureMeeting futureMeeting = mock(FutureMeeting.class);
    addFutureMeeting(mockContacts(3), mockDate(2), id, futureMeeting);
    when(futureMeeting.getDate()).thenReturn(mockDate(-1));
    meetings.convertToPastMeeting(futureMeeting, notes);
  }

  @Test
  public void shouldThrowAnIllegalArgumentExceptionIfTheMeetingDoesNotExist() {
    thrown.expect(IllegalArgumentException.class);

    String notes = "notes";
    int id = 1000;
    FutureMeeting futureMeeting = mock(FutureMeeting.class);
    when(futureMeeting.getDate()).thenReturn(mockDate(-1));
    when(futureMeeting.getId()).thenReturn(id);
    meetings.convertToPastMeeting(futureMeeting, notes);
  }


  private void addPastMeeting(Set<Contact> contacts, Calendar date, String text, PastMeeting mockPastMeeting, int id) {
    setPastMeeting(mockPastMeeting);
    when(mockPastMeeting.getId()).thenReturn(id);
    when(mockPastMeeting.getDate()).thenReturn(date);
    meetings.addNewPastMeeting(contacts, date, text);
  }

  private Set<Contact> mockContacts(int numOfContacts) {
    Set<Contact> contacts = new HashSet<>();
    for(int i = 0; i < numOfContacts; i++) {
      Contact contact = mock(Contact.class);
      contacts.add(contact);
    }
    return contacts;
  }

  private void setFutureMeeting(FutureMeeting futureMeeting) {
    Set<Contact> anySet = any();
    Calendar anyDate = any();
    try {
      when(mockMeetingFactory.createFutureMeeting(anyInt(), anySet, anyDate)).thenReturn(futureMeeting);
    } catch (InvalidMeetingException e) {
      e.printStackTrace();
    }
  }

  private void setPastMeeting(PastMeeting pastMeeting) {
    Set<Contact> anySet = any();
    Calendar anyDate = any();
    try {
      when(mockMeetingFactory.createPastMeeting(anyInt(), anySet, anyDate, anyString())).thenReturn(pastMeeting);
    } catch (InvalidMeetingException e) {
      e.printStackTrace();
    }
  }

  private Calendar mockDay(int hour) {
    Calendar date = Calendar.getInstance();
    date.add(Calendar.HOUR_OF_DAY, hour);
    return date;
  }

  private Calendar mockDate(int day) {
    Calendar date = Calendar.getInstance();
    date.add(Calendar.DATE, day);
    date.add(Calendar.HOUR_OF_DAY, 0);
    return date;
  }

  private int addFutureMeeting(Set<Contact> contacts, Calendar date, int mockId, FutureMeeting mockFutureMeeting) {
    setFutureMeeting(mockFutureMeeting);
    when(mockIdGenerator.getMeetingId()).thenReturn(mockId);
    when(mockFutureMeeting.getId()).thenReturn(mockId);
    when(mockFutureMeeting.getDate()).thenReturn(date);
    when(mockFutureMeeting.getContacts()).thenReturn(contacts);
    return meetings.addFutureMeeting(contacts, date);
  }
}
