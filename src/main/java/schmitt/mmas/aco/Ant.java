package schmitt.mmas.aco;

import schmitt.mmas.graph.Edge;
import schmitt.mmas.graph.Node;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class Ant {

    private Globals _globals;

    private Stack<Node> route;

    private Set<Node> visited;

    private double cost;

    public Ant(Globals globals) {
        _globals = globals;
        route = new Stack<>();
        visited = new HashSet<>();
        cost = Double.MAX_VALUE;
    }

    public void nnTour() {
        visited = new HashSet<>();
        route = new Stack<>();
        cost = Double.MAX_VALUE;
        Node current = _globals.sourceNode;
        route.add(current);
        visited.add(current);
        while (current != _globals.targetNode) {
            Node nextNode = selectNextNearNode(current);
            if(nextNode == null) {
                route.pop();
                current = route.peek();
            } else {
                route.push(nextNode);
                visited.add(nextNode);
                if(nextNode == _globals.targetNode) {
                    break;
                } else {
                    current = nextNode;
                }
            }
        }
        calculateCost();
    }

    private Node selectNextNearNode(Node currentNode) {
        if(currentNode.getEdges().isEmpty()) return null;
        double maxGain = 0.0;
        Node nextNode = null;
        for(Edge edge : currentNode.getEdges()) {
            if(!visited.contains(edge.getTo()) && _globals.HEURISTIC(edge) >= maxGain) {
                nextNode = edge.getTo();
                maxGain = _globals.HEURISTIC(edge);
            }
        }
        return nextNode;
    }

    public void calculateCost() {
        cost = 0.0;
        for(int i = 0; i < route.size() - 1; i++) {
            cost += _globals.calculateDistanceInMeters(route.get(i), route.get(i + 1));
        }
    }

    public Stack<Node> getRoute() {
        return route;
    }

    public void setRoute(Stack<Node> route) {
        this.route = route;
    }

    public Set<Node> getVisited() {
        return visited;
    }

    public void setVisited(Set<Node> visited) {
        this.visited = visited;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
