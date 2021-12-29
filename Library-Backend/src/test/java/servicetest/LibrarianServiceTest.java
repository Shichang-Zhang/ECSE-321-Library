package servicetest;

import ca.mcgill.ecse321.library.dao.*;
import ca.mcgill.ecse321.library.model.*;
import ca.mcgill.ecse321.library.service.LibrarianService;
import ca.mcgill.ecse321.library.service.PersonService;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashSet;

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
 * unit test of librarian service
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class LibrarianServiceTest {

    @Mock
    private LibrarianRepository librarianRepository;
    @Mock
    private BusinessHourRepository businessHourRepository;
    @InjectMocks
    private LibrarianService librarianService;
    //although it is shown as unused, but without the variable, the test will fail
    @Mock
    private PersonRepository personRepository;
    @Mock
    private UserRepository userRepository;
    //although it is shown as unused, but without the variable, the test will fail
    @InjectMocks
    private PersonService personService;

    private static final int LIBRARIAN1_KEY = 1;
    private static final int HEAD_LIBRARIAN_KEY = 10;
    private static final int USER1_KEY = 1;
    private static final String USER1_NAME = "USER";


    private static final int NON_EXISTING_KEY = 114514;
    private static final String LIBRARIAN1_NAME = "NAME1";
    private static final String LIBRARIAN2_NAME = "NAME2";
    private static final String HEAD_LIBRARIAN_NAME = "HEAD";
    private static final int MONDAY = 1;
    private static final int FRIDAY = 5;


    @BeforeEach
    public void setMockOutput() {
//        Set mock output: find a librarian by id
        lenient().when(librarianRepository.findLibrarianById(anyInt())).thenAnswer((InvocationOnMock invocation) ->
        {
            if (invocation.getArgument(0).equals(LIBRARIAN1_KEY)) {
                Librarian librarian = new Librarian();
                librarian.setId(LIBRARIAN1_KEY);
                librarian.setName(LIBRARIAN1_NAME);
                librarian.setIsHeadLibrarian(false);
                BusinessHour businessHour = new BusinessHour();
                businessHour.setDayOfWeek(BusinessHour.DayOfWeek.Friday);
                businessHour.setId(FRIDAY);
                HashSet<BusinessHour> businessHours = new HashSet<>();
                businessHours.add(businessHour);
                librarian.setBusinessHours(businessHours);
                return librarian;
            } else if (invocation.getArgument(0).equals(HEAD_LIBRARIAN_KEY)) {
                Librarian librarian = new Librarian();
                librarian.setId(HEAD_LIBRARIAN_KEY);
                librarian.setName(HEAD_LIBRARIAN_NAME);
                librarian.setIsHeadLibrarian(true);
                return librarian;
            } else {
                return null;
            }
        });
//      Set mock output: find all librarians
        lenient().when(librarianRepository.findAll()).thenAnswer((InvocationOnMock invocation) ->
        {
            Librarian librarian1 = new Librarian();
            Librarian librarian2 = new Librarian();

            librarian1.setName(LIBRARIAN1_NAME);
            librarian2.setName(LIBRARIAN2_NAME);
            ArrayList<Librarian> librarianList = new ArrayList<>();
            librarianList.add(librarian1);
            librarianList.add(librarian2);
            return librarianList;
        });
//        Set mock output: find a business hour by id
        lenient().when(businessHourRepository.findBusinessHourById(anyInt())).thenAnswer((InvocationOnMock invocation) ->
        {
            if (invocation.getArgument(0).equals(MONDAY)) {
                BusinessHour businessHour = new BusinessHour();
                businessHour.setId(MONDAY);
                businessHour.setDayOfWeek(BusinessHour.DayOfWeek.Monday);
                businessHour.setStartTime(Time.valueOf("00:00:00"));
                businessHour.setEndTime(Time.valueOf("12:00:00"));
                return businessHour;
            } else if (invocation.getArgument(0).equals(FRIDAY)) {
                BusinessHour businessHour = new BusinessHour();
                businessHour.setId(FRIDAY);
                businessHour.setDayOfWeek(BusinessHour.DayOfWeek.Friday);
                businessHour.setStartTime(Time.valueOf("00:00:00"));
                businessHour.setEndTime(Time.valueOf("12:00:00"));
                return businessHour;
            } else {
                return null;
            }
        });
//        Set mock output: find a user by id
        lenient().when(userRepository.findUserById(anyInt())).thenAnswer((InvocationOnMock invocation) ->
        {
            if (invocation.getArgument(0).equals(USER1_KEY)) {
                User user = new User();
                user.setId(USER1_KEY);
                user.setName(USER1_NAME);
                return user;
            } else {
                return null;
            }
        });

        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> invocation.getArgument(0);
        lenient().when(librarianRepository.save(any(Librarian.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(businessHourRepository.save(any(BusinessHour.class))).thenAnswer(returnParameterAsAnswer);
    }

    /**
     * test create librarian with valid parameters
     */
    @Test
    public void testCreateLibrarian() {
        String name = "Joe";
        String address = "Montreal";
        boolean isHeadLibrarian = false;
        Librarian librarian = null;
        try {
            librarian = librarianService.createLibrarian(HEAD_LIBRARIAN_KEY, name, address, isHeadLibrarian);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertEquals(name, librarian.getName());
        assertEquals(address, librarian.getAddress());
        assertEquals(isHeadLibrarian, librarian.getIsHeadLibrarian());
    }

    /**
     * test create librarian but the creator does not have the authority
     */
    @Test
    public void testCreateLibrarianWithNoAuthority() {
        String name = "Joe";
        String address = "Montreal";
        boolean isHeadLibrarian = false;
        String error = "";
        try {
            librarianService.createLibrarian(LIBRARIAN1_KEY, name, address, isHeadLibrarian);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals("Librarian does not have authority to hire new librarian!", error);
    }

    /**
     * test create librarian but there is no head librarian in the library
     */
    @Test
    public void testCreateLibrarianWithNonExistingHeadLibrarian() {
        String name = "Joe";
        String address = "Montreal";
        boolean isHeadLibrarian = false;
        Librarian librarian = null;
        String error = "";
        try {
            librarian = librarianService.createLibrarian(NON_EXISTING_KEY, name, address, isHeadLibrarian);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(librarian);
        assertEquals("Head librarian does not exist!", error);
    }

    /**
     * test get all librarian
     */
    @Test
    public void testGetAllLibrarian() {
        assertEquals(2, librarianService.getAllLibrarian().size());
    }

    /**
     * test get librarian by id with valid parameters
     */
    @Test
    public void testGetLibrarianById() {
        assertNotNull(librarianService.getLibrarianById(LIBRARIAN1_KEY));
        assertEquals(LIBRARIAN1_KEY, librarianService.getLibrarianById(LIBRARIAN1_KEY).getId());
        assertEquals(LIBRARIAN1_NAME, librarianService.getLibrarianById(LIBRARIAN1_KEY).getName());
    }

    /**
     * test get librarian with non-existing librarian
     */
    @Test
    public void testGetLibrarianByIdWithNonExistingLibrarian() {
        assertNull(librarianService.getLibrarianById(NON_EXISTING_KEY));
    }

    /**
     * test assign business hour with valid parameters
     */
    @Test
    public void testAssignBusinessHour() {
        Librarian librarian = new Librarian();
        try {
            librarian = librarianService.assignBusinessHour(HEAD_LIBRARIAN_KEY, LIBRARIAN1_KEY, MONDAY);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertEquals(2, librarian.getBusinessHours().size());
    }

    /**
     * test assignment business hour but the operator is not the head librarian
     */
    @Test
    public void testAssignBusinessHourWithNoAuthority() {
        String error = "";
        Librarian librarian = null;
        try {
            librarian = librarianService.assignBusinessHour(LIBRARIAN1_KEY, LIBRARIAN1_KEY, MONDAY);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(librarian);
        assertTrue(error.contains("Librarian does not have authority to assign business hour!"));
    }

    /**
     * test assign business hour with a non-existing librarian
     */
    @Test
    public void testAssignBusinessHourWithNull() {
        String error = "";
        Librarian librarian = null;
        try {
            librarian = librarianService.assignBusinessHour(NON_EXISTING_KEY, NON_EXISTING_KEY, 8);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(librarian);
        assertTrue(error.contains("Librarian does not exist!"));
        assertTrue(error.contains("Head librarian does not exist!"));
        assertTrue(error.contains("business hour does not exist!"));
    }

    /**
     * test assign business hour in the case that the librarian already has been assigned the business hour
     */
    @Test
    public void testAssignBusinessHourWithExistBusinessHour() {
        String error = "";
        Librarian librarian = null;
        try {
            librarian = librarianService.assignBusinessHour(HEAD_LIBRARIAN_KEY, LIBRARIAN1_KEY, 5);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(librarian);
        assertTrue(error.contains("Librarian already has this business hour!"));

    }

    /**
     * test un assign business hour
     */
    @Test
    public void testUnAssignBusinessHour() {
        Librarian librarian = new Librarian();
        try {
            librarian = librarianService.unAssignBusinessHour(HEAD_LIBRARIAN_KEY, LIBRARIAN1_KEY, FRIDAY);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertEquals(0, librarian.getBusinessHours().size());
    }

    /**
     * test unassign business hour but the operator is not a head librarian
     */
    @Test
    public void testUnAssignBusinessHourWithNoAuthority() {
        String error = "";
        Librarian librarian = null;
        try {
            librarian = librarianService.unAssignBusinessHour(LIBRARIAN1_KEY, LIBRARIAN1_KEY, FRIDAY);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(librarian);
        assertTrue(error.contains("Librarian does not have authority to unassign business hour!"));
    }

    /**
     * test unassign business hour but with a non-existing librarian
     */
    @Test
    public void testUnAssignBusinessHourWithNonExistingLibrarian() {
        String error = "";
        Librarian librarian = null;
        try {
            librarian = librarianService.unAssignBusinessHour(NON_EXISTING_KEY, NON_EXISTING_KEY, 8);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(librarian);
        assertTrue(error.contains("Librarian does not exist!"));
        assertTrue(error.contains("Head librarian does not exist!"));
        assertTrue(error.contains("business hour does not exist!"));
    }

    /**
     * test unassign business hour with the business hour that has not been assigned to the librarian
     */
    @Test
    public void testUnAssignBusinessHourWithNonExistingBusinessHour() {
        String error = "";
        Librarian librarian = null;
        try {
            librarian = librarianService.unAssignBusinessHour(HEAD_LIBRARIAN_KEY, LIBRARIAN1_KEY, 1);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(librarian);
        assertTrue(error.contains("Librarian does not have this business hour!"));

    }

    /**
     * test update is head librarian
     */
    @Test
    public void testUpdateIsHeadLibrarian() {
        Librarian librarian = null;
        try {
            librarian = librarianService.updateIsHeadLibrarian(HEAD_LIBRARIAN_KEY, LIBRARIAN1_KEY);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertTrue(librarian.getIsHeadLibrarian());
    }

    /**
     * test update is head librarian with non-existing librarian
     */
    @Test
    public void testUpdateIsHeadLibrarianWithNonExistingLibrarian() {
        Librarian librarian = null;
        String error = "";
        try {
            librarian = librarianService.updateIsHeadLibrarian(NON_EXISTING_KEY, NON_EXISTING_KEY);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(librarian);
        assertTrue(error.contains("Librarian does not exist!"));
        assertTrue(error.contains("Head librarian does not exist!"));
    }

    /**
     * test update is head librarian in the case that the operator is not a head librarian
     */
    @Test
    public void testUpdateIsHeadLibrarianWithNoAuthority() {
        Librarian librarian = null;
        String error = "";
        try {
            librarian = librarianService.updateIsHeadLibrarian(LIBRARIAN1_KEY, LIBRARIAN1_KEY);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(librarian);
        assertTrue(error.contains("Librarian does not have authority to update!"));
    }

    /**
     * test head librarian with the librarian that himself/herself is already the head librarian
     */
    @Test
    public void testUpdateIsHeadLibrarianWithLibrarianAlreadyHead() {
        Librarian librarian = null;
        String error = "";
        try {
            librarian = librarianService.updateIsHeadLibrarian(HEAD_LIBRARIAN_KEY, HEAD_LIBRARIAN_KEY);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(librarian);
        assertTrue(error.contains("There is no need to set a head librarian to be a head librarian"));
    }

    /**
     * test fire librarian with valid condition
     */
    @Test
    public void testFireLibrarian() {
        Librarian librarian = null;
        try {
            librarian = librarianService.fireLibrarian(HEAD_LIBRARIAN_KEY, LIBRARIAN1_KEY);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertEquals(LIBRARIAN1_NAME, librarian.getName());
    }

    /**
     * test fire librarian with no authority
     */
    @Test
    public void testFireLibrarianWithNoAuthority() {
        Librarian librarian = null;
        String error = "";
        try {
            librarian = librarianService.fireLibrarian(LIBRARIAN1_KEY, HEAD_LIBRARIAN_KEY);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(librarian);
        assertTrue(error.contains("Librarian does not have authority to fire!"));
    }

    /**
     * test fire librarian with non-existing librarian
     */
    @Test
    public void testFireLibrarianWithNonExistingLibrarian() {
        Librarian librarian = null;
        String error = "";
        try {
            librarian = librarianService.fireLibrarian(NON_EXISTING_KEY, NON_EXISTING_KEY);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(librarian);
        assertTrue(error.contains("Head librarian does not exist!"));
        assertTrue(error.contains("Librarian does not exist!"));
    }

    /**
     * test delete user with valid condition
     */
    @Test
    public void testDeleteUser() {
        User user = null;
        try {
            user = librarianService.deleteUser(LIBRARIAN1_KEY, USER1_KEY);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertEquals(USER1_NAME, user.getName());
    }

    /**
     * test delete user with non-existing librarian
     */
    @Test
    public void testDeleteUserWithNullLibrarian() {
        User user = null;
        String error = "";
        try {
            user = librarianService.deleteUser(NON_EXISTING_KEY, USER1_KEY);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(user);
        assertEquals("non-existing librarian, no authority to delete the user", error);
    }

    /**
     * test delete user with non-existing user
     */
    @Test
    public void testDeleteUserWithNullUser() {
        User user = null;
        String error = "";
        try {
            user = librarianService.deleteUser(LIBRARIAN1_KEY, NON_EXISTING_KEY);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(user);
        assertEquals("non-existing user", error);
    }


}