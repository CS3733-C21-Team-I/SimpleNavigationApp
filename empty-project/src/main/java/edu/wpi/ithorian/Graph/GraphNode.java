package Graph;

import java.util.Set;
import java.util.*;

public interface GraphNode<T extends GraphNode<T>> {
    String getID();
    int getXcoord();
    int getYcoord();

    List<T> getConnections();
}
