package automaton;
import java.util.ArrayList;

/**
 *
 * @author Christy
 */
public class UnusedPaths {
    
    Edge edge;
    String currentInputString;
    Node currentNode;
    ArrayList<Node> pathTaken = new ArrayList<>();

    public UnusedPaths(Edge edge, String currentInputString, Node currentNode, ArrayList<Node> pathTaken) {
        this.edge = edge;
        this.currentInputString = currentInputString;
        this.currentNode = currentNode;
        this.pathTaken = pathTaken;
    }   

    public Node getCurrentNode() {
        return currentNode;
    }
    
    public Edge getEdge() {
        return edge;
    }

    public String getCurrentInputString() {
        return currentInputString;
    }

    public ArrayList<Node> getPathTaken() {
        return pathTaken;
    }
    
}
