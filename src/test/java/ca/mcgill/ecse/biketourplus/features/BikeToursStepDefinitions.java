package ca.mcgill.ecse.biketourplus.features;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import ca.mcgill.ecse.biketourplus.application.BikeTourPlusApplication;
import ca.mcgill.ecse.biketourplus.controller.BikeTourPlusBTCreationController;
import ca.mcgill.ecse.biketourplus.model.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.jupiter.api.Assertions.*;

public class BikeToursStepDefinitions {
  private static BikeTourPlus btp = BikeTourPlusApplication.getBikeTourPlus();
  private String error;
  private int errorCntr;

  /**
   * Given step definition to make sure that certain guides are present in the
   * system
   * as required by the gherkin feature files
   *
   * @author Samer Abdulkarim
   * @param dataTable
   */
  @Given("the following BikeTourPlus system exists:")
  public void the_following_bike_tour_plus_system_exists(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps();
    for (var row : rows) {
      Date startDate = Date.valueOf(row.get("startDate"));
      int nrWeeks = Integer.parseInt(row.get("nrWeeks"));
      int weeklyGuidePrice = Integer.parseInt(row.get("priceOfGuidePerWeek"));
      btp.setStartDate(startDate);
      btp.setNrWeeks(nrWeeks);
      btp.setPriceOfGuidePerWeek(weeklyGuidePrice);
    }
    // For other transformations you can register a DataTableType.
  }

  /**
   * Given step definition to make sure that certain guides are present in the
   * system
   * as required by the gherkin feature files
   * 
   * @author Karl Bridi
   * @param dataTable
   */
  @Given("the following guides exist in the system:")
  public void the_following_guides_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {

    List<Map<String, String>> rows = dataTable.asMaps();

    for (var row : rows) {
      String email = row.get("email");
      String password = row.get("password");
      String name = row.get("name");
      String emergencyContact = row.get("emergencyContact");
      btp.addGuide(email, password, name,
          emergencyContact);
    }
  }

  /**
   * Given step definition to make sure that certain participants are present in
   * the system
   * as required by the gherkin feature files
   * 
   * @author Walid Aissa
   * @param dataTable
   */
  @Given("the following participants exist in the system:")
  public void the_following_participants_exist_in_the_system(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps();
    for (var row : rows) {
      String email = row.get("email");
      String password = row.get("password");
      String name = row.get("name");
      String emergencyContact = row.get("emergencyContact");
      int numberOfWeeks = Integer.parseInt(row.get("nrWeeks"));
      int weekAvailableFrom = Integer.parseInt(row.get("weeksAvailableFrom"));
      int weekAvailableUntil = Integer.parseInt(row.get("weeksAvailableUntil"));
      boolean lodgeRequired = Boolean.parseBoolean(row.get("lodgeRequired"));
      new Participant(email, password, name, emergencyContact, numberOfWeeks, weekAvailableFrom, weekAvailableUntil,
          lodgeRequired, null, 0, btp);
    }
  }

  /**
   * When step definition that initiates the bike tour
   * 
   * @author Emilien Taisne
   */
  @When("the administrator attempts to initiate the bike tour creation process")
  public void the_administrator_attempts_to_initiate_the_bike_tour_creation_process() {
    callController(BikeTourPlusBTCreationController.initiateBikeToursCreation());
  }

  /**
   * Then step definition to verify that a bike tour exists in the system.
   * 
   * @author Sami Bayoud
   * @param dataTable
   */
  @Then("the following bike tours shall exist in the system:")
  public void the_following_bike_tours_shall_exist_in_the_system(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps();
    for (var row : rows) {
      int id = Integer.parseInt(row.get("id"));
      int startWeek = Integer.parseInt(row.get("startWeek"));
      int endWeek = Integer.parseInt(row.get("endWeek"));
      Guide guide = (Guide) Guide.getWithEmail(row.get("guide"));
      BikeTour bikeTour = BikeTour.getWithId(id);
      assertNotNull(bikeTour);
      assertEquals(startWeek, bikeTour.getStartWeek());
      assertEquals(endWeek, bikeTour.getEndWeek());
      assertEquals(guide, bikeTour.getGuide());
      List<Participant> participantsReal = bikeTour.getParticipants();
      assertEquals(row.get("participants").split(",").length, participantsReal.size());
      for (int i = 0; i < row.get("participants").split(",").length; i++) {
        assertTrue(
            participantsReal.contains((Participant) Participant.getWithEmail(row.get("participants").split(",")[i])));
      }
    }

  }

  /**
   * Then step definition to make sure that a participant with email of type string is marked as a string
   * 
   * @author Clara Jabbour
   * @param string
   * @param string2
   */
  @Then("the participant with email {string} shall be marked as {string}")
  public void the_participant_with_email_shall_be_marked_as(String string, String string2) {
    Participant participant = btp.getParticipantByEmail(string);
    assertNotNull(participant);
    assertEquals(string2, participant.getStatusFullName());
  }

