package pij.ryan.durling.generators;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class IdGeneratorTest {

  private IdGenerator idGenerator;

  @Before
  public void setup() {
    idGenerator = IdGeneratorImpl.getInstance();
  }

  @Test
  public void shouldGetAnIncrementedNumberForAContactId() {
    int original = idGenerator.getContactId();
    int next = idGenerator.getContactId();
    assertTrue(original < next);
    original = idGenerator.getContactId();
    next = idGenerator.getContactId();
    assertTrue(original < next);
    original = idGenerator.getContactId();
    next = idGenerator.getContactId();
    assertTrue(original < next);
  }

  @Test
  public void shouldGetAnIncrementedNumberForAMeetingId() {
    int original = idGenerator.getMeetingId();
    int next = idGenerator.getMeetingId();
    assertTrue(original < next);
    original = idGenerator.getMeetingId();
    next = idGenerator.getMeetingId();
    assertTrue(original < next);
    original = idGenerator.getMeetingId();
    next = idGenerator.getMeetingId();
    assertTrue(original < next);
  }
}
