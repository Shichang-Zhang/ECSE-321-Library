package ca.mcgill.ecse321.library.service;
import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse321.library.HelperMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ca.mcgill.ecse321.library.dao.*;
import ca.mcgill.ecse321.library.model.*;

/**
 * item service methods to manipulate data in backend, used in controller.
 */
@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemReservationRepository itemReservationRepository;
    @Autowired
    private PersonRepository personRepository;

    /**
     * when the library is created, 5 item is created
     */
    @Transactional
    public void initItem(){
        Item.ItemCategory book = Item.ItemCategory.Book;

        //create 5 books
        for(int i=0; i<5; i++){
            Item item=new Item();
            item.setId(item.hashCode()*book.hashCode());
            item.setItemCategory(book);
            item.setName("book"+i);
            item.setIsInLibrary(true);
            item.setIsReserved(false);
            itemRepository.save(item);
        }
    }

    /**
     * Add a new item in the library.
     * Add a new item record in the library database.
     * @param itemCategoryString category : {Book, Movie, MusicAlbum, Newspaper, Archive}
     * @param name item title
     * @return an new item object with the legal input parameters
     */
    @Transactional
    public Item createItem(String itemCategoryString,String name){
        String error = "";
        Item.ItemCategory itemCategory = convertStringToItemCategory(itemCategoryString);
        if (itemCategory==null) error=error+("Item category input is invalid!");
        if(name==null||name=="") error=error+"Item name cannot be empty!";

        error = error.trim();
        if (error.length() > 0) {
            throw new IllegalArgumentException(error);
        }

        Item item=new Item();
        item.setId(item.hashCode()*itemCategory.hashCode());
        item.setItemCategory(itemCategory);

        //When being created, it should be in library and not reserved by any person.
        item.setName(name);
        item.setIsInLibrary(true);
        item.setIsReserved(false);
        itemRepository.save(item);
        return item;
    }

    /**
     * get all items in the library.
     * @return a list of items in the library
     */
    @Transactional
    public List<Item> getAllItems(){
        return HelperMethods.toList(itemRepository.findAll());
    }

    /**
     * find the item database record by the item id.
     * @param id item id
     * @return the item object with the input item id.
     */
    @Transactional
    public Item getItemById(int id){
        if((Integer.valueOf(id) ==null)){
            throw new IllegalArgumentException("Item id cannot be empty!");
        }
        Item item=itemRepository.findItemById(id);
        return item;
    }

    /**
     * find the items that have a certain title
     * @param name item title
     * @return a list of items that have the input title
     */
    @Transactional
    public List<Item> getItemByName(String name){
        return itemRepository.findByName(name);
    }

    /**
     * remove an item from library.
     * delete the item record in the library.
     * @param itemId item id
     * @return the original item object
     */
    @Transactional
    public Item deleteItem(int itemId){
        String error = "";
        Item item=itemRepository.findItemById(itemId);
        if(item==null){
            error=error+"Item does not exist!";
        }
        error = error.trim();
        if (error.length() > 0) {
            throw new IllegalArgumentException(error);
        }
        itemReservationRepository.deleteItemReservationsByItem(item);
        itemRepository.delete(item);
        return item;
    }

    /**
     * update the title of the item with the given id
     * @param itemId item id
     * @param name new title of the item
     * @return the item with the update title
     */
    @Transactional
    public Item updateItemName(int itemId, String name){
        String error = "";
        if(name==null||name==""){
            error=error+("Item name connot be empty!");
        }
        Item item=itemRepository.findItemById(itemId);
        if(item==null){
            error=error+"Item does not exist!";
        }
        error = error.trim();
        if (error.length() > 0) {
            throw new IllegalArgumentException(error);
        }

        item.setName(name);
        return item;
    }

    /**
     * browse all available item titles.
     * Librarian have privilege to browse item titles, but it requires online account for normal users.
     * @param pid person who wants to browse it
     * @return a list of all existing item titles
     */
    public List<String> browseItem(int pid){
        Person person = personRepository.findPersonById(pid);
        if (person==null){
            throw new IllegalArgumentException("person not exist");
        }

        //librarian can browse directly, user needs to have an online account
        if (person instanceof User){
            if (((User) person).getOnlineAccount()==null){
                throw new IllegalArgumentException("only person with online account can browse item.");
            }
        }
        ArrayList<String> itemTitles = new ArrayList<>();
        List<Item> items = this.getAllItems();
        for (Item item : items){
            itemTitles.add(item.getName());
        }
        return itemTitles;
    }

    /**
     * convert item category string to item category enum
     * @param itemCategoryString
     * @return
     */
    private Item.ItemCategory convertStringToItemCategory(String itemCategoryString){
        for (Item.ItemCategory itemCategory : Item.ItemCategory.values()){
            if (itemCategory.toString().equals(itemCategoryString)) return itemCategory;
        }
        return null;
    }

    /**
     * Find items by their category and name
     * If: itemCategory="All" Search only by name
     * If: name=null Search only by item Category
     * else: Search by name and item category
     * @param name
     * @param itemCategory
     * @return list of items
     */
    @Transactional
    public List<Item> findItemByNameAndItemCategory(String name, String itemCategory){
        if(name==null&&itemCategory==null){
            return null;
        }

        if(name==null||name.trim().length()==0){
            return findItemByItemCategory(itemCategory);
        }else if(itemCategory.equals("All")){
            return getItemByName(name);
        }else{
            List<Item> itemList=itemRepository.findByNameAndItemCategory(name,convertStringToItemCategory(itemCategory));
            return itemList;
        }

    }

    /**
     * Find items by their category
     * @param itemCategory
     * @return list of items
     */
    @Transactional
    public List<Item> findItemByItemCategory(String itemCategory){
        if(itemCategory==null){
            throw new IllegalArgumentException("null error");
        }
        List<Item> itemList=itemRepository.findByItemCategory(convertStringToItemCategory(itemCategory));
        return itemList;
    }


}
