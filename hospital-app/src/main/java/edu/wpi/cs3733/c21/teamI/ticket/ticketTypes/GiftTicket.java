package edu.wpi.cs3733.c21.teamI.ticket;

public class GiftTicket {
  private String deliveryDate;
  private String deliveryTime;
  private String giftType;
  private String patientName;
  private String location;

  public GiftTicket(
      String deliveryDate,
      String deliveryTime,
      String giftType,
      String patientName,
      String roomNum) {
    this.deliveryDate = deliveryDate;
    this.deliveryTime = deliveryTime;
    this.giftType = giftType;
    this.patientName = patientName;
    this.location = roomNum;
  }

  public String getDeliveryDate() {
    return deliveryDate;
  }

  public String getDeliveryTime() {
    return deliveryTime;
  }

  public String getGiftType() {
    return giftType;
  }

  public String getPatientName() {
    return patientName;
  }

  public String getLocation() {
    return location;
  }
}
