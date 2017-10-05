package schmitt.mmas.view;

import schmitt.mmas.graph.Edge;
import schmitt.mmas.graph.Graph;
import schmitt.mmas.graph.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

/**
 * Thanks: http://www1.cs.columbia.edu/~bert/courses/3137/hw3_files/GraphDraw.java
 */
public class Visualizer extends JFrame {

    private int viewWidth;
    private int viewHeight;
    private int width;
    private int height;
    private double scaleW;
    private double scaleH;

    private ArrayList<ViewNode> nodes;
    private ArrayList<ViewEdge> edges;

    private Graph graph;
    
    private JLabel stats;

    public Visualizer(Graph graph) {
        super();
        this.graph = graph;
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.stats = new JLabel();
        this.pack();
        this.setLocationRelativeTo(null);
        this.add(stats, BorderLayout.SOUTH);
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
        try {
            Thread.sleep(1000);
        } catch (Exception ex) {
        }
        nodes = new ArrayList<ViewNode>();
        edges = new ArrayList<ViewEdge>();

        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                Component c = (Component) evt.getSource();
                viewWidth = c.getWidth();
                viewHeight = c.getHeight();
                width = 1;
                height = 1;
                scaleW = viewWidth / Math.abs(graph.getUpperX() - graph.getLowerX());
                scaleH = viewHeight / Math.abs(graph.getUpperY() - graph.getLowerY());
                scaleW *= .9;
                scaleH *= .9;
                draw(null);
            }
        });

        viewWidth = this.getWidth();
        viewHeight = this.getHeight();
        width = 1;
        height = 1;
        scaleW = viewWidth / Math.abs(graph.getUpperX() - graph.getLowerX());
        scaleH = viewHeight / Math.abs(graph.getUpperY() - graph.getLowerY());
        scaleW *= .9;
        scaleH *= .9;
    }

    public void draw(int[] tour) {
        this.nodes.clear();
        this.edges.clear();
        for (Node node : graph.getNodes()) {
            int x = (int) (scaleW * (node.getX() - graph.getLowerX()));
            int y = (int) (scaleH * (graph.getUpperY() - node.getY()));
            this.addNode(String.valueOf(node.getId()), x, y);
            for (Edge edges : node.getEdges()) {
                this.addEdge(node.getId(), edges.getTo().getId());
            }
        }
//        for(int i = 0; i < tour.length - 1; i++) {
//            this.addEdge(tour[i], tour[i + 1]);
//        }
        this.repaint();
    }

    public void setStat(String text) {
        this.stats.setText(text);
    }

    class ViewNode {
        int x, y;
        String name;

        public ViewNode(String myName, int myX, int myY) {
            x = myX;
            y = myY;
            name = myName;
        }
    }

    class ViewEdge {
        int i, j;

        public ViewEdge(int ii, int jj) {
            i = ii;
            j = jj;
        }
    }

    // Add a node at pixel (x,y)
    public void addNode(String name, int x, int y) {
        nodes.add(new ViewNode(name, x, y));
    }

    // Add an ViewEdge between nodes i and j
    public void addEdge(int i, int j) {
        edges.add(new ViewEdge(i, j));
    }

    // Clear and repaint the nodes and edges
    public void paint(Graphics g) {
        super.paint(g);
        if (edges != null) {
            FontMetrics f = g.getFontMetrics();
            int nodeHeight = Math.max(height, f.getHeight());
            g.setColor(Color.black);
            for (ViewEdge e : edges) {
                g.drawLine(nodes.get(e.i).x, nodes.get(e.i).y, nodes.get(e.j).x, nodes.get(e.j).y);
            }
            for (ViewNode n : nodes) {
                g.drawOval(n.x - 1, n.y - 1, 2, 2);
            }
        }
    }
}
