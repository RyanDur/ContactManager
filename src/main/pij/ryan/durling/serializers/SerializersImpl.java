package pij.ryan.durling.serializers;

import pij.ryan.durling.controllers.Contacts;
import pij.ryan.durling.controllers.Meetings;

import java.io.*;

public class SerializersImpl implements Serializers {
  private static final String FILE_NAME = "contacts.txt";

  @Override
  public void serialize(Meetings meetings, Contacts contacts) {
    try {
      FileOutputStream fout = new FileOutputStream(FILE_NAME);
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
  public Object[] deserialize() {
    Meetings meetings = null;
    Contacts contacts = null;
    try {
      FileInputStream fin = new FileInputStream(FILE_NAME);
      ObjectInputStream ois = new ObjectInputStream(fin);
      meetings = (Meetings) ois.readObject();
      contacts = (Contacts) ois.readObject();
      ois.close();
      fin.close();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
    return new Object[]{meetings, contacts};
  }
}
