package ca.mcgill.ecse.biketourplus.controller;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse.biketourplus.application.BikeTourPlusApplication;
import ca.mcgill.ecse.biketourplus.model.*;
import ca.mcgill.ecse.biketourplus.persistence.BikeTourPlusPersistence;

/**
 * Class that is part of the Controller of the BikeTourPlus application
 * 
 * @author Emilien Taisne (for all this class)
 *         Part of this code was taken and adapted from the tutorial wiki pages
 *         (BTMS project)
 */
public class BikeTourPlusFeatureSet3Controller {

   private static BikeTourPlus btp = BikeTourPlusApplication.getBikeTourPlus();

   /**
    * Method to register a participant
    * 
    * @author Emilien Taisne
    * @param email
    * @param password
    * @param name
    * @param emergencyContact
    * @param nrWeeks
    * @param weekAvailableFrom
    * @param weekAvailableUntil
    * @param lodgeRequired
    * @return
    */
   public static String registerParticipant(String email, String password, String name, String emergencyContact,
         int nrWeeks, int weekAvailableFrom, int weekAvailableUntil, boolean lodgeRequired) {

      String error = "";

      // Check email
      error += isValidEmail(email);

      // Check name
      error += isValidName(name);

      // Check emergency contact
      error += isValidEmergencyContact(emergencyContact);

      // Check password
      error += isValidPassword(password);

      // Check number of weeks
      error += isValidNrWeeks(nrWeeks, btp);

      // Check weekAvailableUntil and weekAvailableFrom
      error += isValidWeekAvailableFromAndUntil(weekAvailableFrom, weekAvailableUntil, nrWeeks, btp);

      if (error != "") {
         return error;
      }

      try {
         btp.addParticipant(email, password, name, emergencyContact, nrWeeks, weekAvailableFrom, weekAvailableUntil,
               lodgeRequired, "", 0);
         BikeTourPlusPersistence.save();
      } catch (Exception e) {
         return e.getMessage();
      }

      return "";
   }

   /**
    * Method to update all the information of an account except the email
    *
    * @author Emilien Taisne
    * @param email
    * @param newPassword
    * @param newName
    * @param newEmergencyContact
    * @param newNrWeeks
    * @param newWeekAvailableFrom
    * @param newWeekAvailableUntil
    * @param newLodgeRequired
    * @return
    */
   public static String updateParticipant(String email, String newPassword, String newName, String newEmergencyContact,
         int newNrWeeks, int newWeekAvailableFrom, int newWeekAvailableUntil, boolean newLodgeRequired) {

      // Find participant with email
      Participant participant = btp.getParticipantByEmail(email);
      if (participant == null) {
         return "The participant account does not exist";
      }

      String error = "";

      // Check name
      error += isValidName(newName);

      // Check emergency contact
      error += isValidEmergencyContact(newEmergencyContact);

      // Check password
      error += isValidPassword(newPassword);

      // Check number of weeks
      error += isValidNrWeeks(newNrWeeks, btp);

      // Check weekAvailableUntil and weekAvailableFrom
      error += isValidWeekAvailableFromAndUntil(newWeekAvailableFrom, newWeekAvailableUntil, newNrWeeks, btp);

      if (error != "") {
         return error;
      }

      // Update participant
      try {
         participant.setName(newName);
         participant.setEmergencyContact(newEmergencyContact);
         participant.setPassword(newPassword);
         participant.setNrWeeks(newNrWeeks);
         participant.setWeekAvailableFrom(newWeekAvailableFrom);
         participant.setWeekAvailableUntil(newWeekAvailableUntil);
         participant.setLodgeRequired(newLodgeRequired);
         BikeTourPlusPersistence.save();
      } catch (Exception e) {
         return e.getMessage();
      }

      return "";
   }
   
   /**
    * Method used to verify email and password matches
    * 
    * @author Emilien Taisne
    * @param email
    * @param password
    */
   public static String loginParticipant(String email) {
	   Participant participant = btp.getParticipantByEmail(email);
	   if (participant != null) {
			return "";
	   }
	   return "Invalid Participant";
   }

