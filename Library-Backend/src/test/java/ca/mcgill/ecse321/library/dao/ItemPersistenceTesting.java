package ca.mcgill.ecse321.library.dao;

import ca.mcgill.ecse321.library.model.Item;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * test the persistence of item model
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ItemPersistenceTesting {
    @Autowired
    private ItemRepository itemRepository;

    @AfterEach
    public void clearDatabase() {
        itemRepository.deleteAll();
    }

    /**
     * create a item instance, store it into the database, and check whether
     * the item instance has same attributes comparing to the item
     * model in the database
     */
    @Test
    public void testPersistAndLoadItem() {
        Item i=new Item();
        i.setId(1234);
        i.setIsInLibrary(false);
        i.setItemCategory(Item.ItemCategory.Book);
        i.setIsReserved(true);
        itemRepository.save(i);
        i=null;
        i=itemRepository.findItemById(1234);
        assertNotNull(i);
        assertEquals(1234,i.getId());
        assertEquals(false,i.getIsInLibrary());
        assertEquals(Item.ItemCategory.Book,i.getItemCategory());
        assertEquals(true,i.getIsReserved());
    }

}
