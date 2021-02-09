package Graph;

import java.util.Set;

public class Graph<T extends GraphNode> {
    public Set<T> nodes;

    public T getNode(String id){
        return nodes.stream().filter(node -> node.getID().equals(id)).findFirst().orElseThrow(() -> new IllegalArgumentException("No node found with ID"));
    }

}
