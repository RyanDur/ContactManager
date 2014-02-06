package pij.ryan.durling.modules;

import com.google.inject.AbstractModule;
import pij.ryan.durling.factories.MeetingFactory;
import pij.ryan.durling.factories.MeetingFactoryImpl;
import pij.ryan.durling.generators.IdGenerator;
import pij.ryan.durling.generators.IdGeneratorImpl;

public class MeetngsModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(MeetingFactory.class).to(MeetingFactoryImpl.class);
    bind(IdGenerator.class).to(IdGeneratorImpl.class);
  }
}
