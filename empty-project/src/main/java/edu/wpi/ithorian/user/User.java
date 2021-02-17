package edu.wpi.ithorian.user;

import java.util.Set;

public class User {

  public Set<Role> userRoles;

  private Set<Permission> userPermissions;
  private String name;
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
    PATIENT;
  }

  public enum Permission {
    EDIT_MAP,
    VIEW_TICKET,
    REQUEST_TICKET,
    EDIT_ANNOUNCEMENTS
  }
}
