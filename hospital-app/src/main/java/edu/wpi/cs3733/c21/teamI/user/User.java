package edu.wpi.cs3733.c21.teamI.user;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.util.Set;
import javafx.beans.property.SimpleStringProperty;

public class User extends RecursiveTreeObject<User> {

  public static User baseUser;
  public Set<Role> userRoles;
  private Set<Permission> userPermissions;
  private String name;
  private int userId;
  private CovidRisk covidR;
  private EntryApproval entryApproval;

  public User(int userId, String name, Set<Role> userRoles, Set<Permission> userPermissions) {
    this.userRoles = userRoles;
    this.userPermissions = userPermissions;
    this.name = name;
    this.userId = userId;
    this.covidR = CovidRisk.PENDING;
    this.entryApproval = EntryApproval.OFFSITE;
  }

  public enum Role {
    BASE,
    ADMIN,
    VISITOR,
    EMPLOYEE,
    PATIENT,

    SECURITY_EMPLOYEE,
    LAUNDRY_EMPLOYEE,
    SANITATION_EMPLOYEE,
    MAINTENANCE_EMPLOYEE,
    IT_EMPLOYEE,
    TRANSLATOR,
    NURSE,
    RELIGIOUS_CONSULT,
    TRANSPORTATION_EMPLOYEE
  }

  public enum EntryApproval {
    APPROVED,
    REJECTED,
    OFFSITE // not yet arrived
  }

  public enum Permission {
    EDIT_MAP,
    VIEW_TICKET,
    REQUEST_TICKET,
    EDIT_ANNOUNCEMENTS,
    REQUEST_PARKING_PERMIT,

    RESPOND_TO_SECURITY,
    RESPOND_TO_LAUNDRY,
    RESPOND_TO_SANITATION,
    RESPOND_TO_MAINTENANCE,
    RESPOND_TO_AV,
    RESPOND_TO_COMPUTER,
    RESPOND_TO_TRANSLATOR,
    RESPOND_TO_MEDICINE_REQUEST,
    RESPOND_TO_INTERNAL,
    RESPOND_TO_LANGUAGE,
    RESPOND_TO_TRANSPORT,
    RESPOND_TO_RELIGIOUS,
    SUBMIT_COVD_TICKET
  }

  public enum CovidRisk {
    COVID_RISK,
    PENDING,
    NO_COVID_RISK
  }

  public SimpleStringProperty getRiskString() {
    switch (covidR) {
      case COVID_RISK:
        return new SimpleStringProperty("RISK");
      case NO_COVID_RISK:
        return new SimpleStringProperty("CLEARED");
      default:
        return new SimpleStringProperty("PENDING");
    }
  }

  public SimpleStringProperty getApprovalString() {
    switch (entryApproval) {
      case APPROVED:
        return new SimpleStringProperty("APPROVED");
      case REJECTED:
        return new SimpleStringProperty("REJECTED");
      default:
        return new SimpleStringProperty("PENDING");
    }
  }

  public EntryApproval getEntryApproval() {
    return entryApproval;
  }

  public void setEntryApproval(EntryApproval entryApproval) {
    this.entryApproval = entryApproval;
  }

  public void setCovidRisk(CovidRisk cov) {
    this.covidR = cov;
  }

  public CovidRisk getCovidRisk() {
    return covidR;
  }

  public String getScreenName() {
    return name;
  }

  public String getName() {
    return name;
  }

  public int getUserId() {
    return userId;
  }

  /** @param permission */
  public boolean hasPermission(User.Permission permission) {
    // TODO - implement User.hasPermission
    return userPermissions.contains(permission);
  }
}
