package factories;

import serializers.ContactManagerDeserializer;
import serializers.ContactManagerSerializer;

public interface SerializationFactory {

  /**
   * a way to persist an instance of a ConTactManager
   *
   * @return a ContactManagerSerializer
   */
  ContactManagerSerializer createContactManagerSerializer();

  /**
   * a way to retrieve an instance of a ConTactManager
   *
   * @return a ContactManagerDeserializer
   */
  ContactManagerDeserializer createContactManagerDeserializer();
}