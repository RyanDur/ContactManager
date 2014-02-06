package pij.ryan.durling.serializers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pij.ryan.durling.controllers.Contacts;
import pij.ryan.durling.controllers.ContactsImpl;
import pij.ryan.durling.controllers.Meetings;
import pij.ryan.durling.controllers.MeetingsImpl;
import pij.ryan.durling.factories.ContactFactoryImpl;
import pij.ryan.durling.factories.MeetingFactoryImpl;
import pij.ryan.durling.generators.IdGeneratorImpl;
import pij.ryan.durling.models.Contact;

import java.io.File;
import java.util.Calendar;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class SerializersTest {

  private Meetings meetings;
  private Contacts contacts;
  private Calendar date;
  private String name;
  private String fileName;
  private Serializers serializers;
  private Set<Contact> contactSet;

  @Before
  public void setup() {
    meetings = new MeetingsImpl(new MeetingFactoryImpl(), new IdGeneratorImpl());
    contacts = new ContactsImpl(new ContactFactoryImpl(), new IdGeneratorImpl());
    name = "Ryan";
    contacts.add(name, "notes");
    contactSet = contacts.get(name);
    date = Calendar.getInstance();
    date.add(Calendar.DATE, 1);
    meetings.addFutureMeeting(contactSet, date);

    serializers = new SerializersImpl();
    fileName = "contactsTest.txt";
    serializers.setFileName(fileName);
  }

  @After
  public void tearDown() {
    new File(fileName).delete();
  }

  @Test
  public void shouldSerializeAndDeserializeMeetingsAndContacts() {
    serializers.serialize(meetings, contacts);
    Object[] objects = serializers.deserialize();
    Meetings meetings1 = (Meetings) objects[0];
    Contacts contacts1 = (Contacts) objects[1];

    assertNotNull(contacts1);
    assertNotNull(meetings1);
    Set<Contact> contactSet1 = contacts1.get(name);
    Contact contact1 = contactSet1.toArray(new Contact[0])[0];
    Contact contact = contactSet.toArray(new Contact[0])[0];
    assertThat(contact.getName(), is(equalTo(contact1.getName())));
  }

  @Test
  public void shouldKnowIfDataFileExists() {
    assertFalse(serializers.dataExists());
    serializers.serialize(meetings, contacts);
    assertTrue(serializers.dataExists());
  }
}
