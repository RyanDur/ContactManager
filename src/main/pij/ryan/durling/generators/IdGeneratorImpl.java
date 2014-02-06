package pij.ryan.durling.generators;

import com.google.inject.Singleton;

import java.io.Serializable;

@Singleton
public class IdGeneratorImpl implements IdGenerator, Serializable {
  private static final long serialVersionUID = 8246940422068829605L;

  private static IdGeneratorImpl idGeneratorInstance = null;
  private int meetingId;
  private int contactId;

  public int getMeetingId() {
    return meetingId++;
  }

  public int getContactId() {
    return contactId++;
  }
}
