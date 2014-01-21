package controllers;

import factories.ContactFactory;
import generators.IdGenerator;
import models.Contact;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ContactsControllerImpl implements ContactsController {

  private Map<String, Set<Contact>> contactsByName = new HashMap<>();
  private Map<Integer, Contact> contactsById = new HashMap<>();
  private ContactFactory contactFactory;
  private IdGenerator idGenerator;

  public ContactsControllerImpl(ContactFactory contactFactory, IdGenerator idGenerator) {
    this.contactFactory = contactFactory;
    this.idGenerator = idGenerator;
  }

  @Override
  public void add(String name, String notes) {
    if (name == null || notes == null) throw new NullPointerException();
    int id = idGenerator.getContactId();
    if (contactsById.get(id) == null) {
      Contact contact = contactFactory.createContact(id , name);
      contact.addNotes(notes);
      setContactsById(contact);
      setContactsByName(contact);
    }
  }

  @Override
  public Contact get(int id) {
    if (contactsById.get(id) == null) throw new IllegalArgumentException();
    return contactsById.get(id);
  }

  @Override
  public Set<Contact> get(String name) {
    if (name == null) throw new NullPointerException();
    return contactsByName.get(name);
  }

  @Override
  public boolean notValidContactSet(Set<Contact> contacts) {
    if (contacts.isEmpty()) return true;

    for (Contact contact : contacts) {
      if (notValidContact(contact)) {
        return true;
      }
    }
    return false;
  }

  private boolean notValidContact(Contact contact) {
    return contactsById.get(contact.getId()) == null;
  }

  private void setContactsByName(Contact contact) {
    Set<Contact> values = contactsByName.get(contact.getName());
    if (values == null) {
      Set<Contact> contacts = new HashSet<>();
      contacts.add(contact);
      contactsByName.put(contact.getName(), contacts);
    } else {
      values.add(contact);
    }
  }

  private void setContactsById(Contact contact) {
    contactsById.put(contact.getId(), contact);
  }
}
