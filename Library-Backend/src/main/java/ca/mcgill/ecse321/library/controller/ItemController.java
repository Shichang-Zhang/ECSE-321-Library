package ca.mcgill.ecse321.library.controller;

import ca.mcgill.ecse321.library.dao.ItemRepository;
import ca.mcgill.ecse321.library.dto.ItemDto;
import ca.mcgill.ecse321.library.model.Item;
import ca.mcgill.ecse321.library.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * this class contains the controller methods to call perform item database operations using business service methods
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/items")
public class ItemController {
    @Autowired
    ItemService itemService;

    /**
     * get all items in the library
     * @return a list of all existing items in the library
     */
    @GetMapping("/itemList")
    public List<ItemDto> getAllItems(){
        return itemService.getAllItems().stream().map(item -> HelperMethods.convertToDto(item)).collect(Collectors.toList());
    }

    /**
     * browse all available item titles.
     * @param pid person who wants to browse it
     * @return a list of all existing item titles
     */
    @GetMapping("/browse")
    public List<String> browse(int pid){
        return itemService.browseItem(pid);
    }

    /**
     * create a new library item
     * @param itemCategory
     * @return the data transfer object for the created item
     * @throws IllegalArgumentException
     */
    @PostMapping("/createItem")
    public ItemDto createItem(@RequestParam(name="name") String name, @RequestParam(name="itemCategory") String itemCategory)
            throws IllegalArgumentException {
        Item item = itemService.createItem(itemCategory,name);
        return HelperMethods.convertToDto(item);
    }

    /**
     * delete the item
     * @param id item id
     * @return the data transfer object of the delete item
     * @throws IllegalArgumentException
     */
    @DeleteMapping("/deleteItem")
    public ItemDto deleteItem(@RequestParam(name="id") int id)
            throws IllegalArgumentException {
        Item item = itemService.deleteItem(id);
        return HelperMethods.convertToDto(item);
    }

    /**
     * update the item title
     * @param id item id
     * @param newName new name
     * @return the data transfer object of the item with the update name
     * @throws IllegalArgumentException
     */
    @PutMapping("/updateItem")
    public ItemDto updateItem(@RequestParam("id") int id,@RequestParam(name="name") String newName)
            throws IllegalArgumentException {
        Item item = itemService.updateItemName(id,newName);
        return HelperMethods.convertToDto(item);
    }

    /**
     * find items that have a certain name
     * @param name item title
     * @return a list of data transfer objects of the items with the input name
     * @throws IllegalArgumentException
     */
    @GetMapping("/findItemByName")
    public List<ItemDto> findItemByName(@RequestParam("name") String name)
            throws IllegalArgumentException {
        List<Item> itemList=itemService.getItemByName(name);
        return itemDtoToList(itemList);
    }

    @GetMapping("/findItemByItemCategory")
    public List<ItemDto> findItemByItemCategory(@RequestParam("itemCategory") String itemCategory)
            throws IllegalArgumentException {
        List<Item> itemList=itemService.findItemByItemCategory(itemCategory);
        return itemDtoToList(itemList);
    }

    @GetMapping("/findItem")
    public List<ItemDto> findItem(@RequestParam("name") String name,@RequestParam("itemCategory") String itemCategory)
            throws IllegalArgumentException {
        List<Item> itemList=itemService.findItemByNameAndItemCategory(name,itemCategory);
        return itemDtoToList(itemList);
    }

    /**
     * find item by id
     * @param id item id
     * @return the data transfer object of the item with the given id
     * @throws IllegalArgumentException
     */
    @GetMapping("/findItemById")
    public ItemDto findItemById(@RequestParam("id") int id)
            throws IllegalArgumentException {
        Item item=itemService.getItemById(id);
        return HelperMethods.convertToDto(item);
    }

    /**
     * transfer a list of item objects to a list of data transfer objects of items
     * @param items a list of item objects
     * @return alist of data transfer objects of items
     */
    private List<ItemDto> itemDtoToList(List<Item> items){
        if(items==null){
            return null;
        }else{
            List<ItemDto> itemDtoList=new ArrayList<ItemDto>();
            for(Item item:items){
                itemDtoList.add(HelperMethods.convertToDto(item));
            }
            return itemDtoList;
        }
    }


}
