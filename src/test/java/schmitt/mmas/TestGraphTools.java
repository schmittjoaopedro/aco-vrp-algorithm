package schmitt.mmas;

import org.junit.Test;
import schmitt.mmas.graph.Graph;
import schmitt.mmas.reader.JSONConverter;
import schmitt.mmas.view.Visualizer;

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

}
