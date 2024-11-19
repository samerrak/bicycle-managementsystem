package ca.mcgill.ecse.biketourplus.javafx.fxml.controllers;

import ca.mcgill.ecse.biketourplus.controller.BikeTourPlusFeatureSet1Controller;
import ca.mcgill.ecse.biketourplus.controller.TOBikeTour;
import ca.mcgill.ecse.biketourplus.controller.TOParticipantCost;
import ca.mcgill.ecse.biketourplus.javafx.fxml.BtpFxmlView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
/**
 * Class that implements the following features in UI:
 * - Update manager password
 * - View bike tours including their participants and guides
 * 
 * @author Samer Abdelkarim
 */
public class ViewBikeToursAndManagerPageController {

    @FXML
    private TableView overviewTable;
    @FXML
    private ChoiceBox<Integer> bikeTourChoiceBox;
    @FXML
    private TextField changePasswordTextField;
    @FXML
    private TextField endWeekTextField;
    @FXML
    private TextField guideCostTextField;
    @FXML
    private TextField guideEmailTextField;
    @FXML
    private TextField guideNameTextField;
    @FXML
    private TextField startWeekTextField;
    private TableColumn<String, TOParticipantCost> cl1 = new TableColumn<>("Email");
    private TableColumn<String, TOParticipantCost> cl2 = new TableColumn<>("Name");
    private TableColumn<String, TOParticipantCost> cl3 = new TableColumn<>("Gear/Combo Cost");
    private TableColumn<String, TOParticipantCost> cl4 = new TableColumn<>("Total Cost");
    private TableColumn<String, TOParticipantCost> cl5 = new TableColumn<>("Status");
    private TableColumn<String, TOParticipantCost> cl6 = new TableColumn<>("Authorization Code");
    private TableColumn<String, TOParticipantCost> cl7 = new TableColumn<>("Refund");

    /**
     * Method called when application is initiated
     * 
     * @author Samer Abdelkarim
     */
    @FXML
    public void initialize() {
        bikeTourChoiceBox.addEventHandler(BtpFxmlView.REFRESH_EVENT, e -> {
            bikeTourChoiceBox.setItems(ViewUtils.getBikeTours());
            bikeTourChoiceBox.setValue(null);
        });

        overviewTable.getColumns().add(cl1);
        overviewTable.getColumns().add(cl2);
        overviewTable.getColumns().add(cl3);
        overviewTable.getColumns().add(cl4);
        overviewTable.getColumns().add(cl5);
        overviewTable.getColumns().add(cl6);
        overviewTable.getColumns().add(cl7);
        overviewTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        BtpFxmlView.getInstance().registerRefreshEvent(bikeTourChoiceBox);

    }

    /**
     * Method called when the following button is pressed: select bike tour button is pressed
     * 
     * @author Samer Abdelkarim
     */
    @FXML
    void confirmBikeTourClicked(ActionEvent event) {
        try {
            TOBikeTour selected = BikeTourPlusFeatureSet1Controller
                    .getBikeTour(bikeTourChoiceBox.getSelectionModel().getSelectedItem());
            overviewTable.getItems().clear();
            startWeekTextField.setText(Integer.toString(selected.getStartWeek()));
            endWeekTextField.setText(Integer.toString(selected.getEndWeek()));
            guideCostTextField.setText(Integer.toString(selected.getTotalCostForGuide()));
            guideEmailTextField.setText(selected.getGuideEmail());
            guideNameTextField.setText(selected.getGuideName());
            cl1.setCellValueFactory(new PropertyValueFactory<>("participantEmail"));
            cl2.setCellValueFactory(new PropertyValueFactory<>("participantName"));
            cl3.setCellValueFactory(new PropertyValueFactory<>("totalCostForBookableItems"));
            cl4.setCellValueFactory(new PropertyValueFactory<>("totalCostForBikingTour"));
            cl5.setCellValueFactory(new PropertyValueFactory<>("status"));
            cl6.setCellValueFactory(new PropertyValueFactory<>("authorizationCode"));
            cl7.setCellValueFactory(new PropertyValueFactory<>("refundedPercentageAmount"));

            for (TOParticipantCost p : selected.getParticipantCosts())
                overviewTable.getItems().add(p);

            BtpFxmlView.getInstance().refresh();
        } catch (Exception e) {
            ViewUtils.showError("Please select a Biketour first from the select menu.");
        }

    }

    /**
     * Method called when the following button is pressed: update manager password button is pressed
     * 
     * @author Samer Abdelkarim
     */
    @FXML
    public void confirmButtonClicked(ActionEvent actionEvent) {
        String newPassword = changePasswordTextField.getText();
        if (ViewUtils.successful(BikeTourPlusFeatureSet1Controller.updateManager(newPassword),
                "Updated manager password")) {
            BtpFxmlView.getInstance().refresh();
            changePasswordTextField.setText("");
        }
    }
}