   /**
    * Method to add a bookable item to participant
    *
    * @author Emilien Taisne
    * @param email
    * @param bookableItemName
    * @return
    */
   public static String addBookableItemToParticipant(String email, String bookableItemName) {
      // Find participant with email
      Participant participant = btp.getParticipantByEmail(email);
      
      if (participant == null) {
         return "The participant does not exist";
      }

      // Finding bookableItem though its name
      if (!BookableItem.hasWithName(bookableItemName)) {
         return "The piece of gear or combo does not exist";
      }
      BookableItem bookableItem = BookableItem.getWithName(bookableItemName);

      // Find if BookedItem exists
      BookedItem existingBookedItem = null;
      for (BookedItem bookedItem : participant.getBookedItems()) {
         if (bookedItem.getItem().equals(bookableItem)) {
            existingBookedItem = bookedItem;
         }
      }

      try {
         // Add 1 to quantity if link exists, else create BookedItem
         if (existingBookedItem != null) {
            int newQuantity = existingBookedItem.getQuantity() + 1;
            existingBookedItem.setQuantity(newQuantity);
         } else {
            participant.addBookedItem(1, btp, bookableItem);
         }

         BikeTourPlusPersistence.save();
      } catch (Exception e) {
         return e.getMessage();
      }

      return "";
   }

   /**
    * Method to remove a bookable item to participant
    *
    * @author Emilien Taisne
    * @param email
    * @param bookableItemName
    * @return
    */
   public static String removeBookableItemFromParticipant(String email, String bookableItemName) {
      // Find participant with email
      Participant participant = btp.getParticipantByEmail(email);
      if (participant == null) {
         return "The participant does not exist";
      }

      // Finding bookableItem though its name
      if (!BookableItem.hasWithName(bookableItemName)) {
         return "Gear or Combo with that name not found";
      }
      BookableItem bookableItem = BookableItem.getWithName(bookableItemName);

      // Find if BookedItem exists
      BookedItem existingBookedItem = null;
      for (BookedItem bookedItem : participant.getBookedItems()) {
         if (bookedItem.getItem().equals(bookableItem)) {
            existingBookedItem = bookedItem;
         }
      }

      try {
         if (existingBookedItem != null) {
            int itemQuantity = existingBookedItem.getQuantity();
            if (itemQuantity == 1) {
               existingBookedItem.delete();
            } else {
               existingBookedItem.setQuantity(itemQuantity - 1);
            }
         } else {
            return "This participant does not have this Gear or Combo";
         }

         BikeTourPlusPersistence.save();
      } catch (Exception e) {
         return e.getMessage();
      }

      return "";
   }

   public static List<TOBookedItem> getParticipantBookedItem(String email){
      ArrayList<TOBookedItem> TOBookedItems = new ArrayList<TOBookedItem>();
      Participant participant = btp.getParticipantByEmail(email);
      if (participant == null) return null;
      for (BookedItem bookedItem : participant.getBookedItems()){
         TOBookedItems.add(new TOBookedItem(bookedItem.getItem().getName(), bookedItem.getQuantity()));
      }
      return TOBookedItems;
   }

   /**
   * Method to get the name for the update feature in the view
   * in the UI
   * @return
   */
  public static String getName(String email) {
   return ((Participant) User.getWithEmail(email)).getName();
 }

 /**
   * Method to get the name for the update feature in the view
   * in the UI
   * @return
   */
  public static String getPassword(String email) {
   return ((Participant) User.getWithEmail(email)).getPassword();
 }

 /**
   * Method to get the name for the update feature in the view
   * in the UI
   * @return
   */
  public static String getContact(String email) {
   return ((Participant) User.getWithEmail(email)).getEmergencyContact();
 }

 /**
   * Method to get the name for the update feature in the view
   * in the UI
   * @return
   */
  public static String getStartWeek(String email) {
   return Integer.toString(((Participant) User.getWithEmail(email)).getWeekAvailableFrom());
 }

 /**
   * Method to get the name for the update feature in the view
   * in the UI
   * @return
   */
  public static String getWeekUntil(String email) {
   return Integer.toString(((Participant) User.getWithEmail(email)).getWeekAvailableUntil());
 }

 /**
   * Method to get the name for the update feature in the view
   * in the UI
   * @return
   */
  public static String getNrWeeks(String email) {
   return Integer.toString(((Participant) User.getWithEmail(email)).getNrWeeks());
 }

