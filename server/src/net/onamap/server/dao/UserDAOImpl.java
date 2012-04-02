package net.onamap.server.dao;

import java.util.List;

import net.onamap.shared.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

public class UserDAOImpl extends AbstractDAOBase {

	private static Logger log = LoggerFactory.getLogger(UserDAOImpl.class);

	static {
		ObjectifyService.register(User.class);
	}

	public UserDAOImpl() {

	}

	public Long updateUser(User user) {
		Long userId = null;
		Objectify ofyTxn = ofyTrans();
		try {
			userId = ofyTxn.put(user).getId();
			ofyTxn.getTxn().commit();
		} finally {
			if (ofyTxn.getTxn().isActive()) {
				ofyTxn.getTxn().rollback();
			}
		}
		return userId;
	}

	public void deleteAllUsers() {
		List<Key<User>> keys = ofy().query(User.class).listKeys();
		ofy().delete(keys);
	}

	public User findUserByEmail(String email) {
		Query<User> q = ofy().query(User.class);
		q.filter("email", email);
		return q.get();
	}

	public User findUserByUsername(String username) {
		Query<User> q = ofy().query(User.class);
		q.filter("username", username);
		return q.get();
	}

	public User findUser(Long id) {
		return ofy().find(User.class, id);
	}
}