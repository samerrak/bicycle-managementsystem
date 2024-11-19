

package ca.mcgill.ecse.biketourplus.controller;

public class TOBookedItem
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Attributes
  private String bookedItemName;
  private int bookedItemQuantity;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOBookedItem(String aBookedItemName, int aBookedItemQuantity)
  {
    bookedItemName = aBookedItemName;
    bookedItemQuantity = aBookedItemQuantity;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public String getBookedItemName()
  {
    return bookedItemName;
  }

  public int getBookedItemQuantity()
  {
    return bookedItemQuantity;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "bookableItemName" + ":" + getBookedItemName()+ "," +
            "bookableItemQuantity" + ":" + getBookedItemQuantity()+ "]";
  }
}