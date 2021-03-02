package edu.wpi.cs3733.c21.teamI.ticket;

public class GiftTicket extends ServiceTicket {
  private String deliveryDate;
  private String deliveryTime;
  private String giftType;
  private String patientName;

  public GiftTicket(
      int requestID,
      int assignID,
      String location,
      String desc,
      boolean completed,
      String deliveryDate,
      String deliveryTime,
      String giftType,
      String patientName) {
    super(requestID, TicketType.GIFT, location, desc, completed);
    this.deliveryDate = deliveryDate;
    this.deliveryTime = deliveryTime;
    this.giftType = giftType;
    this.patientName = patientName;
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
}
