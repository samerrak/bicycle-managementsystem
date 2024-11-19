package ca.mcgill.ecse.biketourplus.javafx.fxml.controllers;

import ca.mcgill.ecse.biketourplus.controller.BikeTourPlusFeatureSet2Controller;
import ca.mcgill.ecse.biketourplus.controller.BikeTourPlusFeatureSet3Controller;
import ca.mcgill.ecse.biketourplus.controller.TOBookedItem;
import ca.mcgill.ecse.biketourplus.javafx.fxml.BtpFxmlView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Class that implements the following features in UI:
 * - Register participant 
 * - Update participant
 * - Delete participant
 * - Add gear or combo to participant (increases quantity by one)
 * - Remove gear or combo from participant (decreases quantity by one)
 * - See the gear and combo that the participant currently has
 * 
 * @author Emilien Taisne
 */
public class ParticipantPageController {
	private String loggedInEmail = null;
	@FXML
	ChoiceBox<String> participantChoiceBox;
	@FXML
	private TextField signupEmail;
	@FXML
	private TextField signupName;
	@FXML
	private TextField signupPassword;
	@FXML
	private TextField signupEmergencyContact;
	@FXML
	private TextField startWeekTextFeild;
	@FXML
	private TextField endWeekTextFeild;
	@FXML
	private TextField numberOfWeekTextFeild;
	@FXML
	private Button loginButton;
	@FXML
	private Button logoutButton1;
	@FXML
	private Button addGearOrComboButton;
	@FXML
	private Button removeGearOrComboButton;
	@FXML
	private Button signupButton;
	@FXML
	private Button editInfoButton;
	@FXML
	private Label loggedLabel;
	@FXML
	private CheckBox lodgeCheckBox;
	@FXML
	private ChoiceBox<String> gearAndComboChoiceBox;
	@FXML
	private TableView gearAndComboTable;
	private TableColumn<String, TOBookedItem> cl1 = new TableColumn<>("Name");
	private TableColumn<String, TOBookedItem> cl2 = new TableColumn<>("Quantity");

	/**
     * Method called when application is initiated
     * 
     * @author Emilien Taisne
     */
	@FXML
	public void initialize() {
		gearAndComboChoiceBox.addEventHandler(BtpFxmlView.REFRESH_EVENT, e -> {
			gearAndComboChoiceBox.setItems(ViewUtils.getGearsAndCombos());
			gearAndComboChoiceBox.setValue(null);
		});

		participantChoiceBox.addEventHandler(BtpFxmlView.REFRESH_EVENT, e -> {
			participantChoiceBox.setItems(ViewUtils.getParticipants());
			participantChoiceBox.setValue(null);
		});

		gearAndComboTable.getColumns().add(cl1);
		gearAndComboTable.getColumns().add(cl2);

		// let the application be aware of the refreshable node
		BtpFxmlView.getInstance().registerRefreshEvent(gearAndComboChoiceBox);
		BtpFxmlView.getInstance().registerRefreshEvent(participantChoiceBox);
	}

	/**
	 * Method called when the following button is pressed: login participant button
	 * 
	 * @author Emilien Taisne
	 */
	@FXML
	public void loginClicked(ActionEvent event) {
		try {

			String participantEmail = participantChoiceBox.getValue();

			if (participantEmail.isEmpty()) {
				ViewUtils.showError("Email cannot be empty");
			}

			else if (ViewUtils.successful(
					BikeTourPlusFeatureSet3Controller.loginParticipant(participantEmail), "")) {
				login(participantEmail);
			}
		} catch (Exception e) {
			ViewUtils.showError("Please select a participant first.");

		}
	}

	/**
	 * Method called when the following button is pressed: logout participant button
	 * 
	 * @author Emilien Taisne
	 */
	@FXML
	public void logoutClicked(ActionEvent event) {
		logout();
	}

	/**
	 * Method called when the following button is pressed: add gear or combo to participant button
	 * 
	 * @author Emilien Taisne
	 */
	@FXML
	public void addGearOrComboClicked(ActionEvent event) {
		String gearOrComboName = gearAndComboChoiceBox.getValue();
		if (gearOrComboName == null) {
			ViewUtils.showError("Please select a valid gear or combo");
			return;
		}
		ViewUtils.successful(
				BikeTourPlusFeatureSet3Controller.addBookableItemToParticipant(loggedInEmail, gearOrComboName), "");
		refreshTable();
	}

	/**
	 * Method called when the following button is pressed: remove gear or combo from participant button
	 * 
	 * @author Emilien Taisne
	 */
	@FXML
	public void removeGearOrComboClicked(ActionEvent event) {
		String gearOrComboName = gearAndComboChoiceBox.getValue();
		if (gearOrComboName == null) {
			ViewUtils.showError("Please select a valid gear or combo");
			return;
		}
		ViewUtils.successful(
				BikeTourPlusFeatureSet3Controller.removeBookableItemFromParticipant(loggedInEmail, gearOrComboName),
				"");
		refreshTable();
	}

