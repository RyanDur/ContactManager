package controllers;

import exceptions.InvalidMeetingException;
import factories.MeetingFactory;
import generators.IdGenerator;
import models.Contact;
import models.FutureMeeting;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
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

    assertThat(futureMeeting, is(equalTo(mockFutureMeeting)));
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

  private void setFutureMeeting(FutureMeeting futureMeeting) {
    Set<Contact> anySet = any();
    Calendar anyDate = any();
    try {
      when(mockMeetingFactory.createFutureMeeting(anyInt(), anySet, anyDate)).thenReturn(futureMeeting);
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
