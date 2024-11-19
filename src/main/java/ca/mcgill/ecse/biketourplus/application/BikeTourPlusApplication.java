package ca.mcgill.ecse.biketourplus.application;

import java.sql.Date;

import ca.mcgill.ecse.biketourplus.javafx.fxml.BtpFxmlView;
import ca.mcgill.ecse.biketourplus.model.BikeTourPlus;
import ca.mcgill.ecse.biketourplus.persistence.BikeTourPlusPersistence;
import javafx.application.Application;

/**
 * Main class of the Bike Tour Plus application
 * 
 * A large amout of this code was taken and adapted from the tutorial wiki pages
 *  https://github.com/F2022-ECSE223/ca.mcgill.ecse.btms (BTMS project)
 */
public class BikeTourPlusApplication {

  private static BikeTourPlus bikeTourPlus;

  public static final boolean DARK_MODE = false;

  /**
   * Main entry point.
   *
   * @param args
   */
  public static void main(String[] args) {
    // start UI
    Application.launch(BtpFxmlView.class, args);
  }
  public static BikeTourPlus getBikeTourPlus() {
    if (bikeTourPlus == null) {
      // these attributes are default, you should set them later with the setters
      bikeTourPlus = BikeTourPlusPersistence.load();
    }
    return bikeTourPlus;
  }

}
