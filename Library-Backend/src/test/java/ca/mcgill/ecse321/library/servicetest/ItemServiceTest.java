package ca.mcgill.ecse321.library.servicetest;

import ca.mcgill.ecse321.library.dao.*;
import ca.mcgill.ecse321.library.model.Item;
import ca.mcgill.ecse321.library.model.OnlineAccount;
import ca.mcgill.ecse321.library.model.User;
import ca.mcgill.ecse321.library.service.ItemService;
import java.util.ArrayList;
import java.util.List;
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
import static org.mockito.Mockito.*;

/**
 * unit test of item service
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemReservationRepository itemReservationRepository;


    @InjectMocks
    private ItemService itemService;
    private static final int ITEM1_KEY=1;
    private static final int ITEM2_KEY=2;
    private static final int NON_EXISTING_KEY=114514;
    private static final String ITEM1_NAME="BOOK";
    private static final String ITEM2_NAME="NEWSPAPER";
    private static final String NON_EXISTING_NAME="MOVIE";
    private static final int PERSON1_KEY=1;
    private static final int PERSON_NO_ACCOUNT_KEY=2;

    Item item = new Item();

    @BeforeEach
    public void setMockOutput(){
        lenient().when(itemRepository.findItemById(anyInt())).thenAnswer((InvocationOnMock invocation)->
        {
            if(invocation.getArgument(0).equals(ITEM1_KEY)){
                Item item=new Item();
                item.setId(ITEM1_KEY);
                return item;
            }else{
                return null;
            }
        });

        lenient().when(itemRepository.findByName(anyString())).thenAnswer((InvocationOnMock invocation)->
        {
            if(invocation.getArgument(0).equals(ITEM1_NAME)){
                Item item1=new Item();
                item1.setName(ITEM1_NAME);
                ArrayList<Item> itemList=new ArrayList<>();
                itemList.add(item1);
                return itemList;
            }else{
                return null;
            }
        });

        lenient().when(itemRepository.findAll()).thenAnswer((InvocationOnMock invocation)->
        {
            Item item1=new Item();
            item1.setId(ITEM1_KEY);
            item1.setName(ITEM1_NAME);
            Item item2=new Item();
            item2.setId(ITEM2_KEY);
            item2.setName(ITEM2_NAME);
            ArrayList<Item> itemList=new ArrayList<>();
            itemList.add(item1);
            itemList.add(item2);
            return itemList;
        });

        lenient().when(userRepository.findPersonById(anyInt())).thenAnswer((InvocationOnMock invocation)->
        {
            if(invocation.getArgument(0).equals(PERSON1_KEY)){
                User user=new User();
                user.setId(PERSON1_KEY);
                user.setOnlineAccount(new OnlineAccount());
                return user;
            }else if(invocation.getArgument(0).equals(PERSON_NO_ACCOUNT_KEY)){
                User user=new User();
                user.setId(PERSON_NO_ACCOUNT_KEY);
                return user;
            }else{
                return null;
            }
        });
        // Whenever anything is saved, just return the parameter object
        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
            return invocation.getArgument(0);
        };
        lenient().when(itemRepository.save(any(Item.class))).thenAnswer(returnParameterAsAnswer);

    }

    /**
     * test create item with valid parameters
     */
    @Test
    public void testCreateItem(){
        String name="aBook";
        String itemCategory="Book";
        Item item=null;
        try{
            item= itemService.createItem(itemCategory,name);
        }catch(IllegalArgumentException e){
            fail();
        }
        assertNotNull(item);
        assertEquals(name,item.getName());
        assertEquals(itemCategory,item.getItemCategory().toString());
    }

    /**
     * test create item with null parameters
     */
    @Test
    public void testCreateItemNull(){
        String name=null;
        String error = null;
        String itemCategory=null;
        Item item=null;
        try{
            item=itemService.createItem(itemCategory,name);
        }catch (IllegalArgumentException e){
            error=e.getMessage();
        }
        assertNull(item);
        assertTrue(error.contains("Item category input is invalid!"));
        assertTrue(error.contains("Item name cannot be empty!"));
    }

    /**
     * test create item with invalid id
     */
    @Test
    public void testCreateItemInvalidId(){
        String name=null;
        String error = "";
        String itemCategory="Video";
        Item item=null;
        try{
            item=itemService.createItem(itemCategory,name);
        }catch (IllegalArgumentException e){
            error=e.getMessage();
        }
        assertNull(item);
        assertTrue(error.contains("Item category input is invalid!"));
        assertTrue(error.contains("Item name cannot be empty!"));
    }

    /**
     * test create item with different item categories
     */
    @Test
    public void createAllCategoryItem() {
        when(itemRepository.save(item)).thenReturn(item);
        try{
            assertEquals("Book", itemService.createItem("Book", "name").getItemCategory().toString());
            assertEquals("Movie", itemService.createItem("Movie", "name").getItemCategory().toString());
            assertEquals("MusicAlbum", itemService.createItem("MusicAlbum", "name").getItemCategory().toString());
            assertEquals("Newspaper", itemService.createItem("Newspaper", "name").getItemCategory().toString());
            assertEquals("Archive", itemService.createItem("Archive", "name").getItemCategory().toString());
        }catch (IllegalArgumentException e){
            fail();
        }

    }

    /**
     * test get all items
     */
    @Test
    void getAllItems() {
        assertEquals(2,itemService.getAllItems().size());
    }

    /**
     * test get item by id
     */
    @Test
    public void testGetItemById(){
        assertEquals(ITEM1_KEY,itemService.getItemById(ITEM1_KEY).getId());
    }

    /**
     * test get item with a non-existing id
     */
    @Test
    public void testGetNonExistingItem(){
        assertNull(itemService.getItemById(NON_EXISTING_KEY));
    }

    /**
     * test get item by valid name
     */
    @Test
    public void testGetItemByName() {
        assertEquals(ITEM1_NAME,itemService.getItemByName(ITEM1_NAME).get(0).getName());
    }

    /**
     * test get item with a non-existing name
     */
    @Test
    public void testGetItemByNonExistingName() {
        assertNull(itemService.getItemByName(NON_EXISTING_NAME));
    }

    /**
     * test delete item with valid parameters
     */
    @Test
    public void testDeleteItem(){
        assertEquals(ITEM1_KEY, itemService.deleteItem(ITEM1_KEY).getId());
    }

    /**
     * test delete non-existing item
     */
    @Test
    public void testDeleteNonExistingItem(){
        String error="";
        try{
            itemService.deleteItem(NON_EXISTING_KEY);
        }catch(IllegalArgumentException e){
            error=e.getMessage();
        }
        assertEquals("Item does not exist!",error);
    }

    /**
     * test update item with valid parameters
     */
    @Test
    public void testUpdateItem(){
        Item item=null;
        try{
            item=itemService.updateItemName(ITEM1_KEY,ITEM1_NAME);
        }catch (IllegalArgumentException e){
            fail();
        }
        assertNotNull(item);
        assertEquals(ITEM1_KEY,item.getId());
        assertEquals(ITEM1_NAME,item.getName());
    }

    /**
     * test update item with non-existing item
     */
    @Test
    public void testUpdateNonExistingItem(){
        Item item=null;
        String error="";
        try{
            item=itemService.updateItemName(NON_EXISTING_KEY,ITEM1_NAME);
        }catch (IllegalArgumentException e){
            error=e.getMessage();
        }
        assertNull(item);
        assertEquals("Item does not exist!",error);

    }

    /**
     * test update item with empty input name
     */
    @Test
    public void testUpdateEmptyInput(){
        Item item=null;
        String error="";
        try{
            item=itemService.updateItemName(ITEM1_KEY,null);
        }catch (IllegalArgumentException e){
            error=e.getMessage();
        }
        assertNull(item);
        assertEquals("Item name connot be empty!",error);

    }

    /**
     * test browse with valid parameters
     */
    @Test
    public void testBrowse(){
        List<String> browseList=null;
        try{
            browseList=itemService.browseItem(PERSON1_KEY);
        }catch (IllegalArgumentException e){
            fail();
        }
        assertNotNull(browseList);
        assertEquals(2,browseList.size());
        assertTrue(browseList.contains(ITEM1_NAME));
        assertTrue(browseList.contains(ITEM2_NAME));
    }

    /**
     * test browse item but invalid user
     */
    @Test
    public void testBrowseWithNonExistingUser(){
        String error="";
        try{
            itemService.browseItem(NON_EXISTING_KEY);
        }catch (IllegalArgumentException e){
            error=e.getMessage();
        }
        assertEquals("person not exist",error);

    }

    /**
     * test browse items with no online account
     */
    @Test
    public void testBrowseWithoutOnlineAccount(){
        String error="";
        try{
            itemService.browseItem(PERSON_NO_ACCOUNT_KEY);
        }catch (IllegalArgumentException e){
            error=e.getMessage();
        }
        assertEquals("only person with online account can browse item.",error);

    }


}