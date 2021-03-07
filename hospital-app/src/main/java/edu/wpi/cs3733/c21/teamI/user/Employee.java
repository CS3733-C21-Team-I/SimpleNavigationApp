package edu.wpi.cs3733.c21.teamI.user;

import java.util.Set;

public class Employee extends User {

  public enum Gender {
    MALE,
    FEMALE,
    OTHER
  }

  private String firstName;
  private String lastName;
  private Gender gender;

  public Employee(
      int userId,
      String name,
      Set<Role> userRoles,
      Set<Permission> userPermissions,
      String firstName,
      String lastName,
      Gender gender) {
    super(userId, name, userRoles, userPermissions);
    this.firstName = firstName;
    this.lastName = lastName;
    this.gender = gender;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public Gender getGender() {
    return gender;
  }
}
