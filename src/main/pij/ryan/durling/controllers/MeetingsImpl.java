package ryan.durling.controllers;

import java.util.*;

public class MeetingsImpl implements Meetings {
  MeetingFactory meetingFactory;
  private IdGenerator idGenerator;
  private HashMap<Integer, Meeting> meetings = new HashMap<>();

  public MeetingsImpl(MeetingFactory meetingFactory, IdGenerator idGenerator) {
    this.meetingFactory = meetingFactory;
    this.idGenerator = idGenerator;
  }

  @Override
  public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
    if (isInThePast(date)) throw new IllegalArgumentException();
    int meetingId = idGenerator.getMeetingId();
    try {
      FutureMeeting meeting = meetingFactory.createFutureMeeting(meetingId, contacts, date);
      meetings.put(meeting.getId(), meeting);
    } catch (InvalidMeetingException e) {
      System.out.println("Error " + e.getMessage());
      e.printStackTrace();
    }
    return meetingId;
  }

  @Override
  public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
    if (contacts == null || date == null || text == null) throw new NullPointerException();
    int id = idGenerator.getMeetingId();
    try {
      PastMeeting meeting = meetingFactory.createPastMeeting(id, contacts, date, text);
      meetings.put(meeting.getId(), meeting);
    } catch (InvalidMeetingException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Meeting get(int id) {
    return meetings.get(id);
  }

  @Override
  public List<Meeting> get(Calendar date) {
    Calendar tomorrow = Calendar.getInstance();
    tomorrow.roll(Calendar.DATE, true);
    Calendar yesterday = Calendar.getInstance();
    yesterday.roll(Calendar.DATE, false);

    List<Meeting> meetingList = new ArrayList<>();
    for (Meeting meeting : meetings.values()) {
      if (date.before(tomorrow) && date.after(yesterday)) {
        meetingList.add(meeting);
      }
    }
    sort(meetingList);
    return meetingList;
  }

  @Override
  public List<Meeting> get(Contact contact) {
    List<Meeting> meetingList = new ArrayList<>();
    for(Meeting meeting : meetings.values()) {
      if(meeting.getContacts().contains(contact)) {
        meetingList.add(meeting);
      }
    }
    return meetingList;
  }

  @Override
  public void convert(FutureMeeting meeting, String notes) {

  }

  private void sort(List<Meeting> meetings) {
    if (meetings != null) {
      Collections.sort(meetings, dateComparator());
    }
  }

  private Comparator dateComparator() {
    return new Comparator<Meeting>() {
      @Override
      public int compare(Meeting o1, Meeting o2) {
        return o1.getDate().compareTo(o2.getDate());
      }
    };
  }

  private boolean isInThePast(Calendar date) {
    return date.compareTo(Calendar.getInstance()) < 0;
  }
}
