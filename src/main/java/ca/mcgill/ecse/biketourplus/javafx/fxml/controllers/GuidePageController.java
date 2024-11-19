package ca.mcgill.ecse.biketourplus.javafx.fxml.controllers;

import ca.mcgill.ecse.biketourplus.controller.BikeTourPlusFeatureSet4Controller;
import ca.mcgill.ecse.biketourplus.javafx.fxml.BtpFxmlView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

/**
 * Class that implements the following features in UI:
 * - Register guide 
 * - Update guide
 * - Delete guide
 * 
 * @author Sami Bayoud and Clara Jabbour
 */
public class GuidePageController {

    @FXML
    private TextField guideRegistrationNameTextField;
    @FXML
    private TextField guideRegistrationEmailTextField;
    @FXML
    private TextField guideRegistrationContactTextField;
    @FXML
    private TextField guideRegistrationPasswordTextField;
    @FXML
    private Button confirmChangesButton;
    @FXML
    private Button deleteGuideButton;
    @FXML
    private Button guideSignUpButton;
    @FXML
    private TextField guideUpdatePasswordTextField;
    @FXML
    private TextField guideUpdateContactTextField;
    @FXML
    private TextField guideUpdateNameTextField;
    @FXML
    private ChoiceBox<String> guideUpdateChoiceBox;

    /**
     * Method called when application is initiated
     * 
     * @author Sami Bayoud and Clara Jabbour
     */
    @FXML
    public void initialize() {
        // the choice boxes listen to the refresh event
        guideUpdateChoiceBox.addEventHandler(BtpFxmlView.REFRESH_EVENT, e -> {
            guideUpdateChoiceBox.setItems(ViewUtils.getGuides());
            // reset the choice
            guideUpdateChoiceBox.setValue(null);
        });

        guideUpdateChoiceBox.setOnAction((event) -> {
            String email = guideUpdateChoiceBox.getValue();
            if (email != null) {
                guideUpdateNameTextField.setText(BikeTourPlusFeatureSet4Controller.getName(email));
                guideUpdatePasswordTextField.setText(BikeTourPlusFeatureSet4Controller.getPassword(email));
                guideUpdateContactTextField.setText(BikeTourPlusFeatureSet4Controller.getContact(email));
            }
        });

        // let the application be aware of the refreshable node
        BtpFxmlView.getInstance().registerRefreshEvent(guideUpdateChoiceBox);
    }

    /**
     * Method called when the following button is pressed: sign up guide button
     * 
     * @author Sami Bayoud and Clara Jabbour
     */
    @FXML
    public void guideSignUpClicked(ActionEvent event) {
        String name = guideRegistrationNameTextField.getText();
        String email = guideRegistrationEmailTextField.getText();
        String password = guideRegistrationPasswordTextField.getText();
        String emergencycontact = guideRegistrationContactTextField.getText();

        if (ViewUtils
                .successful(BikeTourPlusFeatureSet4Controller.registerGuide(email, password, name, emergencycontact), "Created the guide with email "+ email)) {
            guideRegistrationNameTextField.setText("");
            guideRegistrationEmailTextField.setText("");
            guideRegistrationPasswordTextField.setText("");
            guideRegistrationContactTextField.setText("");
        }
        BtpFxmlView.getInstance().refresh();
    }

    /**
     * Method called when the following button is pressed: update guide button
     * 
     * @author Sami Bayoud and Clara Jabbour
     */
    @FXML
    public void confirmChangesClicked(ActionEvent event) {
        String email = guideUpdateChoiceBox.getValue();
        if (email == null) {
            ViewUtils.showError("Please select a valid guide");
            return;
        }
        String newPassword = guideUpdatePasswordTextField.getText();
        String newName = guideUpdateNameTextField.getText();
        String newContact = guideUpdateContactTextField.getText();

        if (ViewUtils
                .successful(BikeTourPlusFeatureSet4Controller.updateGuide(email, newPassword, newName, newContact), "Updated the guide with email "+ email)) {
            guideUpdateNameTextField.setText("");
            guideUpdatePasswordTextField.setText("");
            guideUpdateContactTextField.setText("");
            BtpFxmlView.getInstance().refresh();
        }
    }

    /**
     * Method called when the following button is pressed: delete guide button
     * 
     * @author Sami Bayoud and Clara Jabbour
     */
    @FXML
    public void deleteGuideClicked(ActionEvent event) {
        String email = guideUpdateChoiceBox.getValue();
        if (email == null) {
            ViewUtils.showError("Please select a valid guide");
        } else {
            BikeTourPlusFeatureSet4Controller.deleteGuide(email);
            guideUpdateNameTextField.setText("");
            guideUpdatePasswordTextField.setText("");
            guideUpdateContactTextField.setText("");
        }
        BtpFxmlView.getInstance().refresh();
    }
}