package net.onamap.server.controller.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.onamap.server.constants.Pages;
import net.onamap.server.constants.Urls;
import net.onamap.server.controller.AbstractController;
import net.onamap.server.dao.UserDAOImpl;
import net.onamap.server.util.MD5Helper;
import net.onamap.server.util.RequestHelper;
import net.onamap.server.util.SessionHelper;
import net.onamap.shared.model.User;

import com.google.inject.Singleton;

@SuppressWarnings("serial")
@Singleton
public class LoginActionController extends AbstractController {
	private static final String PARAM_USER = "user";
	private static final String PARAM_PASS = "pass";
	private static UserDAOImpl userDao = new UserDAOImpl();

	private enum State {
		NONE, INVALID_LOGIN, SUCCESS
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		showView(request, response, Pages.LOGIN);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String emailOrUsername = request.getParameter(PARAM_USER);
		String password = request.getParameter(PARAM_PASS);

		State state = State.NONE;

		User user = null;
		if (!isNullOrEmpty(emailOrUsername) && !isNullOrEmpty(password)) {
			boolean isEmail = false;
			if (emailOrUsername.contains("@")) {
				isEmail = true;
			}
			if (isEmail) {
				user = userDao.findUserByEmail(emailOrUsername);
			} else {
				user = userDao.findUserByUsername(emailOrUsername);
			}

			if (user != null) {
				String passHash = MD5Helper.doDigest(password);

				if (passHash != null && passHash.equals(user.getPasswordHash())) {
					// Successful Login
					// Update user to set Last Login Date
					userDao.updateUser(user);
					state = State.SUCCESS;
				} else {
					// Invalid Password
					state = State.INVALID_LOGIN;
				}

			} else {
				// Invalid Email or Username
				state = State.INVALID_LOGIN;
			}

		} else {
			// TODO Invalid Email or Password
			state = State.INVALID_LOGIN;
		}

		switch (state) {
		case SUCCESS:
			SessionHelper.setCurrentUser(user, request, response);
			// Redirect to "Home"
			response.sendRedirect(Urls.HOME);
			break;
		default:
			prepareForLogin(request, emailOrUsername);

			// Invalid Login Credentials
			RequestHelper.set(request, State.INVALID_LOGIN, true);
			showView(request, response, Pages.LOGIN);
			break;
		}
	}

	public static void prepareForLogin(HttpServletRequest request,
			String emailOrUsername) {
		request.setAttribute(PARAM_USER, emailOrUsername);
	}
}
