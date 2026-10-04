package automaton;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;

/**
 *
 * @author Christy
 */
public final class NDFAutomaton extends JFrame{
    
    private NDFAGraph automaton;
    private String currentAutomaton;
    private String inputString;
    private GraphPanel graphPanel;
    
    public NDFAutomaton(){
        JFrame frame = new JFrame("NDFA Simulator & Visualizer");
        JPanel controlPanel = new JPanel();
        JPanel outputPanel = new JPanel();

        JButton runAutomaton = new JButton("Run");
        runAutomaton.setEnabled(false);
        JButton automaton1Button = new JButton("Automaton 1");
        JButton automaton2Button = new JButton("Automaton 2");
        JButton automaton3Button = new JButton("Automaton 3");

        JTextField input = new JTextField("");
        input.setToolTipText("Enter input string here");

        JLabel inputLabel = new JLabel("Input String: e.g. abba");

        JTextArea output = new JTextArea("");
        output.setEditable(false);

        graphPanel = new GraphPanel();
        
        automaton1Button.addActionListener((e) -> {
            Node node0 = new Node(0);
            Node node1 = new Node(1);
            Node node2 = new Node(2);
            Node node3 = new Node(3);
            List<Edge> edges = Arrays.asList(
                new Edge(node0, node0, "a"), new Edge(node0, node1, "b"),
                new Edge(node0, node2, "a"), new Edge(node1, node3, "b"),
                new Edge(node2, node3, "b")
            );
            automaton = new NDFAGraph(edges);
            automaton.setInitialNode(node0);
            automaton.setFinalNode(node3);
            currentAutomaton = "1";
            output.setText("Automaton 1 loaded. Accepts strings consisting of 'a' and 'b'.");
            graphPanel.setGraph(automaton);
            runAutomaton.setEnabled(true);
            
        });
        
        automaton2Button.addActionListener((e) -> {
            Node node0 = new Node(0);
            Node node1 = new Node(1);
            Node node2 = new Node(2); 
            List<Edge> edges = Arrays.asList(
                new Edge(node0, node0, "a"), new Edge(node0, node0, "b"),
                new Edge(node0, node1, "a"), new Edge(node1, node2, "a"),
                new Edge(node2, node1, "b")
            );
            automaton = new NDFAGraph(edges);
            automaton.setInitialNode(node0);
            automaton.setFinalNode(node2);
            currentAutomaton = "2";
            output.setText("Automaton 2 loaded. Accepts strings consisting of 'a' and 'b'.");
            graphPanel.setGraph(automaton);
            runAutomaton.setEnabled(true);
        });
        
        automaton3Button.addActionListener((e) -> {
            Node node0 = new Node(0);
            Node node1 = new Node(1);
            Node node2 = new Node(2);
            List<Edge> edges = Arrays.asList(
                new Edge(node0, node0, "a"), new Edge(node0, node1, "b"),
                new Edge(node1, node1, "a"), new Edge(node1, node2, "b"),
                new Edge(node2, node2, "a"), new Edge(node2, node2, "b")
            );
            automaton = new NDFAGraph(edges);
            automaton.setInitialNode(node0);
            automaton.setFinalNode(node1);
            currentAutomaton = "3";
            output.setText("Automaton 3 loaded. Accepts strings consisting of 'a' and 'b'.");
            graphPanel.setGraph(automaton);
            runAutomaton.setEnabled(true);
        });
        
        runAutomaton.addActionListener((e) -> {
            if (automaton == null || automaton.getInitialNode() == null || automaton.getFinalNode() == null) {
                output.setText("Please select an automaton first.");
                return;
            }
            inputString = input.getText();
            boolean passed = automaton.processInput(automaton.getInitialNode(), inputString, new ArrayList<>());
            System.out.println(automaton.inputPassedAutomaton());
            output.setText("For Automaton "+ currentAutomaton + "\n" +"With input: " + input.getText() + "\n" +"Input Passed Automaton = " + passed +"\n" +automaton.pathTakenString());
            graphPanel.setActivePath(automaton.getPathTaken());
        });
        
        frame.setLayout(new BorderLayout());
        controlPanel.setLayout(new FlowLayout());
        controlPanel.add(automaton1Button);
        controlPanel.add(automaton2Button);
        controlPanel.add(automaton3Button);
        input.setPreferredSize(new Dimension(200, 50));
        controlPanel.add(new JLabel("Input"));
        controlPanel.add(input);
        controlPanel.add(runAutomaton);
        controlPanel.add(inputLabel);
        
        outputPanel.setLayout(new BorderLayout());
        output.setPreferredSize(new Dimension(300, 100));
        outputPanel.add(new JScrollPane(output), BorderLayout.SOUTH);

        frame.add(controlPanel, BorderLayout.NORTH);
        frame.add(graphPanel, BorderLayout.CENTER);
        frame.add(outputPanel, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        frame.setTitle("NFA Visualizer & Simulator");
        frame.setVisible(true);       
    }
    
    public static void main(String[] args) {
      SwingUtilities.invokeLater(NDFAutomaton::new);
    }
       
}
