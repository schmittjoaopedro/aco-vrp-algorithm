package schmitt.mmas.aco;

import schmitt.mmas.graph.Edge;
import schmitt.mmas.graph.Graph;
import schmitt.mmas.graph.Node;

public class VRPSolver {

    private Globals _globals;

    public VRPSolver(Graph graph, Node sourceNode, Node targetNode) {
        _globals = new Globals();
        _globals.graph = graph;
        _globals.sourceNode = sourceNode;
        _globals.targetNode = targetNode;
    }

    public void solve() {
        // Configuration phase
        _globals.timer.startTimer();
        allocateAnts();
        restartMatrices();
        System.out.println("Configuration took: " + _globals.timer.elapsedTime());

        // MM-AS Heuristic phase
        initTry();
    }

    private void allocateAnts() {
        _globals.ants = new Ant[_globals.numberAnts];
        for(int i = 0; i < _globals.numberAnts; i++) {
            _globals.ants[i] = new Ant(_globals);
        }
        _globals.bestSoFar = new Ant(_globals);
        _globals.restartBestAnt = new Ant(_globals);
    }

    private void restartMatrices() {
        for(Edge edge : _globals.graph.getEdges()) {
            edge.setPheromone(0.0);
            edge.setTotal(0.0);
        }
    }

    private void initTry() {
        _globals.timer.startTimer();
        _globals.iteration = 0;
        _globals.restartIteration = 0;
        _globals.foundBestIteration = 0;
    }


}
