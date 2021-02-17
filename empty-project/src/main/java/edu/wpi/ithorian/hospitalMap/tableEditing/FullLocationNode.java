package edu.wpi.ithorian.hospitalMap.tableEditing;

import edu.wpi.ithorian.hospitalMap.LocationNode;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FullLocationNode extends LocationNode {

  private final String floor;
  private final String building;
  private final String type;
  private final Set<FullLocationNode> connections;

  public FullLocationNode(
      String id,
      int xCoord,
      int yCoord,
      String floor,
      String building,
      String type,
      String shortName,
      String longName,
      String teamAssigned,
      Set<FullLocationNode> connections) {
    super(id, xCoord, yCoord, shortName, longName, teamAssigned, new HashSet<>());
    this.connections = connections;
    this.floor = floor;
    this.building = building;
    this.type = type;
  }

  public List<String> getNodeString() {
    List<String> returnList =
        Arrays.asList(
            String.valueOf(getID()),
            String.valueOf(getxCoord()),
            String.valueOf(getyCoord()),
            floor,
            building,
            type,
            getShortName(),
            getLongName(),
            getTeamAssigned());
    return returnList;
  }

  @Override
  public String toString() {
    return "FullLocationNode{"
        + "id='"
        + getID()
        + ", xCoord="
        + getxCoord()
        + ", yCoord="
        + getyCoord()
        + ", floor="
        + floor
        + ", building="
        + building
        + ", type="
        + type
        + ", sName="
        + getShortName()
        + ", lName="
        + getLongName()
        + ", tName="
        + getTeamAssigned()
        + '}';
  }
}
