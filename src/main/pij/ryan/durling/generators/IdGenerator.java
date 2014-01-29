package pij.ryan.durling.generators;

public interface IdGenerator {
  /**
   * generate a unique id for a meeting.
   *
   * @return an id for a meeting
   */
  int getMeetingId();

  /**
   * generate a unique id for a contact.
   *
   * @return an id for a contact
   */
  int getContactId();
}
