package edu.wpi.ithorian.user;

import java.util.Set;

public class User {

  public Role userRole;
  private Set<Permission> userPermissions;
  private String name;
  private int userId;

  /** @param permission */
  public boolean hasPermission(User.Permission permission) {
    // TODO - implement User.hasPermission
    throw new UnsupportedOperationException();
  }

  public enum Role {
    UNIDENTIFIED,
    ADMIN,
    VISITOR,
    EMPLOYEE,
    PATIENT
  }

  public enum Permission {
    EDIT_MAP,
    VIEW_TICKET,
    REQUEST_TICKET,
    EDIT_ANNOUNCEMENTS
  }
}
