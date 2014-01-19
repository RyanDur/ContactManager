package serializers;

import controllers.ContactManager;
import models.Contact;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Set;

public class ContactManagerDeserializerImpl implements ContactManagerDeserializer {
  String fileName;

  public ContactManagerDeserializerImpl(String fileName) {
    this.fileName = fileName;
  }

  @Override
  public ContactManager deserializeContactManager() {
    ContactManager contactManager = null;
    try {
      FileInputStream fin = new FileInputStream(fileName);
      ObjectInputStream ois = new ObjectInputStream(fin);
      contactManager = (ContactManager) ois.readObject();
      ois.close();
      fin.close();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }

    return contactManager;
  }
}
