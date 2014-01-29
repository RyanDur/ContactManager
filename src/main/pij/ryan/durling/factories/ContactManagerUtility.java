package pij.ryan.durling.factories;

import pij.ryan.durling.controllers.Contacts;
import pij.ryan.durling.generators.IdGenerator;

public interface ContactManagerUtility {

  ContactFactory createContactFactory();

  IdGenerator createIdGenerator();

  MeetingFactory createMeetingFactory();

  SerializationFactory createSerializationFactory();

  HookFactory createHookFactory();

  Contacts createContacts();
}
