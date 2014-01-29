package ryan.durling.serializers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ContactManagerSerializerImpl implements ContactManagerSerializer {
  private String fileName;

  public ContactManagerSerializerImpl(String fileName) {
    this.fileName = fileName;
  }

  @Override
  public void serializeContactManager(ContactManager contactManager) {
    try {
      FileOutputStream fout = new FileOutputStream(fileName);
      ObjectOutputStream oos = new ObjectOutputStream(fout);
      oos.writeObject(contactManager);
      oos.close();
      fout.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
