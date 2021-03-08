package edu.wpi.cs3733.c21.teamI.ticket;

public class CovidTicket extends ServiceTicket {
  private boolean soreThroat;
  private boolean breathing;
  private boolean tasteSmellLoss;
  private boolean fever;
  private boolean congestion;
  private boolean cough;
  private boolean nausea;
  private boolean diarrhea;
  private boolean headache;
  private boolean noneSymptoms;
  private boolean contactCovidConfirmed;
  private boolean contactCovidSymptoms;
  private boolean noneContact;
  private boolean covidTest;

  public CovidTicket(
      int requestID,
      String location,
      String desc,
      boolean completed,
      boolean soreThroat,
      boolean breathing,
      boolean tasteSmellLoss,
      boolean fever,
      boolean congestion,
      boolean cough,
      boolean nausea,
      boolean diarrhea,
      boolean headache,
      boolean noneSymptoms,
      boolean contactCovidConfirmed,
      boolean contactCovidSymptoms,
      boolean noneContact,
      boolean covidTest) {
    super(requestID, TicketType.COVID, location, desc, completed);
    this.soreThroat = soreThroat;
    this.breathing = breathing;
    this.tasteSmellLoss = tasteSmellLoss;
    this.fever = fever;
    this.congestion = congestion;
    this.cough = cough;
    this.nausea = nausea;
    this.diarrhea = diarrhea;
    this.headache = headache;
    this.noneSymptoms = noneSymptoms;
    this.contactCovidConfirmed = contactCovidConfirmed;
    this.contactCovidSymptoms = contactCovidSymptoms;
    this.noneContact = noneContact;
    this.covidTest = covidTest;
  }

  public boolean isSoreThroat() {
    return soreThroat;
  }

  public boolean isBreathing() {
    return breathing;
  }

  public boolean isTasteSmellLoss() {
    return tasteSmellLoss;
  }

  public boolean isFever() {
    return fever;
  }

  public boolean isCongestion() {
    return congestion;
  }

  public boolean isCough() {
    return cough;
  }

  public boolean isNausea() {
    return nausea;
  }

  public boolean isDiarrhea() {
    return diarrhea;
  }

  public boolean isHeadache() {
    return headache;
  }

  public boolean isNoneSymptoms() {
    return noneSymptoms;
  }

  public boolean isContactCovidConfirmed() {
    return contactCovidConfirmed;
  }

  public boolean isContactCovidSymptoms() {
    return contactCovidSymptoms;
  }

  public boolean isNoneContact() {
    return noneContact;
  }

  public boolean isCovidTest() {
    return covidTest;
  }
}
