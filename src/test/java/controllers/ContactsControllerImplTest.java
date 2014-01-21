package controllers;

import factories.ContactFactory;
import generators.IdGenerator;
import models.Contact;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.rules.ExpectedException;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ContactsControllerImplTest {
  ContactFactory mockContactFactory;
  ContactsController contacts;
  Contact contact;
  int id;
  String name;
  String notes;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setup() {
    id = 0;
    name = "Ryan";
    notes = "note";

    mockContactFactory = mock(ContactFactory.class);
    contacts = new ContactsControllerImpl(mockContactFactory, mock(IdGenerator.class));

    contact = mock(Contact.class);
    registerContact(contact, name, id);
    contacts.add(name, notes);
  }

  @Test
  public void shouldBeAbleToGetAContactById() {
    contacts.add(name, notes);

    assertThat(contact, is(equalTo(contacts.get(id))));
  }

  @Test
  public void shouldBeAbleToGetAContactByName() {
    contacts.add(name, notes);
    Set<Contact> contactSet = new HashSet<>();
    contactSet.add(contact);

    assertThat(contactSet, is(equalTo(contacts.get(name))));
  }

  @Test
  public void shouldThrowAnIllegalArgumentExceptionIfIdPassedDoesNotExist() {
    thrown.expect(IllegalArgumentException.class);

    contacts.get(500);
  }

  @Test
  public void shouldThrowAnIllegalArgumentExceptionIfIdPassedNameIsNull() {
    thrown.expect(NullPointerException.class);

    String name = null;
    contacts.get(name);
  }

  @Test
  public void shouldBeAbleToRetrieveContactsOfSamNameButDifferentId() {
    Contact contact1 = mock(Contact.class);
    registerContact(contact1, name, 3);
    contacts.add(name, notes);

    Set<Contact> expected = new HashSet<>();
    expected.add(contact1);
    expected.add(contact);
    Set<Contact> actual = contacts.get(name);

    assertThat(expected, is(equalTo(actual)));
  }

  @Test
  public void shouldNotBeAbleToRetrieveContactsOfSamNameAndId() {
    Contact contact1 = mock(Contact.class);
    registerContact(contact1, name, id);
    contacts.add(name, notes);

    Set<Contact> expected = new HashSet<>();
    expected.add(contact);
    Set<Contact> actual = contacts.get(name);

    assertThat(expected, is(equalTo(actual)));
  }

  @Test
  public void shouldThrowANullPointerExceptionIfNameIsNullWhenAddingAContact() {
    thrown.expect(NullPointerException.class);

    contacts.add(null, notes);
  }

  @Test
  public void shouldThrowANullPointerExceptionIfNotesAreNullWhenAddingAContact() {
    thrown.expect(NullPointerException.class);

    contacts.add(name, null);
  }

  private void registerContact(Contact contact, String name, int id) {
    when(contact.getId()).thenReturn(id);
    when(contact.getName()).thenReturn(name);
    when(mockContactFactory.createContact(anyInt(), anyString())).thenReturn(contact);
  }
}
