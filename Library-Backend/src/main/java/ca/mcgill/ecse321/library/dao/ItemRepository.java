package ca.mcgill.ecse321.library.dao;

import ca.mcgill.ecse321.library.model.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * item repository.
 * containing methods that can interact with the item table in the database.
 * findItemById method will return an item object if the item's id is the input.
 * findByName method will return a list of items whose name is the input name.
 */
@RepositoryRestResource(collectionResourceRel = "item_data", path = "item_data")
public interface ItemRepository extends CrudRepository<Item, Integer> {
    Item findItemById(int id);

    List<Item> findByName(String name);

    List<Item> findByNameAndItemCategory(String name, Item.ItemCategory itemCategory);

    List<Item> findByItemCategory(Item.ItemCategory itemCategory);
}
