package automaton;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.util.*;
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
    private Screen s;
    
    public NDFAutomaton(){
        JFrame frame = new JFrame();
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JButton runAutomaton = new JButton("Run");
        runAutomaton.setEnabled(false);
        JButton automaton1Button = new JButton("Automaton 1");
        JButton automaton2Button = new JButton("Automaton 2");
        JButton automaton3Button = new JButton("Automaton 3");
        JTextField input = new JTextField("");
        JTextArea output = new JTextArea("");
        output.setEditable(false);
        
        automaton1Button.addActionListener((e) -> {
            Node node0 = new Node(0);
            Node node1 = new Node(1);
            Node node2 = new Node(2);
            Node node3 = new Node(3);
            List<Edge> edges = Arrays.asList(new Edge(node0, node0, "a"),new Edge(node0, node1, "b"),new Edge(node0, node2, "a"),new Edge(node1, node3, "b"),new Edge(node2, node3, "b"));
            automaton = new NDFAGraph(edges);
            automaton.setInitialNode(node0);
            automaton.setFinalNode(node3);
            currentAutomaton = "1";
            output.setText("");
            runAutomaton.setEnabled(true);
            
        });
        
        automaton2Button.addActionListener((e) -> {
            Node node0 = new Node(0);
            Node node1 = new Node(1);
            Node node2 = new Node(2); 
            List<Edge> edges = Arrays.asList(new Edge(node0, node0, "a"),new Edge(node0, node0, "b"),new Edge(node0, node1, "a"),new Edge(node1, node2, "a"),new Edge(node2, node1, "b"));
            automaton = new NDFAGraph(edges);
            automaton.setInitialNode(node0);
            automaton.setFinalNode(node2);
            currentAutomaton = "2";
            output.setText("");
            runAutomaton.setEnabled(true);
        });
        
        automaton3Button.addActionListener((e) -> {
            Node node0 = new Node(0);
            Node node1 = new Node(1);
            Node node2 = new Node(2);
            List<Edge> edges = Arrays.asList(new Edge(node0, node0, "a"),new Edge(node0, node1, "b"),new Edge(node1, node1, "a"),new Edge(node1, node2, "b"), new Edge(node2, node2, "a"),new Edge(node2, node2, "b"));
            automaton = new NDFAGraph(edges);
            automaton.setInitialNode(node0);
            automaton.setFinalNode(node1);
            currentAutomaton = "3";
            output.setText("");
            runAutomaton.setEnabled(true);
        });
        
        runAutomaton.addActionListener((e) -> {
            inputString = input.getText();
            automaton.traverseAutomaton(inputString);
            System.out.println(automaton.inputPassedAutomaton());
            output.setText("");
            output.setText("For Automaton "+ currentAutomaton + "\n" +"Input Passed Automaton = " + automaton.inputPassedAutomaton() +"\n" +automaton.pathTakenString());
            runAutomaton.setEnabled(false);
        });
        
        frame.setLayout(new FlowLayout());
        frame.add(panel1);
        frame.add(panel2);
        
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
        panel1.add(automaton1Button);
        panel1.add(automaton2Button);
        panel1.add(automaton3Button);
        input.setPreferredSize(new Dimension(200,50));
        panel1.add(input);
        panel1.add(runAutomaton);
        
        
        
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
        output.setPreferredSize(new Dimension(300,300));
        panel2.add(output);
        panel2.add(new JLabel(new ImageIcon("img/Automata1.jpg")));
        panel2.add(new JLabel(new ImageIcon("img/Automata2.jpg")));
        panel2.add(new JLabel(new ImageIcon("img/Automata3.jpg")));
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(200,30, 800, 600);
        frame.setTitle("Simulator");
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
        panel1.setVisible(true);
        panel2.setVisible(true);
        
        init();    
        
    }
    
    public static void main(String[] args) {
      new NDFAutomaton();
    }
       
    public void init(){
        setLocationRelativeTo(null);
        s = new Screen();
        add(s);
    }
    
    public class Screen extends JPanel{
        public Screen(){
            repaint();
        };
        
        public void paint(Graphics g){
            g.drawOval(100, 100, 50, 50);
        }  
    }
    
}
