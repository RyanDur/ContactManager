package ryan.durling.generators;

import java.io.Serializable;

public class IdGeneratorImpl implements IdGenerator, Serializable {
  private static final long serialVersionUID = 8246940422068829605L;
  private static IdGeneratorImpl idGeneratorInstance = null;
  private int meetingId;
  private int contactId;

  private IdGeneratorImpl() {
    meetingId = 0;
    contactId = 0;
  }

  public static IdGeneratorImpl getInstance() {
    if (idGeneratorInstance == null) {
      idGeneratorInstance = new IdGeneratorImpl();
    }
    return idGeneratorInstance;
  }

  public int getMeetingId() {
    return meetingId;
  }

  public int getContactId() {
    return contactId;
  }

  protected Object readResolve() {
    return getInstance();
  }
}
