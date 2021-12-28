package ca.mcgill.ecse321.library.servicetest;

import ca.mcgill.ecse321.library.dao.PersonRepository;
import ca.mcgill.ecse321.library.model.Person;
import ca.mcgill.ecse321.library.model.User;
import ca.mcgill.ecse321.library.service.PersonService;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;

/**
 * unit test of person service
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;
    @InjectMocks
    private PersonService personService;

    private static final int PERSON1_KEY = 1;
    private static final int NON_EXISTING_KEY = 114514;
    private static final String PERSON1_NAME = "NAME1";
    private static final String PERSON2_NAME = "NAME2";
    private static final String USER_ROLE = "User";
    private static final String NON_EXISTING_USER_ROLE = "Manager";
    private static final String ADDRESS = "Montreal";


    @BeforeEach
    public void setMockOutput() {
//        Set mock output: find a person by id
        lenient().when(personRepository.findPersonById(anyInt())).thenAnswer((InvocationOnMock invocation) ->
        {
            if (invocation.getArgument(0).equals(PERSON1_KEY)) {
                Person person = new User();
                person.setId(PERSON1_KEY);
                person.setName(PERSON1_NAME);
                person.setAddress(ADDRESS);
                return person;
            } else {
                return null;
            }
        });
//        Set mock output: find all persons
        lenient().when(personRepository.findAll()).thenAnswer((InvocationOnMock invocation) ->
        {
            Person person1 = new User();
            person1.setName(PERSON1_NAME);
            Person person2 = new User();
            person2.setName(PERSON2_NAME);
            ArrayList<Person> personList = new ArrayList<>();
            personList.add(person1);
            personList.add(person2);
            return personList;
        });
    }

    /**
     * test create person with valid parameters
     */
    @Test
    public void testCreatePerson() {
        String name = "Joe";
        String address = "Montreal";
        Person person = null;
        try {
            person = personService.createPerson(name, address, USER_ROLE);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertNotNull(person);
        assertEquals(address, person.getAddress());
        assertEquals(name, person.getName());
        assertTrue(person instanceof User);
    }

    /**
     * test create person with null parameters
     */
    @Test
    public void testCreatePersonWithNullParameters() {
        String name = null;
        String address = null;
        Person person = null;
        String error = "";
        try {
            person = personService.createPerson(name, address, NON_EXISTING_USER_ROLE);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(person);
        assertTrue(error.contains("Person name cannot be empty!"));
        assertTrue(error.contains("Person address cannot be empty!"));
        assertTrue(error.contains("Invalid Userrole input, please input Librarian or User!"));
    }

    /**
     * test update address with valid address
     */
    @Test
    public void testUpdateAddress() {
        Person person = null;
        String newAddress = "Toronto";
        try {
            person = personService.updateAddress(PERSON1_KEY, newAddress);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertEquals(newAddress, person.getAddress());

    }

    /**
     * test update address with null address
     */
    @Test
    public void testUpdateAddressWithNullAddress() {
        Person person = null;
        String newAddress = null;
        String error = "";
        try {
            person = personService.updateAddress(PERSON1_KEY, newAddress);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(person);
        assertEquals("address cannot be empty!", error);

    }

    /**
     * test update address with non-existing person
     */
    @Test
    public void testUpdateAddressWithNonExistingPerson() {
        Person person = null;
        String newAddress = "Toronto";
        String error = "";
        try {
            person = personService.updateAddress(NON_EXISTING_KEY, newAddress);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(person);
        assertEquals("operator must be a user or librarian", error);

    }

    /**
     * test get person by id
     */
    @Test
    public void testGetPersonById() {
        assertEquals(PERSON1_NAME, personService.getPersonById(PERSON1_KEY).getName());
        assertEquals(PERSON1_KEY, personService.getPersonById(PERSON1_KEY).getId());

    }

    /**
     * test get person by id with non-existing id
     */
    @Test
    public void testGetPersonByIdWithNonExistingId() {
        assertNull(personService.getPersonById(NON_EXISTING_KEY));

    }

    /**
     * test get all people
     */
    @Test
    public void testGetAllPersons() {
        assertEquals(2, personService.getAllPerson().size());
    }

}