  /**
   * Then step to verify that the number of bike tours shall be <string>.
   *
   * @author Samer Abdulkarim
   * @param string string: the number of bike tours
   */

  @Then("the number of bike tours shall be {string}")
  public void the_number_of_bike_tours_shall_be(String string) {
    int size = btp.getBikeTours().size();
    assertEquals(size, Integer.parseInt(string));
  }

  /**
   * Then step to verify that the error <string> has been caught by the system.
   * 
   * @author Walid Aissa
   * @param string string: the given error
   */
  @Then("the system shall raise the error {string}")
  public void the_system_shall_raise_the_error(String string) {
    assertEquals(string, error);
  }

  /**
   * Given step definition to make sure that certain pieces of gear are present in
   * the system
   * as required by the gherkin feature files
   * 
   * @author Karl Bridi
   * @param dataTable
   */
  @Given("the following pieces of gear exist in the system:")
  public void the_following_pieces_of_gear_exist_in_the_system(
      io.cucumber.datatable.DataTable dataTable) {

    List<Map<String, String>> rows = dataTable.asMaps();

    for (var row : rows) {
      String name = row.get("name");
      int pricePerWeek = Integer.parseInt(row.get("pricePerWeek"));
      new Gear(name, pricePerWeek, btp);
    }
  }

  // Emilien
  @Given("the participant with email {string} has cancelled their tour")
  public void the_participant_with_email_has_cancelled_their_tour(String string) {
    Participant participant = btp.getParticipantByEmail(string);
    if (participant != null) {
      participant.cancelTrip();
    }
  }

