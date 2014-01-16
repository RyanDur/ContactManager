public class InvalidMeetingException extends Exception {

  public InvalidMeetingException() {
    super("Needs atleast one contact");
  }
}
