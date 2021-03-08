package edu.wpi.cs3733.c21.teamI.ticket;

public class LanguageTicket extends ServiceTicket {
  private String language;
  private String meetingTime;
  private boolean legalDocs;

  public LanguageTicket(
      int requestID,
      String location,
      String desc,
      boolean completed,
      String language,
      String meetingTime,
      boolean legalDocs) {
    super(requestID, TicketType.LANGUAGE, location, desc, completed);
    this.language = language;
    this.meetingTime = meetingTime;
    this.legalDocs = legalDocs;
  }

  public String getLanguage() {
    return language;
  }

  public String getMeetingTime() {
    return meetingTime;
  }

  public boolean isLegalDocs() {
    return legalDocs;
  }
}
