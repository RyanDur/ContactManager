package controllers;

import exceptions.InvalidMeetingException;
import factories.MeetingFactory;
import generators.IdGenerator;
import models.Contact;
import models.FutureMeeting;
import org.junit.Before;
import org.junit.Test;

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

  @Before
  public void setup() {
    mockMeetingFactory = mock(MeetingFactory.class);
    IdGenerator mockIdGenerator = mock(IdGenerator.class);
    meetings = new MeetingsImpl(mockMeetingFactory, mockIdGenerator);
  }

  @Test
  public void shouldBeAbleToAddAMeetingForTheFuture() {
    Calendar date = new GregorianCalendar();
    Contact contact = mock(Contact.class);
    Set<Contact> contacts = new HashSet<>();
    contacts.add(contact);
    FutureMeeting mockFutureMeeting = mock(FutureMeeting.class);
    setFutureMeeting(mockFutureMeeting);

    int id = meetings.addFutureMeeting(contacts, date);
    FutureMeeting futureMeeting = meetings.getFutureMeeting(id);

    assertThat(futureMeeting, is(equalTo(mockFutureMeeting)));
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
}
