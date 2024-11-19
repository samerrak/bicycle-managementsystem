

package ca.mcgill.ecse.biketourplus.controller;

public class TOUser
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Attributes
  private String userEmail;
  private String userName;
  private String typeOfUser;
  private String userStatus;
  private String userStartWeek;
  private String userEndWeek;
  private String userNumberOfWeek;
  private String userLodgeRequirement;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOUser(String aUserEmail, String aUserName, String aTypeOfUser, String aUserStatus, int aUserStartWeek, int aUserEndtWeek, int aUserNumberOfWeek, boolean aUserLodgeRequirement)
  {
    userEmail = aUserEmail;
    userName = aUserName;
    typeOfUser = aTypeOfUser;
    userStatus = aUserStatus;
    userStartWeek = String.valueOf(aUserStartWeek);
    userEndWeek = String.valueOf(aUserEndtWeek);
    userNumberOfWeek = String.valueOf(aUserNumberOfWeek);
    userLodgeRequirement = String.valueOf(aUserLodgeRequirement);
  }

  public TOUser(String aUserEmail, String aUserName, String aTypeOfUser){
    userEmail = aUserEmail;
    userName = aUserName;
    typeOfUser = aTypeOfUser;
    userStatus = "N.P.";
    userStartWeek = "N.P.";
    userEndWeek = "N.P.";
    userNumberOfWeek = "N.P.";
    userLodgeRequirement = "N.P.";
  }

  //------------------------
  // INTERFACE
  //------------------------

  public String getUserEmail()
  {
    return userEmail;
  }

  public String getUserName()
  {
    return userName;
  }

  public String getTypeOfUser()
  {
    return typeOfUser;
  }
  public String getUserStatus(){
    return userStatus;
  }
  public String getUserStartWeek()
  {
    return userStartWeek;
  }

  public String getUserEndWeek()
  {
    return userEndWeek;
  }

  public String getUserNumberOfWeek()
  {
    return userNumberOfWeek;
  }

  public String getUserLodgeRequirement()
  {
    return userLodgeRequirement;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "userEmail" + ":" + getUserEmail()+ "," +
            "userName" + ":" + getUserName()+ "," +
            "isParticipant" + ":" + getTypeOfUser()+ "," +
            "userStartWeek" + ":" + getUserStartWeek()+ "," +
            "userEndWeek" + ":" + getUserEndWeek()+ "," +
            "userNumberOfWeek" + ":" + getUserNumberOfWeek()+ "," +
            "userLodgeRequirement" + ":" + getUserLodgeRequirement()+ "]";
  }
}