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
  private String stepDetails;
  private Image icon;
  public DirectionStep prevStep;
  public DirectionStep nextStep;
  private double centerX;
  private double centerY;

  public DirectionStep(
      HospitalMapNode a,
      HospitalMapNode b,
      String textDirection,
      DirectionStep prev,
      DirectionStep next) {
    pointA = a;
    pointB = b;
    stepDetails = textDirection;
    prevStep = prev;
    nextStep = next;
    centerX = calcCenterPointX(a, b);
    centerY = calcCenterPointY(a, b);

    zoom(a, b);
  }

  private double calcCenterPointX(HospitalMapNode a, HospitalMapNode b) {
    return a.getxCoord() + -1 * (a.getxCoord() - b.getxCoord() / 2);
  }

  private double calcCenterPointY(HospitalMapNode a, HospitalMapNode b) {
    return a.getyCoord() + -1 * (a.getyCoord() - b.getyCoord() / 2);
  }

  private void zoom(HospitalMapNode a, HospitalMapNode b) {
    // loading scene fxml
    try {
      FXMLLoader loader =
          new FXMLLoader(getClass().getClassLoader().getResource("fxml/map/Pathfinding.fxml"));
      Parent root = loader.load();
      // getting controller object
      MapPathfindingController controller = loader.getController();

      double width = Math.abs(a.getxCoord() - b.getxCoord());
      double height = Math.abs(a.getyCoord() - b.getyCoord());
      controller.zoomToPoint(centerX, centerY, width, height, 0);

    } catch (IOException e) {
      e.printStackTrace();
    }

    // MapPathfindingController.getInstance().zoomToPoint(centerX, centerY, width, height, 0);
  }
}
