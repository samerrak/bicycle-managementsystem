package ca.mcgill.ecse.biketourplus.controller;

import java.util.ArrayList;

import ca.mcgill.ecse.biketourplus.application.BikeTourPlusApplication;
import ca.mcgill.ecse.biketourplus.model.*;
import ca.mcgill.ecse.biketourplus.persistence.BikeTourPlusPersistence;

/**
 * Class that is part of the Controller of the BikeTourPlus application
 * 
 * @author Walid Aissa
 *
 */
public class BikeTourPlusFeatureSet1Controller {

	private static BikeTourPlus btp = BikeTourPlusApplication.getBikeTourPlus();
	/**
	 * Method to update the BikeTourPlus manager, namely their password
	 * 
	 * @param password : the new password of the manager
	 * @return error in case there is one
	 */
	public static String updateManager(String password) {
		var error = "";
		Manager manager = btp.getManager();

			if (password == null || password.isBlank()) {
				error = "Password cannot be empty";
			} else if (password.indexOf("!") == -1 && password.indexOf("#") == -1 && password.indexOf("$") == -1) {
				error = "Password must contain one character out of !#$";
			} else if (password.equals(password.toLowerCase())) {
				error = "Password must contain one upper-case character";
			} else if (password.equals(password.toUpperCase())) {
				error = "Password must contain one lower-case character";
			} else if (password.length() < 4) {
				error = "Password must be at least four characters long";
			}
			if (!error.isEmpty()) {
				return error.trim();
			}
			try {
				manager.setPassword(password);
				BikeTourPlusPersistence.save();
			} catch (RuntimeException e) {
				return e.getMessage();
			}
			return error;
	}

	/**
	 * Method to get the BikeTour with id <id> and all the information related to
	 * it.
	 * 
	 * @param id, the id of the BikeTour sought
	 * @return TOBikeTour
	 */
	public static TOBikeTour getBikeTour(int id) {
		BikeTour bt = btp.getBikeTour(id-1);
		if (bt == null) {
			return null;
		}
		ArrayList<TOParticipantCost> TOparticipants = new ArrayList<TOParticipantCost>();
		int numberOfWeeks = (bt.getEndWeek() - bt.getStartWeek() + 1);
		for (Participant p : bt.getParticipants()) {
			int totalCostForBookableItems = 0;
			for (BookedItem bi : p.getBookedItems()) {
				BookableItem item = bi.getItem();
				if (item instanceof Gear) {
					totalCostForBookableItems += ((Gear) item).getPricePerWeek() * numberOfWeeks * bi.getQuantity();
				} else {
					int ComboPrice = 0;
					for (ComboItem ci : ((Combo) item).getComboItems()) {
						ComboPrice += ci.getGear().getPricePerWeek() * ci.getQuantity() * numberOfWeeks;
					}
					if (p.getLodgeRequired()) {
						ComboPrice *= ((100 - ((Combo) item).getDiscount()) / 100.0);
					}

					totalCostForBookableItems += ComboPrice * bi.getQuantity();
				}
			}
			int totalCostForBikingTour = numberOfWeeks * btp.getPriceOfGuidePerWeek() + totalCostForBookableItems;
			TOparticipants.add(new TOParticipantCost(p.getEmail(), p.getName(), p.getStatusFullName(), totalCostForBookableItems,
					totalCostForBikingTour, p.getAuthorizationCode(), p.getRefundedPercentageAmount()));
		}
		TOBikeTour TObt = new TOBikeTour(bt.getId(), bt.getStartWeek(), bt.getEndWeek(), bt.getGuide().getEmail(),
				bt.getGuide().getName(), btp.getPriceOfGuidePerWeek() * numberOfWeeks,
				TOparticipants.toArray(new TOParticipantCost[0]));

		return TObt;
	}

	public static ArrayList<Integer> getAllBikeTours(){
		ArrayList<Integer> toBikeTours = new ArrayList<>();
		for (BikeTour bikeTour : btp.getBikeTours()) {
			toBikeTours.add(bikeTour.getId());
		}
		// for (int i=0; i < btp.getBikeTours().size(); i++)
		// 	toBikeTours.add((getBikeTour(btp.getBikeTour(i).getId())).getId());

		return toBikeTours;

	}

}