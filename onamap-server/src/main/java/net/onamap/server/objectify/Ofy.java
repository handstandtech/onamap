package net.onamap.server.objectify;

import com.googlecode.objectify.impl.ObjectifyImpl;


public class Ofy extends ObjectifyImpl<Ofy>
{
    /** */
    public Ofy(OfyFactory base) {
        super(base);
    }

    /** More wrappers, fun */
    @Override
    public OfyLoader load() {
        return new OfyLoader(this);
    }
}