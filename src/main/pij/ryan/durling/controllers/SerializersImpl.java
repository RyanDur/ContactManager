package pij.ryan.durling.controllers;

/**
 * Created by ryandurling on 2/5/14.
 */
public class SerializersImpl implements Serializers {
  @Override
  public void onExit(final ContactManager contactManager) {
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        contactManager.flush();
      }
    });
  }

  @Override
  public void onOpen() {
    //TODO
  }
}
