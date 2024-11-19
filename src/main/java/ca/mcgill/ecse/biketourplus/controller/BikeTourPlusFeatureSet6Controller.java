package ca.mcgill.ecse.biketourplus.controller;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse.biketourplus.application.BikeTourPlusApplication;
import ca.mcgill.ecse.biketourplus.model.BikeTourPlus;
import ca.mcgill.ecse.biketourplus.model.Combo;
import ca.mcgill.ecse.biketourplus.model.ComboItem;
import ca.mcgill.ecse.biketourplus.model.Gear;
import ca.mcgill.ecse.biketourplus.persistence.BikeTourPlusPersistence;
import ca.mcgill.ecse.biketourplus.model.BookableItem;
import ca.mcgill.ecse.biketourplus.model.BookedItem;

/**
 * Class that is part of the Controller of the BikeTourPlus application
 * 
 * @author Karl Bridi (for all this class)
 *         Part of this code was taken and adapted from the tutorial wiki pages
 *         (BTMS project)
 */
public class BikeTourPlusFeatureSet6Controller {

  private static BikeTourPlus btp = BikeTourPlusApplication.getBikeTourPlus();

  /**
   * Private constructor to prevent creation of instances of this class
   */
  private BikeTourPlusFeatureSet6Controller() {
  }

  /**
   * Method to add a Combo
   * 
   * @author Karl Bridi
   * @param name     of the combo
   * @param discount percentage if lodge is taken
   * @return error in case there is one
   */
  public static String addCombo(String name, int discount) {
    // discount input validation
    var error = isValidCombo(discount);
    if (!error.isEmpty()) {
      return error;
    }

    // name of new combo input validation
    if (name == null) {
      return "The name must not be null";
    } else if (name.isEmpty()) {
      return "The name must not be empty";
    }

    // make sure there is no gear with the same name
    if (isGear(name)) {
      return "A piece of gear with the same name already exists";
    }

    try {
      btp.addCombo(name, discount);
      BikeTourPlusPersistence.save();
    } catch (RuntimeException e) {
      error = e.getMessage();
      // if there is still an error than it must be because another combo has the same
      // name
      if (error.startsWith("Cannot create due to duplicate name.")) {
        error = "A combo with the same name already exists";
      }
    }

    return error; // empty string means operation was successful (no error)
  }

  /**
   * Method to update an existing combo
   * 
   * @author Karl Bridi
   * @param oldName     of the existing combo
   * @param newName
   * @param newDiscount
   * @return error if there are any
   */
  public static String updateCombo(String oldName, String newName, int newDiscount) {
    // discount input validation
    var error = isValidCombo(newDiscount);
    if (!error.isEmpty()) {
      return error;
    }

    // name of new combo input validation
    if (newName == null) {
      return "The name must not be null";
    } else if (newName.isEmpty()) {
      return "The name must not be empty";
    }

    // make sure that the old combo exists
    if (!isCombo(oldName)) {
      return "The combo does not exist";
    }

    // make sure a gear doesn't have the same name as new combo name
    if (isGear(newName)) {
      return "A piece of gear with the same name already exists";
    }

    Combo combo = (Combo) BookableItem.getWithName(oldName);
    try {
      // if the new name wasn't set then a combo with the same name exists
      if (!combo.setName(newName)) {
        return "A combo with the same name already exists";
      }

      // update discount attribute
      combo.setDiscount(newDiscount);
      BikeTourPlusPersistence.save();
    } catch (Exception e) {
      return e.getMessage();
    }

    return error; // empty string means operation was successful (no error)
  }

