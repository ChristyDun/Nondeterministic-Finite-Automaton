package automaton;

/**
 *
 * @author Christy
 */
public class Edge {
    
    Node src;
    Node dest;
    String key;

    public Edge(Node src, Node dest,String key) {
        this.src = src;
        this.dest = dest;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public Node getSrc() {
        return src;
    }

    public Node getDest() {
        return dest;
    }
    
    
    
}
