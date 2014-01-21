package controllers;

import models.Contact;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
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

  @Before
  public void setup() {
    contacts = new ContactsControllerImpl();
    id = 0;
    name = "Ryan";
    contact = mock(Contact.class);
    when(contact.getId()).thenReturn(id);
    when(contact.getName()).thenReturn(name);
  }

  @Test
  public void shouldBeAbleToGetAContactById() {
    contacts.add(contact);
    Set<Contact> contactSet = new HashSet<>();
    contactSet.add(contact);

    assertThat(contactSet, is(equalTo(contacts.get(id))));
  }

  @Test
  public void shouldBeAbleToGetAContactByName() {
    contacts.add(contact);
    Set<Contact> contactSet = new HashSet<>();
    contactSet.add(contact);

    assertThat(contactSet, is(equalTo(contacts.get(name))));
  }
}