  /**
   * Method to add a Gear to a certain combo
   * 
   * @author Karl Bridi
   * @param gearName
   * @param comboName
   * @return error if there are any
   */
  public static String addGearToCombo(String gearName, String comboName) {

    // make sure that the gear exists
    if (!isGear(gearName)) {
      return "The piece of gear does not exist";
    }

    // make sure that the combo exists
    if (!isCombo(comboName)) {
      return "The combo does not exist";
    }

    // get the gear and combo
    Combo combo = (Combo) BookableItem.getWithName(comboName);
    Gear gear = (Gear) BookableItem.getWithName(gearName);

    ComboItem comboItem = getComboItem(combo, gear);

    try {
      // case where the gear is already part of the combo
      if (comboItem != null) {
        comboItem.setQuantity(comboItem.getQuantity() + 1);
        // case where gear is added for the first time to combo
      } else {
        combo.addComboItem(1, btp, gear);
      }

      BikeTourPlusPersistence.save();
    } catch (Exception e) {
      return e.getMessage();
    }

    return "";
  }

  /**
   * Method to remove gear from a certain combo
   * 
   * @author Karl Bridi
   * @param gearName
   * @param comboName
   * @return
   */
  public static String removeGearFromCombo(String gearName, String comboName) {

    // make sure that the combo exists
    if (!isCombo(comboName)) {
      return "The combo does not exist";
    }

    // make sure that the gear exists, if not then do nothing because it is already
    // not a part of the combo
    if (!isGear(gearName)) {
      return "";
    }

    // get the gear and combo
    Combo combo = (Combo) BookableItem.getWithName(comboName);
    Gear gear = (Gear) BookableItem.getWithName(gearName);

    // if the gear is already not a part of the combo then do nothing
    ComboItem comboItem = getComboItem(combo, gear);
    if (comboItem == null) {
      return "";
    }

    try {
      // case where there is just 1 quantity of the gear left
      if (comboItem.getQuantity() == 1) {
        // make sure we can remove the gear and that there remains at least 2 other gear
        if (combo.numberOfComboItems() <= Combo.minimumNumberOfComboItems()) {
          return "A combo must have at least two pieces of gear";
        } else {
          comboItem.delete();
        }
      } else {
        comboItem.setQuantity(comboItem.getQuantity() - 1);
      }
      BikeTourPlusPersistence.save();
    } catch (Exception e) {
      return e.getMessage();
    }

    return "";
  }

  /**
   * Method to get the combos for the choiceBoxes
   * in the UI
   * @return
   */
  public static List<String> getCombos() {
    return btp.getCombos().stream().map(Combo::getName).toList();
  }
  
  /**
   * Method to get the discount for the update feature in the view
   * in the UI
   * @return
   */
  public static String getDiscount(String combo) {
    int discount = ((Combo) BookableItem.getWithName(combo)).getDiscount();
    return Integer.toString(discount);
  }
  
  /**
   * Helper predicate method to know if something is a gear by name
   * 
   * @param name
   * @return
   */
  private static boolean isGear(String name) {
    return BookableItem.getWithName(name) instanceof Gear;
  }

  /**
   * Helper predicate method to know if something is a combo by name
   * 
   * @param name
   * @return
   */
  private static boolean isCombo(String name) {
    return BookableItem.getWithName(name) instanceof Combo;
  }

  /**
   * Helper method for discount input validation
   * 
   * @param discount
   * @return
   */
  private static String isValidCombo(int discount) {
    // discount input validation
    var error = "";
    if (discount < 0) {
      error = "Discount must be at least 0";
    } else if (discount > 100) {
      error = "Discount must be no more than 100";
    }
    return error;
  }

  /**
   * Helper method to get a certain comboItem
   * 
   * @param combo
   * @param gear
   * @return
   */
  private static ComboItem getComboItem(Combo combo, Gear gear) {
    ComboItem comboItem = null;
    for (ComboItem item : btp.getComboItems()) {
      if (item.getCombo() == combo && item.getGear() == gear) {
        comboItem = item;
      }
    }
    return comboItem;
  }

  public static List<TOBookedItem> getComboBookedItem(String combo){
    ArrayList<TOBookedItem> TOBookedItems = new ArrayList<TOBookedItem>();
    Combo aCombo = (Combo) BookableItem.getWithName(combo);
    if (aCombo == null) return null;
    for (ComboItem bookedItem : aCombo.getComboItems()){
       TOBookedItems.add(new TOBookedItem(bookedItem.getGear().getName(), bookedItem.getQuantity()));
    }
    return TOBookedItems;
 }
}