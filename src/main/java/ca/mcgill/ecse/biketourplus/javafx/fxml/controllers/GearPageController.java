package ca.mcgill.ecse.biketourplus.javafx.fxml.controllers;

import ca.mcgill.ecse.biketourplus.controller.BikeTourPlusFeatureSet5Controller;
import ca.mcgill.ecse.biketourplus.javafx.fxml.BtpFxmlView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

/**
 * Class that implements the following features in UI:
 * - Add gear
 * - Update gear
 * - Delete gear
 * 
 * @author Walid Aissa
 */
public class GearPageController {
    @FXML
    private TextField gearAddNameTextField;
    @FXML
    private TextField gearAddPriceTextField;
    @FXML
    private TextField gearUpdateNameTextField;
    @FXML
    private TextField gearUpdatePriceTextField;
    @FXML
    private Button addGearButton;
    @FXML
    private Button updateGearButton;
    @FXML
    private Button removeGearButton;
    @FXML
    private ChoiceBox<String> updateGearChoiceBox;
    
	/**
     * Method called when application is initiated
     * 
     * @author Walid Aissa
     */
    @FXML
    public void initialize() {
    	updateGearChoiceBox.addEventHandler(BtpFxmlView.REFRESH_EVENT, e -> {	
    	      updateGearChoiceBox.setItems(ViewUtils.getGears());
    	      updateGearChoiceBox.setValue(null);
    	    });

			updateGearChoiceBox.setOnAction((event) -> {
				String name = updateGearChoiceBox.getValue();
				if (name != null) {
					gearUpdateNameTextField.setText(name);
					gearUpdatePriceTextField.setText(BikeTourPlusFeatureSet5Controller.getPrice(name));
				}
			  });
    	
    		BtpFxmlView.getInstance().registerRefreshEvent(updateGearChoiceBox);
    }
    
	/**
     * Method called when the following button is pressed: add gear button
     * 
     * @author Walid Aissa
     */
    @FXML
    public void addGearClicked(ActionEvent event) {
    	String name = gearAddNameTextField.getText();
    	int price = -1;
    	try {
    		price = Integer.parseInt(gearAddPriceTextField.getText());
    	}
    	catch (NumberFormatException e){
    		ViewUtils.showError("Price must be a valid non-empty integer");
			return;
    	}
    	if(ViewUtils.successful(BikeTourPlusFeatureSet5Controller.addGear(name, price), "Created the gear "+ name)) {
    		gearAddNameTextField.setText("");
    		gearAddPriceTextField.setText("");	
    	}
		BtpFxmlView.getInstance().refresh();
    }
    
	/**
     * Method called when the following button is pressed: update gear button
     * 
     * @author Walid Aissa
     */
    @FXML
    public void updateGearClicked(ActionEvent event) {
    	String oldName = updateGearChoiceBox.getValue();
    	if (oldName == null) {
  	      ViewUtils.showError("Please select a valid gear");
		  return;
    	}
    	String name = gearUpdateNameTextField.getText();
    	int price = -1;
    	
    	try {
    		price = Integer.parseInt(gearUpdatePriceTextField.getText());
    	}
    	catch (NumberFormatException e){
    		ViewUtils.showError("Price must be a valid non-empty integer");
			return;
    	}
    	
    	if(ViewUtils.successful(BikeTourPlusFeatureSet5Controller.updateGear(oldName ,name, price), "Updated the gear "+ name)) {
    		gearUpdateNameTextField.setText("");
    		gearUpdatePriceTextField.setText("");
    		
    	}
    	BtpFxmlView.getInstance().refresh();
    }
    
	/**
     * Method called when the following button is pressed: remove gear button
     * 
     * @author Walid Aissa
     */
    @FXML
    public void removeGearClicked(ActionEvent event) {
    	String gearName = updateGearChoiceBox.getValue();
    	
    	if(gearName == null) {
    		ViewUtils.showError("Please select a valid gear");
        } 
    	else {
    		ViewUtils.successful(BikeTourPlusFeatureSet5Controller.deleteGear(gearName), "Removed the gear "+ gearName);
    		gearUpdateNameTextField.setText("");
    		gearUpdatePriceTextField.setText("");
			BtpFxmlView.getInstance().registerRefreshEvent(updateGearChoiceBox);
			BtpFxmlView.getInstance().refresh();
          
        }
    }
    
    
}
