package net.onamap.server.filter;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

@Singleton
public abstract class AbstractFilter implements Filter {

	private static Logger log = LoggerFactory.getLogger(AbstractFilter.class
			.getName());

	protected FilterConfig config;

	/** Creates new SessionFilter */
	public AbstractFilter() {
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("Instance created of " + getClass().getName());
		this.config = filterConfig;
	}

	@Override
	public void destroy() {
		// called before the Filter instance is removed from service by the web
		// container
	}
}