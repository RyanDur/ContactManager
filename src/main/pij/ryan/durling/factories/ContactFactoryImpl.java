package pij.ryan.durling.factories;

import com.google.inject.Singleton;
import pij.ryan.durling.models.Contact;
import pij.ryan.durling.models.ContactImpl;

import java.io.Serializable;

@Singleton
public class ContactFactoryImpl implements ContactFactory, Serializable {
  private static final long serialVersionUID = 1252231808573165874L;

  @Override
  public Contact createContact(int id, String name) {
    return new ContactImpl(id, name);
  }
}
