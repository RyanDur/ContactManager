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

    @Before
    public void setup() {
        date = new GregorianCalendar();

        Set<Contact> mockContacts = new HashSet<Contact>();
        mockContacts.add(mock(Contact.class));

        mockMeeting = mock(Meeting.class);
        when(mockMeeting.getId()).thenReturn(3);
        when(mockMeeting.getContacts()).thenReturn(mockContacts);
        when(mockMeeting.getDate()).thenReturn(date);
    }

    @Test
    public void shouldHaveNotesOfTheMeeting() throws InvalidMeetingException {
        String notes = "note";
        PastMeeting pastMeeting = new PastMeetingImpl(mockMeeting, notes);

        assertThat(pastMeeting.getNotes(), is(equalTo(notes)));
    }

    @Test
    public void shouldHaveAnEmptyStringIfNoNotes() throws InvalidMeetingException {
	String notes = null;
        PastMeeting pastMeeting = new PastMeetingImpl(mockMeeting, notes);

        assertThat(pastMeeting.getNotes(), is(equalTo("")));
    }
}
