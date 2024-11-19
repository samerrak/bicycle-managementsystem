package ca.mcgill.ecse.biketourplus.controller;

import ca.mcgill.ecse.biketourplus.application.BikeTourPlusApplication;
import ca.mcgill.ecse.biketourplus.model.*;
import ca.mcgill.ecse.biketourplus.persistence.BikeTourPlusPersistence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class that is part of the Controller of the BikeTourPlus application
 * It implements the necessary API methods to create a bike tour (by the
 * manager)
 * 
 * Part of this code was taken and adapted from the tutorial wiki pages
 * (BTMS project)
 */
public class BikeTourPlusBTCreationController {

  private static BikeTourPlus btp = BikeTourPlusApplication.getBikeTourPlus();

  /**
   * Private constructor to prevent creation of instances of this class
   */
  private BikeTourPlusBTCreationController() {
  }

  /**
   * Method to initiate a bike tours creation based on the guides and the
   * participants that are available in the system.
   *
   * @author Samer Abdulkarim
   * @return error in case there is one
   */
  public static String initiateBikeToursCreation() {
    List<Guide> unAssignedGuides = btp.getGuides();
    List<Participant> unAssignedParticipants = btp.getParticipants();

    //Extra code to cover edge cases
    if (unAssignedGuides.isEmpty())
      return "A guide must exist.";
    if (unAssignedParticipants.isEmpty())
      return "A participant must exist.";
    
    try {
      for (Guide guide : unAssignedGuides) {
        for (Participant participant : unAssignedParticipants) {
          //Makes sure that the participant hasn't been assigned yet
          if (participant.getBikeTour() == null) {
            participant.assign(guide);
          }
        }
      }
      BikeTourPlusPersistence.save();
    } catch (Exception e) {
      return e.getMessage();
    }

    //Check if a participant was not assigned
    for (Participant p : unAssignedParticipants) {
      if (p.getBikeTour() == null)
        return "At least one participant could not be assigned to their bike tour";
    }

    return "";
  }

  /**
   * Method to allow a participant to pay for a trip.
   * 
   * @author Sami Bayoud
   * @param email
   * @param authorizationCode
   * @return error in case there is one
   */
  public static String payForTrip(String email, String authorizationCode) { // sami
    Participant p = btp.getParticipantByEmail(email);

    //Make sure that the participant exists
    if (p == null) {
      return "Participant with email address " + email + " does not exist";
    }

    try {
      p.payForTrip(authorizationCode);
      BikeTourPlusPersistence.save();
    } catch (Exception e) {
      return e.getMessage();
    }

    return "";
  }

  /**
   * Method to start a trip for participants for a specific week
   * 
   * @author Clara Jabbour
   * @param week
   * @return error in case there is one
   */
  public static String startAllTripsForSpecificWeek(int week) {

    List<BikeTour> btour = btp.getBikeTours();

    try {
      for (BikeTour biketour : btour) {

        if (biketour.getStartWeek() == week) {
          List<Participant> participants = biketour.getParticipants();
          for (Participant p : participants) {
            p.startBikeTour();
          }
        }
      }
      BikeTourPlusPersistence.save();
    } catch (Exception e) {
      return e.getMessage();
    }

    return "";
  }

  /**
   * Method to put an end to a participant's trip in the BikeTourPlus application.
   * 
   * @author Walid Aissa
   * @param email : the email of the participant
   * @return error in case there is one
   */
  public static String finishParticipantTrip(String email) {

    Participant p = btp.getParticipantByEmail(email);

    //Make sure that the participant exists
    if (p == null) {
      return "Participant with email address " + email + " does not exist";
    }

    try {
      p.finishBikeTour();
      BikeTourPlusPersistence.save();
    } catch (RuntimeException e) {
      return e.getMessage();
    }

    return "";
  }

  /**
   * Method to cancel and in some cases refund a participant's trip in the
   * BikeTourPlus application.
   *
   * @author Samer Abdulkarim
   * @param email : the email of the participant
   * @return error in case there is one
   */
  public static String cancelParticipantTrip(String email) {

    Participant p = btp.getParticipantByEmail(email);

    //Make sure that the participant exists
    if (p == null) {
      return "Participant with email address " + email + " does not exist";
    }

    try {
      p.cancelTrip();
      BikeTourPlusPersistence.save();
    } catch (RuntimeException e) {
      return e.getMessage();
    }

    return "";
  }


  public static List<TOUser> getUsersOfSystem(){
    ArrayList<TOUser> usersOfSystem = new ArrayList<TOUser>();
    for (Participant participant : btp.getParticipants()) {
      usersOfSystem.add(new TOUser(
        participant.getEmail(),
        participant.getName(),
        "Participant",
        participant.getStatusFullName(),
        participant.getWeekAvailableFrom(),
        participant.getWeekAvailableUntil(),
        participant.getNrWeeks(),
        participant.isLodgeRequired()));
    }
    for (Guide guide : btp.getGuides()) {
      usersOfSystem.add(new TOUser(
        guide.getEmail(),
        guide.getName(),
        "Guide"));
    }
    return usersOfSystem;
  }
}