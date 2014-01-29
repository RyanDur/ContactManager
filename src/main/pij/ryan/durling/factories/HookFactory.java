package pij.ryan.durling.factories;

import pij.ryan.durling.controllers.ContactManager;
import pij.ryan.durling.hooks.ShutDownHook;

public interface HookFactory {

  ShutDownHook createShutDownHook(ContactManager contactManager);
}
