package pij.ryan.durling.controllers;

import pij.ryan.durling.factories.ContactFactory;
import pij.ryan.durling.generators.IdGenerator;
import pij.ryan.durling.models.Contact;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ContactsImpl implements Contacts, Serializable {
    private static final long serialVersionUID = -8708289797724094321L;

    private Map<String, Set<Contact>> contactsByName = new HashMap<>();
    private Map<Integer, Contact> contactsById = new HashMap<>();
    private ContactFactory contactFactory;
    private IdGenerator idGenerator;

    public ContactsImpl(ContactFactory contactFactory, IdGenerator idGenerator) {
        this.contactFactory = contactFactory;
        this.idGenerator = idGenerator;
    }

    @Override
    public void add(String name, String notes) throws NullPointerException {
        if (name == null || notes == null) throw new NullPointerException();
        int id = idGenerator.getContactId();
        if (contactsById.get(id) == null) {
            Contact contact = contactFactory.createContact(id, name);
            contact.addNotes(notes);
            setContactsById(contact);
            setContactsByName(contact);
        }
    }

    @Override
    public Contact get(int id) {
        if (contactsById.get(id) == null) throw new IllegalArgumentException();
        return contactsById.get(id);
    }

    @Override
    public Set<Contact> get(String name) {
        if (name == null) throw new NullPointerException();
        return contactsByName.get(name);
    }

    @Override
    public boolean notValidContactSet(Set<Contact> contacts) {
        if (contacts.isEmpty()) return true;
        for (Contact contact : contacts) {
            if (notValidContactId(contact.getId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean notValidContactId(int... ids) {
        for (int id : ids) {
            if (contactsById.get(id) == null) {
                return true;
            }
        }
        return false;
    }

    private void setContactsByName(Contact contact) {
        Set<Contact> values = contactsByName.get(contact.getName());
        if (values == null) {
            Set<Contact> contacts = new HashSet<>();
            contacts.add(contact);
            contactsByName.put(contact.getName(), contacts);
        } else {
            values.add(contact);
        }
    }

    private void setContactsById(Contact contact) {
        contactsById.put(contact.getId(), contact);
    }
}
