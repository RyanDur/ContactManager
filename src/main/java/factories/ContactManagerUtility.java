package factories;

import controllers.Contacts;
import generators.IdGenerator;

public interface ContactManagerUtility {

  ContactFactory createContactFactory();

  IdGenerator createIdGenerator();

  MeetingFactory createMeetingFactory();

  SerializationFactory createSerializationFactory();

  HookFactory createHookFactory();

  Contacts createContacts();
}
