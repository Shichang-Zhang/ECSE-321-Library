package ca.mcgill.ecse321.library.dto;

import ca.mcgill.ecse321.library.model.Item;

/**
 * data transfer object of item model.
 * have id, item category, isInLibrary, name fields.
 * have getter and setter methods for private fields.
 */
public class ItemDto {
    private int id;
    private Item.ItemCategory itemCategory;
    private boolean isInLibrary;
    private String name;

    public ItemDto(int id){
        this.id=id;
        this.itemCategory = Item.ItemCategory.Book;
        this.isInLibrary=true;
    }

    public ItemDto(int id, Item.ItemCategory itemCategory, boolean isInLibrary,String name) {
        this.id = id;
        this.name=name;
        this.itemCategory = itemCategory;
        this.isInLibrary = isInLibrary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Item.ItemCategory getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(Item.ItemCategory itemCategory) {
        this.itemCategory = itemCategory;
    }

    public boolean isInLibrary() {
        return isInLibrary;
    }

    public void setInLibrary(boolean inLibrary) {
        isInLibrary = inLibrary;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
