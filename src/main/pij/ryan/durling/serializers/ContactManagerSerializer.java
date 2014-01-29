package ryan.durling.serializers;

public interface ContactManagerSerializer {

  /**
   *  Serialize an instance of a contact manager
   *
   * @param contactManager   the contact manager instance to be serialized
   */
  void serializeContactManager(ContactManager contactManager);
}
