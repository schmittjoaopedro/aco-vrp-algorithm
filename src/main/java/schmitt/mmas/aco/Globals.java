package schmitt.mmas.aco;

import schmitt.mmas.graph.Edge;
import schmitt.mmas.graph.Graph;
import schmitt.mmas.graph.Node;
import schmitt.mmas.utils.Timer;

public class Globals {

    public Graph graph;

    public Node sourceNode;

    public Node targetNode;

    public Timer timer;

    public int numberAnts;

    public double alpha;

    public double beta;

    public double rho;

    public double maxTries;

    public double maxTime; // In seconds

    public double branchFactor;

    public int iteration;

    public int restartIteration;

    public int foundBestIteration;

    public double lambda;

    public double trailMax;

    public double trailMin;

    public Ant[] ants;

    public Ant bestSoFar;

    public Ant restartBestAnt;

    public Globals() {
        timer = new Timer();
        numberAnts = 30;
        alpha = 1.0;
        beta = 2.0;
        rho = 0.02;
        maxTries = 100.0;
        maxTime = 10.0;
        branchFactor = 1.00001;
        lambda = 0.05;
        iteration = 0;
        restartIteration = 0;
        foundBestIteration = 0;
    }

    public double HEURISTIC(Edge edge) {
        double distToTarget = calculateDistanceInMeters(edge.getTo(), targetNode);
        double normalizeCost = edge.getDistance() / calculateDistanceInMeters(edge.getFrom(), edge.getTo());
        double cost = 1.0 / (normalizeCost * distToTarget);
        return cost;
    }

    public double calculateDistanceInMeters(Node from, Node to) {
        double earthRadius = 6371000;
        double dY = Math.toRadians(from.getY() - to.getY());
        double dX = Math.toRadians(from.getX() - to.getX());
        double a = Math.sin(dY / 2.0) * Math.sin(dY / 2.0) + Math.cos(Math.toRadians(from.getY())) * Math.cos(Math.toRadians(to.getY())) * Math.sin(dX / 2.0) * Math.sin(dX / 2.0);
        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));
        return earthRadius * c;
    }

}
