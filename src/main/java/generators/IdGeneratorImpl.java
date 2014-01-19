package generators;

public class IdGeneratorImpl implements IdGenerator {
  private static IdGeneratorImpl idGeneratorInstance = null;
  private static int meetingId = 0;
  private static int contactId = 0;

  private IdGeneratorImpl() {
  }

  public static IdGeneratorImpl getInstance() {
    if (idGeneratorInstance == null) {
      idGeneratorInstance = new IdGeneratorImpl();
    }
    return idGeneratorInstance;
  }

  public int getMeetingId() {
    return meetingId++;
  }

  public int getContactId() {
    return contactId++;
  }
}
