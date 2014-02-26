package pij.ryan.durling.controllers;

import pij.ryan.durling.models.Contact;

import java.util.Set;

public interface Contacts {

    /**
     * add a new contact
     *
     * @param name
     * @param notes
     * @throws NullPointerException if either param is null
     */
    void add(String name, String notes);

    /**
     * get a contact by id;
     *
     * @param id
     * @return a contact
     * @throws IllegalArgumentException if id does not correspond with contact
     */
    Contact get(int id);

    /**
     * get contact by name.
     *
     * @param name
     * @return a contact
     * @throws NullPointerException if name is null
     */
    Set<Contact> get(String name);

    /**
     * checks to see if any of the contacts are not valid or if the contact list is empty.
     *
     * @param contacts
     * @return return true if a contact does not exist in the collection and false otherwise
     */
    boolean notValidContactSet(Set<Contact> contacts);

    /**
     * check to see if any of the ids do not correspond with a contact
     *
     * @param ids
     * @return true if not a valid contact id false otherwise.
     */
    boolean notValidContactId(int... ids);
}
