package edu.wpi.cs3733.c21.teamI.pathfinding;

import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;
import javafx.scene.image.Image;

public class DirectionStep {
  private final double width;
  private final double height;
  private HospitalMapNode pointA;
  private HospitalMapNode pointB;
  public String stepDetails;
  private Image icon;
  // public DirectionStep prevStep;
  // public DirectionStep nextStep;
  private double centerX;
  private double centerY;

  public DirectionStep(HospitalMapNode a, HospitalMapNode b, String textDirection) {
    pointA = a;
    pointB = b;
    stepDetails = textDirection;
    centerX = calcCenterPointX(a, b);
    centerY = calcCenterPointY(a, b);
    width = calcWidth(a, b);
    height = calcHeight(a, b);
  }

  public static double calcCenterPointX(HospitalMapNode a, HospitalMapNode b) {
    return a.getxCoord() + -1 * (a.getxCoord() - b.getxCoord() / 2);
  }

  public static double calcCenterPointY(HospitalMapNode a, HospitalMapNode b) {
    return a.getyCoord() + -1 * (a.getyCoord() - b.getyCoord() / 2);
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
}
