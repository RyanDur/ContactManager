package pij.ryan.durling.serializers;

import pij.ryan.durling.controllers.ContactManager;
import pij.ryan.durling.controllers.Contacts;
import pij.ryan.durling.controllers.Meetings;

public interface Serializers {
  public void serialize(Meetings meetings, Contacts contacts);

  public ContactManager deserialize();
}
