package pij.ryan.durling.controllers;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class CalendarDatesTest {
  CalendarDates cd;
  @Before
  public void setup() {
    cd = new CalendarDatesImpl();
  }

  @Test
  public void shouldBeAbleToGetADateKey() {
    Calendar date = Calendar.getInstance();
    int year = 2014;
    int month = 11;
    int week = 2;
    int day = 1;
    int expected = Integer.parseInt("" + year + month + week + day);
    date.set(Calendar.YEAR, year);
    date.set(Calendar.MONTH, month);
    date.set(Calendar.WEEK_OF_MONTH, week);
    date.set(Calendar.DAY_OF_WEEK, day);
    int actual = cd.key(date);

    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void shouldKnowIfDateIsBeforeToday() {
    Calendar date = Calendar.getInstance();
    date.add(Calendar.DATE, -1);

    assertTrue(cd.beforeToday(date));
  }

  @Test
  public void shouldKnowIfDateIsAfterToday() {
    Calendar date = Calendar.getInstance();
    date.add(Calendar.DATE, 1);

    assertTrue(cd.afterToday(date));
  }
}
