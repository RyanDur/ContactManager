package ryan.durling.serializers;

public interface ContactManagerDeserializer {

  /**
   * retrieve a contact manager from storage
   *
   * @return an instance of a contact manager
   */
  ContactManager deserializeContactManager();
}
