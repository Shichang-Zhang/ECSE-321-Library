package ca.mcgill.ecse321.library.servicetest;

import ca.mcgill.ecse321.library.dao.OnlineAccountRepository;
import ca.mcgill.ecse321.library.dao.UserRepository;
import ca.mcgill.ecse321.library.model.OnlineAccount;
import ca.mcgill.ecse321.library.model.User;
import ca.mcgill.ecse321.library.service.UserService;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * unit test of user service
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private OnlineAccountRepository onlineAccountRepository;

    @InjectMocks
    private UserService userService;

    private static final int USER1_KEY = 1;
    private static final int USER2_KEY = 2;
    private static final int NON_EXISTING_KEY = 114514;
    private static final String USER1_NAME = "NAME1";
    private static final String USER2_NAME = "NAME2";
    private static final String EXISTING_NAME = "EXISTING";
    private static final String ONLINE_ACCOUNT_USERNAME = "USER123";
    private static final String ONLINE_ACCOUNT_PASSWORD = "123456";
    private static final String ONLINE_ACCOUNT_EMAIL = "GMAIL";

    @BeforeEach
    public void setMockOutput() {
//        Set mock output: find all users
        lenient().when(userRepository.findUserById(anyInt())).thenAnswer((InvocationOnMock invocation) ->
        {
            if (invocation.getArgument(0).equals(USER1_KEY)) {
                User user = new User();
                user.setId(USER1_KEY);
                user.setName(USER1_NAME);
                return user;
            } else if (invocation.getArgument(0).equals(USER2_KEY)) {
                User user = new User();
                user.setId(USER2_KEY);
                user.setName(USER2_NAME);
                OnlineAccount onlineAccount = new OnlineAccount();
                onlineAccount.setEmail(ONLINE_ACCOUNT_EMAIL);
                onlineAccount.setPassword(ONLINE_ACCOUNT_PASSWORD);
                onlineAccount.setUsername(ONLINE_ACCOUNT_USERNAME);
                onlineAccount.setUser(user);
                user.setOnlineAccount(onlineAccount);
                return user;
            } else {
                return null;
            }
        });
//        Set mock output: find all users
        lenient().when(userRepository.findAll()).thenAnswer((InvocationOnMock invocation) ->
        {
            User user1 = new User();
            user1.setName(USER1_NAME);
            User user2 = new User();
            user2.setName(USER2_NAME);
            ArrayList<User> userList = new ArrayList<>();
            userList.add(user1);
            userList.add(user2);
            return userList;
        });
//        Set mock output: find an online account by username of it
        lenient().when(onlineAccountRepository.findOnlineAccountByUsername(anyString())).thenAnswer((InvocationOnMock invocation) ->
        {
            if (invocation.getArgument(0).equals(EXISTING_NAME)) {
                OnlineAccount onlineAccount = new OnlineAccount();
                onlineAccount.setUsername(EXISTING_NAME);
                return onlineAccount;
            } else {
                return null;
            }
        });

        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> invocation.getArgument(0);
        lenient().when(userRepository.save(any(User.class))).thenAnswer(returnParameterAsAnswer);
    }

    /**
     * test create user
     */
    @Test
    public void testCreateUser() {
        String name = "Joe";
        String address = "Montreal";
        boolean isLocal = true;
        User user = null;
        try {
            user = userService.createUser(address, name, isLocal);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertNotNull(user);
        assertEquals(address, user.getAddress());
        assertEquals(name, user.getName());
        assertEquals(isLocal, user.getIsLocal());
    }

    /**
     * test create user with null parameters
     */
    @Test
    public void testCreateUserWithNullParameters() {
        String name = null;
        String address = null;
        boolean isLocal = true;
        User user = null;
        String error = "";
        try {
            user = userService.createUser(address, name, isLocal);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(user);
        assertTrue(error.contains("Person name cannot be empty!"));
        assertTrue(error.contains("Person address cannot be empty!"));
    }

    /**
     * test get all users
     */
    @Test
    public void testGetAllUser() {
        assertEquals(2, userService.getAllUser().size());
    }

    /**
     * test get user by id
     */
    @Test
    public void testGetUserById() {
        assertEquals(USER1_KEY, userService.getUserById(USER1_KEY).getId());
        assertEquals(USER1_NAME, userService.getUserById(USER1_KEY).getName());
    }

    /**
     * test get user by non-existing id
     */
    @Test
    public void testGetNonExistingUserById() {
        assertNull(userService.getUserById(NON_EXISTING_KEY));
    }

    /**
     * test update is local
     */
    @Test
    public void testUpdateIsLocal() {
        User user = null;
        try {
            user = userService.updateIsLocal(USER1_KEY, true);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertNotNull(user);
        assertTrue(user.getIsLocal());
        assertEquals(USER1_NAME, user.getName());
    }

    /**
     * test update is local with non existing user
     */
    @Test
    public void testUpdateIsLocalNonExistingUser() {
        String error = "";
        try {
            userService.updateIsLocal(NON_EXISTING_KEY, true);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals("non-existing user", error);
    }

    /**
     * test update online account
     */
    @Test
    public void testUpdateOnlineAccount() {
        User user = null;
        try {
            user = userService.updateOnlineAccount(USER1_KEY, ONLINE_ACCOUNT_USERNAME, ONLINE_ACCOUNT_PASSWORD, ONLINE_ACCOUNT_EMAIL);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertNotNull(user);
        assertEquals(ONLINE_ACCOUNT_USERNAME, user.getOnlineAccount().getUsername());
        assertEquals(ONLINE_ACCOUNT_PASSWORD, user.getOnlineAccount().getPassword());
        assertEquals(ONLINE_ACCOUNT_EMAIL, user.getOnlineAccount().getEmail());
    }

    /**
     * test update online account with non-existing user
     */
    @Test
    public void testUpdateOnlineAccountWithNonExistingUser() {
        String error = "";
        try {
            userService.updateOnlineAccount(NON_EXISTING_KEY, ONLINE_ACCOUNT_USERNAME, ONLINE_ACCOUNT_PASSWORD, ONLINE_ACCOUNT_EMAIL);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals("non-existing user", error);

    }

    /**
     * test update online account with null username
     */
    @Test
    public void testUpdateOnlineAccountWithNullUsername() {
        String error = "";
        try {
            userService.updateOnlineAccount(USER1_KEY, null, ONLINE_ACCOUNT_PASSWORD, ONLINE_ACCOUNT_EMAIL);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals("username is empty", error);

    }

    /**
     * test update online account with null password
     */
    @Test
    public void testUpdateOnlineAccountWithNullPassword() {
        String error = "";
        try {
            userService.updateOnlineAccount(USER1_KEY, ONLINE_ACCOUNT_USERNAME, null, ONLINE_ACCOUNT_EMAIL);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals("password is empty", error);

    }

    /**
     * test update online account with null password
     */
    @Test
    public void testUpdateOnlineAccountWithNullEmail() {
        String error = "";
        try {
            userService.updateOnlineAccount(USER1_KEY, ONLINE_ACCOUNT_USERNAME, ONLINE_ACCOUNT_PASSWORD, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals("email is empty", error);

    }

    /**
     * test update online account with existing username
     */
    @Test
    public void testUpdateOnlineAccountWithExistingUsername() {
        String error = "";
        try {
            userService.updateOnlineAccount(USER1_KEY, EXISTING_NAME, ONLINE_ACCOUNT_PASSWORD, ONLINE_ACCOUNT_EMAIL);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals("username is registered, choose another username", error);
    }

    /**
     * test find online account by username
     */
    @Test
    public void testFindOnlineAccountByUsername() {
        OnlineAccount onlineAccount = null;
        try {
            onlineAccount = userService.findOnlineAccountByUsername(EXISTING_NAME);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertNotNull(onlineAccount);
        assertEquals(EXISTING_NAME, onlineAccount.getUsername());
    }

    /**
     * test find non-existing online account
     */
    @Test
    public void testFindNonExistingOnlineAccount() {
        String error = "";
        try {
            userService.findOnlineAccountByUsername(USER2_NAME);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals("online account not exist", error);
    }

}