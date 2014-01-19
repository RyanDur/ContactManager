package factories;

import serializers.ContactManagerDeserializer;
import serializers.ContactManagerDeserializerImpl;
import serializers.ContactManagerSerializer;
import serializers.ContactManagerSerializerImpl;

import java.io.Serializable;

public class SerializationFactoryImpl implements SerializationFactory, Serializable {
  private static SerializationFactory serializationFactoryInstance = null;
  private String fileName = "contacts.txt";

  public static SerializationFactory getInstance() {
    if(serializationFactoryInstance == null) {
      serializationFactoryInstance = new SerializationFactoryImpl();
    }
    return serializationFactoryInstance;
  }

  @Override
  public ContactManagerSerializer createContactManagerSerializer() {
    return new ContactManagerSerializerImpl(fileName);
  }

  @Override
  public ContactManagerDeserializer createContactManagerDeserializer() {
    return new ContactManagerDeserializerImpl(fileName);
  }
}
