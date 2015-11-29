///*
// */
//
//package net.onamap.server.guice;
//
//import com.google.appengine.tools.appstats.AppstatsFilter;
//import com.google.appengine.tools.appstats.AppstatsServlet;
//import com.google.inject.AbstractModule;
//import com.google.inject.Guice;
//import com.google.inject.Injector;
//import com.google.inject.matcher.Matchers;
//import com.google.inject.servlet.GuiceServletContextListener;
//import com.google.inject.servlet.ServletModule;
//import com.googlecode.objectify.ObjectifyFactory;
//import com.googlecode.objectify.ObjectifyFilter;
//import com.motomapia.action.Places;
//import com.motomapia.action.SignIn;
//import com.motomapia.action.TxnTest;
//import com.motomapia.util.ObjectMapperProvider;
//import com.motomapia.util.txn.Transact;
//import com.motomapia.util.txn.TransactInterceptor;
//import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
//import lombok.extern.slf4j.Slf4j;
//import net.onamap.server.controller.OnAMapHTTPResource;
//import net.onamap.server.controller.action.*;
//import net.onamap.server.filter.LoggedInFilter;
//import net.onamap.server.objectify.OfyFactory;
//import net.onamap.server.objectify.OfyService;
//import net.onamap.server.singleton.AppstatsSingletonFilter;
//import net.onamap.server.singleton.AppstatsSingletonServlet;
//import net.onamap.server.task.ReverseGeocodeTaskServlet;
//
//import javax.inject.Singleton;
//import javax.servlet.ServletContextEvent;
//
//
///**
// * Creates our Guice module
// *
// * @author Jeff Schnitzer
// */
//@Slf4j
//public class GuiceConfig extends GuiceServletContextListener
//{
//	/** */
//	static class MotomapiaServletModule extends ServletModule
//	{
//		/* (non-Javadoc)
//		 * @see com.google.inject.servlet.ServletModule#configureServlets()
//		 */
//		@Override
//		protected void configureServlets() {
////			Map<String, String> appstatsParams = Maps.newHashMap();
////			appstatsParams.put("logMessage", "Appstats: /admin/appstats/details?time={ID}");
////			appstatsParams.put("calculateRpcCosts", "true");
////			filter("/*").through(AppstatsFilter.class, appstatsParams);
////			serve("/appstats/*").with(AppstatsServlet.class);
////
////			filter("/*").through(ObjectifyFilter.class);
////			filter("/*").through(BraceletFilter.class);
////
////			serve("/download/*").with(DownloadServlet.class);
////
////			Map<String, String> params = Maps.newHashMap();
////			params.put("com.sun.jersey.config.property.packages", "com.motomapia.action");
////			serve("/api/*").with(GuiceContainer.class, params);
//
//            // Model object managers
//            bind(ObjectifyFactory.class).in(Singleton.class);
//            appstats().through(AppstatsSingletonFilter.class);
//            filter("/*").through(ObjectifyFilter.class);
//
//            // Filters
//            filter("*").through(LoggedInFilter.class);
//
//            bind(OnAMapHTTPResource.class);
//            bind(LoginActionController.class);
//            bind(LogoutActionController.class);
//            bind(SignupActionController.class);
//            bind(FlickrConnectResource.class);
//            bind(FlickrLoadPhotosetActionController.class);
//            bind(AdminResource.class);
//            bind(ReverseGeocodeTaskServlet.class);
//
//            //Appstats
//            serve("/appstats*").with(AppstatsSingletonServlet.class);
//            bind(GuicyInterface.class).to(GuicyInterfaceImpl.class);
//
//            // Route all requests through GuiceContainer
//            serve("/*").with(GuiceContainer.class);
//		}
//        protected FilterKeyBindingBuilder appstats() {
//            StringBuffer sb = new StringBuffer();
//            sb.append("(?!/_ah)");
//            sb.append("(?!/appstats)");
//            sb.append("(?!/assets)");
//            sb.append("(?!/remote_api)");
//            sb.append("(?!/favicon)");
//            return filterRegex(createFilterString(sb.toString()));
//        }
//        private String createFilterString(String middle) {
//            return "^" + middle + "(.*)$";
//        }
//	}
//
//	/** Public so it can be used by unit tests */
//	public static class MotompaiaModule extends AbstractModule
//	{
//		/* (non-Javadoc)
//		 * @see com.google.inject.AbstractModule#configure()
//		 */
//		@Override
//		protected void configure() {
//			requestStaticInjection(OfyService.class);
//
//			// Lets us use @Transact
//			bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transact.class), new TransactInterceptor());
//
//			// Use jackson for jaxrs
//			bind(ObjectMapperProvider.class);
//
//			// External things that don't have Guice annotations
//			bind(AppstatsFilter.class).in(Singleton.class);
//			bind(AppstatsServlet.class).in(Singleton.class);
//			bind(ObjectifyFilter.class).in(Singleton.class);
//
//			bind(Places.class);
//			bind(SignIn.class);
//			bind(TxnTest.class);
//		}
//	}
//
//	/**
//	 * Logs the time required to initialize Guice
//	 */
//	@Override
//	public void contextInitialized(ServletContextEvent servletContextEvent) {
//		long time = System.currentTimeMillis();
//
//		super.contextInitialized(servletContextEvent);
//
//		long millis = System.currentTimeMillis() - time;
//		log.info("Guice initialization took " + millis + " millis");
//	}
//
//	/* (non-Javadoc)
//	 * @see com.google.inject.servlet.GuiceServletContextListener#getInjector()
//	 */
//	@Override
//	protected Injector getInjector() {
//		return Guice.createInjector(new MotomapiaServletModule(), new MotompaiaModule());
//	}
//
//}