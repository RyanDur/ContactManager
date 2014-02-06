package pij.ryan.durling.modules;

import com.google.inject.AbstractModule;
import pij.ryan.durling.controllers.Contacts;
import pij.ryan.durling.controllers.ContactsImpl;
import pij.ryan.durling.controllers.Meetings;
import pij.ryan.durling.controllers.MeetingsImpl;
import pij.ryan.durling.serializers.Serializers;
import pij.ryan.durling.serializers.SerializersImpl;

public class ContactManagerModule extends AbstractModule {
  @Override
  protected void configure() {
    install(new ContactsModule());
    install(new MeetngsModule());
    bind(Serializers.class).to(SerializersImpl.class);
    bind(Contacts.class).to(ContactsImpl.class);
    bind(Meetings.class).to(MeetingsImpl.class);
  }
}
