package schmitt.mmas.aco;

import schmitt.mmas.graph.Edge;
import schmitt.mmas.graph.Graph;
import schmitt.mmas.graph.Node;

import java.util.*;

public class Ant {

    private Graph graph;

    private Set<Integer> visited;

    private Stack<Integer> bestRoute;

    public Ant(Graph graph) {
        this.graph = graph;
        this.visited = new HashSet<Integer>();
    }

    public List<Integer> findBestRoute(int from, int to) {
        bestRoute = new Stack<Integer>();
        int current = from;
        bestRoute.push(current);
        visited.add(current);
        while(current != to) {
            Node nextNode = getProportionalNextNode(current, to);
            if(nextNode == null) {
                bestRoute.pop();
                current = bestRoute.peek();
            } else {
                bestRoute.push(nextNode.getId());
                visited.add(nextNode.getId());
                if(nextNode.getId() == to) {
                    break;
                } else {
                    current = nextNode.getId();
                }
            }
        }
        return bestRoute;
    }

    private Node getProportionalNextNode(int current, int to) {
        Node currentNode = graph.getNode(current);
        if(currentNode.getEdges().size() == 0) return null;
        Edge[] edges = currentNode.getEdges().toArray(new Edge[] {});
        double[] probs = new double[edges.length];
        double cumSum = 0.0;
        for(int i = 0; i < edges.length; i++) {
            if(edges[i].getTo().getId() == to) return edges[i].getTo();
            if(visited.contains(edges[i].getTo().getId())) {
                probs[i] = 0.0;
            } else {
                probs[i] = heuristic(current, edges[i].getTo().getId(), to);
                cumSum += probs[i];
            }
        }
        if(cumSum <= 0) return null;
        double rand = Math.random() * cumSum;
        int j = 0;
        double probability = probs[j];
        while(probability <= rand) {
            j++;
            probability += probs[j];
        }
        if(j == edges.length) {
            return null;
        } else {
            return edges[j].getTo();
        }
    }

    private double heuristic(int current, int from, int to) {
        Node fromNode = graph.getNode(from);
        Node toNode = graph.getNode(to);
        Node currentNode = graph.getNode(current);
        //double dist = Math.sqrt(Math.pow(fromNode.getX() - toNode.getX(), 2) + Math.pow(fromNode.getY() - toNode.getY(), 2));
        double distFrom = getLatToMeter(fromNode.getX(), fromNode.getY(), toNode.getX(), toNode.getY());
        double distCurrent = getLatToMeter(currentNode.getX(), currentNode.getY(), toNode.getX(), toNode.getY());
        double dist = 1.0 / (distFrom / distCurrent);
        return dist;
    }

    private double getLatToMeter(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);
        return dist;
    }

    public double getCost() {
        double total = 0.0;
        for(int i = 0; i < bestRoute.size() - 1; i++) {
            Node fromNode = graph.getNode(bestRoute.get(i));
            Node toNode = graph.getNode(bestRoute.get(i + 1));
            total += getLatToMeter(fromNode.getX(), fromNode.getY(), toNode.getX(), toNode.getY());
        }
        return total;
    }
}
