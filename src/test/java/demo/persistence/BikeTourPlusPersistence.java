package demo.persistence;

import ca.mcgill.ecse.biketourplus.model.BikeTourPlus;

import java.sql.Date;

public class BikeTourPlusPersistence {

  private static String filename = "btp.data";
  private static JsonSerializer serializer = new JsonSerializer("ca.mcgill.ecse.biketourplus");

  public static void save(BikeTourPlus bikeTourPlus) {
    serializer.serialize(bikeTourPlus, filename);
  }

  public static BikeTourPlus load() {
    var bikeTourPlus = (BikeTourPlus) serializer.deserialize(filename);
    // model cannot be loaded - create empty BikeTourPlus
    if (bikeTourPlus == null) {
      bikeTourPlus = new BikeTourPlus(new Date(0L), 0, 0);
    } else {
      bikeTourPlus.reinitialize();
    }
    return bikeTourPlus;
  }

  public static void setFilename(String filename) {
	  BikeTourPlusPersistence.filename = filename;
  }

}
