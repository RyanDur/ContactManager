package controllers;

import factories.ContactFactory;
import generators.IdGenerator;
import models.Contact;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ContactsImplTest {
  ContactFactory mockContactFactory;
  IdGenerator mockIdGenerator;
  Contacts contacts;
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
    mockIdGenerator = mock(IdGenerator.class);
    contacts = new ContactsControllerImpl(mockContactFactory, mockIdGenerator);

    contact = mock(Contact.class);
    registerContact(contact, name, id);
    contacts.add(name, notes);
  }

  @Test
  public void shouldBeAbleToGetAContactById() {
    assertThat(contact, is(equalTo(contacts.get(id))));
  }

  @Test
  public void shouldBeAbleToGetAContactByName() {
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

    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void shouldNotBeAbleToRetrieveContactsOfSamNameAndId() {
    Contact contact1 = mock(Contact.class);
    registerContact(contact1, name, id);
    contacts.add(name, notes);

    Set<Contact> expected = new HashSet<>();
    expected.add(contact);
    Set<Contact> actual = contacts.get(name);

    assertThat(actual, is(equalTo(expected)));
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

  @Test
  public void shouldKnowIfAContactExistsInTheCollection() {
    Contact contact1 = mock(Contact.class);
    registerContact(contact1, name, 3);
    contacts.add(name, notes);

    Set<Contact> expected = new HashSet<>();
    expected.add(contact);
    expected.add(contact1);

    assertFalse(contacts.notValidContactSet(expected));

    int unknownId = 4;
    Contact unknownContact = mock(Contact.class);
    when(unknownContact.getId()).thenReturn(unknownId);
    when(unknownContact.getName()).thenReturn(name);
    expected.add(unknownContact);

    assertTrue(contacts.notValidContactSet(expected));
  }

  @Test
  public void shouldNotAllowAnEmptyCollection() {
    Set<Contact> expected = new HashSet<>();
    assertTrue(contacts.notValidContactSet(expected));
  }

  private void registerContact(Contact contact, String name, int id) {
    when(mockIdGenerator.getContactId()).thenReturn(id);
    when(contact.getId()).thenReturn(id);
    when(contact.getName()).thenReturn(name);
    when(mockContactFactory.createContact(anyInt(), anyString())).thenReturn(contact);
  }
}
