package factories;

import controllers.ContactManager;
import hooks.ShutDownHook;

public interface HookFactory {

  ShutDownHook createShutDownHook(ContactManager contactManager);
}
