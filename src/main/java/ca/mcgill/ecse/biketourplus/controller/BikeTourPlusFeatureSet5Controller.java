package ca.mcgill.ecse.biketourplus.controller;

import java.util.List;

import ca.mcgill.ecse.biketourplus.application.BikeTourPlusApplication;
import ca.mcgill.ecse.biketourplus.model.BikeTourPlus;
import ca.mcgill.ecse.biketourplus.model.BookableItem;
import ca.mcgill.ecse.biketourplus.model.Combo;
import ca.mcgill.ecse.biketourplus.model.Gear;
import ca.mcgill.ecse.biketourplus.persistence.BikeTourPlusPersistence;

/**
 * Class that is a part of the controller of the BikeTourPlus application
 * 
 * @author Sami Bayoud (for all this class)
 *         Part of this code was taken and adapted from the tutorial wiki pages
 *         (BTMS project)
 */

public class BikeTourPlusFeatureSet5Controller {

   private static BikeTourPlus btp = BikeTourPlusApplication.getBikeTourPlus();

   /**
    * Private constructor to prevent creation of instances of this class
    */
   private BikeTourPlusFeatureSet5Controller() {
   }

   /**
    * Method to add a Gear
    * 
    * @param name         of the gear
    * @param pricePerWeek
    * @return error in case there is one
    */
   public static String addGear(String name, int pricePerWeek) {
      // pricePerWeek input validation
      var error = isValidGear(pricePerWeek);
      if (!error.isEmpty()) {
         return error;
      }
      // name of new gear input validation
      if (name == null || name.isEmpty()) {
         return "The name must not be empty";
      }
      if (isGear(name)) {
         return "A piece of gear with the same name already exists";
      }
      // if the new name wasn't set then a combo with the same name exists
      if (isCombo(name)) {
         return "A combo with the same name already exists";
      }

      try {
         btp.addGear(name, pricePerWeek);
         BikeTourPlusPersistence.save();
      } catch (Exception e) {
         return e.getMessage();
      }

      return error; // empty string means operation was succesful (no error)
   }

   private static String isValidGear(int pricePerWeek) {
      // pricePerWeek input validation
      var error = "";
      if (pricePerWeek < 0) {
         error = "The price per week must be greater than or equal to 0";
      } else {
         return error = "";
      }
      return error;
   }

   private static boolean isGear(String name) {
      return BookableItem.getWithName(name) instanceof Gear;
   }

   private static boolean isCombo(String name) {
      return BookableItem.getWithName(name) instanceof Combo;
   }

   /**
    * Method to update an existing Gear
    * 
    * @param oldName         of the exisiting Gear
    * @param newName
    * @param newPricePerWeek
    * @return error if there are any
    */
   public static String updateGear(String oldName, String newName, int newPricePerWeek) {
      // Price per week input validation
      var error = isValidGear(newPricePerWeek);
      if (!error.isEmpty()) {
         return error;
      }

      // name of new Gear input validation
      if (newName == null || newName.isEmpty()) {
         return "The name must not be empty";
      }

      // make sure that the old gear exists
      if (!isGear(oldName)) {
         return "The piece of gear does not exist";
      }
      // make sure a gear doesnt have the same name as the new gear
      if (isGear(newName) && !newName.equals(oldName)) {
         return "A piece of gear with the same name already exists";
      }
      // make sure a gear doesnt have the same name as the new gear
      if (isCombo(newName)) {
         return "A combo with the same name already exists";
      }
      Gear gear = (Gear) BookableItem.getWithName(oldName);
      // update price per week
      try {
         gear.setName(newName);
         gear.setPricePerWeek(newPricePerWeek);
         BikeTourPlusPersistence.save();
      } catch (Exception e) {
         return e.getMessage();
      }

      return error; // empty string means operation was successful (no error)
   }

   /**
   * Method to get the gears for the choiceBoxes
   * in the UI
   * @return
   */
  public static List<String> getGears() {
   return btp.getGear().stream().map(Gear::getName).toList();
 }

  
  
  public static String deleteGear(String name) {
      // make sure that the gear exists
      if (!isGear(name)) {
         return "The piece of gear does not exist";
      }
      try {
         for (Gear gear : btp.getGear()) {
            if (gear.getName().equals(name)) {
               if (gear.getComboItems().size() == 0) {
                  gear.delete();
                  BikeTourPlusPersistence.save();
               } else {
                  return "The piece of gear is in a combo and cannot be deleted";
               }
            }
         }

      } catch (RuntimeException ignored) {
      }
      

      return "";
   }

   /**
   * Method to get the discount for the update feature in the view
   * in the UI
   * @return
   */
  public static String getPrice(String gear) {
   int price = ((Gear) BookableItem.getWithName(gear)).getPricePerWeek();
   return Integer.toString(price);
 }

}
