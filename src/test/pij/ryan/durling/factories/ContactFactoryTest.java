package pij.ryan.durling.factories;

import org.junit.Test;
import pij.ryan.durling.models.Contact;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class ContactFactoryTest {

  @Test
  public void shouldGetAnInstanceOfAContact() {
    ContactFactory cf = spy(new ContactFactoryImpl());
    Contact expected = mock(Contact.class);
    doReturn(expected).when(cf).createContact(anyInt(), anyString());
    Contact actual = cf.createContact(0, "Ryan");

    assertThat(actual, is(equalTo(expected)));
  }
}