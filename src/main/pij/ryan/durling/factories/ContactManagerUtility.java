package ryan.durling.factories;

public interface ContactManagerUtility {

  ContactFactory createContactFactory();

  IdGenerator createIdGenerator();

  MeetingFactory createMeetingFactory();

  SerializationFactory createSerializationFactory();

  HookFactory createHookFactory();

  Contacts createContacts();
}
