package pij.ryan.durling.exceptions;

public class InvalidMeetingException extends Exception {
    private static final long serialVersionUID = 6591904799872118872L;

    public InvalidMeetingException() {
        super("Needs at least one contact");
    }
}
