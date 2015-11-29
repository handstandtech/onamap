package net.onamap.server.objectify;

import com.googlecode.objectify.impl.LoaderImpl;

/**
 * Extend the Loader command with our own logic
 * 
 * @author Jeff Schnitzer
 */
public class OfyLoader extends LoaderImpl<OfyLoader> {
	/** */
	public OfyLoader(Ofy base) {
		super(base);
	}

}