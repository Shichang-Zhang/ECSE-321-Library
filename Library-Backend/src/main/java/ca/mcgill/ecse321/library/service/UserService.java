package ca.mcgill.ecse321.library.service;

import ca.mcgill.ecse321.library.HelperMethods;
import ca.mcgill.ecse321.library.dao.OnlineAccountRepository;
import ca.mcgill.ecse321.library.dao.UserRepository;
import ca.mcgill.ecse321.library.model.OnlineAccount;
import ca.mcgill.ecse321.library.model.Person;
import ca.mcgill.ecse321.library.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * event service methods to manipulate data in backend, used in controller.
 */
@Service
public class UserService extends PersonService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OnlineAccountRepository onlineAccountRepository;


    /**
     * create a user
     *
     * @param address user's address
     * @param name    user's name
     * @param isLocal whether the user is local
     * @return a user object if successful, else null
     */
    @Transactional
    public User createUser(String address, String name, boolean isLocal) {
        User user = (User) createPerson(name, address, "User");
        user.setIsLocal(isLocal);

        userRepository.save(user);
        return user;
    }

    /**
     * get all users from the database records
     *
     * @return a list of existing users
     */
    @Transactional
    public List<Person> getAllUser() {
        List<Person> tempList = HelperMethods.toList(userRepository.findAll());
        List<Person> userList = new ArrayList<>();
        for (Person p : tempList) {
            if (p instanceof User) {
                userList.add(p);
            }
        }
        return userList;
    }

    /**
     * get user by id
     *
     * @param id user id
     * @return user found or null if not find
     */
    @Transactional
    public User getUserById(int id) {
        return userRepository.findUserById(id);
    }

    /**
     * update isLocal attribute of the user
     *
     * @param id      user id
     * @param isLocal whether the user is local
     * @return user with the update isLocal attribute
     */
    @Transactional
    public User updateIsLocal(int id, boolean isLocal) {
        //check user first
        User user = getUserById(id);
        if (user == null) {
            throw new IllegalArgumentException("non-existing user");
        }
        user.setIsLocal(isLocal);
        userRepository.save(user);
        return user;
    }

    /**
     * update the online account for the user.
     * If the user does not have the online account, an online account will be created.
     *
     * @param uid user id
     * @param username the new username
     * @param password the new password
     * @param email the new email
     * @return the user with the update online account
     */
    @Transactional
    public User updateOnlineAccount(int uid, String username, String password, String email) {

        User user = getUserById(uid);
        if (user == null) throw new IllegalArgumentException("non-existing user");
        if (username == null || username.trim().length() == 0) throw new IllegalArgumentException("username is empty");
        if (password == null || password.trim().length() == 0) throw new IllegalArgumentException("password is empty");
        if (email == null || email.trim().length() == 0) throw new IllegalArgumentException("email is empty");

        //if user does not have an online account, create a new one
        OnlineAccount onlineAccount;
        if (user.getOnlineAccount() == null) {
            onlineAccount = new OnlineAccount();
            onlineAccount.setId(username.hashCode() + password.hashCode() * email.hashCode());
        } else {
            onlineAccount = user.getOnlineAccount();
        }

        //make the username unique
        if (onlineAccountRepository.findOnlineAccountByUsername(username) != null) {
            throw new IllegalArgumentException("username is registered, choose another username");
        }

        onlineAccount.setUsername(username);
        onlineAccount.setPassword(password);
        onlineAccount.setEmail(email);
        user.setOnlineAccount(onlineAccount);
        onlineAccount.setUser(user);
        onlineAccountRepository.save(onlineAccount);
        userRepository.save(user);
        return user;
    }

    /**
     * update online account username
     *
     * @param uid user id
     * @param username new username
     * @return the user data with updated username
     */
    @Transactional
    public User updateOnlineAccountUsername(int uid, String username) {

        User user = getUserById(uid);
        if (user == null) throw new IllegalArgumentException("non-existing user");
        if (username == null || username.trim().length() == 0) throw new IllegalArgumentException("username is empty");

        //if user does not have an online account, create a new one
        OnlineAccount onlineAccount = user.getOnlineAccount();

        //make the username unique
        if (onlineAccountRepository.findOnlineAccountByUsername(username) != null) {
            throw new IllegalArgumentException("username is registered, choose another username");
        }

        onlineAccount.setUsername(username);
        user.setOnlineAccount(onlineAccount);
        onlineAccount.setUser(user);
        onlineAccountRepository.save(onlineAccount);
        userRepository.save(user);
        return user;
    }

    /**
     * update the username email address
     *
     * @param uid user id
     * @param email user's email
     * @return update info user
     */
    @Transactional
    public User updateOnlineAccountEmail(int uid, String email) {
        //check input
        User user = getUserById(uid);
        if (user == null) {
            throw new IllegalArgumentException("non-existing user");
        }
        if (email == null || email.trim().length() == 0) {
            throw new IllegalArgumentException("email is empty");
        }
        OnlineAccount onlineAccount = user.getOnlineAccount();
        if (onlineAccount==null) {
            throw new IllegalArgumentException("the user has no online account");
        }

        //update the email
        onlineAccount.setEmail(email);
        user.setOnlineAccount(onlineAccount);
        onlineAccount.setUser(user);
        onlineAccountRepository.save(onlineAccount);
        userRepository.save(user);
        return user;
    }

    /**
     * find online account by username
     *
     * @param username username
     * @return the online account that contains the username
     */
    @Transactional
    public OnlineAccount findOnlineAccountByUsername(String username) {
        OnlineAccount onlineAccount = onlineAccountRepository.findOnlineAccountByUsername(username);
        if (onlineAccount == null) {
            throw new IllegalArgumentException("online account not exist");
        }
        return onlineAccount;
    }

}
