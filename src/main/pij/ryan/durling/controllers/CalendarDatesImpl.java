package pij.ryan.durling.controllers;

import java.util.Calendar;

public class CalendarDatesImpl implements CalendarDates {
  @Override
  public int key(Calendar date) {
    int year = date.get(Calendar.YEAR);
    int month = date.get(Calendar.MONTH);
    int week = date.get(Calendar.WEEK_OF_MONTH);
    int day = date.get(Calendar.DAY_OF_WEEK);
    String sDate = "" + year + month + week + day;
    return Integer.parseInt(sDate);
  }

  @Override
  public boolean beforeToday(Calendar date) {
    int today = key(Calendar.getInstance());
    int otherDay = key(date);
    return otherDay < today;
  }

  @Override
  public boolean afterToday(Calendar date) {
    int today = key(Calendar.getInstance());
    int otherDay = key(date);
    return otherDay > today;
  }
}
