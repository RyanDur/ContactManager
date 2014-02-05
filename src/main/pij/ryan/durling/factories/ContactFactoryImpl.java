package pij.ryan.durling.factories;

import pij.ryan.durling.models.Contact;
import pij.ryan.durling.models.ContactImpl;

import java.io.Serializable;

public class ContactFactoryImpl implements ContactFactory, Serializable {
  private static ContactFactory contactFactoryInstance = null;

  private ContactFactoryImpl() {
  }

  public static ContactFactory getInstance() {
    if (contactFactoryInstance == null) {
      contactFactoryInstance = new ContactFactoryImpl();
    }
    return contactFactoryInstance;
  }

  @Override
  public Contact createContact(int id, String name) {
    return new ContactImpl(id, name);
  }
}
