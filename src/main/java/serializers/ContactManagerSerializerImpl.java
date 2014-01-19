package serializers;

import controllers.ContactManager;
import controllers.ContactManagerImpl;
import factories.ContactFactory;
import factories.ContactFactoryImpl;
import factories.MeetingFactory;
import factories.MeetingFactoryImpl;
import generators.IdGeneratorImpl;
import models.Contact;
import models.ContactImpl;
import models.FutureMeeting;
import models.Meeting;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

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
