package ca.mcgill.ecse321.library.dao;

import ca.mcgill.ecse321.library.model.OnlineAccount;
import ca.mcgill.ecse321.library.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * test the persistence of online account model
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OnlineAccountPersistenceTesting {
    @Autowired
    UserRepository userRepository;
    @Autowired
    OnlineAccountRepository onlineAccountRepository;

    @AfterEach
    public void clearDatabase() {
        userRepository.deleteAll();
        onlineAccountRepository.deleteAll();
    }

    /**
     * create an online account instance, store it into the database, and check whether
     * the online account instance has same attributes comparing to the online account
     * model in the database
     */
    @Test
    public void testPersistAndLoadOnlineAccount() {
        int id = 1111;
        OnlineAccount account = new OnlineAccount();
        account.setId(id);

        User user = new User();
        user.setId(5678);
        user.setName("x");
        user.setIsLocal(true);
        account.setUser(user);

        account.setUsername("user1234");
        account.setPassword("123456");
        account.setEmail("gmail");
        user.setOnlineAccount(account);
        userRepository.save(user);
        onlineAccountRepository.save(account);

        OnlineAccount account1 = onlineAccountRepository.findOnlineAccountById(id);

        assertNotNull(account1);
        assertEquals(account.getUsername(), account1.getUsername());
        assertEquals(account.getPassword(), account1.getPassword());
        assertEquals(account.getEmail(), account1.getEmail());

        User user1 = account1.getUser();
        assertNotNull(user1);
        assertEquals(user.getId(), user1.getId());
        assertEquals(user.getName(), user1.getName());
        assertEquals(user.getIsLocal(), user1.getIsLocal());
    }

}
