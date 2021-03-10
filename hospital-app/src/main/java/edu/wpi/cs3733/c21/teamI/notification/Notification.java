package edu.wpi.cs3733.c21.teamI.notification;

public class Notification {
  private int notificationID;
  private int userID;
  private String details;
  private String timestamp;
  private boolean hasDisplayed;

  public Notification(int userID, String details, String timestamp) {
    this.userID = userID;
    this.details = details;
    this.timestamp = timestamp;
    this.hasDisplayed = false;
  }

  public int getNotificationID() {
    return notificationID;
  }

  public int getUserID() {
    return userID;
  }

  public String getDetails() {
    return details;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public boolean isHasDisplayed() {
    return hasDisplayed;
  }

  public void setNotificationID(int notificationID) {
    this.notificationID = notificationID;
  }

  public void setHasDisplayed(boolean hasDisplayed) {
    this.hasDisplayed = hasDisplayed;
  }
}
