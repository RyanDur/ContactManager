package factories;

import controllers.ContactManager;
import hooks.ShutDownHook;

import java.io.Serializable;


public class HookFactoryImpl implements HookFactory, Serializable {
  private static HookFactory hookFactoryInstance = null;

  public static HookFactory getInstance() {
    if(hookFactoryInstance == null) {
      hookFactoryInstance = new HookFactoryImpl();
    }
    return hookFactoryInstance;
  }

  @Override
  public ShutDownHook createShutDownHook(ContactManager contactManager) {
    return new ShutDownHook(contactManager);
  }
}
