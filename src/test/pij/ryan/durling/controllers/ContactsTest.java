package pij.ryan.durling.controllers;


import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import pij.ryan.durling.factories.ContactFactory;
import pij.ryan.durling.generators.IdGenerator;
import pij.ryan.durling.models.Contact;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ContactsTest {

    @Mock
    private ContactFactory mockContactFactory;
    @Mock
    private IdGenerator mockIdGenerator;
    @Mock
    private Contact contact;

    private Contacts contacts;
    private String notes = "note";
    private String name = "Ryan";
    private int id = 0;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        initMocks(this);
        contacts = new ContactsImpl(mockContactFactory, mockIdGenerator);
        registerContact(contact, name, id);
        contacts.add(name, notes);
    }

    @Test
    public void shouldBeAbleToGetAContactById() {
        MatcherAssert.assertThat(contact, is(equalTo(contacts.get(id))));
    }

    @Test
    public void shouldBeAbleToGetAContactByName() {
        Set<Contact> contactSet = new HashSet<>();
        contactSet.add(contact);

        MatcherAssert.assertThat(contactSet, is(equalTo(contacts.get(name))));
    }

    @Test
    public void shouldThrowAnIllegalArgumentExceptionIfIdPassedDoesNotExist() {
        thrown.expect(IllegalArgumentException.class);

        contacts.get(500);
    }

    @Test
    public void shouldThrowAnIllegalArgumentExceptionIfIdPassedNameIsNull() {
        thrown.expect(NullPointerException.class);

        String name = null;
        contacts.get(name);
    }

    @Test
    public void shouldBeAbleToRetrieveContactsOfSamNameButDifferentId() {
        Contact contact1 = mock(Contact.class);
        registerContact(contact1, name, 3);
        contacts.add(name, notes);

        Set<Contact> expected = new HashSet<>();
        expected.add(contact1);
        expected.add(contact);
        Set<Contact> actual = contacts.get(name);

        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void shouldNotBeAbleToRetrieveContactsOfSamNameAndId() {
        Contact contact1 = mock(Contact.class);
        registerContact(contact1, name, id);
        contacts.add(name, notes);

        Set<Contact> expected = new HashSet<>();
        expected.add(contact);
        Set<Contact> actual = contacts.get(name);

        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void shouldThrowANullPointerExceptionIfNameIsNullWhenAddingAContact() {
        thrown.expect(NullPointerException.class);

        contacts.add(null, notes);
    }

    @Test
    public void shouldThrowANullPointerExceptionIfNotesAreNullWhenAddingAContact() {
        thrown.expect(NullPointerException.class);

        contacts.add(name, null);
    }

    @Test
    public void shouldKnowIfAContactExistsInTheCollection() {
        Contact contact1 = mock(Contact.class);
        registerContact(contact1, name, 3);
        contacts.add(name, notes);

        Set<Contact> expected = new HashSet<>();
        expected.add(contact);
        expected.add(contact1);

        assertFalse(contacts.notValidContactSet(expected));

        int unknownId = 4;
        Contact unknownContact = mock(Contact.class);
        when(unknownContact.getId()).thenReturn(unknownId);
        when(unknownContact.getName()).thenReturn(name);
        expected.add(unknownContact);

        assertTrue(contacts.notValidContactSet(expected));
    }

    @Test
    public void shouldNotAllowAnEmptyCollection() {
        Set<Contact> expected = new HashSet<>();
        assertTrue(contacts.notValidContactSet(expected));
    }

    @Test
    public void shouldBeAbleToCheckIfIndeterminateNumberOfIdsExistInContacts() {
        assertTrue(contacts.notValidContactId(1, 2, 4));

        assertTrue(contacts.notValidContactId(id, 2));

        assertFalse(contacts.notValidContactId(id));
    }

    private void registerContact(Contact contact, String name, int id) {
        when(mockIdGenerator.getContactId()).thenReturn(id);
        when(contact.getId()).thenReturn(id);
        when(contact.getName()).thenReturn(name);
        when(mockContactFactory.createContact(anyInt(), anyString())).thenReturn(contact);
    }
}
