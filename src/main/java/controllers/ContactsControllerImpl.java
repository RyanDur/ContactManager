package controllers;

import models.Contact;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by ryandurling on 1/21/14.
 */
public class ContactsControllerImpl implements ContactsController {

  private Map<String, Set<Contact>> contactsByName = new HashMap<>();
  private Map<Integer, Contact> contactsById = new HashMap<>();

  @Override
  public void add(Contact contact) {
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
  public Set<Contact> get(int id) {
    Set<Contact> contactSet = new HashSet<>();
    contactSet.add(contactsById.get(id));
    return contactSet;
  }

  @Override
  public Set<Contact> get(String name) {
    return contactsByName.get(name);
  }
}
