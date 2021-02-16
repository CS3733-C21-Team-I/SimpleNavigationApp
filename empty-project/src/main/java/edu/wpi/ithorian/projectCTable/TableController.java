package edu.wpi.ithorian.projectCTable;

import javafx.scene.Group;
import javafx.stage.Stage;

public class TableController {

  private static TableController ourInstance;
  private Group root = null;
  private Stage stage = null;
  private TableAppView tableAppView = null;

  public static void init() {
    ourInstance = new TableController();
  }

  public static TableController getInstance() {
    return ourInstance;
  }

  public void startTableAppView() {
    this.root = new Group();
    TableAppView tableAppView = new TableAppView();
  }

  public TableController() {}

  public void setRoot(Group root) {
    this.root = root;
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }
}
