package ca.mcgill.ecse321.library.model;

import javax.persistence.*;

/**
 * items in the library.
 * items have categories: Book, Movie, MusicAlbum, Newspaper, Archive.
 * items have the attribute isInLibrary telling whether the item is checked out.
 * items have the attribute isReserved telling whether the item is reserved.
 * items have attribute name showing their tiles.
 * items have their own id.
 */
@Entity
@Table(name = "item")
public class Item {

  @Column
  private int id;
  @Column
  private boolean isInLibrary;
  @Column
  private boolean isReserved;
  @Column
  private ItemCategory itemCategory;
  @Column
  private String name;

  public enum ItemCategory { Book, Movie, MusicAlbum, Newspaper, Archive }
  public Item(){
  }

  @Id
  public int getId() {
    return id;
  }

  public void setId(int aId) {
    id = aId;
  }

  public void setItemCategory(ItemCategory aItemCategory) {
    itemCategory = aItemCategory;
  }

  public ItemCategory getItemCategory()
  {
    return itemCategory;
  }

  public boolean getIsInLibrary()
  {
    return isInLibrary;
  }

  public void setIsInLibrary(boolean inLibrary) {
    isInLibrary = inLibrary;
  }

  public void setIsReserved(boolean reserved) {
    isReserved = reserved;
  }

  public boolean getIsReserved()
  {
    return isReserved;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}