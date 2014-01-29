package pij.ryan.durling.models;

import pij.ryan.durling.exceptions.InvalidMeetingException;
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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class MeetingTest {
  private Calendar date;
  private Set<Contact> contacts;
  private int id;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private Set<Contact> getMockContacts(int numOfMocks) {
    Set<Contact> set = new HashSet<>();
    for (int i = 0; i < numOfMocks; i++) {
      set.add(mock(Contact.class));
    }
    assertEquals(numOfMocks, set.size());
    return set;
  }

  @Before
  public void setup() {
    contacts = getMockContacts(3);
    date = new GregorianCalendar();
    id = 1;
  }

  @Test
  public void shouldThrowExceptionIfContacsAreNull() throws InvalidMeetingException {
    thrown.expect(InvalidMeetingException.class);
    thrown.expectMessage("Needs atleast one contact");

    new MeetingImpl(id, null, date);
  }

  @Test
  public void shouldThrowExceptionIfContacsAreEmpty() throws InvalidMeetingException {
    thrown.expect(InvalidMeetingException.class);
    thrown.expectMessage("Needs atleast one contact");

    new MeetingImpl(id, getMockContacts(0), date);
  }

  @Test
  public void shouldHaveAnIdDateAndContacts() throws InvalidMeetingException {
    Meeting meeting = new MeetingImpl(id, contacts, date);

    assertThat(id, is(equalTo(meeting.getId())));
    assertThat(date, is(equalTo(meeting.getDate())));
    assertThat(contacts, is(equalTo(meeting.getContacts())));
  }
}
