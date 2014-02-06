package pij.ryan.durling.generators;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class IdGeneratorTest {

  private IdGenerator idGenerator;

  @Before
  public void setup() {
    idGenerator = IdGeneratorImpl.getInstance();
  }

  @Test
  public void shouldGetAnIncrementedNumberForAContactId() {
    assertThat(idGenerator.getContactId(), is(equalTo(0)));
    assertThat(idGenerator.getContactId(), is(equalTo(1)));
    assertThat(idGenerator.getContactId(), is(equalTo(2)));
  }

  @Test
  public void shouldGetAnIncrementedNumberForAMeetingId() {
    assertThat(idGenerator.getMeetingId(), is(equalTo(0)));
    assertThat(idGenerator.getMeetingId(), is(equalTo(1)));
    assertThat(idGenerator.getMeetingId(), is(equalTo(2)));
  }
}
