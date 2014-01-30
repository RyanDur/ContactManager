package pij.ryan.durling.controllers;

import pij.ryan.durling.models.Contact;

import java.util.Set;

public interface Contacts {

  void add(String name, String notes);

  Contact get(int id);

  Set<Contact> get(String name);

  boolean notValidContactSet(Set<Contact> contacts);

  boolean notValidContactId(int... ids);
}
