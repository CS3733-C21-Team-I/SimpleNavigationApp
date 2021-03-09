package edu.wpi.cs3733.c21.teamI.util;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.image.Image;

public class ImageLoader {

  private static final Map<String, Image> loadedImages = new HashMap<>();

  public static Image loadImage(String url) {
    if (!loadedImages.containsKey(url)) {
      try {
        loadedImages.put(url, new Image((ImageLoader.class.getResource(url)).toURI().toString()));
      } catch (URISyntaxException e) {
        e.printStackTrace();
      }
    }

    return loadedImages.get(url);
  }
}
