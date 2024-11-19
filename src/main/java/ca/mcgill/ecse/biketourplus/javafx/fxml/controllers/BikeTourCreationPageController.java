package ca.mcgill.ecse.biketourplus.javafx.fxml.controllers;

import ca.mcgill.ecse.biketourplus.controller.BikeTourPlusBTCreationController;
import ca.mcgill.ecse.biketourplus.controller.TOUser;
import ca.mcgill.ecse.biketourplus.javafx.fxml.BtpFxmlView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Class that implements the following features in UI:
 * - Initiate the bike tour creation process for all guides and participants
 * - See all the current users in the system
 * 
 * @author Emilien Taisne
 */
public class BikeTourCreationPageController {
    @FXML
    private Button initiateBikeToursButton;
    @FXML
    private TableView usersTable;
    
    private TableColumn<String, TOUser> cl1 = new TableColumn<>("Email");
    private TableColumn<String, TOUser> cl2 = new TableColumn<>("Name");
    private TableColumn<String, TOUser> cl3 = new TableColumn<>("IsParticipant");
    private TableColumn<String, TOUser> cl4 = new TableColumn<>("Status");
    private TableColumn<String, TOUser> cl5 = new TableColumn<>("Start Week");
    private TableColumn<String, TOUser> cl6 = new TableColumn<>("End Week");
    private TableColumn<String, TOUser> cl7 = new TableColumn<>("# of Week");
    private TableColumn<String, TOUser> cl8 = new TableColumn<>("Lodge");

    /**
     * Method called when the following button is pressed: initiate the bike tour button
     * 
     * @author Emilien Taisne
     */
    @FXML
    public void initiateClicked(ActionEvent event) {
        ViewUtils.successful(BikeTourPlusBTCreationController.initiateBikeToursCreation(), "Assignements of participants");
        BtpFxmlView.getInstance().refresh();
    }

    /**
     * Method called when application is initiated
     * 
     * @author Emilien Taisne
     */
    @FXML
    public void initialize() {
        usersTable.getColumns().add(cl1);
        usersTable.getColumns().add(cl2);
        usersTable.getColumns().add(cl3);
        usersTable.getColumns().add(cl4);
        usersTable.getColumns().add(cl5);
        usersTable.getColumns().add(cl6);
        usersTable.getColumns().add(cl7);
        usersTable.getColumns().add(cl8);
        usersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        usersTable.addEventHandler(BtpFxmlView.REFRESH_EVENT, e -> refreshTable());

        BtpFxmlView.getInstance().registerRefreshEvent(usersTable);
        BtpFxmlView.getInstance().refresh();
    }

    /**
     * Private helper method that clears the table and put the updated information back
     * 
     * @author Emilien Taisne
     */
    private void refreshTable(){
		try{
            usersTable.getItems().clear();

            cl1.setCellValueFactory(new PropertyValueFactory<>("userEmail"));
            cl2.setCellValueFactory(new PropertyValueFactory<>("userName"));
            cl3.setCellValueFactory(new PropertyValueFactory<>("typeOfUser"));
            cl4.setCellValueFactory(new PropertyValueFactory<>("userStatus"));
            cl5.setCellValueFactory(new PropertyValueFactory<>("userStartWeek"));
            cl6.setCellValueFactory(new PropertyValueFactory<>("userEndWeek"));
            cl7.setCellValueFactory(new PropertyValueFactory<>("userNumberOfWeek"));
            cl8.setCellValueFactory(new PropertyValueFactory<>("userLodgeRequirement"));

            for (TOUser user : BikeTourPlusBTCreationController.getUsersOfSystem())
                usersTable.getItems().add(user);

        }
        catch (Exception e)
        {
            ViewUtils.showError(e.toString());
        }
	}
}
