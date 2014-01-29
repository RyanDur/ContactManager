package pij.ryan.durling.factories;

import pij.ryan.durling.models.Contact;

public interface ContactFactory {

  /**
   *  create an instance of a Contact
   *
   * @param id     id of the contact
   * @param name   name of the contact
   * @return a new instance of a contact
   */
  Contact createContact(int id, String name);
}
