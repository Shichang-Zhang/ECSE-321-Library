package ca.mcgill.ecse321.library.servicetest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import ca.mcgill.ecse321.library.service.*;
import ca.mcgill.ecse321.library.dao.*;

/**
 * unit test of library service
 */
@ExtendWith(MockitoExtension.class)
@ExtendWith(MockitoExtension.class)
public class LibrarySystemServiceTest {
    @Mock
    private LibrarySystemRepository librarysystemrepository;
    @InjectMocks
    private LibrarySystemService librarysystemservice;

    /**
     * test create library system
     */
    @Test
    public void testCreateLibrarySystem() {
        librarysystemservice.createLibrarySystem();
        assertNotNull(librarysystemrepository.findAll());
    }
}