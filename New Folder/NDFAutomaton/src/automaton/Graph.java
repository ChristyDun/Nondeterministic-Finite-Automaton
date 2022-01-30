package automaton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Christy
 */
public class Graph {
    
    private Map<Vertex, List<Vertex>> vertexList;

    public Graph(Map<Vertex, List<Vertex>> vertexList) {
        this.vertexList = vertexList;
    }

    public Map<Vertex, List<Vertex>> getVertexList() {
        return vertexList;
    }

    public void setVertexList(Map<Vertex, List<Vertex>> vertexList) {
        this.vertexList = vertexList;
    }
    
    public void addVertex(String label){
        vertexList.putIfAbsent(new Vertex(label), new ArrayList<>());
    }
    
    public void removeVertex(String label){
        Vertex v = new Vertex(label);
        vertexList.values().stream().forEach(e -> e.remove(v));
        vertexList.remove(new Vertex(label));
    }
    
    public void addEdge(String label1, String label2){
        Vertex v1 = new Vertex(label1);
        Vertex v2 = new Vertex(label2);
        vertexList.get(v1).add(v2);
        vertexList.get(v2).add(v1);
    }
    
    public void removeEdge(String label1, String label2){
        Vertex v1 = new Vertex(label1);
        Vertex v2 = new Vertex(label2);
        List<Vertex> ev1 = vertexList.get(v1);
        List<Vertex> ev2 = vertexList.get(v2);
        if (ev1 != null) {
            ev1.remove(v2);
        }
        if (ev2 != null){
            ev2.remove(v1);
        }
    }
    
}
