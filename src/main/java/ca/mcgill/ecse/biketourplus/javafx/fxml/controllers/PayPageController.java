package ca.mcgill.ecse.biketourplus.javafx.fxml.controllers;

import ca.mcgill.ecse.biketourplus.controller.BikeTourPlusBTCreationController;
import ca.mcgill.ecse.biketourplus.javafx.fxml.BtpFxmlView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

/**
 * Class that implements the following features in UI:
 * - Pay for a participant’s trip
 * 
 * @author Karl Bridi
 */
public class PayPageController {
    @FXML
    private TextField participantPaymentTextField;
    @FXML
    private Button confirmPaymentButton;
    @FXML
    private ChoiceBox<String> participantPaymentChoiceBox;

    /**
     * Method called when application is initiated
     * 
     * @author Karl Bridi
     */
    @FXML
    public void initialize() {
        // the choice boxes listen to the refresh event
        participantPaymentChoiceBox.addEventHandler(BtpFxmlView.REFRESH_EVENT, e -> {
            participantPaymentChoiceBox.setItems(ViewUtils.getParticipants());
            // reset the choice
            participantPaymentChoiceBox.setValue(null);
        });

        // let the application be aware of the refreshable node
        BtpFxmlView.getInstance().registerRefreshEvent(participantPaymentChoiceBox);
    }

    /**
	 * Method called when the following button is pressed: pay bike tour button
	 * 
	 * @author Karl Bridi
	 */
    @FXML
    public void confirmPayClicked(ActionEvent event) {
        String email = participantPaymentChoiceBox.getValue();
        String authorizationCode = participantPaymentTextField.getText();

        if (ViewUtils.successful(BikeTourPlusBTCreationController.payForTrip(email, authorizationCode), "Payed $$$$$")) {
            participantPaymentTextField.setText("");
            ViewUtils.callController("");
        }
    }
}