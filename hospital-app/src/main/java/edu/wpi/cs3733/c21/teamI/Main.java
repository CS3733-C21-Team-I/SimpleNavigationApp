package edu.wpi.cs3733.c21.teamI;

import edu.wpi.cs3733.c21.teamI.database.DatabaseManager;
import edu.wpi.cs3733.c21.teamI.parking.reservations.PeripheralSlipManager;
import edu.wpi.cs3733.c21.teamI.view.HomeController;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import javafx.application.Application;

public class Main {

  public static void main(String[] args) {

    String libName =
        System.getProperty("sun.arch.data.model").equals("32")
            ? "rxtxSerial32.dll"
            : "rxtxSerial.dll"; // The name of the file in resources/ dir
    URL url = Main.class.getResource("/" + libName);
    File tmpDir = null;
    try {
      tmpDir = Files.createTempDirectory("my-native-lib").toFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
    tmpDir.deleteOnExit();
    File nativeLibTmpFile = new File(tmpDir, libName);
    nativeLibTmpFile.deleteOnExit();
    try (InputStream in = url.openStream()) {
      Files.copy(in, nativeLibTmpFile.toPath());
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.load(nativeLibTmpFile.getAbsolutePath());

    List<String> argsList = new ArrayList<String>();
    Map<String, String> optsList = new HashMap<>();
    List<String> doubleOptsList = new ArrayList<String>();

    for (int i = 0; i < args.length; i++) {
      switch (args[i].charAt(0)) {
        case '-':
          if (args[i].length() < 2)
            throw new IllegalArgumentException("Not a valid argument: " + args[i]);
          if (args[i].charAt(1) == '-') {
            if (args[i].length() < 3)
              throw new IllegalArgumentException("Not a valid argument: " + args[i]);
            // --opt
            doubleOptsList.add(args[i].substring(2, args[i].length()));
          } else {
            if (args.length - 1 == i)
              throw new IllegalArgumentException("Expected arg after: " + args[i]);
            // -opt
            optsList.put(args[i], args[i + 1]);
            System.out.println("Added opt: " + args[i] + " " + args[i + 1]);
            i++;
          }
          break;
        default:
          // arg
          argsList.add(args[i]);
          break;
      }
    }

    ApplicationDataController.init();

    if (!argsList.contains("client")) {
      DatabaseManager.startNetworkServer();
    }

    if (argsList.contains("regenerate")) {
      DatabaseManager.initDatabaseManagers(true);
      DatabaseManager.regenTables();
      DatabaseManager.initPeripheralDatabaseManagers(true);
      DatabaseManager.regenPeripheralDB();
    } else {
      DatabaseManager.initDatabaseManagers(false);
      DatabaseManager.initPeripheralDatabaseManagers(false);
    }

    // MapDataEntity.loadMapBackground(); // Done to prevent lag on loading

    if (optsList.containsKey("-p")) {
      System.out.println("Thermal Printer connected at Port: " + optsList.get("-p"));
      PeripheralSlipManager.init(new String[] {optsList.get("-p")});
    } else {
      System.out.println("No thermal printer connected");
      PeripheralSlipManager.init(new String[] {});
    }

    Application.launch(HomeController.class);
  }
}
