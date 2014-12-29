package net.onamap.server.singleton;

import com.google.appengine.tools.appstats.AppstatsFilter;
import com.google.inject.Singleton;

@Singleton
public class AppstatsSingletonFilter extends AppstatsFilter {
	
	public AppstatsSingletonFilter() {
		super();
	}

}
