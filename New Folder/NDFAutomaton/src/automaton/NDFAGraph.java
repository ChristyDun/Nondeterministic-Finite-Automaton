package automaton;

import java.util.*;

/**
 *
 * @author Christy
 */
public class NDFAGraph {

    String inputString;
    String currentInputString;
    Node initialNode = null;
    Node finalNode = null;
    Node currentNode = null;
    Node destinationNode = null;
    boolean multiplePaths;
    UnusedPaths unusedPair;
    boolean actionTaken;
    boolean inputStringFailed;
    boolean inputPassed;       
    String pathTakenString;
    ArrayList<Node> pathTaken = new ArrayList<>();
    Stack<Character> inputStringAsChars = new Stack<>();
    Stack<Character> tempStack = new Stack<>();

    public Node getInitialNode() {
        return initialNode;
    }

    public void setInitialNode(Node initialNode) {
        this.currentNode = initialNode;
    }

    public Node getFinalNode() {
        return finalNode;
    }

    public void setFinalNode(Node finalNode) {
        this.finalNode = finalNode;
    }

    public Node getCurrentNode() {
        return currentNode;
    }

    public ArrayList<Node> getPathTaken() {
        return pathTaken;
    }

    List<List<Node>> node_list = new ArrayList<>();
    List<Edge> edge_list = new ArrayList<>();
    Stack<UnusedPaths> unusedPaths = new Stack<>();

    public NDFAGraph(List<Edge> edges) {
        edge_list = edges;
        for (int i = 0; i < edges.size(); i++) {
            node_list.add(i, new ArrayList<>());
        }
        edges.forEach((e) -> {
            node_list.get(e.src.getValue()).add(new Node(e.dest.getValue()));
        });
    }

    public void printNodes() {
        node_list.forEach((node_list1) -> {
            for (int i = 0; i < node_list1.size(); i++) {
                System.out.println(node_list1.get(i).getValue());
            }
        });
    }

    public void printEdges() {
        for (int i = 0; i < edge_list.size(); i++) {
            System.out.println("Edge" + edge_list.get(i).src.getValue() + "-" + edge_list.get(i).dest.getValue() + " " + edge_list.get(i).key);
        }
    }
    
    public String pathTakenString(){
        pathTakenString = "";
        pathTakenString = pathTakenString + "Path Taken = ";
        pathTaken.forEach((node) -> {
            pathTakenString = pathTakenString + " Node: ";
            pathTakenString = pathTakenString + String.valueOf(node.getValue()) ; 
        });
        return pathTakenString;
    }

    //method for chnage node (follow an Edge)
    public Node changeNode(String Key) {
        multiplePaths = false;
        if (currentNode != null) {
            edge_list.forEach((e) -> {
                actionTaken = false;
                //Change Node Based of Possible edge paths with key
                if (e.key.equals(Key) && e.getSrc().getValue() == currentNode.getValue() && !multiplePaths && actionTaken == false) {
                    destinationNode = e.dest;
                    multiplePaths = true;
                    actionTaken = true;

                } //If multiple possible edge paths, add not used to stack with saved state of input string
                else if (e.key.equals(Key) && e.getSrc().getValue() == currentNode.getValue() && multiplePaths && actionTaken == false) {
                    unusedPair = new UnusedPaths(e, currentInputString, e.src, pathTaken);
                    if (unusedPaths.isEmpty() && actionTaken == false) {
                        unusedPaths.push(unusedPair);
                        System.out.println("From Empty Unused Path Added ");
                        System.out.println("SRC " +unusedPair.getEdge().src.getValue() +" DEST " +unusedPair.getEdge().dest.getValue());
                        actionTaken = true;
                    }
                    else if (unusedPair != unusedPaths.peek() && actionTaken == false) {
                        System.out.println("CURRENT NODE " + currentNode.getValue());
                        unusedPaths.push(unusedPair);
                        System.out.println("Unused Path Added ");
                        System.out.println("SRC " +unusedPair.getEdge().src.getValue() +" DEST " +unusedPair.getEdge().dest.getValue());
                        actionTaken = true;
                    }
                }
            });
        }
        return destinationNode;
    }

    public void followEdge(Edge takenEdge) {
        currentNode = takenEdge.getDest();
        if(pathTaken.size() > 1){
            pathTaken.remove(pathTaken.size()-1);
            System.out.println("PATH TAKEN REMOVED");
        }
        pathTaken.add(currentNode);
        System.out.println("PATH TAKEN ADDED");
        if (currentInputString.length() > 1){
            currentInputString = currentInputString.substring(1);
        }
        else if(currentInputString.length() == 1){
            currentInputString = "";
        }        
    }

    public boolean inFinalNode() {
        return currentNode == finalNode;
    }

    public boolean inputPassedAutomaton() {
        return inFinalNode() && currentInputString.isEmpty();
    }

    public void inputAsStackOfChars(String inputString) {
        for (char chars : inputString.toCharArray()) {
            tempStack.add(chars);
        }
        while (!tempStack.empty()) {
            inputStringAsChars.add(tempStack.pop());
        }
    }

    //method for accepting input (Boolean)
    public boolean traverseAutomaton(String inputString) {
        currentInputString = inputString;
        inputStringFailed = false;
        String nextChar;
        inputAsStackOfChars(inputString);
        System.out.println("In Node " + currentNode.getValue());

        while (!inFinalNode() && !inputStringAsChars.isEmpty() && !inputStringFailed && !currentInputString.isEmpty()) {
            for (int i = 0; i < currentInputString.length(); i++) {
                System.out.println("Current String " + currentInputString);
                nextChar = inputStringAsChars.pop().toString();
                System.out.println("Change Node Using " + nextChar);
                currentNode = changeNode(nextChar);
                pathTaken.add(currentNode);
                System.out.println("PATH TAKEN ADDED");
                if (currentInputString.length() > 1){
                    currentInputString = currentInputString.substring(1);
                }
                else if(currentInputString.length() == 1){
                    currentInputString = "";
                }

                i++;
            }
        }
        System.out.println("Current Node " + currentNode.value);
        //If no paths go back to stack and if empty stack dont accept input (return false)
        if (!inFinalNode() && !unusedPaths.isEmpty()) {
            unusedPair = unusedPaths.pop();
            currentNode = unusedPair.getCurrentNode();
            pathTaken = unusedPair.getPathTaken();
            currentInputString = unusedPair.getCurrentInputString();
            inputAsStackOfChars(currentInputString);
            nextChar = inputStringAsChars.pop().toString();
            if (unusedPair.getEdge().key.equals(nextChar) && unusedPair.getEdge().src == currentNode) {
                System.out.println("TAKEN UNUSED PATH - " + unusedPair);
                System.out.println("SRC " +unusedPair.getEdge().src.getValue() +" DEST " +unusedPair.getEdge().dest.getValue() + " KEY "+ unusedPair.getEdge().key);
                System.out.println(currentInputString);
                System.out.println("EDGE FOLLOWED");
                followEdge(unusedPair.getEdge());
                System.out.println("In Node " + currentNode.getValue());
                System.out.println("CURRENT INPUT STRING " + currentInputString);
                traverseAutomaton(currentInputString);
            }
        }

        //If end of input and in final Node accept input (return True)
        return inFinalNode();
    }

    //Paser with input (CYK algorithm?)
}
