package pij.ryan.durling.serializers;

import org.junit.Test;
import pij.ryan.durling.controllers.Contacts;
import pij.ryan.durling.controllers.ContactsImpl;
import pij.ryan.durling.controllers.Meetings;
import pij.ryan.durling.controllers.MeetingsImpl;
import pij.ryan.durling.factories.ContactFactoryImpl;
import pij.ryan.durling.factories.MeetingFactoryImpl;
import pij.ryan.durling.generators.IdGeneratorImpl;
import pij.ryan.durling.models.Contact;
import pij.ryan.durling.models.Meeting;

import java.util.Calendar;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by ryandurling on 2/5/14.
 */
public class SerializersTest {

  @Test
  public void shouldBeAbleToSerializeContactsAndMeetings() {
    Contacts mockContacts = new ContactsImpl(ContactFactoryImpl.getInstance(), IdGeneratorImpl.getInstance());
    Meetings mockMeetings = new MeetingsImpl(MeetingFactoryImpl.getInstance(), IdGeneratorImpl.getInstance());
    String name = "Ryan";
    mockContacts.add(name, "notes");
    mockContacts.get(name);
    mockMeetings.addFutureMeeting(mockContacts.get(name), Calendar.getInstance());

    Serializers serializers = new SerializersImpl();
    serializers.serialize(mockMeetings, mockContacts);

    Object[] returned = serializers.deserialize();
    Meetings meetings = (Meetings) returned[0];
    Contacts contacts = (Contacts) returned[1];

    Set<Contact> contactSet = contacts.get(name);
    Set<Contact> mockContactSet = mockContacts.get(name);
    Contact contact1 = contactSet.toArray(new Contact[0])[0];
    Contact mockContact1 = mockContactSet.toArray(new Contact[0])[0];

    Meeting meeting = meetings.get(0);
    Meeting mockMeeting = mockMeetings.get(0);

    assertThat(contact1.getName(), is(equalTo(mockContact1.getName())));
    assertThat(meeting.getDate(), is(equalTo(mockMeeting.getDate())));
  }
}
