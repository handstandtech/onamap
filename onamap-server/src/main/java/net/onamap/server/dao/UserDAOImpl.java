package net.onamap.server.dao;

import com.googlecode.objectify.cmd.QueryKeys;
import lombok.NoArgsConstructor;
import net.onamap.shared.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static net.onamap.server.objectify.OfyService.ofy;

@NoArgsConstructor
public class UserDAOImpl {

    private static Logger log = LoggerFactory.getLogger(UserDAOImpl.class);

    public Long updateUser(User user) {
        return ofy().save().entity(user).now().getId();
    }

    public void deleteAllUsers() {
        QueryKeys keys = ofy().load().type(User.class).keys();
        ofy().delete().keys(keys);
    }

    public User findUserByEmail(String email) {
        return ofy().load().type(User.class).filter("email", email).first().now();
    }

    public User findUserByUsername(String username) {
        return ofy().load().type(User.class).filter("username", username).first().now();
    }

    public User findUser(Long id) {
        return ofy().load().type(User.class).id(id).now();
    }

    public List<User> getAllUsers() {
        return ofy().load().type(User.class).list();
    }
}