	/**
	 * Method called when the following button is pressed: signup participant button
	 * 
	 * @author Emilien Taisne
	 */
	@FXML
	public void signupClicked(ActionEvent event) {
		String registerEmail = signupEmail.getText();
		String registerName = signupName.getText();
		String registerPassword = signupPassword.getText();
		String registerEmergencyContact = signupEmergencyContact.getText();
		String startWeekString = startWeekTextFeild.getText();
		String endWeekString = endWeekTextFeild.getText();
		String numberOfWeekString = numberOfWeekTextFeild.getText();
		int numberOfWeek;
		int endWeek;
		int startWeek;
		boolean requireLodge = lodgeCheckBox.isSelected();

		try {
			startWeek = Integer.parseInt(startWeekString);
			endWeek = Integer.parseInt(endWeekString);
			numberOfWeek = Integer.parseInt(numberOfWeekString);
		} catch (Exception e) {
			ViewUtils.showError("Please enter numbers for the fields start week, end week and number of weeks");
			return;
		}

		if (ViewUtils.successful(BikeTourPlusFeatureSet3Controller.registerParticipant(registerEmail, registerPassword,
				registerName, registerEmergencyContact, numberOfWeek, startWeek, endWeek, requireLodge),
				"Created the participant with email " + registerEmail)) {
			{
				login(registerEmail);
			}
		}

	}

	/**
	 * Method called when the following button is pressed: remove participant button
	 * 
	 * @author Emilien Taisne
	 */
	@FXML
	public void removeParticipantClicked(ActionEvent event) {
		BikeTourPlusFeatureSet2Controller.deleteParticipant(loggedInEmail);
		logout();
	}

	/**
	 * Method called when the following button is pressed: update participant button
	 * 
	 * @author Emilien Taisne
	 */
	@FXML
	public void editInfoClicked(ActionEvent event) {
		String newName = signupName.getText();
		String newPassword = signupPassword.getText();
		String newEmergencyContact = signupEmergencyContact.getText();
		String startWeekString = startWeekTextFeild.getText();
		String endWeekString = endWeekTextFeild.getText();
		String numberOfWeekString = numberOfWeekTextFeild.getText();
		int numberOfWeek;
		int endWeek;
		int startWeek;
		boolean requireLodge = lodgeCheckBox.isSelected();

		try {
			startWeek = Integer.parseInt(startWeekString);
			endWeek = Integer.parseInt(endWeekString);
			numberOfWeek = Integer.parseInt(numberOfWeekString);
		} catch (Exception e) {
			ViewUtils.showError("Please enter numbers for the feilds start week, end week and number of weeks");
			return;
		}

		ViewUtils.successful(BikeTourPlusFeatureSet3Controller.updateParticipant(loggedInEmail, newPassword, newName,
				newEmergencyContact, numberOfWeek, startWeek, endWeek, requireLodge),
				"Updated the participant with email " + loggedInEmail);
		BtpFxmlView.getInstance().refresh();
	}

	/**
	* Private helper method that changes the state of the button and fills/clear the text feilds correctly
	* to show that the participant is logged in
	* 
	* @author Emilien Taisne
	*/
	private void login(String email) {
		loggedInEmail = email;
		loggedLabel.setText("You are logged in as " + loggedInEmail);
		signupEmail.setDisable(true);
		loginButton.setDisable(false);
		addGearOrComboButton.setDisable(false);
		removeGearOrComboButton.setDisable(false);
		signupButton.setDisable(true);
		editInfoButton.setDisable(false);
		signupName.setText(BikeTourPlusFeatureSet3Controller.getName(email));
		signupPassword.setText(BikeTourPlusFeatureSet3Controller.getPassword(email));
		signupEmail.setText(email);
		startWeekTextFeild.setText(BikeTourPlusFeatureSet3Controller.getStartWeek(email));
		endWeekTextFeild.setText(BikeTourPlusFeatureSet3Controller.getWeekUntil(email));
		numberOfWeekTextFeild.setText(BikeTourPlusFeatureSet3Controller.getNrWeeks(email));
		signupEmergencyContact.setText(BikeTourPlusFeatureSet3Controller.getContact(email));
		BtpFxmlView.getInstance().refresh();
		refreshTable();

	}

	/**
	* Private helper method that changes the state of the button and fills/clear the text feilds correctly
	* to show that the participant is logged out
	* 
	* @author Emilien Taisne
	*/
	private void logout() {
		signupName.setText("");
		signupPassword.setText("");
		signupEmergencyContact.setText("");
		startWeekTextFeild.setText("");
		endWeekTextFeild.setText("");
		numberOfWeekTextFeild.setText("");
		signupEmail.setText("");
		lodgeCheckBox.setSelected(false);

		loggedInEmail = null;
		loggedLabel.setText("You are not logged in");
		signupEmail.setDisable(false);
		loginButton.setDisable(false);
		addGearOrComboButton.setDisable(true);
		removeGearOrComboButton.setDisable(true);
		signupButton.setDisable(false);
		editInfoButton.setDisable(true);
		BtpFxmlView.getInstance().refresh();
		gearAndComboTable.getItems().clear();
	}

	/**
	* Private helper method that clears the table and put the updated information back
	* 
	* @author Emilien Taisne
	*/
	private void refreshTable() {
		try {
			gearAndComboTable.getItems().clear();

			cl1.setCellValueFactory(new PropertyValueFactory<>("bookedItemName"));
			cl2.setCellValueFactory(new PropertyValueFactory<>("bookedItemQuantity"));

			for (TOBookedItem bookedItem : BikeTourPlusFeatureSet3Controller.getParticipantBookedItem(loggedInEmail))
				gearAndComboTable.getItems().add(bookedItem);

		} catch (Exception e) {
			ViewUtils.showError("Error");
		}
	}

}
