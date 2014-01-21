package controllers;

import exceptions.InvalidMeetingException;
import factories.MeetingFactory;
import generators.IdGenerator;
import models.Contact;
import models.FutureMeeting;
import models.PastMeeting;
import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

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
    FutureMeeting futureMeeting = meetings.getFutureMeeting(id);

    assertThat(futureMeeting, is(IsEqual.equalTo(mockFutureMeeting)));
  }


  @Test
  public void shouldThrowAnIllegalArgumentExceptionIfDateIsInThePast() {
    thrown.expect(IllegalArgumentException.class);

    Set<Contact> contacts = new HashSet<>();
    meetings.addFutureMeeting(contacts, mockDate(-1));
  }

  @Test
  public void shouldThrowIllegalArgumentExceptionIfFoundMeetingHasHappened() {
    thrown.expect(IllegalArgumentException.class);

    FutureMeeting mockFutureMeeting = mock(FutureMeeting.class);
    int id = addFutureMeeting(new HashSet<Contact>(), mockDate(1), 1, mockFutureMeeting);
    when(mockFutureMeeting.getDate()).thenReturn(mockDate(-1));

    meetings.getFutureMeeting(id);
  }

  @Test
  public void shouldBeAbleToAddAPastMeeting() {
    PastMeeting mockPastMeeting = mock(PastMeeting.class);
    setPastMeeting(mockPastMeeting);

    int id = 0;
    addPastMeeting(mockContacts(3), mockDate(-1), "note", mockPastMeeting, id);
    PastMeeting meeting = meetings.getPastMeeting(id);

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
  public void shouldThrowANullPointerExceptionIfTheNotesAreNull() {
    thrown.expect(NullPointerException.class);

    Set<Contact> contacts = mockContacts(3);
    Calendar date = mockDate(-1);
    meetings.addNewPastMeeting(contacts, date, null);
  }

  @Test
  public void shouldThrowIllegalArgumentExceptionIfThereIsAPastMeetingWithThatIDHappeningInTheFuture() {
    thrown.expect(IllegalArgumentException.class);

    int id = 0;
    PastMeeting mockPastMeeting = mock(PastMeeting.class);
    addPastMeeting(mockContacts(3), mockDate(1), "note", mockPastMeeting, id);
    meetings.getPastMeeting(id);
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

  private Calendar mockDate(int days) {
    Calendar date = new GregorianCalendar();
    date.add(Calendar.DATE, days);
    return date;
  }

  private int addFutureMeeting(Set<Contact> contacts, Calendar date, int mockId, FutureMeeting mockFutureMeeting) {
    setFutureMeeting(mockFutureMeeting);
    when(mockIdGenerator.getMeetingId()).thenReturn(mockId);
    when(mockFutureMeeting.getId()).thenReturn(mockId);
    when(mockFutureMeeting.getDate()).thenReturn(date);
    return meetings.addFutureMeeting(contacts, date);
  }
}
