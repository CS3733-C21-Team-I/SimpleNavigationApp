package edu.wpi.ithorian.projectCTable;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class TableAppView extends Application {

  public static void main(String[] args) {
    Font.loadFont(TableAppView.class.getResourceAsStream("/OpenSans-Regular.ttf"), 16);
    launch(args);
  }

  @Override
  public void init() {
    System.out.println("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/NodeTable.fxml"));
    primaryStage.setTitle("Table View");
    primaryStage.setScene(new Scene(root, 973, 800));
    primaryStage.show();
  }

  @Override
  public void stop() {
    System.out.println("Shutting Down");
  }
}
