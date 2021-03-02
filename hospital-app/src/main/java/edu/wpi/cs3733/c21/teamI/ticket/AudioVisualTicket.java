package edu.wpi.cs3733.c21.teamI.ticket;

public class AudioVisualTicket extends ServiceTicket {
  private String patientName;
  private String mediaType;

  public AudioVisualTicket(
      int requestID,
      int assignID,
      String location,
      String desc,
      boolean completed,
      String patientName,
      String mediaType) {
    super(requestID, TicketType.AUDIO_VISUAL, location, desc, completed);
    this.patientName = patientName;
    this.mediaType = mediaType;
  }

  public String getPatientName() {
    return patientName;
  }

  public String getMediaType() {
    return mediaType;
  }
}
