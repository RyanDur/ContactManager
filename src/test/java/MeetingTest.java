import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.HashSet;

import org.junit.*;
import org.junit.rules.ExpectedException;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MeetingTest {
    private Calendar date;
    private Set<Contact> contacts;
    private int id;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Set<Contact> getMockContacts(int numOfMocks) {
        Set<Contact> set = new HashSet<>();
        for(int i = 0; i < numOfMocks; i++) {
            set.add(mock(Contact.class));
        }
        assertEquals(numOfMocks, set.size());
        return set;
    }

    @Before
    public void setup() {
        contacts = getMockContacts(3);
        date =  new GregorianCalendar();
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
