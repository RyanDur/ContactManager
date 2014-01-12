public class PastMeetingImpl extends MeetingImpl implements PastMeeting {
    private String notes;

    public PastMeetingImpl(Meeting meeting, String notes) throws InvalidMeetingException {
	super(meeting.getId(), meeting.getContacts(), meeting.getDate());
	this.notes = notes == null ? "" : notes;
    }

    public String getNotes() {
	return notes;
    }
}
