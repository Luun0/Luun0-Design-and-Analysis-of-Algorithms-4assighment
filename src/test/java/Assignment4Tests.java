import graph.Graph;
import graph.scc.TarjanSCC;
import graph.topo.Condensation;
import graph.topo.KahnTopo;
import graph.dagsp.DagSP;
import graph.dagsp.DagSP.Result;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Assignment4Tests {

    private Graph buildSampleGraph() {
        Graph g = new Graph(6);
        g.addEdge(0,1,1);
        g.addEdge(1,2,1);
        g.addEdge(2,0,1);
        g.addEdge(2,3,2);
        g.addEdge(3,4,1);
        g.addEdge(4,5,1);
        g.addEdge(1,5,5);
        return g;
    }

    @Test
    public void testTarjanSCCProducesExpectedComponents() {
        Graph g = buildSampleGraph();
        TarjanSCC tarjan = new TarjanSCC(g);
        List<List<Integer>> comps = tarjan.run();

        assertEquals(4, tarjan.getCompCount(), "Expecting 4 components after compression");
        boolean foundSize3 = comps.stream().anyMatch(c -> c.size() == 3);
        assertTrue(foundSize3, "must be SCC size 3 (0,1,2)");
    }

    @Test
    public void testCondensationIsDAGAndTopologicalOrderValid() {
        Graph g = buildSampleGraph();
        TarjanSCC tarjan = new TarjanSCC(g);
        List<List<Integer>> comps = tarjan.run();
        Condensation dag = new Condensation(g, tarjan.getCompId(), tarjan.getCompCount());

        KahnTopo kahn = new KahnTopo();
        boolean ok = kahn.run(dag.adj);
        assertTrue(ok, "Condensation must be DAG");

        int n = dag.compN;
        int[] pos = new int[n];
        for (int i = 0; i < kahn.order.size(); i++) pos[kahn.order.get(i)] = i;

        for (Graph.Edge e : g.edges) {
            int cu = tarjan.getCompId()[e.u];
            int cv = tarjan.getCompId()[e.v];
            if (cu != cv) {
                assertTrue(pos[cu] < pos[cv], "In topo order, the position of the ancestor must be before the child");
            }
        }
    }

    @Test
    public void testDagShortestAndLongestPathsOnCondensation() {
        Graph g = buildSampleGraph();
        TarjanSCC tarjan = new TarjanSCC(g);
        tarjan.run();
        Condensation dag = new Condensation(g, tarjan.getCompId(), tarjan.getCompCount());

        DagSP dsp = new DagSP(dag);
        int src = tarjan.getCompId()[0];
        int comp5 = tarjan.getCompId()[5];

        Result shortest = dsp.shortestFrom(src);
        assertEquals(4L, shortest.dist[comp5], "Waiting for the shortest path of length 4");

        dsp.relaxations = 0;
        Result longest = dsp.longestFrom(src);
        assertEquals(5L, longest.dist[comp5], "Waiting for the  longest path of length 5");
    }

    @Test
    public void testEmptyGraphDoesNotCrash() {
        Graph g = new Graph(0);
        TarjanSCC tarjan = new TarjanSCC(g);
        List<List<Integer>> comps = tarjan.run();
        assertEquals(0, comps.size());
    }
}