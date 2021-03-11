package edu.wpi.cs3733.c21.teamI.user.view;

import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import edu.wpi.cs3733.c21.teamI.user.User;
import javafx.util.Callback;

public class RoleTreeFactory implements Callback<JFXListView<User.Role>, JFXListCell<User.Role>> {
  @Override
  public JFXListCell<User.Role> call(JFXListView<User.Role> param) {
    return new RoleTree();
  }
}
