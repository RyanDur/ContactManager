package pij.ryan.durling.models;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactTest {
  private Contact contact;

  @Before
  public void setup() {
    contact = new ContactImpl(3, "Bob");
  }

  @Test
  public void shouldHaveAnIdAndName() {
    assertThat(contact.getId(), is(equalTo(3)));
    assertThat(contact.getName(), is(equalTo("Bob")));
  }

  @Test
  public void shouldNotHaveNotesIfNoneHaveBeenAdded() {
    assertThat(contact.getNotes(), is(equalTo("")));
  }

  @Test
  public void shouldHaveNotesIfTheyHaveBeenAdded() {
    String newline = System.lineSeparator();
    String format = newline + newline;
    String note = "note 1";
    contact.addNotes(note);
    assertThat(contact.getNotes(), is(equalTo(note.concat(format))));

    String note1 = "note 2";
    contact.addNotes(note1);
    String expected = note + format + note1 + format;
    assertThat(contact.getNotes(), is(equalTo(expected)));

    String note2 = "note 3";
    contact.addNotes(note2);
    String expected1 = note + format + note1 + format + note2 + format;
    assertThat(contact.getNotes(), is(equalTo(expected1)));
  }
}
