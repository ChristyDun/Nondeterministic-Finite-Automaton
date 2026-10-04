package automaton;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;

public class GraphPanel extends JPanel {

	private NDFAGraph graph;
	private List<Node> activePath = new ArrayList<>();

	public GraphPanel() {
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(500,500));
	}

	public void setGraph(NDFAGraph graph){
		this.graph = graph;
		this.activePath.clear();
		repaint();
	}

	public void setActivePath(List<Node> path) {
		this.activePath = new ArrayList<>(path);
		repaint();
	}

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (graph == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Collect distinct nodes
        List<Node> nodes = getUniqueNodes();
        int nodeCount = nodes.size();
        if (nodeCount == 0) return;

        // Calculate node coordinates in a circular layout
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int layoutRadius = Math.min(centerX, centerY) - 80;
        int nodeRadius = 22;

        Point[] coords = new Point[nodeCount];
        for (int i = 0; i < nodeCount; i++) {
            double angle = 2 * Math.PI * i / nodeCount - Math.PI / 2;
            int x = (int) (centerX + layoutRadius * Math.cos(angle));
            int y = (int) (centerY + layoutRadius * Math.sin(angle));
            coords[i] = new Point(x, y);
        }

        Map<Object, Object> mergedEdges = new HashMap<>();
        for(Edge edge : graph.edge_list) {
            String key = edge.getSrc().getValue() + "->" + edge.getDest().getValue();
            if (mergedEdges.containsKey(key)) {
                mergedEdges.put(key, mergedEdges.get(key) + "," + edge.getKey());
            } else {
                mergedEdges.put(key, edge.getKey());
            }
        }

        for (Map.Entry<Object, Object> entry : mergedEdges.entrySet()) {
            String[] parts = entry.getKey().toString().split("->");
            int srcValue = Integer.parseInt(parts[0]);
            int destValue = Integer.parseInt(parts[1]);
            String mergedKeys = entry.getValue().toString();

            int srcIdx = getNodeIndex(nodes, new Node(srcValue));
            int destIdx = getNodeIndex(nodes, new Node(destValue));

            if(srcIdx != -1 && destIdx != -1) {
                Point p1 = coords[srcIdx];
                Point p2 = coords[destIdx];

                if (srcIdx == destIdx) {
                    // Draw Self-Loop
                    g2.setColor(Color.DARK_GRAY);
                    g2.drawOval(p1.x - 15, p1.y - 35, 30, 30);
                    g2.drawString(mergedKeys, p1.x, p1.y - 40);
                } else {
                    // Draw Directed Edge
                    boolean  isBidirectional = mergedEdges.containsKey(destValue + "->" + srcValue);
                    if (isBidirectional) {
                        drawCurvedEdge(g2, p1, p2, mergedKeys, nodeRadius);
                    } else {
                        g2.setColor(Color.DARK_GRAY);
                        drawArrow(g2, p1.x, p1.y, p2.x, p2.y, nodeRadius);

                        int midX = (p1.x + p2.x) / 2;
                        int midY = (p1.y + p2.y) / 2;
                        g2.setColor(Color.BLUE);
                        g2.drawString(mergedKeys, midX + 5, midY - 5);
                }
            }


        }

        // 2. Draw Nodes
        for (int i = 0; i < nodeCount; i++) {
            Node node = nodes.get(i);
            Point p = coords[i];

            boolean isInPath = false;
            for (Node pathNode : activePath) {
                if (pathNode.getValue() == node.getValue()) {
                    isInPath = true;
                    break;
                }
            }

            boolean isInitial = (graph.getInitialNode() != null && node.getValue() == graph.getInitialNode().getValue());
            boolean isFinal = (graph.getFinalNode() != null && node.getValue() == graph.getFinalNode().getValue());

                if (isInPath) {
                    g2.setColor(new Color(180, 230, 180));
                }
                else {
                    g2.setColor(Color.CYAN);
                }

                g2.fillOval(p.x - nodeRadius, p.y - nodeRadius, 2 * nodeRadius, 2 * nodeRadius);

                if (isFinal) {
                    g2.setColor(Color.BLACK);
                    g2.drawOval(p.x - nodeRadius + 3, p.y - nodeRadius + 3, 2 * (nodeRadius - 3), 2 * (nodeRadius - 3));
                }

                g2.setColor(Color.BLACK);
                g2.drawOval(p.x - nodeRadius, p.y - nodeRadius, 2 * nodeRadius, 2 * nodeRadius);

                String label = "q" + node.getValue();
                FontMetrics fm = g2.getFontMetrics();
                int tx = p.x - fm.stringWidth(label) / 2;
                int ty = p.y + fm.getAscent() / 2 - 2;
                g2.drawString(label, tx, ty);

                if (isInitial) {
                    g2.drawString("Start →", p.x - nodeRadius - 50, p.y + 5);
                }
            }
        }
    }
     

    private void drawCurvedEdge(Graphics2D g2, Point p1, Point p2, String lable, int radius){
        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        double offset = 35; // Adjust this value for more or less curvature
        double midX = (p1.x + p2.x) / 2;
        double midY = (p1.y + p2.y) / 2;

        double controlX = midX - offset * (dy / distance);
        double controlY = midY + offset * (dx / distance);

        double angleStart = Math.atan2(controlY - p1.y, controlX - p1.x);
        double angleEnd = Math.atan2(p2.y - controlY, p2.x - controlX);

        int startX = (int) (p1.x + radius * Math.cos(angleStart));
        int startY = (int) (p1.y + radius * Math.sin(angleStart));
        int endX = (int) (p2.x + radius * Math.cos(angleEnd));
        int endY = (int) (p2.y + radius * Math.sin(angleEnd));

        QuadCurve2D curve = new QuadCurve2D.Double(startX, startY, controlX, controlY, endX, endY);
        g2.setColor(Color.DARK_GRAY);
        g2.draw(curve);

        double tangentX = endX - controlX;
        double tangentY = endY - controlY;
        double tangentAngle = Math.atan2(tangentY, tangentX);

        drawArrowHead(g2, endX, endY, tangentAngle);

        g2.setColor(Color.BLUE);
        FontMetrics fm = g2.getFontMetrics();
        int labelX = (int) (0.25 * startX + 0.5 * controlX + 0.25 * endX) - fm.stringWidth(lable) / 2;
        int labelY = (int) (0.25 * startY + 0.5 * controlY + 0.25 * endY);
        g2.drawString(lable, labelX, labelY);
        }

    private void drawArrow(Graphics2D g2, int x1, int y1, int x2, int y2, int radius) {
        double angle = Math.atan2(y2 - y1, x2 - x1);
        int startX = (int) (x1 + radius * Math.cos(angle));
        int startY = (int) (y1 + radius * Math.sin(angle));
        int endX = (int) (x2 - radius * Math.cos(angle));
        int endY = (int) (y2 - radius * Math.sin(angle));

        g2.drawLine(startX, startY, endX, endY);
        drawArrowHead(g2, endX, endY, angle);
    }

    private void drawArrowHead(Graphics2D g2, int tipX, int tipY, double angle) {
        double arrowLength = 10;
        double arrowWidth = 6;

        double x1 = tipX -arrowLength * Math.cos(angle) + arrowWidth * Math.sin(angle);
        double y1 = tipY -arrowLength * Math.sin(angle) - arrowWidth * Math.cos(angle);
        double x2 = tipX -arrowLength * Math.cos(angle) - arrowWidth * Math.sin(angle);
        double y2 = tipY -arrowLength * Math.sin(angle) + arrowWidth * Math.cos(angle);

        Path2D arrowHead = new Path2D.Double();
        arrowHead.moveTo(tipX, tipY);
        arrowHead.lineTo(x1, y1);
        arrowHead.lineTo(x2, y2);
        arrowHead.closePath();

        Color originalColor = g2.getColor();
        g2.setColor(Color.DARK_GRAY);
        g2.fill(arrowHead);
        g2.setColor(originalColor);
    }

    private List<Node> getUniqueNodes() {
        List<Node> list = new ArrayList<>();
        if (graph == null) return list;
        for (Edge e : graph.edge_list) {
            if (getNodeIndex(list, e.getSrc()) == -1) list.add(e.getSrc());
            if (getNodeIndex(list, e.getDest()) == -1) list.add(e.getDest());
        }
        return list;
    }

    private int getNodeIndex(List<Node> list, Node node) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getValue() == node.getValue()) return i;
        }
        return -1;
    }
}