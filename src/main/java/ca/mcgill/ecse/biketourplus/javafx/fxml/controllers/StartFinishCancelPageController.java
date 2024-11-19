package ca.mcgill.ecse.biketourplus.javafx.fxml.controllers;

import ca.mcgill.ecse.biketourplus.controller.BikeTourPlusBTCreationController;
import ca.mcgill.ecse.biketourplus.controller.BikeTourPlusFeatureSet1Controller;
import ca.mcgill.ecse.biketourplus.controller.TOBikeTour;
import ca.mcgill.ecse.biketourplus.javafx.fxml.BtpFxmlView;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

/**
 * Class that implements the following features in UI:
 * - Start all trips for a specific week
 * - Finish a participant’s trip
 * - Cancel a participant’s trip
 * 
 * @author Samer AbdelKarim
 */
public class StartFinishCancelPageController {

    @FXML
    private TextField emailTextField;
    @FXML
    private TextField weekTextField;
    @FXML
    private ChoiceBox<Integer> bikeTourChoiceBox;
    @FXML
    private ChoiceBox<String> participantChoiceBox;
    private String participantChoice;

    /**
     * Method called when application is initiated
     * 
     * @author Samer Abdelkarim
     */
    @FXML
    public void initialize() {
        // the choice boxes listen to the refresh event
        bikeTourChoiceBox.addEventHandler(BtpFxmlView.REFRESH_EVENT, e -> {
            bikeTourChoiceBox.setItems(ViewUtils.getBikeTours());
            bikeTourChoiceBox.setValue(null);
        });

        bikeTourChoiceBox.setOnAction((event -> {
            if (bikeTourChoiceBox.isShowing() && bikeTourChoiceBox.getValue() != null) {
                int bikeTour = bikeTourChoiceBox.getValue();
                TOBikeTour toBikeTour = BikeTourPlusFeatureSet1Controller.getBikeTour(bikeTour);
                participantChoiceBox.setItems(
                        FXCollections.observableList(toBikeTour.participantsEmail(toBikeTour.getParticipantCosts())));
                participantChoiceBox.setValue(null);
            }

        }));

        participantChoiceBox.setOnAction((event -> {
            if (participantChoiceBox.getValue() != null) {
                participantChoice = participantChoiceBox.getValue();

            }
        }));
        BtpFxmlView.getInstance().registerRefreshEvent(bikeTourChoiceBox, participantChoiceBox);
    }

    /**
	 * Method called when the following button is pressed: start bike tour button
	 * 
	 * @author Samer Abdelkarim
	 */
    @FXML
    public void startClicked(ActionEvent event) {
        String weekString = weekTextField.getText();
        try {
            int week = Integer.parseInt(weekString);
            if (week <= 0)
                ViewUtils.showError("Please enter a number larger than 0");
            else
                ViewUtils.successful(BikeTourPlusBTCreationController.startAllTripsForSpecificWeek(week),
                        "Started tour for week" + weekString);

            BtpFxmlView.getInstance().refresh();

        } catch (Exception e) {
            if (weekString.equals(""))
                ViewUtils.showError("Please enter a week before attempting to start trips for a certain week.");
            else if (e instanceof NumberFormatException)
                ViewUtils.showError("Please enter number only, in the format suggested");

        }
    }

    /**
	 * Method called when the following button is pressed: cancel bike tour button
	 * 
	 * @author Samer Abdelkarim
	 */
    @FXML
    public void cancelClicked(ActionEvent event) {
        if (participantChoice != null) {
            String email = participantChoice;
            String cancel = BikeTourPlusBTCreationController.cancelParticipantTrip(email);
            if (!cancel.isEmpty())
                ViewUtils.showError(cancel);
            else {
                participantChoice = null;
                bikeTourChoiceBox.getSelectionModel().clearSelection();
                participantChoiceBox.getSelectionModel().clearSelection();
                ViewUtils.successful("", "Successfully canceled!");
                BtpFxmlView.getInstance().refresh();

            }
        } else
            ViewUtils.showError("Please select both a BikeTour and a Participant");
    }

    /**
	 * Method called when the following button is pressed: finish bike tour button
	 * 
	 * @author Samer Abdelkarim
	 */
    @FXML
    public void finishClicked(ActionEvent event) {
        if (participantChoice != null) {
            String email = participantChoice;
            String finish = BikeTourPlusBTCreationController.finishParticipantTrip(email);
            if (!finish.isEmpty())
                ViewUtils.showError(finish);
            else {
                participantChoice = null;
                bikeTourChoiceBox.getSelectionModel().clearSelection();
                participantChoiceBox.getSelectionModel().clearSelection();
                ViewUtils.successful("", "Successfully finished!");
                BtpFxmlView.getInstance().refresh();

            }
        } else
            ViewUtils.showError("Please select both a BikeTour and a Participant");
    }

}
