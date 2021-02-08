package edu.wpi.ithorian.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class NavDatabaseManager extends DatabaseManager {

  private static final String DB_URL = "jdbc:derby:navDB";

  private static NavDatabaseManager ourInstance;
  private Connection connection;

  public static void init(boolean regen) {
    ourInstance = new NavDatabaseManager(regen);
  }

  private NavDatabaseManager(boolean regen) {
    super(DB_URL, regen);
  }

  void dropTables() {

  }

  void createTables() {

try {
  Statement stmt= connection.createStatement();
  try{
   stmt.execute("CREATE TABLE navMaps(map_ID integer NOT NULL," +
           " map_Name varchar(45), floor_Number integer, building_Name varchar(45)," +
           " teamAssigned varchar(1), PRIMARY KEY (map_ID))");
   stmt.execute("CREATE TABLE navNodes(node_ID integer NOT NULL," +
           " x_Coord integer NOT NULL, y_Coord integer  NOT NULL,is_Named boolean, node_Type varchar(4)," +
           "long_Name varchar(45), short_Name varchar(45),map_ID, " +
           "PRIMARY KEY(node_ID), FOREIGN KEY (map_ID) references navMaps(map_ID))");
   stmt.execute("CREATE TABLE navEdges(edge_ID integer NOT NULL, " +
           "from_node integer, to_node integer, PRIMARY KEY(edge_ID), " +
           "FOREIGN KEY (from_Node, to_Node) references navNodes(node_ID))");
  }
  catch(SQLException e){
    System.out.println("Error creating tables");
  }
}
catch( Exception e){

e.printStackTrace();
 }
  }


}
