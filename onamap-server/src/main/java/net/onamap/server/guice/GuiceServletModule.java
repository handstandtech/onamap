package net.onamap.server.guice;

import com.google.inject.Singleton;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyFilter;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import net.onamap.server.controller.OnAMapHTTPResource;
import net.onamap.server.controller.action.*;
import net.onamap.server.filter.LoggedInFilter;
import net.onamap.server.objectify.OfyFactory;
import net.onamap.server.objectify.OfyService;
import net.onamap.server.singleton.AppstatsSingletonFilter;
import net.onamap.server.singleton.AppstatsSingletonServlet;
import net.onamap.server.task.ReverseGeocodeTaskServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sam Edwards
 */
public class GuiceServletModule extends JerseyServletModule {

    private static final String ACTION = "/action/";
    protected static final Logger log = LoggerFactory
            .getLogger(GuiceServletModule.class);

    public GuiceServletModule() {
        log.info("Instance created of " + getClass().getName());
    }

    @Override
    public void configureServlets() {
        requestStaticInjection(OfyService.class);
        requestStaticInjection(OfyFactory.class);

        // Model object managers
        bind(ObjectifyFactory.class).in(Singleton.class);
        bind(ObjectifyFilter.class).in(Singleton.class);
        appstats().through(AppstatsSingletonFilter.class);
        filter("/*").through(ObjectifyFilter.class);


        // Filters
        filter("*").through(LoggedInFilter.class);
//        filter("*").through(SubdomainFilter.class);

        bind(OnAMapHTTPResource.class);
        bind(LoginActionController.class);
        bind(LogoutActionController.class);
        bind(SignupActionController.class);
        bind(FlickrConnectResource.class);
        bind(FlickrLoadPhotosetActionController.class);
        bind(AdminResource.class);
        bind(ReverseGeocodeTaskServlet.class);

        //Appstats
        serve("/appstats*").with(AppstatsSingletonServlet.class);
        bind(GuicyInterface.class).to(GuicyInterfaceImpl.class);

        // Route all requests through GuiceContainer
        serve("/*").with(GuiceContainer.class);
    }

    protected FilterKeyBindingBuilder prePageLoadFilter() {
        StringBuffer sb = new StringBuffer();
        sb.append("(?!/_ah)");
        sb.append("(?!/appstats)");
        sb.append("(?!/assets)");
        sb.append("(?!/remote_api)");
        sb.append("(?!/favicon)");
        sb.append("(?!/_ah/warmup)");
        return filterRegex(createFilterString(sb.toString()));
    }

    protected FilterKeyBindingBuilder storeContinueUrlFilter() {
        StringBuffer sb = new StringBuffer();
        sb.append("(?!/_ah)");
        sb.append("(?!/account)");
        sb.append("(?!/action)");
        sb.append("(?!/appstats)");
        sb.append("(?!/assets)");
        sb.append("(?!/cron)");
        sb.append("(?!/login)");
        sb.append("(?!/signup)");
        sb.append("(?!/remote_api)");
        sb.append("(?!/auth*)");
        sb.append("(?!/util)");
        sb.append("(?!/tasks)");
        sb.append("(?!/favicon)");
        sb.append("(?!/*.png)");
        return filterRegex(createFilterString(sb.toString()));
    }

    protected FilterKeyBindingBuilder appstats() {
        StringBuffer sb = new StringBuffer();
        sb.append("(?!/_ah)");
        sb.append("(?!/appstats)");
        sb.append("(?!/assets)");
        sb.append("(?!/remote_api)");
        sb.append("(?!/favicon)");
        return filterRegex(createFilterString(sb.toString()));
    }

    private String createFilterString(String middle) {
        return "^" + middle + "(.*)$";
    }

    protected ServletKeyBindingBuilder ts(String string) {
        return serveRegex(string + "/?$");
    }

}
