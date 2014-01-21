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

  @Before
  public void setup() {
    mockMeetingFactory = mock(MeetingFactory.class);
    IdGenerator mockIdGenerator = mock(IdGenerator.class);
    meetings = new MeetingsImpl(mockMeetingFactory, mockIdGenerator);
  }

  @Test
  public void shouldBeAbleToAddAMeetingForTheFuture() {
    Set<Contact> contacts = new HashSet<>();
    FutureMeeting mockFutureMeeting = mock(FutureMeeting.class);
    setFutureMeeting(mockFutureMeeting);

    int id = meetings.addFutureMeeting(contacts, mockDate(1));
    FutureMeeting futureMeeting = meetings.getFutureMeeting(id);

    assertThat(futureMeeting, is(equalTo(mockFutureMeeting)));
  }

  @Test
  public void shouldThrowAnIllegalArgumentExceptionIfDateIsInThePast() {
    thrown.expect(IllegalArgumentException.class);

    Set<Contact> contacts = new HashSet<>();
    meetings.addFutureMeeting(contacts, mockDate(-1));
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
}
