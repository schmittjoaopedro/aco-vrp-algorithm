package schmitt.mmas;

import org.junit.Test;
import schmitt.mmas.aco.Ant;
import schmitt.mmas.aco.Globals;
import schmitt.mmas.aco.VRPSolver;
import schmitt.mmas.graph.Edge;
import schmitt.mmas.graph.Graph;
import schmitt.mmas.graph.Node;
import schmitt.mmas.reader.JSONConverter;
import schmitt.mmas.view.Visualizer;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class TestGraphTools {

    @Test
    public void testReaderJoinvilleJson() {
        ClassLoader classLoader = getClass().getClassLoader();
        String jsonFile = classLoader.getResource("joinville.json").getFile().toString();
        Graph graph = JSONConverter.readGraph(jsonFile);

        assertThat(graph).isNotNull();
        assertThat(graph.getNodesLength()).isEqualTo(2796);
        assertThat(graph.getEdgesLength()).isEqualTo(6178);

        /**
         * Test route only forward
         */

        assertThat(graph.getNode(868).getX()).isEqualTo(-26.32767076777676);
        assertThat(graph.getNode(868).getY()).isEqualTo(-48.864337217755704);
        assertThat(graph.getNode(868).getEdges().size()).isEqualTo(1);
        assertThat(graph.getEdge(0, 868)).isNull();
        assertThat(graph.getEdge(868, 0).getDistance()).isEqualTo(405.112);

        assertThat(graph.getNode(0).getX()).isEqualTo(-26.33074934774822);
        assertThat(graph.getNode(0).getY()).isEqualTo(-48.862193499405535);
        assertThat(graph.getNode(0).getEdges().size()).isEqualTo(2);

        assertThat(graph.getNode(1239).getX()).isEqualTo(-26.333280123769047);
        assertThat(graph.getNode(1239).getY()).isEqualTo(-48.861051884168056);
        assertThat(graph.getNode(1239).getEdges().size()).isEqualTo(1);
        assertThat(graph.getEdge(1239, 0)).isNull();
        assertThat(graph.getEdge(0, 1239).getDistance()).isEqualTo(303.545);

        assertThat(graph.getNode(1321).getX()).isEqualTo(-26.331491984385817);
        assertThat(graph.getNode(1321).getY()).isEqualTo(-48.862099435823396);
        assertThat(graph.getNode(1321).getEdges().size()).isEqualTo(1);
        assertThat(graph.getEdge(1321, 0)).isNull();
        assertThat(graph.getEdge(0, 1321).getDistance()).isEqualTo(83.107);

        /**
         * Test a route with a curve, to check if the distance is not Euclidean (220m).
         * Test if route is forward and backward
         */
        assertThat(graph.getNode(346).getX()).isEqualTo(-26.307000249035664);
        assertThat(graph.getNode(346).getY()).isEqualTo(-48.85931142850171);
        assertThat(graph.getNode(346).getEdges().size()).isEqualTo(3);

        assertThat(graph.getNode(358).getX()).isEqualTo(-26.306899479930838);
        assertThat(graph.getNode(358).getY()).isEqualTo(-48.861525741104614);
        assertThat(graph.getNode(358).getEdges().size()).isEqualTo(4);

        assertThat(graph.getEdge(346, 358).getDistance()).isEqualTo(306.948);
        assertThat(graph.getEdge(358, 346).getDistance()).isEqualTo(306.948);

    }

    @Test
    public void testVisualizerMap() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        String jsonFile = classLoader.getResource("joinville.json").getFile().toString();
        Graph graph = JSONConverter.readGraph(jsonFile);

        Visualizer visualizer = new Visualizer(graph);
        visualizer.draw(null);

        while(true) {
            Thread.sleep(100);
        }
    }

    @Test
    public void testSimpleHeuristic() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        String jsonFile = classLoader.getResource("joinville.json").getFile().toString();
        Graph graph = JSONConverter.readGraph(jsonFile);

        Visualizer visualizer = new Visualizer(graph);
        visualizer.draw(null);
        Thread.sleep(2000);

        //43->171
        //103->1980
        VRPSolver vrpSolver = new VRPSolver(graph, graph.getNode(1980), graph.getNode(103));
        vrpSolver.solve();

        Globals globals = new Globals();
        globals.graph = graph;
        globals.sourceNode = graph.getNode(1980);
        globals.targetNode = graph.getNode(103);
        Ant ant = new Ant(globals);
        ant.nnTour();
        int[] route = new int[ant.getRoute().size()];
        for(int i = 0; i < route.length; i++) {
            route[i] = ant.getRoute().get(i).getId();
        }
        visualizer.draw(route);
        visualizer.setStat("Cost = " + ant.getCost());
        while(true) {
            Thread.sleep(1000);
        }
    }

    @Test
    public void testSimpleHeuristicJoinville() throws Exception {

        Graph graph = new Graph();
        graph.addNode(0, -8, -0);
        graph.addNode(1, -6, -1);
        graph.addNode(2, -3, -1);
        graph.addNode(3, -4, -3);
        graph.addNode(4, -1, -2);
        graph.addNode(5, -1, -3);
        graph.addNode(6, -1, -5);
        graph.addNode(7, -6, -5);
        graph.addNode(8, -3, -4);

        graph.addEdge(1, 0, 4.0);
        graph.addEdge(1, 2, 7.0);
        graph.addEdge(1, 3, 6.0);
        graph.addEdge(1, 7, 9.5);
        graph.addEdge(2, 1, 7.0);
        graph.addEdge(2, 4, 5.0);
        graph.addEdge(3, 2, 5.0);
        graph.addEdge(3, 4, 8.0);
        graph.addEdge(3, 7, 7.0);
        graph.addEdge(4, 5, 3.0);
        graph.addEdge(5, 2, 7.0);
        graph.addEdge(5, 6, 5.0);
        graph.addEdge(5, 7, 13.0);
        graph.addEdge(5, 8, 6.0);
        graph.addEdge(6, 7, 12.0);
        graph.addEdge(8, 3, 4.0);
        graph.addEdge(8, 6, 6.0);

        Visualizer visualizer = new Visualizer(graph);
        visualizer.draw(null);
        Thread.sleep(1000);

        VRPSolver vrpSolver = new VRPSolver(graph, graph.getNode(1), graph.getNode(6));
        vrpSolver.solve();

        Globals globals = new Globals();
        globals.graph = graph;
        globals.sourceNode = graph.getNode(1);
        globals.targetNode = graph.getNode(6);
        Ant ant = new Ant(globals);
        ant.nnTour();
        int[] route = new int[ant.getRoute().size()];
        for(int i = 0; i < route.length; i++) {
            route[i] = ant.getRoute().get(i).getId();
        }
        visualizer.draw(route);

        while(true) {
            Thread.sleep(1000);
        }
    }

}
