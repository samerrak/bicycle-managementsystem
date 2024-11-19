/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse.biketourplus.model;
import java.util.*;

// line 1 "../../../../../BikeTourPlusStates.ump"
// line 60 "../../../../../BikeTourPlus.ump"
public class Participant extends NamedUser
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Participant Attributes
  private int nrWeeks;
  private int weekAvailableFrom;
  private int weekAvailableUntil;
  private boolean lodgeRequired;
  private String authorizationCode;
  private int refundedPercentageAmount;

  //Participant State Machines
  public enum Status { NotAssigned, Assigned, Paid, Started, Finished, Cancelled, Banned }
  private Status status;

  //Participant Associations
  private BikeTourPlus bikeTourPlus;
  private BikeTour bikeTour;
  private List<BookedItem> bookedItems;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Participant(String aEmail, String aPassword, String aName, String aEmergencyContact, int aNrWeeks, int aWeekAvailableFrom, int aWeekAvailableUntil, boolean aLodgeRequired, String aAuthorizationCode, int aRefundedPercentageAmount, BikeTourPlus aBikeTourPlus)
  {
    super(aEmail, aPassword, aName, aEmergencyContact);
    nrWeeks = aNrWeeks;
    weekAvailableFrom = aWeekAvailableFrom;
    weekAvailableUntil = aWeekAvailableUntil;
    lodgeRequired = aLodgeRequired;
    authorizationCode = aAuthorizationCode;
    refundedPercentageAmount = aRefundedPercentageAmount;
    boolean didAddBikeTourPlus = setBikeTourPlus(aBikeTourPlus);
    if (!didAddBikeTourPlus)
    {
      throw new RuntimeException("Unable to create participant due to bikeTourPlus. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    bookedItems = new ArrayList<BookedItem>();
    setStatus(Status.NotAssigned);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setNrWeeks(int aNrWeeks)
  {
    boolean wasSet = false;
    nrWeeks = aNrWeeks;
    wasSet = true;
    return wasSet;
  }

  public boolean setWeekAvailableFrom(int aWeekAvailableFrom)
  {
    boolean wasSet = false;
    weekAvailableFrom = aWeekAvailableFrom;
    wasSet = true;
    return wasSet;
  }

  public boolean setWeekAvailableUntil(int aWeekAvailableUntil)
  {
    boolean wasSet = false;
    weekAvailableUntil = aWeekAvailableUntil;
    wasSet = true;
    return wasSet;
  }

  public boolean setLodgeRequired(boolean aLodgeRequired)
  {
    boolean wasSet = false;
    lodgeRequired = aLodgeRequired;
    wasSet = true;
    return wasSet;
  }

  public boolean setAuthorizationCode(String aAuthorizationCode)
  {
    boolean wasSet = false;
    authorizationCode = aAuthorizationCode;
    wasSet = true;
    return wasSet;
  }

  public boolean setRefundedPercentageAmount(int aRefundedPercentageAmount)
  {
    boolean wasSet = false;
    refundedPercentageAmount = aRefundedPercentageAmount;
    wasSet = true;
    return wasSet;
  }

  public int getNrWeeks()
  {
    return nrWeeks;
  }

  public int getWeekAvailableFrom()
  {
    return weekAvailableFrom;
  }

  public int getWeekAvailableUntil()
  {
    return weekAvailableUntil;
  }

  public boolean getLodgeRequired()
  {
    return lodgeRequired;
  }

  public String getAuthorizationCode()
  {
    return authorizationCode;
  }

  public int getRefundedPercentageAmount()
  {
    return refundedPercentageAmount;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isLodgeRequired()
  {
    return lodgeRequired;
  }

  public String getStatusFullName()
  {
    String answer = status.toString();
    return answer;
  }

  public Status getStatus()
  {
    return status;
  }

  public boolean assign(Guide guide)
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case NotAssigned:
        if (canBeAssigned(guide))
        {
        // line 8 "../../../../../BikeTourPlusStates.ump"
          assignment(guide);
          setStatus(Status.Assigned);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean cancelTrip()
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case NotAssigned:
        setStatus(Status.Cancelled);
        wasEventProcessed = true;
        break;
      case Assigned:
        setStatus(Status.Cancelled);
        wasEventProcessed = true;
        break;
      case Paid:
        // line 34 "../../../../../BikeTourPlusStates.ump"
        refundParticipant(50);
        setStatus(Status.Cancelled);
        wasEventProcessed = true;
        break;
      case Started:
        // line 46 "../../../../../BikeTourPlusStates.ump"
        refundParticipant(10);
        setStatus(Status.Cancelled);
        wasEventProcessed = true;
        break;
      case Finished:
        // line 66 "../../../../../BikeTourPlusStates.ump"
        rejectCancellation("Cannot cancel tour because the participant has finished their tour");
        setStatus(Status.Finished);
        wasEventProcessed = true;
        break;
      case Cancelled:
        // line 80 "../../../../../BikeTourPlusStates.ump"
        rejectCancellation("Cannot cancel tour because the participant has already cancelled their tour");
        setStatus(Status.Cancelled);
        wasEventProcessed = true;
        break;
      case Banned:
        // line 94 "../../../../../BikeTourPlusStates.ump"
        rejectCancellation("Cannot cancel tour because the participant is banned");
        setStatus(Status.Banned);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean payForTrip(String authorizationCode)
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case NotAssigned:
        // line 12 "../../../../../BikeTourPlusStates.ump"
        rejectPayment("The participant has not been assigned to their tour");
        setStatus(Status.NotAssigned);
        wasEventProcessed = true;
        break;
      case Assigned:
        if (validAuthorizationCode(authorizationCode))
        {
        // line 20 "../../../../../BikeTourPlusStates.ump"
          updateAuthorizationCode(authorizationCode);
          setStatus(Status.Paid);
          wasEventProcessed = true;
          break;
        }
        if (!(validAuthorizationCode(authorizationCode)))
        {
        // line 23 "../../../../../BikeTourPlusStates.ump"
          rejectPayment("Invalid authorization code");
          setStatus(Status.Assigned);
          wasEventProcessed = true;
          break;
        }
        break;
      case Paid:
        // line 37 "../../../../../BikeTourPlusStates.ump"
        rejectPayment("The participant has already paid for their tour");
        setStatus(Status.Paid);
        wasEventProcessed = true;
        break;
      case Started:
        // line 49 "../../../../../BikeTourPlusStates.ump"
        rejectPayment("The participant has already paid for their tour");
        setStatus(Status.Started);
        wasEventProcessed = true;
        break;
      case Finished:
        // line 57 "../../../../../BikeTourPlusStates.ump"
        rejectPayment("The participant has already paid for their tour");
        setStatus(Status.Finished);
        wasEventProcessed = true;
        break;
      case Cancelled:
        // line 71 "../../../../../BikeTourPlusStates.ump"
        rejectPayment("Cannot pay for tour because the participant has cancelled their tour");
        setStatus(Status.Cancelled);
        wasEventProcessed = true;
        break;
      case Banned:
        // line 85 "../../../../../BikeTourPlusStates.ump"
        rejectPayment("Cannot pay for tour because the participant is banned");
        setStatus(Status.Banned);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean finishBikeTour()
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case NotAssigned:
        // line 15 "../../../../../BikeTourPlusStates.ump"
        rejectFinishBikeTour("Cannot finish a tour for a participant who has not started their tour");
        setStatus(Status.NotAssigned);
        wasEventProcessed = true;
        break;
      case Assigned:
        // line 28 "../../../../../BikeTourPlusStates.ump"
        rejectFinishBikeTour("Cannot finish a tour for a participant who has not started their tour");
        setStatus(Status.Assigned);
        wasEventProcessed = true;
        break;
      case Paid:
        // line 40 "../../../../../BikeTourPlusStates.ump"
        rejectFinishBikeTour("Cannot finish a tour for a participant who has not started their tour");
        setStatus(Status.Paid);
        wasEventProcessed = true;
        break;
      case Started:
        setStatus(Status.Finished);
        wasEventProcessed = true;
        break;
      case Finished:
        // line 63 "../../../../../BikeTourPlusStates.ump"
        rejectFinishBikeTour("Cannot finish tour because the participant has already finished their tour");
        setStatus(Status.Finished);
        wasEventProcessed = true;
        break;
      case Cancelled:
        // line 77 "../../../../../BikeTourPlusStates.ump"
        rejectFinishBikeTour("Cannot finish tour because the participant has cancelled their tour");
        setStatus(Status.Cancelled);
        wasEventProcessed = true;
        break;
      case Banned:
        // line 91 "../../../../../BikeTourPlusStates.ump"
        rejectFinishBikeTour("Cannot finish tour because the participant is banned");
        setStatus(Status.Banned);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean startBikeTour()
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Assigned:
        setStatus(Status.Banned);
        wasEventProcessed = true;
        break;
      case Paid:
        setStatus(Status.Started);
        wasEventProcessed = true;
        break;
      case Started:
        // line 52 "../../../../../BikeTourPlusStates.ump"
        rejectStartBikeTour("Cannot start tour because the participant has already started their tour");
        setStatus(Status.Started);
        wasEventProcessed = true;
        break;
      case Finished:
        // line 60 "../../../../../BikeTourPlusStates.ump"
        rejectStartBikeTour("Cannot start tour because the participant has finished their tour");
        setStatus(Status.Finished);
        wasEventProcessed = true;
        break;
      case Cancelled:
        // line 74 "../../../../../BikeTourPlusStates.ump"
        rejectStartBikeTour("Cannot start tour because the participant has cancelled their tour");
        setStatus(Status.Cancelled);
        wasEventProcessed = true;
        break;
      case Banned:
        // line 88 "../../../../../BikeTourPlusStates.ump"
        rejectStartBikeTour("Cannot start tour because the participant is banned");
        setStatus(Status.Banned);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private void setStatus(Status aStatus)
  {
    status = aStatus;
  }
  /* Code from template association_GetOne */
  public BikeTourPlus getBikeTourPlus()
  {
    return bikeTourPlus;
  }
  /* Code from template association_GetOne */
  public BikeTour getBikeTour()
  {
    return bikeTour;
  }

  public boolean hasBikeTour()
  {
    boolean has = bikeTour != null;
    return has;
  }
  /* Code from template association_GetMany */
  public BookedItem getBookedItem(int index)
  {
    BookedItem aBookedItem = bookedItems.get(index);
    return aBookedItem;
  }

  public List<BookedItem> getBookedItems()
  {
    List<BookedItem> newBookedItems = Collections.unmodifiableList(bookedItems);
    return newBookedItems;
  }

  public int numberOfBookedItems()
  {
    int number = bookedItems.size();
    return number;
  }

  public boolean hasBookedItems()
  {
    boolean has = bookedItems.size() > 0;
    return has;
  }

  public int indexOfBookedItem(BookedItem aBookedItem)
  {
    int index = bookedItems.indexOf(aBookedItem);
    return index;
  }
  /* Code from template association_SetOneToMany */
  public boolean setBikeTourPlus(BikeTourPlus aBikeTourPlus)
  {
    boolean wasSet = false;
    if (aBikeTourPlus == null)
    {
      return wasSet;
    }

    BikeTourPlus existingBikeTourPlus = bikeTourPlus;
    bikeTourPlus = aBikeTourPlus;
    if (existingBikeTourPlus != null && !existingBikeTourPlus.equals(aBikeTourPlus))
    {
      existingBikeTourPlus.removeParticipant(this);
    }
    bikeTourPlus.addParticipant(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToMany */
  public boolean setBikeTour(BikeTour aBikeTour)
  {
    boolean wasSet = false;
    BikeTour existingBikeTour = bikeTour;
    bikeTour = aBikeTour;
    if (existingBikeTour != null && !existingBikeTour.equals(aBikeTour))
    {
      existingBikeTour.removeParticipant(this);
    }
    if (aBikeTour != null)
    {
      aBikeTour.addParticipant(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfBookedItems()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public BookedItem addBookedItem(int aQuantity, BikeTourPlus aBikeTourPlus, BookableItem aItem)
  {
    return new BookedItem(aQuantity, aBikeTourPlus, this, aItem);
  }

  public boolean addBookedItem(BookedItem aBookedItem)
  {
    boolean wasAdded = false;
    if (bookedItems.contains(aBookedItem)) { return false; }
    Participant existingParticipant = aBookedItem.getParticipant();
    boolean isNewParticipant = existingParticipant != null && !this.equals(existingParticipant);
    if (isNewParticipant)
    {
      aBookedItem.setParticipant(this);
    }
    else
    {
      bookedItems.add(aBookedItem);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeBookedItem(BookedItem aBookedItem)
  {
    boolean wasRemoved = false;
    //Unable to remove aBookedItem, as it must always have a participant
    if (!this.equals(aBookedItem.getParticipant()))
    {
      bookedItems.remove(aBookedItem);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addBookedItemAt(BookedItem aBookedItem, int index)
  {  
    boolean wasAdded = false;
    if(addBookedItem(aBookedItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfBookedItems()) { index = numberOfBookedItems() - 1; }
      bookedItems.remove(aBookedItem);
      bookedItems.add(index, aBookedItem);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveBookedItemAt(BookedItem aBookedItem, int index)
  {
    boolean wasAdded = false;
    if(bookedItems.contains(aBookedItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfBookedItems()) { index = numberOfBookedItems() - 1; }
      bookedItems.remove(aBookedItem);
      bookedItems.add(index, aBookedItem);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addBookedItemAt(aBookedItem, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    BikeTourPlus placeholderBikeTourPlus = bikeTourPlus;
    this.bikeTourPlus = null;
    if(placeholderBikeTourPlus != null)
    {
      placeholderBikeTourPlus.removeParticipant(this);
    }
    if (bikeTour != null)
    {
      BikeTour placeholderBikeTour = bikeTour;
      this.bikeTour = null;
      placeholderBikeTour.removeParticipant(this);
    }
    for(int i=bookedItems.size(); i > 0; i--)
    {
      BookedItem aBookedItem = bookedItems.get(i - 1);
      aBookedItem.delete();
    }
    super.delete();
  }


  /**
   * 
   * Method implementing a guard condition for the assign event
   * 
   * @author Karl
   */
  // line 106 "../../../../../BikeTourPlusStates.ump"
   private boolean canBeAssigned(Guide guide){
    List<BikeTour> bikeTours = guide.getBikeTours();

        //Case where the guide has no BikeTours
        BikeTour bikeTour;
        if(bikeTours.isEmpty()){
            return true;
        }

        //Case where the guide already has at least 1 biketour
        for (BikeTour aBikeTour : bikeTours){
            int startWeek = aBikeTour.getStartWeek();
            int endWeek = aBikeTour.getEndWeek();
            if(((endWeek - startWeek + 1) == getNrWeeks()) && weekAvailableFrom <= startWeek && weekAvailableUntil >= endWeek){
                return true;
            }
        }

        //Try to fit a new biketour to the guide if the participant couldn't participate in any of the already existing
        int counter = 0;
        boolean conflicting = false;
        for (int i = weekAvailableFrom; i <= weekAvailableUntil; i++){
            for(BikeTour aBikeTour : bikeTours){
                int startWeek = aBikeTour.getStartWeek();
                int endWeek = aBikeTour.getEndWeek();
                if (i >= startWeek && i <= endWeek){
                        conflicting = true;
                }
            }
            if(!conflicting){
                counter++;
                if(counter == getNrWeeks()){
                    return true;
                }
            } else{
                counter = 0;
            }
            conflicting = false;
        }

        return false;
  }


  /**
   * Method implementing the action of the assign event
   * 
   * @author Karl Bridi
   */
  // line 153 "../../../../../BikeTourPlusStates.ump"
   private void assignment(Guide guide){
    List<BikeTour> bikeTours = guide.getBikeTours();

        //Code to get the id of the next BikeTour
        List<BikeTour> bikeToursOfSystem = bikeTourPlus.getBikeTours();
        int nextId;
        if(!bikeToursOfSystem.isEmpty()){
            nextId = bikeToursOfSystem.get(bikeToursOfSystem.size() - 1).getId() + 1;
        } else{
            nextId = 1;
        }

        //Case where the guide has no BikeTours
        BikeTour bikeTour;
        if(bikeTours.isEmpty()){
            bikeTour = new BikeTour(nextId, weekAvailableFrom, weekAvailableFrom + getNrWeeks() - 1, guide, bikeTourPlus);
            bikeTour.addParticipant(this);
            return;
        }

        //Case where the guide already has at least 1 biketour
        for (BikeTour aBikeTour : bikeTours){
            int startWeek = aBikeTour.getStartWeek();
            int endWeek = aBikeTour.getEndWeek();
            if(((endWeek - startWeek + 1) == getNrWeeks()) && weekAvailableFrom <= startWeek && weekAvailableUntil >= endWeek){
                aBikeTour.addParticipant(this);
                return;
            }
        }

        //Try to fit a new biketour to the guide if the participant couldn't participate in any of the already existing
        int counter = 0;
        boolean conflicting = false;
        for (int i = weekAvailableFrom; i <= weekAvailableUntil; i++){
            for(BikeTour aBikeTour : bikeTours){
                int startWeek = aBikeTour.getStartWeek();
                int endWeek = aBikeTour.getEndWeek();
                if (i >= startWeek && i <= endWeek){
                        conflicting = true;
                }
            }

            if(!conflicting){
                counter++;
                if(counter == getNrWeeks()){
                    bikeTour = new BikeTour(nextId, i - counter + 1, i, guide, bikeTourPlus);
                    bikeTour.addParticipant(this);
                    return;
                }
            } else{
                counter = 0;
            }
            conflicting = false;
        }
  }


  /**
   * Method implementing a guard condition for the payForTrip event
   * 
   * @author Emilien Taisne
   */
  // line 213 "../../../../../BikeTourPlusStates.ump"
   private boolean validAuthorizationCode(String authorizationCode){
    if (authorizationCode == null || authorizationCode.isEmpty()) {
            return false;
        }
        return true;
  }


  /**
   * Method implementing the action for the payForTrip event
   * 
   * @author Emilien Taisne
   */
  // line 224 "../../../../../BikeTourPlusStates.ump"
   private void updateAuthorizationCode(String authorizationCode){
    this.authorizationCode = authorizationCode;
  }


  /**
   * Method implementing part of the action for the payForTrip event
   * 
   * @author Karl Bridi
   */
  // line 232 "../../../../../BikeTourPlusStates.ump"
   private void rejectPayment(String errorMessage){
    throw new RuntimeException(errorMessage);
  }


  /**
   * Method implementing part of the action for the payForTrip event
   * 
   * @author Emilien Taisne
   */
  // line 240 "../../../../../BikeTourPlusStates.ump"
   private void refundParticipant(int percentage){
    refundedPercentageAmount = percentage;
  }


  /**
   * Method to implement action for the startBikeTour event
   * 
   * @author Karl Bridi
   */
  // line 248 "../../../../../BikeTourPlusStates.ump"
   private void rejectStartBikeTour(String errorMessage){
    throw new RuntimeException(errorMessage);
  }


  /**
   * Method to implement action for the finishBikeTour event
   * 
   * @author Karl Bridi
   */
  // line 256 "../../../../../BikeTourPlusStates.ump"
   private void rejectFinishBikeTour(String errorMessage){
    throw new RuntimeException(errorMessage);
  }


  /**
   * Method to implement action for the cancel event
   * 
   * @author Karl Bridi
   */
  // line 264 "../../../../../BikeTourPlusStates.ump"
   private void rejectCancellation(String errorMessage){
    throw new RuntimeException(errorMessage);
  }


  public String toString()
  {
    return super.toString() + "["+
            "nrWeeks" + ":" + getNrWeeks()+ "," +
            "weekAvailableFrom" + ":" + getWeekAvailableFrom()+ "," +
            "weekAvailableUntil" + ":" + getWeekAvailableUntil()+ "," +
            "lodgeRequired" + ":" + getLodgeRequired()+ "," +
            "authorizationCode" + ":" + getAuthorizationCode()+ "," +
            "refundedPercentageAmount" + ":" + getRefundedPercentageAmount()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "bikeTourPlus = "+(getBikeTourPlus()!=null?Integer.toHexString(System.identityHashCode(getBikeTourPlus())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "bikeTour = "+(getBikeTour()!=null?Integer.toHexString(System.identityHashCode(getBikeTour())):"null");
  }
}