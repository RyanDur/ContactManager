import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.HashSet;

import org.junit.*;
import org.junit.rules.ExpectedException;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PastMeetingTest {
    private Meeting mockMeeting;
    private Calendar date;
    private int id;
    private Set<Contact> mockContacts;

    @Before
    public void setup() {
	id = 3;
        date = new GregorianCalendar();

        mockContacts = new HashSet<Contact>();
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
	String notes = null;
        PastMeeting pastMeeting = new PastMeetingImpl(id, mockContacts, date, notes);

        assertThat(pastMeeting.getNotes(), is(equalTo("")));
    }
}
