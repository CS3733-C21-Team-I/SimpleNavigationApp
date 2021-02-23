package edu.wpi.cs3733.c21.teamI.view;

import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapCSVBuilder;
import edu.wpi.cs3733.c21.teamI.view.MapEditManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class HomeController extends Application {
	MapEditManager mapManager;

	//init() calls initClock()
	@FXML
	Label dateTime;

	@FXML
	public void initClock() {
		Timeline clock =
				new Timeline(
						new KeyFrame(
								Duration.ZERO,
								e -> {
									DateTimeFormatter formatter =
											DateTimeFormatter.RFC_1123_DATE_TIME; // "yyyy-MM-dd HH:mm:ss"
									if (dateTime != null) {
										String dateTimeString = ZonedDateTime.now().format(formatter);
										dateTime.setText(dateTimeString.substring(0, dateTimeString.length() - 9));
									}
								}),
						new KeyFrame(Duration.seconds(1)));
		clock.setCycleCount(Animation.INDEFINITE);
		clock.play();
	}

	@FXML
	public void exit() {
		mapManager.getStage().close();

		HospitalMapCSVBuilder.saveCSV(
				NavDatabaseManager.getInstance().loadMapsFromMemory().values(),
				"csv/MapINewNodes.csv",
				"csv/MapINewEdgers.csv");
	}

	public void navigate(ActionEvent e) throws IOException {ApplicationView.navigate(e);}

	@Override
	public void start(Stage primaryStage) throws Exception {

	}
}
