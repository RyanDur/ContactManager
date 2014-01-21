package controllers;

import models.Contact;
import java.util.Set;

public interface ContactsController {

  void add(Contact contact);

  Set<Contact> get(int id);

  Set<Contact> get(String name);
}
