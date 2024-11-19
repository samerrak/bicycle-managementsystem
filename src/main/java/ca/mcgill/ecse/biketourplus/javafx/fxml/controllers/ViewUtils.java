package ca.mcgill.ecse.biketourplus.javafx.fxml.controllers;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse.biketourplus.controller.BikeTourPlusFeatureSet1Controller;
import ca.mcgill.ecse.biketourplus.controller.BikeTourPlusFeatureSet3Controller;
import ca.mcgill.ecse.biketourplus.controller.BikeTourPlusFeatureSet4Controller;
import ca.mcgill.ecse.biketourplus.controller.BikeTourPlusFeatureSet5Controller;
import ca.mcgill.ecse.biketourplus.controller.BikeTourPlusFeatureSet6Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Class to provide utils to the other view controllers
 * of the BikeTourPlus application
 * 
 * A large amout of this code was taken and adapted from the tutorial wiki pages
 *  https://github.com/F2022-ECSE223/ca.mcgill.ecse.btms (BTMS project)
 * 
 * (For the rest that was not taken from tutorial we all
 * changed it a bit to make it work in our case)
 */
public class ViewUtils {

  /** Calls the controller and shows an error, if applicable. */
  public static boolean callController(String result) {
    if (result.isEmpty()) {
      // BtpFxmlView.getInstance().refresh();
      return true;
    }
    showError(result);
    return false;
  }

  /** Calls the controller and returns true on success. This method is included for readability. */
  public static boolean successful(String controllerResult, String intent) {
    if (callController(controllerResult)){
      if(!intent.isEmpty()) makePopupWindow("Succesfully: ", intent);
      return true;
    }
    return false;
  }

  /**
   * Creates a popup window.
   *
   * @param title: title of the popup window
   * @param message: message to display
   */
  public static void makePopupWindow(String title, String message) {
    Stage dialog = new Stage();
    dialog.initModality(Modality.APPLICATION_MODAL);
    VBox dialogPane = new VBox();

    // create UI elements
    Text text = new Text(message);
    Button okButton = new Button("OK");
    okButton.setOnAction(a -> dialog.close());

    // display the popup window
    int innerPadding = 10; // inner padding/spacing
    int outerPadding = 100; // outer padding
    dialogPane.setSpacing(innerPadding);
    dialogPane.setAlignment(Pos.CENTER);
    dialogPane.setPadding(new Insets(innerPadding, innerPadding, innerPadding, innerPadding));
    dialogPane.getChildren().addAll(text, okButton);
    Scene dialogScene = new Scene(dialogPane, outerPadding + 5 * message.length(), outerPadding);
    dialog.setScene(dialogScene);
    dialog.setTitle(title);
    dialog.show();
  }

  public static void showError(String message) {
    makePopupWindow("Error", message);
  }

  public static ObservableList<String> getCombos() {
    return FXCollections.observableList(BikeTourPlusFeatureSet6Controller.getCombos());
  }

  public static ObservableList<String> getParticipants() {
    return FXCollections.observableList(BikeTourPlusFeatureSet3Controller.getParticipants());
  }

  public static ObservableList<String> getGears() {
    return FXCollections.observableList(BikeTourPlusFeatureSet5Controller.getGears());
  }

  public static ObservableList<String> getGearsAndCombos() {
    List<String> gearsAndCombos = new ArrayList<>();
    gearsAndCombos.addAll(BikeTourPlusFeatureSet6Controller.getCombos());
    gearsAndCombos.addAll(BikeTourPlusFeatureSet5Controller.getGears());
    return FXCollections.observableList(gearsAndCombos);
  }

  public static ObservableList<String> getGuides() {
    return FXCollections.observableList(BikeTourPlusFeatureSet4Controller.getGuides());
  }

  // public static ObservableList<Integer> getItemOfParticipant(String email){
  //   return FXCollections.observableList(BikeTourPlusFeatureSet3Controller.getParticipantBookableItems(email));
  // }

  public static ObservableList<Integer> getBikeTours() {
    List<Integer> bikeTours = new ArrayList<>();
    BikeTourPlusFeatureSet1Controller.getAllBikeTours();
    try {
      int i = 1;
      while (i < Integer.MAX_VALUE && BikeTourPlusFeatureSet1Controller.getBikeTour(i)!= null) {
        BikeTourPlusFeatureSet1Controller.getBikeTour(i);
        bikeTours.add((i));
        i++;
      }
    }
    catch (Exception e) {
      return FXCollections.observableList(bikeTours);
    }

    return FXCollections.observableList(bikeTours);

  }


}