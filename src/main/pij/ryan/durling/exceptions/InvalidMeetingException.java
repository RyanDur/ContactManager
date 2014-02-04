package pij.ryan.durling.exceptions;

public class InvalidMeetingException extends Exception {

  public InvalidMeetingException() {
    super("Needs at least one contact");
  }
}
