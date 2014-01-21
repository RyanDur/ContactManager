package controllers;

import factories.ContactFactory;
import generators.IdGenerator;
import models.Contact;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by ryandurling on 1/21/14.
 */
public class ContactsControllerImplTest {
  ContactsController contacts;
  Contact contact;
  int id;
  String name;
  String notes;

  @Before
  public void setup() {
    ContactFactory mockContactFactory = mock(ContactFactory.class);
    contacts = new ContactsControllerImpl(mockContactFactory, mock(IdGenerator.class));
    id = 0;
    name = "Ryan";
    notes = "note";
    contact = mock(Contact.class);
    when(contact.getId()).thenReturn(id);
    when(contact.getName()).thenReturn(name);
    when(mockContactFactory.createContact(anyInt(), anyString())).thenReturn(contact);
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
}
