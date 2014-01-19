package factories;

import models.Contact;

public interface ContactFactory {

  /**
   * @param id
   * @param name
   * @return
   */
  Contact createContact(int id, String name);
}
