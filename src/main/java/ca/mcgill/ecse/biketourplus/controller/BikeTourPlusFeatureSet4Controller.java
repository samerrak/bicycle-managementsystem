package ca.mcgill.ecse.biketourplus.controller;

import java.util.List;
import ca.mcgill.ecse.biketourplus.model.User;
import ca.mcgill.ecse.biketourplus.persistence.BikeTourPlusPersistence;
import ca.mcgill.ecse.biketourplus.application.BikeTourPlusApplication;
import ca.mcgill.ecse.biketourplus.model.BikeTourPlus;
import ca.mcgill.ecse.biketourplus.model.Guide;

/**
 * Class that is a part of the controller of the BikeTourPlus application
 * 
 * @author Clara Jabbour (for all this class)
 *         Part of this code was taken and adapted from the tutorial wiki pages
 *         (BTMS project)
 */

public class BikeTourPlusFeatureSet4Controller {

   private static BikeTourPlus btp = BikeTourPlusApplication.getBikeTourPlus();

   /**
    * Private constructor to prevent creation of instances of this class
    */
   private BikeTourPlusFeatureSet4Controller() {
   }

   /**
    * Method to register a guide to the system
    * 
    * @param email            email of the guide
    * @param password         password of the guide
    * @param name             name of the guide
    * @param emergencyContact emergency Contact of the guide
    * @return error in case there is one
    */
   public static String registerGuide(String email, String password, String name, String emergencyContact) {

      // check input validity
      String error = "";
      error = checkEmail(email);
      if (!error.isEmpty()) {
         return error;
      }
      // Check name
      if (name == null || name.isEmpty()) {
         error = "Name cannot be empty";
      } else if (password == null || password.isEmpty()) { // check password
         error = "Password cannot be empty";
      } else if (emergencyContact == null || emergencyContact.isEmpty()) { // check emergency contact
         return "Emergency contact cannot be empty";
      } else if (email == null || email.isEmpty()) {
         error = "Email cannot be empty";
      }
      if (!error.isEmpty()) {
         return error;
      }

      try {
         btp.addGuide(email, password, name, emergencyContact);
         BikeTourPlusPersistence.save();
      } catch (RuntimeException e) {
         error = e.getMessage();
         if (error.startsWith("Cannot create due to duplicate email.")) {
            List<Guide> guideList = btp.getGuides();
            for (Guide guide : guideList) {
               if (email.equals(guide.getEmail())) {
                  return "Email already linked to a guide account";
               }
               if (name.equals(guide.getName())) {
                  return "Name already linked to a guide account";
               }
            }
            return "Email already linked to a participant account";
         }
      }

      return "";
   }

   /**
    * Method to update attributes of a guide
    * 
    * @author Clara Jabbour
    * @param email               of the guide to update
    * @param newPassword
    * @param newName
    * @param newEmergencyContact
    * @return an error if any
    */
   public static String updateGuide(String email, String newPassword, String newName, String newEmergencyContact) {
      var error = "";

      // First check if the guide account exists
      if (User.getWithEmail(email) == null) {
         return "The guide account does not exist";
      }

      // validation of input
      if (newName == null || newName.isEmpty()) {
         error = "Name cannot be empty";
      } else if (newPassword == null || newPassword.isEmpty()) {
         error = "Password cannot be empty";
      } else if (newEmergencyContact == null || newEmergencyContact.isEmpty()) {
         return "Emergency contact cannot be empty";
      }
      if (!error.isEmpty()) {
         return error;
      }

      // Actually updating a valid guide
      List<Guide> guideList = btp.getGuides();
      boolean wasUpdated = false;
      for (Guide guide : guideList) {
         if (guide.getEmail().equals(email)) {
            try {
               guide.setPassword(newPassword);
               guide.setName(newName);
               guide.setEmergencyContact(newEmergencyContact);
               BikeTourPlusPersistence.save();
            } catch (Exception e) {
               return e.getMessage();
            }
            wasUpdated = true;
         }
      }

      // There is still the possibility that the guide wasn't in the btp.getGuides
      // list (if the email is of a Participant or the Manager)
      if (!wasUpdated) {
         return "The guide account does not exist";
      }

      return ""; // if everything was successful
   }

   /**
    * Method to delete a guide
    * 
    * @param email
    */
   public static void deleteGuide(String email) {
      try {
         for (Guide guide : btp.getGuides()) {
            if (guide.getEmail().equals(email)) {
               guide.delete();
            }
         }
         BikeTourPlusPersistence.save();
      } catch (RuntimeException ignored) {
      }

   }

   /**
    * Help method to the view controller
    * 
    * @param email
    * @return
    */
   public static String getName(String email) {
      return ((Guide) User.getWithEmail(email)).getName();
   }

   /**
    * Help method to the view controller
    * 
    * @param email
    * @return
    */
   public static String getPassword(String email) {
      return ((Guide) User.getWithEmail(email)).getPassword();
   }

   /**
    * Help method to the view controller
    * 
    * @param email
    * @return
    */
    public static String getContact(String email) {
      return ((Guide) User.getWithEmail(email)).getEmergencyContact();
   }

   /**
   * Method to get the guides for the choiceBoxes
   * in the UI
   * @return
   */
  public static List<String> getGuides() {
   return btp.getGuides().stream().map(Guide::getEmail).toList();
 }

   /**
    * Method to validate email
    *
    * @param email
    * @return
    */
   private static String checkEmail(String email) {

      // Check for empty space
      if (email.contains(" ")) {
         return "Email must not contain any spaces";
      }
      // Check if name is not be empty or null
      if (email == null || email.isEmpty()) {
         return "Email cannot be empty";
      }

      if (!email.contains("@")) {
         return "Invalid email";
      }

      // Check for correct syntax of email
      if (email.indexOf("@") == 0 ||
            email.indexOf("@") != email.lastIndexOf("@") ||
            email.indexOf("@") >= email.lastIndexOf(".") - 1 ||
            email.lastIndexOf(".") >= email.length() - 1) {
         return "Invalid email";
      }

      // Check if email is the managers email
      if (email.equals("manager@btp.com")) {
         return "Email cannot be manager@btp.com";
      }

      return "";
   }

}