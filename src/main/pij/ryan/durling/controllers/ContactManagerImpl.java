package pij.ryan.durling.controllers;

import pij.ryan.durling.exceptions.InvalidMeetingException;
import pij.ryan.durling.factories.*;
import pij.ryan.durling.generators.IdGenerator;
import pij.ryan.durling.hooks.ShutDownHook;
import pij.ryan.durling.models.Contact;
import pij.ryan.durling.models.FutureMeeting;
import pij.ryan.durling.models.Meeting;
import pij.ryan.durling.models.PastMeeting;
import pij.ryan.durling.serializers.ContactManagerSerializer;

import java.util.*;

public class ContactManagerImpl implements ContactManager {
  private HookFactory hookFactory;
  private SerializationFactory serializationFactory;
  private MeetingFactory meetingFactory;
  private ContactFactory contactFactory;
  private IdGenerator idGenerator;
  private Map<Integer, Meeting> meetings = new HashMap<>();
  private Map<String, Set<Contact>> contactsByName = new HashMap<>();
  private Map<Integer, Contact> contactsById = new HashMap<>();

  public ContactManagerImpl(ContactManagerUtility contactManagerUtility) {
    this.idGenerator = contactManagerUtility.createIdGenerator();
    this.contactFactory = contactManagerUtility.createContactFactory();
    this.meetingFactory = contactManagerUtility.createMeetingFactory();
    this.serializationFactory = contactManagerUtility.createSerializationFactory();
    this.hookFactory = contactManagerUtility.createHookFactory();
    registerHooks();
  }

  @Override
  public int addFutureMeeting(Set<Contact> contacts, Calendar date) throws IllegalArgumentException {
    if (notValidFutureMeeting(contacts, date)) throw new IllegalArgumentException();
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
  public PastMeeting getPastMeeting(int id) throws IllegalArgumentException {
    PastMeeting meeting = (PastMeeting) getMeeting(id);
    if (isInTheFuture(meeting.getDate())) throw new IllegalArgumentException();
    return meeting;
  }

  @Override
  public FutureMeeting getFutureMeeting(int id) throws IllegalArgumentException {
    FutureMeeting meeting = (FutureMeeting) getMeeting(id);
    if (isInThePast(meeting.getDate())) throw new IllegalArgumentException();
    return meeting;
  }

  @Override
  public Meeting getMeeting(int id) {
    return meetings.get(id);
  }

  @Override
  public List<Meeting> getFutureMeetingList(Contact contact) throws IllegalArgumentException {
    if (notValidContact(contact)) throw new IllegalArgumentException();
    List<Meeting> futureMeetingList = new ArrayList<>();
    for (Meeting meeting : meetings.values()) {
      Set<Contact> contacts = meeting.getContacts();
      if (meeting instanceof FutureMeeting && contacts.contains(contact)) {
        futureMeetingList.add(meeting);
      }
    }
    return futureMeetingList;
  }

  @Override
  public List<Meeting> getFutureMeetingList(Calendar date) {
    List<Meeting> futureMeetingList = new ArrayList<>();
    for (Meeting meeting : meetings.values()) {
      if (date == meeting.getDate()) {
        futureMeetingList.add(meeting);
      }
    }
    return futureMeetingList;
  }

  @Override
  public List<PastMeeting> getPastMeetingList(Contact contact) throws IllegalArgumentException {
    if (notValidContact(contact)) throw new IllegalArgumentException();
    List<PastMeeting> pastMeetingList = new ArrayList<>();
    for (Meeting meeting : meetings.values()) {
      Set<Contact> contacts = meeting.getContacts();
      if (meeting instanceof PastMeeting && contacts.contains(contact)) {
        pastMeetingList.add((PastMeeting) meeting);
      }
    }
    return pastMeetingList;
  }

  @Override
  public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) throws IllegalArgumentException, NullPointerException {
    if (contacts == null || date == null || text == null) throw new NullPointerException();
    if (notValidContacts(contacts)) throw new IllegalArgumentException();
    int meetingId = idGenerator.getMeetingId();
    try {
      PastMeeting meeting = meetingFactory.createPastMeeting(meetingId, contacts, date, text);
      meetings.put(meeting.getId(), meeting);
    } catch (InvalidMeetingException e) {
      System.out.println("Error " + e.getMessage());
      e.printStackTrace();
    }
  }

  @Override
  public void addMeetingNotes(int id, String text) throws IllegalArgumentException, IllegalStateException, NullPointerException {
    if (text == null) throw new NullPointerException();
    if (getMeeting(id) == null) throw new IllegalArgumentException();
    FutureMeeting meeting = (FutureMeeting) getMeeting(id);
    if (isInTheFuture(meeting.getDate())) throw new IllegalStateException();
    try {
      PastMeeting pastMeeting = meetingFactory.createPastMeeting(meeting.getId(), meeting.getContacts(), meeting.getDate(), text);
      meetings.put(pastMeeting.getId(), pastMeeting);
    } catch (InvalidMeetingException e) {
      System.out.println("Error " + e.getMessage());
      e.printStackTrace();
    }
  }

  @Override
  public void addNewContact(String name, String notes) throws NullPointerException {
    if (name == null || notes == null) throw new NullPointerException();
    Contact contact = contactFactory.createContact(idGenerator.getContactId(), name);
    contact.addNotes(notes);
    if (contactsByName.get(contact.getName()) == null) {
      Set<Contact> contacts = new HashSet<>();
      contacts.add(contact);
      contactsByName.put(contact.getName(), contacts);
    } else {
      Set<Contact> values = contactsByName.get(contact.getName());
      values.add(contact);
    }
    contactsById.put(contact.getId(), contact);
  }

  @Override
  public Set<Contact> getContacts(int... ids) throws IllegalArgumentException {
    HashSet<Contact> contacts = new HashSet<>();
    for (int id : ids) {
      if (contactsById.get(id) == null) throw new IllegalArgumentException();
      contacts.add(contactsById.get(id));
    }
    return contacts;
  }

  @Override
  public Set<Contact> getContacts(String name) throws NullPointerException {
    if (name == null) throw new NullPointerException();
    return contactsByName.get(name);
  }

  @Override
  public void flush() {
    ContactManagerSerializer contactManagerSerializer = serializationFactory.createContactManagerSerializer();
    contactManagerSerializer.serializeContactManager(this);
  }

  private boolean isInThePast(Calendar date) {
    return date.compareTo(new GregorianCalendar()) < 0;
  }

  private boolean isInTheFuture(Calendar date) {
    return date.compareTo(new GregorianCalendar()) > 0;
  }

  private boolean notValidFutureMeeting(Set<Contact> contacts, Calendar date) {
    return isInThePast(date) || notValidContacts(contacts);
  }

  private boolean notValidContacts(Set<Contact> contacts) {
    if (contacts.isEmpty()) return true;

    for (Contact contact : contacts) {
      if (notValidContact(contact)) {
        return true;
      }
    }
    return false;
  }

  private boolean notValidContact(Contact contact) {
    return contactsById.get(contact.getId()) == null;
  }

  private void registerHooks() {
    ShutDownHook onExit = hookFactory.createShutDownHook(this);
    Runtime.getRuntime().addShutdownHook(onExit.flushHook());
  }
}
