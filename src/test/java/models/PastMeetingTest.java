package models;

import exceptions.InvalidMeetingException;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class PastMeetingTest {
  private Calendar date;
  private int id;
  private Set<Contact> mockContacts;

  @Before
  public void setup() {
    id = 3;
    date = new GregorianCalendar();

    mockContacts = new HashSet<>();
    mockContacts.add(mock(Contact.class));
  }

  @Test
  public void shouldHaveNotesOfTheMeeting() throws InvalidMeetingException {
    String notes = "note";
    PastMeeting pastMeeting = new PastMeetingImpl(id, mockContacts, date, notes);

    assertThat(pastMeeting.getNotes(), is(equalTo(notes)));
  }

  @Test
  public void shouldHaveAnEmptyStringIfNoNotes() throws InvalidMeetingException {
    PastMeeting pastMeeting = new PastMeetingImpl(id, mockContacts, date, null);

    assertThat(pastMeeting.getNotes(), is(equalTo("")));
  }
}
