package pij.ryan.durling.exceptions;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class InvalidMeetingExceptionTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldGiveACustomMessageWhenThrown() throws InvalidMeetingException {
        thrown.expect(InvalidMeetingException.class);
        thrown.expectMessage("Needs at least one contact");

        throw new InvalidMeetingException();
    }
}
