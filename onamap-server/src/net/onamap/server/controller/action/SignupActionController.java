package net.onamap.server.controller.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.onamap.server.constants.Pages;
import net.onamap.server.controller.AbstractController;
import net.onamap.server.dao.UserDAOImpl;
import net.onamap.server.util.EmailValidator;
import net.onamap.server.util.MD5Helper;
import net.onamap.server.util.RequestHelper;
import net.onamap.server.util.SessionHelper;
import net.onamap.shared.model.User;

import com.google.inject.Singleton;

@SuppressWarnings("serial")
@Singleton
public class SignupActionController extends AbstractController {
	private static final String PARAM_PASSWORD = "pass";
	private static final String PARAM_EMAIL = "email";
	private static final String PARAM_USERNAME = "username";
	private static UserDAOImpl userDao = new UserDAOImpl();

	private enum State {
		NONE, EMAIL_TAKEN, USERNAME_TAKEN, INVALID_USERNAME, INVALID_PASSWORD, INVALID_EMAIL, MISSING_USERNAME, MISSING_PASSWORD, MISSING_EMAIL, SUCCESS
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String email = request.getParameter(PARAM_EMAIL);
		String username = request.getParameter(PARAM_USERNAME);
		String password = request.getParameter(PARAM_PASSWORD);

		State state = State.NONE;
		
		//TODO Trim Inputs
		//TODO No Special Characters in Usernames

		User user = null;
		if (isNullOrEmpty(email)) {
			state = State.MISSING_EMAIL;
			RequestHelper.set(request, State.MISSING_EMAIL, true);
		} else if (!EmailValidator.isValidEmailAddress(email)) {
			//Check if email is valid format
			state = State.INVALID_EMAIL;
			RequestHelper.set(request, State.INVALID_EMAIL, true);
		} else if (!isEmailAvailable(email)) {
			// Check if email are already taken
			state = State.EMAIL_TAKEN;
			RequestHelper.set(request, State.EMAIL_TAKEN, true);
		} else if (isNullOrEmpty(username)) {
			state = State.MISSING_USERNAME;
			RequestHelper.set(request, State.MISSING_USERNAME, true);
		} else if (username.length() < 2) {
			state = State.INVALID_USERNAME;
			RequestHelper.set(request, State.INVALID_USERNAME, true);
		} else if (!isUsernameAvailable(username)) {
			// Check if username is already taken
			state = State.USERNAME_TAKEN;
			RequestHelper.set(request, State.USERNAME_TAKEN, true);
		} else if (isNullOrEmpty(password)) {
			state = State.MISSING_PASSWORD;
			RequestHelper.set(request, State.MISSING_PASSWORD, true);
		} else if (password.length() < 8) {
			// Check if password is valid format
			state = State.INVALID_PASSWORD;
			RequestHelper.set(request, State.INVALID_PASSWORD, true);
		} else {
			user = new User();
			String passwordHash = MD5Helper.doDigest(password);
			user.setEmail(email);
			user.setPasswordHash(passwordHash);
			user.setUsername(username);
			Long userId = userDao.updateUser(user);
			user = userDao.findUser(userId);
			state = State.SUCCESS;
		}

		switch (state) {
		case SUCCESS:
			SessionHelper.setCurrentUser(user, request, response);
			showView(request, response, Pages.HOME);
			// TODO Redirect to "Home"
			break;
		default:
			request.setAttribute(PARAM_EMAIL, email);
			request.setAttribute(PARAM_USERNAME, username);

			// TODO Invalid Login Credentials
			showView(request, response, Pages.SIGNUP);
			break;
		}
	}

	private boolean isUsernameAvailable(String username) {
		return userDao.findUserByUsername(username) == null;
	}

	private boolean isEmailAvailable(String email) {
		return userDao.findUserByEmail(email) == null;
	}

}
