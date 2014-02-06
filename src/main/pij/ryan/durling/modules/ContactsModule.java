package pij.ryan.durling.modules;

import com.google.inject.AbstractModule;
import pij.ryan.durling.factories.ContactFactory;
import pij.ryan.durling.factories.ContactFactoryImpl;
import pij.ryan.durling.generators.IdGenerator;
import pij.ryan.durling.generators.IdGeneratorImpl;

public class ContactsModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(ContactFactory.class).to(ContactFactoryImpl.class);
    bind(IdGenerator.class).to(IdGeneratorImpl.class);
  }
}
