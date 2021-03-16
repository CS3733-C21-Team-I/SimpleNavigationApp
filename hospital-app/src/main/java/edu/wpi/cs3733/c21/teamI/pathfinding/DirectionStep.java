package edu.wpi.cs3733.c21.teamI.pathfinding;

import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;
import java.util.HashMap;

public class DirectionStep {
  private final double width;
  private final double height;
  private HospitalMapNode pointA;
  private HospitalMapNode pointB;
  public String stepDetails;
  private String iconPath;
  private double centerX;
  private double centerY;
  private StepType stepType;

  private HashMap<StepType, String> directionToIcon = new HashMap<>();

  public DirectionStep(HospitalMapNode a, HospitalMapNode b, String textDirection, StepType type) {
    initHashMap();
    pointA = a;
    pointB = b;
    stepDetails = textDirection;
    centerX = calcCenterPointX(a, b);
    centerY = calcCenterPointY(a, b);
    width = calcWidth(a, b);
    height = calcHeight(a, b);
    stepType = type;
    assignIcon();
  }

  public static double calcCenterPointX(HospitalMapNode a, HospitalMapNode b) {
    return (a.getxCoord() + b.getxCoord()) / 2.0;
  }

  public static double calcCenterPointY(HospitalMapNode a, HospitalMapNode b) {
    return (a.getyCoord() + b.getyCoord()) / 2.0;
  }

  public static double calcWidth(HospitalMapNode a, HospitalMapNode b) {
    return Math.abs(a.getxCoord() - b.getxCoord());
  }

  public static double calcHeight(HospitalMapNode a, HospitalMapNode b) {
    return Math.abs(a.getyCoord() - b.getyCoord());
  }

  public double getCenterX() {
    return this.centerX;
  }

  public double getCenterY() {
    return this.centerY;
  }

  public double getWidth() {
    return width;
  }

  public double getHeight() {
    return height;
  }

  public HospitalMapNode getPointA() {
    return pointA;
  }

  public HospitalMapNode getPointB() {
    return pointB;
  }

  private void initHashMap() {
    directionToIcon.put(StepType.LEFT, "fxml/map/mapImages/directionIcons/turn-left.png");
    directionToIcon.put(StepType.RIGHT, "fxml/map/mapImages/directionIcons/turn-right.png");
    directionToIcon.put(StepType.ELEVATOR, "fxml/map/mapImages/directionIcons/elevator.png");
    directionToIcon.put(StepType.SLIGHT_LEFT, "fxml/map/mapImages/directionIcons/slight-left.png");
    directionToIcon.put(
        StepType.SLIGHT_RIGHT, "fxml/map/mapImages/directionIcons/slight-right.png");
    directionToIcon.put(StepType.STAIR, "fxml/map/mapImages/directionIcons/stairs.png");
    directionToIcon.put(StepType.STRAIGHT, "fxml/map/mapImages/directionIcons/straight.png");
    directionToIcon.put(StepType.EXIT, "fxml/map/mapImages/directionIcons/exit.png");
  }

  private void assignIcon() {
    iconPath = directionToIcon.get(stepType);
  }

  public String getIconPath() {
    return iconPath;
  }

  public StepType getStepType() {
    return stepType;
  }
}
