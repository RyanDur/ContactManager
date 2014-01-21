package factories;

import controllers.ContactsController;
import generators.IdGenerator;

public interface ContactManagerUtility {

  ContactFactory createContactFactory();

  IdGenerator createIdGenerator();

  MeetingFactory createMeetingFactory();

  SerializationFactory createSerializationFactory();

  HookFactory createHookFactory();

  ContactsController createContactController();
}
