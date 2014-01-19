package serializers;

import controllers.ContactManager;

public interface ContactManagerDeserializer {

  /**
   * retrieve a contact manager from storage
   *
   * @return an instance of a contact manager
   */
  ContactManager deserializeContactManager();
}
