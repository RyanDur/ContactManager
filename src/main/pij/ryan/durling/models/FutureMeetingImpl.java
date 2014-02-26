package pij.ryan.durling.models;

import pij.ryan.durling.exceptions.InvalidMeetingException;

import java.util.Calendar;
import java.util.Set;

public class FutureMeetingImpl extends MeetingImpl implements FutureMeeting {
    private static final long serialVersionUID = 3304443300446874243L;

    public FutureMeetingImpl(int id, Set<Contact> contacts, Calendar date) throws InvalidMeetingException {
        super(id, contacts, date);
    }
}
