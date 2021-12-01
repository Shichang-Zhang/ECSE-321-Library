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
    public void testPersistAndLoadOnlineAccount(){
        int id=1111;
        OnlineAccount account=new OnlineAccount();
        account.setId(id);

        User u=new User();
        u.setId(5678);
        u.setName("x");
        u.setIsLocal(true);
        account.setUser(u);

        account.setUsername("user1234");
        account.setPassword("123456");
        account.setEmail("gmail");
        u.setOnlineAccount(account);
        userRepository.save(u);
        onlineAccountRepository.save(account);

        OnlineAccount account1 = null;
        User u1 = null;

        account1 = onlineAccountRepository.findOnlineAccountById(id);
        assertNotNull(account1);
        assertEquals(account.getUsername(),account1.getUsername());
        assertEquals(account.getPassword(),account1.getPassword());
        assertEquals(account.getEmail(),account1.getEmail());

        u1 = account1.getUser();
        assertNotNull(u1);
        assertEquals(u.getId(),u1.getId());
        assertEquals(u.getName(),u1.getName());
        assertEquals(u.getIsLocal(),u1.getIsLocal());
    }

}
