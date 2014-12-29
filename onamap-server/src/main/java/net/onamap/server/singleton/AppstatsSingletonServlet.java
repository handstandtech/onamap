package net.onamap.server.singleton;

import com.google.appengine.tools.appstats.AppstatsServlet;
import com.google.inject.Singleton;

@Singleton
public class AppstatsSingletonServlet extends AppstatsServlet {
	
	public AppstatsSingletonServlet() {
		super();
	}

}
