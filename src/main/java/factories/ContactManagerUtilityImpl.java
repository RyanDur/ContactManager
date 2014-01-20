package factories;

import generators.IdGenerator;
import generators.IdGeneratorImpl;

public class ContactManagerUtilityImpl implements ContactManagerUtility {
  private static ContactManagerUtility contactManagerUtilityInstance = null;

  public static ContactManagerUtility getInstance() {
    if(contactManagerUtilityInstance == null) {
      contactManagerUtilityInstance = new ContactManagerUtilityImpl();
    }
    return contactManagerUtilityInstance;
  }

  @Override
  public ContactFactory createContactFactory() {
    return ContactFactoryImpl.getInstance();
  }

  @Override
  public IdGenerator createIdGenerator() {
    return IdGeneratorImpl.getInstance();
  }

  @Override
  public MeetingFactory createMeetingFactory() {
    return MeetingFactoryImpl.getInstance();
  }

  @Override
  public SerializationFactory createSerializationFactory() {
    return SerializationFactoryImpl.getInstance();
  }

  @Override
  public HookFactory createHookFactory() {
    return HookFactoryImpl.getInstance();
  }
}
