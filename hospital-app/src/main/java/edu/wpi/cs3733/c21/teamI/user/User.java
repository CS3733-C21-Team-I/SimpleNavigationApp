package edu.wpi.cs3733.c21.teamI.user;

import java.util.Set;

public class User {

  public static User baseUser;

  public Set<Role> userRoles;

  private Set<Permission> userPermissions;
  private String name;

  public String getName() {
    return name;
  }

  public int getUserId() {
    return userId;
  }

  private int userId;

  public User(int userId, String name, Set<Role> userRoles, Set<Permission> userPermissions) {
    this.userRoles = userRoles;
    this.userPermissions = userPermissions;
    this.name = name;
    this.userId = userId;
  }

  /** @param permission */
  public boolean hasPermission(User.Permission permission) {
    // TODO - implement User.hasPermission
    return userPermissions.contains(permission);
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
    NURSE
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
    RESPOND_TO_INTERNAL
  }
}
