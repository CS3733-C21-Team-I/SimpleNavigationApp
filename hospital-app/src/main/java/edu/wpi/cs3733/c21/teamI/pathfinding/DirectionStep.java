package edu.wpi.cs3733.c21.teamI.pathfinding;

import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;
import edu.wpi.cs3733.c21.teamI.view.maps.MapPathfindingController;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;

public class DirectionStep {
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
  }

  private double calcCenterPointX(HospitalMapNode a, HospitalMapNode b) {
    return a.getxCoord() + -1 * (a.getxCoord() - b.getxCoord() / 2);
  }

  private double calcCenterPointY(HospitalMapNode a, HospitalMapNode b) {
    return a.getyCoord() + -1 * (a.getyCoord() - b.getyCoord() / 2);
  }

  public double getCenterX() {
    return this.centerX;
  }

  public double getCenterY() {
    return this.centerY;
  }

  public double getWidth() {
    return Math.abs(pointA.getxCoord() - pointB.getxCoord());
  }

  public double getHeight() {
    return Math.abs(pointA.getyCoord() - pointB.getyCoord());
  }

}