   /**
    * Method to validate email
    *
    * @param email
    * @return
    */
   private static String isValidEmail(String email) {
      String error = "";

      // Check for empty space
      if (email.contains(" ")) {
         error += "Email must not contain any spaces.";
      }

      // Check for correct syntax of email
      if (email.indexOf("@") == 0 ||
            email.indexOf("@") != email.lastIndexOf("@") ||
            email.indexOf("@") >= email.lastIndexOf(".") - 1 ||
            email.lastIndexOf(".") >= email.length() - 1) {
         error += "Invalid email";
      }

      // Check if email is the managers email
      if (email.equals("manager@btp.com")) {
         error += "Email cannot be manager@btp.com.";
      }

      if (User.hasWithEmail(email)) {
         if(User.getWithEmail(email) instanceof Participant){
            error += "Email already linked to a participant account.";
         }
         else{
            error += "Email already linked to a guide account.";
         }
      }
      if (Guide.hasWithEmail(email)) {
      }

      // Check if name is not be empty or null
      if (email == null || email.isEmpty()) {
         error += "Email cannot be empty.";
      }

      return error;
   }

   /**
    * Method to validate name
    *
    * @param name
    * @return
    */
   private static String isValidName(String name) {
      // Check if name is not be empty or null
      if (name == null || name.isEmpty()) {
         return "Name cannot be empty.";
      }

      return "";
   }

   /**
    * Method to validate emergency contact
    * 
    * @param emergencyContact
    * @return
    */
   private static String isValidEmergencyContact(String emergencyContact) {
      // Check if emegency contact is not be empty or null
      if (emergencyContact == null || emergencyContact.isEmpty()) {
         return "Emergency contact cannot be empty.";
      }

      return "";
   }

   /**
    * Method to validate password
    * 
    * @param password
    * @return
    */
   private static String isValidPassword(String password) {
      // Check if password is not be empty or null
      if (password == null || password.isEmpty()) {
         return "Password cannot be empty.";
      }

      return "";
   }

   /**
    * method to validate nrWeeks
    * 
    * @param nrWeeks
    * @param btp
    * @return
    */
   private static String isValidNrWeeks(int nrWeeks, BikeTourPlus btp) {
      // Check if number of weeks is greater than zero and smaller than the amout of
      // safe weeks
      if (nrWeeks <= 0) {
         return "Number of weeks must be greater than zero.";
      } else if (nrWeeks > btp.getNrWeeks()) {
         return "Number of weeks must be less than or equal to the number of biking weeks in the biking season.";
      }

      return "";
   }

   /**
    * method to validate weekAvailableFrom and weekAvailableUntil
    * 
    * @param weekAvailableFrom
    * @param weekAvailableUntil
    * @param nrWeeks
    * @param btp
    * @return
    */
   private static String isValidWeekAvailableFromAndUntil(int weekAvailableFrom, int weekAvailableUntil, int nrWeeks,
         BikeTourPlus btp) {
      // Check if weekAvailableFrom is valid
      if (weekAvailableFrom <= 0 || weekAvailableFrom > btp.getNrWeeks()) {
         return "Available weeks must be within weeks of biking season (1-"+btp.getNrWeeks()+")";
      }

      // Check if weekAvailableUntil is valid
      if (weekAvailableUntil <= 0 || weekAvailableUntil > btp.getNrWeeks()) {
         return "Available weeks must be within weeks of biking season (1-"+btp.getNrWeeks()+")";
      }

      // Check if weekAvailableUntil is after weekAvailableFrom
      if (weekAvailableFrom > weekAvailableUntil) {
         return "Week from which one is available must be less than or equal to the week until which one is available";
      }

      // Check if weekAvailableUntil to weekAvailableFrom is less than the number of
      // available weeks
      if (weekAvailableUntil - weekAvailableFrom + 1 < nrWeeks) {
         return "Number of weeks must be less than or equal to the number of available weeks";
      }

      return "";
   }

   /**
   * Method to get the participants for the choiceBoxes
   * in the UI
   * @return
   */
  public static List<String> getParticipants() {
   return btp.getParticipants().stream().map(Participant::getEmail).toList();
 }

}