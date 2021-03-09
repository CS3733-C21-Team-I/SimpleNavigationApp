package edu.wpi.cs3733.c21.teamI.Notification;

public class Notification {
  private int notificationID;
  private int userID;
  private String details;
  private String timestamp;

  public Notification(int userID, String details, String timestamp) {
    this.userID = userID;
    this.details = details;
    this.timestamp = timestamp;
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

  public void setNotificationID(int notificationID) {
    this.notificationID = notificationID;
  }
}
