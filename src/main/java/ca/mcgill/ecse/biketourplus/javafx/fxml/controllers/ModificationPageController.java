package ca.mcgill.ecse.biketourplus.javafx.fxml.controllers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.sql.Date;

import ca.mcgill.ecse.biketourplus.controller.BikeTourPlusFeatureSet2Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

/**
 * Class that implements the following features in UI:
 * - Update BikeTourPlus general information (biking season and price of guide)
 * 
 * @author Walid Aissa
 */
public class ModificationPageController {
	
	@FXML
	private TextField updateGuideTextField;
	@FXML
	private Button seasonButton;
	@FXML
	private DatePicker startSeasonDatePicker;
	@FXML
	private TextField numberOfWeeksTextField;
	
	/**
	 * Method called when the following button is pressed: update season button
	 * 
	 * @author Walid Aissa
	 */
	@FXML
	public void seasonButtonClicked(ActionEvent event) {
		LocalDate startDate = startSeasonDatePicker.getValue();
		ZoneId defaultZoneId = ZoneId.systemDefault();
		int guidePrice = -1;
		int numberOfWeeks = -1;
		try {
			numberOfWeeks = Integer.parseInt(numberOfWeeksTextField.getText());
			guidePrice = Integer.parseInt(updateGuideTextField.getText());
		}
		catch (NumberFormatException e) {
			ViewUtils.showError("Enter valid non-empty integers.");
			return;
		}
		if (ViewUtils.successful(BikeTourPlusFeatureSet2Controller.updateBikeTourPlus(Date.valueOf(startDate), numberOfWeeks, guidePrice), "Updated the season")) {
			updateGuideTextField.setText("");
			numberOfWeeksTextField.setText("");
			startSeasonDatePicker.setValue(null);
		}
	}
	  
    
}
