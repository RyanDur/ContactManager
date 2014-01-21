package controllers;

import models.Contact;
import java.util.Set;

public interface ContactsController {

  void add(String name, String notes);

  Contact get(int id);

  Set<Contact> get(String name);
}
