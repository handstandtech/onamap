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
public class OfyFactory extends ObjectifyFactory {
    /** */
    private Injector injector;

    public static final Class[] MODEL_TYPES = {GMapsModel.class, Photo.class, User.class, Photoset.class};

    /**
     * Register our entity types
     */
    @Inject
    public OfyFactory(Injector injector) {
        this.injector = injector;

        long time = System.currentTimeMillis();
        for (Class clazz : MODEL_TYPES) {
            this.register(clazz);
        }
        this.register(GsonKeySerializerHack.class);

        long millis = System.currentTimeMillis() - time;
        log.info("Registration took " + millis + " millis");
    }

    /**
     * Use guice to make instances instead!
     */
    @Override
    public <T> T construct(Class<T> type) {
        return injector.getInstance(type);
    }

    @Override
    public Ofy begin() {
        return new Ofy(this);
    }
}