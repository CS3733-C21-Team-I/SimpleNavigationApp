package edu.wpi.cs3733.c21.teamI.parking.view;

import com.jfoenix.controls.JFXListView;
import edu.wpi.cs3733.c21.teamI.database.ParkingPeripheralServerManager;
import edu.wpi.cs3733.c21.teamI.parking.Lot;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ActiveLotsController extends Application {

  @FXML private JFXListView lotView;

  private ObservableList<Lot> lots;

  @FXML
  public void initialize() {
    System.out.println("Initialized ActiveLotsController");
    lots =
        FXCollections.observableArrayList(
            ParkingPeripheralServerManager.getInstance().loadLots().values());
    lotView.setItems(lots);
    lotView.setCellFactory(new LotPaneFactory());
    lotView.setOrientation(Orientation.HORIZONTAL);



    //    Timer timer = new Timer();
    //    timer.schedule(
    //        new TimerTask() {
    //
    //          @Override
    //          public void run() {
    //            lotView.refresh();
    //          }
    //        },
    //        0,
    //        10000);
    //
    //    // No this isnt a mistake it is a solution to the most asinine bug I have ever discoverd
    //    // I mean it comment out the second timer and have a look, its bizzare.
    //    Timer timer2 = new Timer();
    //    timer2.schedule(
    //        new TimerTask() {
    //
    //          @Override
    //          public void run() {
    //            lotView.refresh();
    //          }
    //        },
    //        9900,
    //        10000);

    Timeline fiveSecondsWonder =
        new Timeline(
            new KeyFrame(
                Duration.seconds(5),
                new EventHandler<ActionEvent>() {

                  @Override
                  public void handle(ActionEvent event) {
                    //                    lotView.setItems(null);
                    //                    lots =
                    //                        FXCollections.observableArrayList(
                    //
                    // ParkingPeripheralServerManager.getInstance().loadLots().values());
                    //                    lotView.setItems(lots);
                    lotView.refresh();
                    System.out.println("this is called every 5 seconds on UI thread");
                  }
                }));
    fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
    fiveSecondsWonder.play();
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    AnchorPane root = FXMLLoader.load(getClass().getResource("/fxml/ActiveLots.fxml"));
    primaryStage.setTitle("Hospital App");
    Scene applicationScene = new Scene(root, 973, 800);
    primaryStage.setScene(applicationScene);
    primaryStage.show();
  }
}
