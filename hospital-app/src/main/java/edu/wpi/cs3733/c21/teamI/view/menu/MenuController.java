package edu.wpi.cs3733.c21.teamI.view.menu;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MenuController implements Initializable {
  @FXML JFXHamburger ham1;
  @FXML JFXDrawer drawer;
  @FXML StackPane replacePane;
  @FXML Label titleLabel;

  public void initialize(URL url, ResourceBundle rb) {
    VBox box = null;
    try {
      box = FXMLLoader.load(getClass().getResource("/fxml/menuFiles/VisitorMenu.fxml"));
      titleLabel.setText("Language Service Request");
      replacePane
          .getChildren()
          .add(
              FXMLLoader.load(
                  getClass().getResource("/fxml/serviceRequests/LanguageRequest.fxml")));
    } catch (IOException e) {
      e.printStackTrace();
    }
    drawer.setSidePane(box);

    HamburgerSlideCloseTransition hamburgerTransition = new HamburgerSlideCloseTransition(ham1);
    hamburgerTransition.setRate(-1);
    ham1.addEventHandler(
        MouseEvent.MOUSE_CLICKED,
        (e) -> {
          hamburgerTransition.setRate(hamburgerTransition.getRate() * -1);
          hamburgerTransition.play();

          if (drawer.isOpened()) {
            drawer.close();
          } else {
            drawer.open();
          }
        });
  }
}
