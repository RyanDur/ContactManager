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
    Contact contact = contactFactory.createContact(idGenerator.getContactId(), name);
    contact.addNotes(notes);
    contactsById.put(contact.getId(), contact);
    Set<Contact> values = contactsByName.get(contact.getName());
    if (values == null) {
      Set<Contact> contacts = new HashSet<>();
      contacts.add(contact);
      contactsByName.put(contact.getName(), contacts);
    } else {
      values.add(contact);
    }
  }

  @Override
  public Contact get(int id) {
    return contactsById.get(id);
  }

  @Override
  public Set<Contact> get(String name) {
    return contactsByName.get(name);
  }
}
