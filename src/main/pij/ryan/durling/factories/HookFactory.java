package ryan.durling.factories;

public interface HookFactory {

  ShutDownHook createShutDownHook(ContactManager contactManager);
}
