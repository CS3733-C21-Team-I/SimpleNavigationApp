package edu.wpi.ithorian;

import edu.wpi.ithorian.database.NavDatabaseManager;
import edu.wpi.ithorian.hospitalMap.HospitalMap;

import edu.wpi.ithorian.projectCTable.TableAppView;

import java.util.Arrays;
import javafx.application.Application;

public class Main {

  public static void main(String[] args) {

    if ((args.length > 0) && Arrays.asList(args).contains("regenerate")) {
      NavDatabaseManager.init(true);
    } else {
      NavDatabaseManager.init(false);
    }


    String os = System.getProperty("os.name");
    System.out.println(os);
    String path =
        System.getProperty("user.dir")
            + "\\src\\main\\java\\edu\\wpi\\ithorian\\hospitalMap\\mapEditing\\";

    Application.launch(TableAppView.class);
    // Application.launch(ApplicationView.class);
  }
}
