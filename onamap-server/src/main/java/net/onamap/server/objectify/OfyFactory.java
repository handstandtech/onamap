package net.onamap.server.objectify;

import com.google.inject.Injector;
import com.googlecode.objectify.ObjectifyFactory;
import lombok.extern.slf4j.Slf4j;
import net.onamap.shared.model.*;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Our version of ObjectifyFactory which integrates with Guice. You could and
 * convenience methods here too.
 *
 * @author Jeff Schnitzer
 */
@Singleton
@Slf4j
public class OfyFactory extends ObjectifyFactory
{
    /** */
    private Injector injector;

    /** Register our entity types */
    @Inject
    public OfyFactory(Injector injector) {
        this.injector = injector;

        long time = System.currentTimeMillis();
        this.register(GMapsModel.class);
        this.register(Photo.class);
        this.register(User.class);
        this.register(Photoset.class);
        this.register(GsonKeySerializerHack.class);

        long millis = System.currentTimeMillis() - time;
        log.info("Registration took " + millis + " millis");
    }

    /** Use guice to make instances instead! */
    @Override
    public <T> T construct(Class<T> type) {
        return injector.getInstance(type);
    }

    @Override
    public Ofy begin() {
        return new Ofy(this);
    }
}