package ca.mcgill.ecse.biketourplus.persistence;

import java.sql.Date;
import java.util.Calendar;

import ca.mcgill.ecse.biketourplus.application.BikeTourPlusApplication;
import ca.mcgill.ecse.biketourplus.model.BikeTourPlus;
import ca.mcgill.ecse.biketourplus.model.Manager;

/**
 * Class that helps to implement persistence
 * in the BikeTourPlus project
 * 
 * @author Karl Bridi
 * 
 *  A large amout of this code was taken and adapted from the tutorial wiki pages
 *  https://github.com/F2022-ECSE223/ca.mcgill.ecse.btms (BTMS project)
 */
public class BikeTourPlusPersistence {

  private static String filename = "btp.data";
  private static JsonSerializer serializer = new JsonSerializer("ca.mcgill.ecse.biketourplus");

  public static void setFilename(String filename) {
    BikeTourPlusPersistence.filename = filename;
  }

  public static void save() {
    save(BikeTourPlusApplication.getBikeTourPlus());
  }

  public static void save(BikeTourPlus btp) {
    serializer.serialize(btp, filename);
  }

  public static BikeTourPlus load() {
    var btp = (BikeTourPlus) serializer.deserialize(filename);
    // model cannot be loaded - create empty BTP
    if (btp == null) {
      Calendar calendar = Calendar.getInstance();
		  calendar.set(2023, Calendar.JUNE, 1, 0, 0, 0);
		  btp = new BikeTourPlus(new Date(0), 5, 50);
      new Manager("manager@btp.com", "manager", btp);
    } else {
      btp.reinitialize();
    }
    return btp;
  }

}