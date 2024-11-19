package ca.mcgill.ecse.biketourplus.javafx.fxml.controllers;

import ca.mcgill.ecse.biketourplus.controller.BikeTourPlusFeatureSet2Controller;
import ca.mcgill.ecse.biketourplus.controller.BikeTourPlusFeatureSet6Controller;
import ca.mcgill.ecse.biketourplus.controller.TOBookedItem;
import ca.mcgill.ecse.biketourplus.javafx.fxml.BtpFxmlView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Class that implements the following features in UI:
 * - Add combo
 * - Update combo
 * - Delete combo
 * - Add gear to combo (increases quantity by one)
 * - Remove gear from combo (decreases quantity by one)
 * - See all the current gear in a combo
 * 
 * @author Karl Bridi
 */
public class ComboPageController {
  @FXML
  private TextField comboNameAddTextField;
  @FXML
  private TextField comboDiscountAddTextField;
  @FXML
  private TextField comboNameUpdateTextField;
  @FXML
  private TextField comboDiscountUpdateTextField;
  @FXML
  private Button addComboButton;
  @FXML
  private Button updateComboButton;
  @FXML
  private Button deleteComboButton;
  @FXML
  private Button addGearToComboButton;
  @FXML
  private Button removeGearFromComboButton;
  @FXML
  private ChoiceBox<String> UpdateComboChoiceBox;
  @FXML
  private ChoiceBox<String> UpdateComboGearChoiceBox;
  @FXML
	private TableView gearTable;
  @FXML
	private TableColumn<String, TOBookedItem> cl1;
  @FXML
	private TableColumn<String, TOBookedItem> cl2;

  /**
   * Method called when application is initiated
   * 
   * @author Karl Bridi
   */
  @FXML
  public void initialize() {
    // the choice boxes listen to the refresh event
    UpdateComboChoiceBox.addEventHandler(BtpFxmlView.REFRESH_EVENT, e -> {
      UpdateComboChoiceBox.setItems(ViewUtils.getCombos());
      // reset the choice
      UpdateComboChoiceBox.setValue(null);
    });

    UpdateComboGearChoiceBox.addEventHandler(BtpFxmlView.REFRESH_EVENT, e -> {
      UpdateComboGearChoiceBox.setItems(ViewUtils.getGears());
      UpdateComboGearChoiceBox.setValue(null);
    });

    UpdateComboChoiceBox.setOnAction((event) -> {
      String name = UpdateComboChoiceBox.getValue();
      if (name != null) {
        comboNameUpdateTextField.setText(name);
        comboDiscountUpdateTextField.setText(BikeTourPlusFeatureSet6Controller.getDiscount(name));
        refreshTable();
      }
    });

    // let the application be aware of the refreshable node
    BtpFxmlView.getInstance().registerRefreshEvent(UpdateComboChoiceBox, UpdateComboGearChoiceBox);
  }

  /**
   * Method called when the following button is pressed: add combo button is pressed
   * 
   * @author Karl Bridi
   */
  @FXML
  public void addComboClicked(ActionEvent event) {
    String name = comboNameAddTextField.getText();
    int discount = -1;
    try {
      discount = Integer.parseInt(comboDiscountAddTextField.getText());
    } catch (NumberFormatException e) {
      ViewUtils.showError("Discount must be a valid non-empty integer.");
      return;
    }

    if (ViewUtils.successful(BikeTourPlusFeatureSet6Controller.addCombo(name, discount), "Added the combo " + name)) {
      BtpFxmlView.getInstance().refresh();
      comboNameAddTextField.setText("");
      comboDiscountAddTextField.setText("");
      UpdateComboChoiceBox.setValue(name);
      refreshTable();
      comboDiscountUpdateTextField.setText(BikeTourPlusFeatureSet6Controller.getDiscount(name));
      comboNameUpdateTextField.setText(name);
    }

  }

  /**
   * Method called when the following button is pressed: update combo button is pressed
   * 
   * @author Karl Bridi
   */
  @FXML
  public void updateComboClicked(ActionEvent event) {
    String oldName = UpdateComboChoiceBox.getValue();
    if (oldName == null) {
      ViewUtils.showError("Please select a valid combo");
      return;
    }
    String newName = comboNameUpdateTextField.getText();
    int discount = -1;
    try {
      discount = Integer.parseInt(comboDiscountUpdateTextField.getText());
    } catch (NumberFormatException e) {
      ViewUtils.showError("Discount must be a valid non-empty integer.");
      return;
    }

    if (ViewUtils.successful(BikeTourPlusFeatureSet6Controller.updateCombo(oldName, newName, discount), "Updated the combo "+ newName)) {
      comboNameUpdateTextField.setText("");
      comboDiscountUpdateTextField.setText("");
    }
    BtpFxmlView.getInstance().refresh();
  }

  /**
   * Method called when the following button is pressed: delete combo button is pressed
   * 
   * @author Karl Bridi
   */
  @FXML
  public void deleteComboClicked(ActionEvent event) {
    String combo = UpdateComboChoiceBox.getValue();
    if (combo == null) {
      ViewUtils.showError("Please select a valid combo");
    } else {
      BikeTourPlusFeatureSet2Controller.deleteCombo(combo);
      ViewUtils.makePopupWindow("Succesfully: ", "Deleted the combo "+ combo);
      comboNameUpdateTextField.setText("");
      comboDiscountUpdateTextField.setText("");
    }
    BtpFxmlView.getInstance().refresh();
  }

  /**
   * Method called when the following button is pressed: add gear to combo button is pressed
   * 
   * @author Karl Bridi
   */
  @FXML
  public void addGearToComboClicked(ActionEvent event) {
    String gear = UpdateComboGearChoiceBox.getValue();
    String combo = UpdateComboChoiceBox.getValue();

    if (gear == null || combo == null) {
      ViewUtils.showError("Please select a valid combo and gear");
    } else {
      ViewUtils.successful(BikeTourPlusFeatureSet6Controller.addGearToCombo(gear, combo), "");
      refreshTable();
    }
  }

  /**
   * Method called when the following button is pressed: remove gear from combo button is pressed
   * 
   * @author Karl Bridi
   */
  @FXML
  public void removeGearFromComboClicked(ActionEvent event) {
    String gear = UpdateComboGearChoiceBox.getValue();
    String combo = UpdateComboChoiceBox.getValue();
    if (gear == null || combo == null) {
      ViewUtils.showError("Please select a valid combo and gear");
    } else {
      ViewUtils.successful(BikeTourPlusFeatureSet6Controller.removeGearFromCombo(gear, combo), "");
      refreshTable();
    }
  }

  /**
   * Private helper method that clears the table and put the updated information back
   * 
   * @author Karl Bridi
   */
  private void refreshTable() {
		try {
			gearTable.getItems().clear();

			cl1.setCellValueFactory(new PropertyValueFactory<>("bookedItemName"));
			cl2.setCellValueFactory(new PropertyValueFactory<>("bookedItemQuantity"));

			for (TOBookedItem bookedItem : BikeTourPlusFeatureSet6Controller.getComboBookedItem(UpdateComboChoiceBox.getValue()))
				gearTable.getItems().add(bookedItem);

		} catch (Exception e) {
			ViewUtils.showError("Error");
		}
	}

}