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
        while(!terminateCondition()) {
            constructSolutions();
            updateStatistics();
            pheromoneTrailUpdate();
            searchControlAndStatistics();
            _globals.iteration++;
        }
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
        _globals.restartFoundBestIteration = 0;
        _globals.foundBestIteration = 0;
        _globals.ants[0].nnTour();
        _globals.trailMax = 1.0 / (_globals.rho * _globals.ants[0].getCost());
        _globals.trailMin = _globals.trailMax / (2.0 * _globals.graph.getNodes().size());
        initPheromoneTrails(_globals.trailMax);
        computeTotalInformation();
    }

    private void initPheromoneTrails(double trail) {
        for(Edge edge : _globals.graph.getEdges()) {
            edge.setPheromone(trail);
            edge.setTotal(trail);
        }
    }

    private void computeTotalInformation() {
        for(Edge edge : _globals.graph.getEdges()) {
            double value = Math.pow(edge.getPheromone(), _globals.alpha) * Math.pow(_globals.HEURISTIC(edge), _globals.beta);
            edge.setTotal(value);
        }
    }

    private boolean terminateCondition() {
        return _globals.timer.elapsedTime() > _globals.maxTime * 1000 || _globals.iteration > _globals.maxIterations;
    }

    private void constructSolutions() {
        for(Ant ant : _globals.ants) {
            ant.heuristicTour();
        }
    }

    public void updateStatistics() {
        Ant iterationBestAnt = findBestAnt();
        if(iterationBestAnt.getCost() < _globals.bestSoFar.getCost()) {
            _globals.bestSoFar = iterationBestAnt.clone();
            _globals.restartBestAnt = iterationBestAnt.clone();
            _globals.foundBestIteration = _globals.iteration;
            _globals.restartFoundBestIteration = _globals.iteration;
            _globals.trailMax = 1.0 / (_globals.rho * _globals.bestSoFar.getCost());
            _globals.trailMin = _globals.trailMax / (2.0 / _globals.graph.getNodes().size());
        }
        if(iterationBestAnt.getCost() < _globals.restartBestAnt.getCost()) {
            _globals.restartBestAnt = iterationBestAnt.clone();
            _globals.restartFoundBestIteration = _globals.iteration;
        }
    }

    public Ant findBestAnt() {
        Ant bestAnt = _globals.ants[0];
        for(int i = 1; i < _globals.numberAnts; i++) {
            if(_globals.ants[i].getCost() < bestAnt.getCost()) {
                bestAnt = _globals.ants[i];
            }
        }
        return bestAnt;
    }

    private void pheromoneTrailUpdate() {
        pheromoneEvaporation();
        if(_globals.iteration % _globals.uGb == 0) {
            pheromoneUpdate(findBestAnt());
        } else if (_globals.uGb == 1 && (_globals.iteration - _globals.restartFoundBestIteration) > 50) {
            pheromoneUpdate(_globals.bestSoFar);
        } else {
            pheromoneUpdate(_globals.restartBestAnt);
        }
        checkPheromoneTrails();
        computeTotalInformation();
    }

    private void pheromoneEvaporation() {
        for(Edge edge : _globals.graph.getEdges()) {
            edge.setPheromone((1.0 - _globals.rho) * edge.getPheromone());
        }
    }

    private void pheromoneUpdate(Ant ant) {
        double dTau = 1.0 / ant.getCost();
        for(int i = 0; i < ant.getRoute().size() - 1; i++) {
            int fromId = ant.getRoute().get(i).getId();
            int toId = ant.getRoute().get(i + 1).getId();
            _globals.graph.getEdge(fromId, toId).setPheromone(dTau);
        }
    }

    private void checkPheromoneTrails() {
        for(Edge edge : _globals.graph.getEdges()) {
            if(edge.getPheromone() < _globals.trailMin) {
                edge.setPheromone(_globals.trailMin);
            }
            if(edge.getPheromone() > _globals.trailMax) {
                edge.setPheromone(_globals.trailMax);
            }
        }
    }

    private void searchControlAndStatistics() {
        _globals.branchFactor = calculateBranchingFactor();
    }

    private double calculateBranchingFactor() {
        double min, max, cutoff;
        double[] numBranches;

        return 0.0;
    }
}
