package pij.ryan.durling.serializers;

import pij.ryan.durling.controllers.ContactManager;
import pij.ryan.durling.controllers.Contacts;
import pij.ryan.durling.controllers.Meetings;
import pij.ryan.durling.serializers.Serializers;

import java.io.*;

public class SerializersImpl implements Serializers {
  private static final String fileName = "contacts.txt";

  @Override
  public void serialize(Meetings meetings, Contacts contacts) {
    try {
      FileOutputStream fout = new FileOutputStream(fileName);
      ObjectOutputStream oos = new ObjectOutputStream(fout);
      oos.writeObject(meetings);
      oos.writeObject(contacts);
      oos.close();
      fout.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public ContactManager deserialize() {
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
