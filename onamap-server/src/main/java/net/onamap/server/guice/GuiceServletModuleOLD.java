//package net.onamap.server.guice;
//
//import net.onamap.server.controller.AboutPageController;
//import net.onamap.server.controller.AdminPageController;
//import net.onamap.server.controller.IndexPageController;
//import net.onamap.server.controller.LoginPageController;
//import net.onamap.server.controller.PrivacyPageController;
//import net.onamap.server.controller.SignupPageController;
//import net.onamap.server.controller.TermsPageController;
//import net.onamap.server.controller.action.AdminDeleteActionController;
//import net.onamap.server.controller.action.FlickrDisconnectActionController;
//import net.onamap.server.controller.action.FlickrLoadPhotosetActionController;
//import net.onamap.server.controller.action.FlickrLoadPhotosetListActionController;
//import net.onamap.server.controller.action.FlickrOAuth10aCallback;
//import net.onamap.server.controller.action.FlickrOAuth10aLogin;
//import net.onamap.server.controller.action.LoginActionController;
//import net.onamap.server.controller.action.LogoutActionController;
//import net.onamap.server.controller.action.SignupActionController;
//import net.onamap.server.filter.LoggedInFilter;
//import net.onamap.server.filter.SubdomainFilter;
//import net.onamap.server.task.ReverseGeocodeTaskServlet;
//import net.onamap.server.task.TaskHelper;
//
//import com.google.inject.Singleton;
//import com.google.inject.servlet.ServletModule;
//import com.googlecode.objectify.ObjectifyFactory;
//
///**
// * @author Sam Edwards
// */
//public class GuiceServletModuleOLD extends ServletModule {
//	private static final String ACTION = "/action/";
//
//	@Override
//	public void configureServlets() {
//
//		// Model object managers
//		bind(ObjectifyFactory.class).in(Singleton.class);
//
//		// Filters
//		filter("*").through(LoggedInFilter.class);
//		filter("*").through(SubdomainFilter.class);
//
//		// Pages
//		serve("/").with(IndexPageController.class);
//		serve("/login*").with(LoginPageController.class);
//		serve("/signup*").with(SignupPageController.class);
//		serve("/about*").with(AboutPageController.class);
//		serve("/privacy*").with(PrivacyPageController.class);
//		serve("/terms*").with(TermsPageController.class);
//
//		// Actions
//		serve(ACTION + "login*").with(LoginActionController.class);
//		serve(ACTION + "signup*").with(SignupActionController.class);
//		serve(ACTION + "logout*").with(LogoutActionController.class);
//
//		serve(ACTION + "flickr/connect*").with(FlickrOAuth10aLogin.class);
//		serve(ACTION + "flickr/disconnect*").with(
//				FlickrDisconnectActionController.class);
//		serve(ACTION + "flickr/callback*").with(FlickrOAuth10aCallback.class);
//
//		serve(ACTION + "flickr/photoset*").with(
//				FlickrLoadPhotosetActionController.class);
//		serve(ACTION + "flickr/load_photosets*").with(
//				FlickrLoadPhotosetListActionController.class);
//
//		// Admin
//		serve("/admin*").with(AdminPageController.class);
//		serve(ACTION + "admin/delete*").with(AdminDeleteActionController.class);
//
//		// Tasks
//
//		serve(TaskHelper.REVERSE_GEOCODE_TASK_URL).with(
//				ReverseGeocodeTaskServlet.class);
//
//	}
//
//}
