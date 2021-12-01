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
 * test the persistence of time slot model
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserPersistenceTesting {
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
     * create a user instance, store it into the database, and check whether
     * the user instance has same attributes comparing to the user
     * model in the database
     */
    @Test
    public void testPersistAndLoadUser(){
        int id = 111;
        User u=new User();
        u.setId(id);
        u.setName("Micheal");
        u.setAddress("Montreal");

        OnlineAccount account=new OnlineAccount();
        account.setUser(u);
        account.setId(1111);
        account.setUsername("ECSE321");
        account.setPassword("qwerty");
        account.setEmail("qqmail");
        u.setOnlineAccount(account);
        userRepository.save(u);
        onlineAccountRepository.save(account);

        User u1 = null;
        OnlineAccount account1 = null;
        u1=userRepository.findUserById(id);
        assertNotNull(u1);
        assertEquals("Micheal",u.getName());
        assertEquals("Montreal",u.getAddress());

        account1=u1.getOnlineAccount();
        assertNotNull(account1);
        assertEquals(1111,account.getId());
        assertEquals("ECSE321",account.getUsername());
        assertEquals("qwerty",account.getPassword());
        assertEquals("qqmail",account.getEmail());
    }
}
