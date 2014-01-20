package hooks;

import controllers.ContactManager;

public class ShutDownHook {
  private ContactManager contactManager;
  public ShutDownHook(ContactManager contactManager) {
    this.contactManager = contactManager;
  }

  public Thread flushHook() {
    return new Thread() {
       @Override
       public void run() {
         contactManager.flush();
       }
     };
  }
}
