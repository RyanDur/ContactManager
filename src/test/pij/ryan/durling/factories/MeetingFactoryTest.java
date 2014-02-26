package pij.ryan.durling.factories;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Spy;
import pij.ryan.durling.exceptions.InvalidMeetingException;
import pij.ryan.durling.models.Contact;
import pij.ryan.durling.models.FutureMeeting;
import pij.ryan.durling.models.PastMeeting;

import java.util.Calendar;
import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class MeetingFactoryTest {

    @Spy
    private MeetingFactory mf = MeetingFactoryImpl.getInstance();

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void shouldBeAbleToGetAnInstanceOfAPastMeeting() throws InvalidMeetingException {
        PastMeeting expected = mock(PastMeeting.class);
        doReturn(expected).when(mf).createPastMeeting(anyInt(), anySetOf(Contact.class), any(Calendar.class), anyString());
        PastMeeting actual = mf.createPastMeeting(0, new HashSet<Contact>(), Calendar.getInstance(), "notes");

        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void shouldBeAbleToGetAnInstanceOfAFutureMeeting() throws InvalidMeetingException {
        FutureMeeting expected = mock(FutureMeeting.class);
        doReturn(expected).when(mf).createFutureMeeting(anyInt(), anySetOf(Contact.class), any(Calendar.class));
        FutureMeeting actual = mf.createFutureMeeting(0, new HashSet<Contact>(), Calendar.getInstance());

        assertThat(actual, is(equalTo(expected)));
    }
}
