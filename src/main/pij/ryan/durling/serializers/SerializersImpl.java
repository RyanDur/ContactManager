package pij.ryan.durling.serializers;

import pij.ryan.durling.controllers.Contacts;
import pij.ryan.durling.controllers.Meetings;

import java.io.*;

public class SerializersImpl implements Serializers {
  private String fileName;

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
  public Object[] deserialize() {
    Meetings meetings = null;
    Contacts contacts = null;
    try {
      FileInputStream fin = new FileInputStream(fileName);
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

  @Override
  public boolean dataExists() {
    return new File(fileName).exists();
  }

  @Override
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
}