  /**
   * Given step definition to make sure that a specific combo exists in the system
   * 
   * @author Sami Bayoud
   * @param dataTable
   */
  @Given("the following combos exist in the system:")
  public void the_following_combos_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps();
    for (var row : rows) {
      String name = row.get("name");
      int discount = Integer.parseInt(row.get("discount"));
      Combo combo = new Combo(name, discount, btp);
      for (int i = 0; i < row.get("items").split(",").length; i++) {
        combo.addComboItem(Integer.parseInt(row.get("quantity").split(",")[i]), btp,
            (Gear) BookableItem.getWithName(row.get("items").split(",")[i]));
      }
    }
  }

  /**
   * Given step definition to make sure that participants request the gears they
   * want
   * 
   * @author Clara Jabbour
   * @param dataTable
   */
  @Given("the following participants request the following pieces of gear:")
  public void the_following_participants_request_the_following_pieces_of_gear(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps();
    for (var row : rows) {
      String email = row.get("email");
      String gearpieces = row.get("gear");
      int quantity = Integer.parseInt(row.get("quantity"));
      Participant participant = btp.getParticipantByEmail(email);
      Gear gear = (Gear) BookableItem.getWithName(gearpieces);
      participant.addBookedItem(quantity, btp, gear);
    }
  }

  /**
   * Given step definition to make sure that participants request the combos they
   * want in system
   * as required by the gherkin feature files
   * 
   * @author Andrew Kan/Samer Abdulkarim
   * @param dataTable
   */
  @Given("the following participants request the following combos:")
  public void the_following_participants_request_the_following_combos(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps();
    for (var row : rows) {
      String email = row.get("email");
      String comboName = row.get("gear");
      int quantity = Integer.parseInt(row.get("quantity"));
      Participant participant = btp.getParticipantByEmail(email);
      Combo combo = (Combo) BookableItem.getWithName(comboName);
      participant.addBookedItem(quantity, btp, combo);
    }
  }

  /**
   * Given step definition to make sure that the participant with email <email> is
   * banned
   * as required by the gherkin feature files
   * 
   * @author Walid Aissa
   * @param string string : email of the given participant
   */
  @Given("the participant with email {string} is banned")
  public void the_participant_with_email_is_banned(String string) {
    Participant p = btp.getParticipantByEmail(string);
    if (p != null) {
      p.assign(btp.getGuide(0));
      p.startBikeTour();
    }
  }

  /**
   * Given step definition to make sure that certain participants have started
   * their tour
   * as required by the gherkin feature files
   * 
   * @author Karl Bridi
   * @param string : email of the given participant
   */
  @Given("the participant with email {string} has started their tour")
  public void the_participant_with_email_has_started_their_tour(String string) {
    Participant participant = btp.getParticipantByEmail(string);
    if (participant != null) {
      participant.assign(btp.getGuide(0));
      participant.payForTrip("Payup");
      participant.startBikeTour();
    }
  }

  // Emilien
  @Given("the participant with email {string} has paid for their tour")
  public void the_participant_with_email_has_paid_for_their_tour(String string) {
    Participant participant = btp.getParticipantByEmail(string);
    if (participant != null) {
      participant.assign(btp.getGuide(0));
      participant.payForTrip("Payup");
    }
  }

  /**
   * Given step definition to make sure that a certain bike tour exists in the
   * system.
   * 
   * @author Sami Bayoud
   * @param dataTable
   */
  @Given("the following bike tours exist in the system:")
  public void the_following_bike_tours_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {

    List<Map<String, String>> rows = dataTable.asMaps();
    for (var row : rows) {
      int id = Integer.parseInt(row.get("id"));
      int startWeek = Integer.parseInt(row.get("startWeek"));
      int endWeek = Integer.parseInt(row.get("endWeek"));
      Guide guide = (Guide) Guide.getWithEmail(row.get("guide"));
      new BikeTour(id, startWeek, endWeek, guide, btp);
      for (int i = 0; i < row.get("participants").split(",").length; i++) {
        Participant p = (Participant) Participant.getWithEmail(row.get("participants").split(",")[i]);
        p.assign(guide);
      }
    }
  }

  /**
   * Given step definition to make sure that a participant with email string
   * finished their tour
   * 
   * @author Clara Jabbour
   * @param string
   */
  @Given("the participant with email {string} has finished their tour")
  public void the_participant_with_email_has_finished_their_tour(String string) {
    // Write code here that turns the phrase above into concrete actions
    Participant participant = btp.getParticipantByEmail(string);
    if (participant != null) {
      participant.assign(btp.getGuide(0));
      participant.payForTrip("Payup");
      participant.startBikeTour();
      participant.finishBikeTour();
    }
  }

  // Samer
  @When("the manager attempts to cancel the tour for email {string}")
  public void the_manager_attempts_to_cancel_the_tour_for_email(String string) {
    callController(BikeTourPlusBTCreationController.cancelParticipantTrip(string));
  }

  /**
   * When step that uses the controller to finish the trip of the participant with
   * email <email>
   * when the manager tries to.
   * 
   * @author Walid Aissa
   * @param string string : email of the given participant
   */
  @When("the manager attempts to finish the tour for the participant with email {string}")
  public void the_manager_attempts_to_finish_the_tour_for_the_participant_with_email(
      String string) {
    callController(BikeTourPlusBTCreationController.finishParticipantTrip(string));
  }

  /**
   * When step definition that triggers the cancellation process of a tour for a
   * certain participant
   * as required by the gherkin feature files
   *
   * @author Samer Abdulkarim
   * @param string : email of the participant
   */
  @When("the manager attempts to start the tours for week {string}")
  public void the_manager_attempts_to_start_the_tours_for_week(String string) {
    callController(BikeTourPlusBTCreationController.startAllTripsForSpecificWeek(Integer.parseInt(string)));
  }

  /**
   * When step definition that triggers the payment process of a certain
   * participant
   * as required by the gherkin feature files
   * 
   * @author Karl Bridi
   * @param string  : email of the participant
   * @param string2 : authorization code for the payment process
   */
  @When("the manager attempts to confirm payment for email {string} using authorization code {string}")
  public void the_manager_attempts_to_confirm_payment_for_email_using_authorization_code(
      String string, String string2) {
    callController(BikeTourPlusBTCreationController.payForTrip(string, string2));
  }

  /**
   * Then step definition to make sure a participant with a specific email shall
   * not exist
   * 
   * @auhtor Clara Jabbour
   * @param string
   */
  @Then("a participant account shall not exist with email {string}")
  public void a_participant_account_shall_not_exist_with_email(String string) {
    assertNull(Participant.getWithEmail(string));
  }

  /**
   * Then step definition to make sure that the number of participants is equal to
   * <string>
   * as required by the gherkin feature files
   *
   * @author Samer Abdulkarim
   * @param string : Number of participants
   */
  @Then("the number of participants shall be {string}")
  public void the_number_of_participants_shall_be(String string) {
    assertEquals(btp.getParticipants().size(), Integer.parseInt(string));
  }

  /**
   * Then step definition to make sure that the participant with email {string} exists and that this participant has a refund percentage of {string}
   * as required by the gherkin feature files
   *
   * @author Emilien Taisne
   * @param string : email of the paticiapant
   * @param string2 : refund percentage
   */
  @Then("a participant account shall exist with email {string} and a refund of {string} percent")
  public void a_participant_account_shall_exist_with_email_and_a_refund_of_percent(String string,
      String string2) {
    Participant participant = btp.getParticipantByEmail(string);
    assertNotNull(participant);
    assertEquals(Integer.parseInt(string2), participant.getRefundedPercentageAmount());
  }

  /**
   * Then step definition to make sure that a certain participant exists and has a
   * certain authorization code
   * as required by the gherkin feature files
   * 
   * @author Karl Bridi
   * @param string  : email of the participant
   * @param string2 : authorization code for the payment process
   */
  @Then("a participant account shall exist with email {string} and authorization code {string}")
  public void a_participant_account_shall_exist_with_email_and_authorization_code(String string,
      String string2) {
    Participant participant = btp.getParticipantByEmail(string);
    assertNotNull(participant);
    assertEquals(string2, participant.getAuthorizationCode());
  }

  /**
   * Helper method to register the error code when a call from the controller is issued
   * @param result
   */
  private void callController(String result) {
    if (!result.isEmpty()) {
      error = result;
      errorCntr += 1;
    }
  }
}
