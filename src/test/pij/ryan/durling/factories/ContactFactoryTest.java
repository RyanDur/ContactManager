package pij.ryan.durling.factories;

import org.junit.Test;
import pij.ryan.durling.models.Contact;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;

public class ContactFactoryTest {

    @Test
    public void shouldGetAnInstanceOfAContact() {
        ContactFactory cf = spy(ContactFactoryImpl.getInstance());
        Contact expected = mock(Contact.class);
        doReturn(expected).when(cf).createContact(anyInt(), anyString());
        Contact actual = cf.createContact(0, "Ryan");

        assertThat(actual, is(equalTo(expected)));
    }
}