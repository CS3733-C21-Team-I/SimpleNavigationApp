package edu.wpi.cs3733.c21.teamI.view.maps;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.*;
import com.google.maps.model.AutocompletePrediction;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.FindPlaceFromText;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public abstract class GoogleMap extends Application {
  @FXML private JFXButton submitButton;
  @FXML JFXTextField addressSearch;
  @FXML JFXListView autofillView;
  @FXML StackPane root;
  @FXML WebView webview;

  String API_KEY = "AIzaSyDffrpgnSe9viRPYC1v_u6qth7IL5ufPJE";
  String url = "https://maps.googleapis.com/maps/api/place/autocomplete/output";
  GeoApiContext context;
  PlaceAutocompleteRequest.SessionToken token;
  String destination =
      "Brigham and Women's Faulkner Hospital, Centre Street, Jamaica Plain, MA, USA";
  String destinationID = "";
  HashMap<String, String> idLookUp;
  String directionsLink = "";

  @FXML
  public void initialize() {
    idLookUp = new HashMap<>();
    context = new GeoApiContext.Builder().apiKey(API_KEY).build();
    token = new PlaceAutocompleteRequest.SessionToken();
    setupRequestView();
    destinationID = searchForPlaceID(destination);
    autofillView.setVisible(false);
    submitButton.setDisable(true);
    additionalInits();
  }

  public abstract void additionalInits();

  public abstract double getZoom();

  @FXML
  public void submit(ActionEvent e) {
    String search = addressSearch.getText();
    getDirections(search);

    // Checks if location has a known id, otherwise, it searches for it
    String originID = "";
    if (idLookUp.containsKey(search)) {
      originID = idLookUp.get(search);
      // System.out.println("Used known ID: " + originID);
    } else originID = searchForPlaceID(search);

    directionsLink = formDirectionsUrl(originID, destinationID);

    submitButton.setVisible(false);
    addressSearch.setVisible(false);
    autofillView.setVisible(false);

    webview.setVisible(true);
    webview.setZoom(getZoom());
    webview.getEngine().load(directionsLink);
    //    getHostServices().showDocument(directionsLink);
  }

  @FXML
  public void autoComplete() {
    String currText = addressSearch.getText();

    if (currText.length() <= 0) {
      autofillView.setVisible(false);
      submitButton.setDisable(true);
    } else {
      autofillView.setVisible(true);
      submitButton.setDisable(false);
    }

    AutocompletePrediction[] result =
        PlacesApi.placeAutocomplete(context, currText, token).awaitIgnoreError();

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    //    System.out.println(gson.toJson(result[0].description));
    List<String> suggestions = new ArrayList<>();

    for (int i = 0; i < result.length; i++) {
      suggestions.add(result[i].description);
      idLookUp.put(result[i].description, result[i].placeId);
    }

    ObservableList<String> items = FXCollections.observableArrayList(suggestions);
    autofillView.setItems(items);
  }

  private void setupRequestView() {
    autofillView
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (ChangeListener<String>)
                (ov, oldVal, newVal) -> {
                  addressSearch.setText(newVal);
                  // serviceLocationList.setVisible(false);
                });
  }

  public void getDirections(String origin) {
    DirectionsResult result =
        DirectionsApi.getDirections(context, origin, destination).awaitIgnoreError();

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    System.out.println(gson.toJson(result));
  }

  private String formDirectionsUrl(String originID, String destinationID) {
    String url = "https://www.google.com/maps/dir/?api=1";

    url += "&origin=start&origin_place_id=" + originID;
    url += "&destination=end&destination_place_id=" + destinationID;

    System.out.println("URL:   " + url);
    return url;
  }

  private String searchForPlaceID(String location) {
    FindPlaceFromText result =
        PlacesApi.findPlaceFromText(
                context, location, FindPlaceFromTextRequest.InputType.TEXT_QUERY)
            .awaitIgnoreError();

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    String id = result.candidates[0].placeId;

    System.out.println(id);
    return id;
  }

  public abstract void switchWindow(ActionEvent actionEvent) throws IOException;

  @Override
  public void start(Stage primaryStage) {}
}
