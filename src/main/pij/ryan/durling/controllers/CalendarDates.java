package pij.ryan.durling.controllers;

import java.util.Calendar;

public interface CalendarDates {

  int key(Calendar date);

  boolean beforeToday(Calendar date);

  boolean afterToday(Calendar date);
}
