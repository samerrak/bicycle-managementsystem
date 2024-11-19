package ca.mcgill.ecse.biketourplus.controller;

import ca.mcgill.ecse.biketourplus.application.BikeTourPlusApplication;
import ca.mcgill.ecse.biketourplus.model.*;
import ca.mcgill.ecse.biketourplus.persistence.BikeTourPlusPersistence;

import java.sql.Date;

/**
 * Class that is part of the Controller of the BikeTourPlus application
 * 
 * @author Samer Abdelkarim (for all this class)
 *         Part of this code was taken and adapted from the tutorial wiki pages
 *         (BTMS project)
 */
public class BikeTourPlusFeatureSet2Controller {

   private static BikeTourPlus btp = BikeTourPlusApplication.getBikeTourPlus();

   /**
    * Method that updates the system (BikeTourPlus)
    *
    * @author Samer AbdelKarim
    * @param startDate
    * @param nrWeeks
    * @param priceOfGuidePerWeek
    * @return
    */
   public static String updateBikeTourPlus(Date startDate, int nrWeeks, int priceOfGuidePerWeek) {
      // input validation
      var error = "";
      if (nrWeeks < 0) {
         error = "The number of riding weeks must be greater than or equal to zero";
      }
      if (priceOfGuidePerWeek < 0) {
         error = "The price of guide per week must be greater than or equal to zero";
      }
      if (startDate == null) {
         error = "A start date must be specified to update the tour";
      }
      if (startDate.getYear() < btp.getStartDate().getYear()) { // fixed condition.
         error = "The start date cannot be from previous year or earlier";
      }
      if (!error.isEmpty()) {
         return error.trim();
      }
      try {
         btp.setNrWeeks(nrWeeks);
         btp.setStartDate(startDate);
         btp.setPriceOfGuidePerWeek(priceOfGuidePerWeek);
         BikeTourPlusPersistence.save();
      } catch (RuntimeException e) {
         return e.getMessage();
      }
      return error;
   }

   /**
    * Method that deletes a certain participant
    *
    * @author Samer AbdelKarim
    * @param email
    */
   public static void deleteParticipant(String email) {
      try {
         for (Participant participant : btp.getParticipants()) {
            if (participant.getEmail().equals(email))
               participant.delete();
         }
         BikeTourPlusPersistence.save();
      } catch (RuntimeException ignored) { // allows for more continuity if user enters null
      }
   }

   /**
    * Method that deletes a combo
    * 
    * @author Samer AbdelKarim
    * @param name
    */
   public static void deleteCombo(String name) {
      try {
         for (Combo combo : btp.getCombos()) {
            if (combo.getName().equals(name))
               combo.delete(); // this will also delete comboItem due to association.
         }
         BikeTourPlusPersistence.save();
      } catch (RuntimeException ignored) { // allows for more continuity if user enters null
      }

   }
}