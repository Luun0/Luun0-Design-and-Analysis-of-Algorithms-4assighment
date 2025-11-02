import com.google.gson.*;
import graph.Graph;
import graph.scc.TarjanSCC;
import graph.topo.Condensation;
import graph.topo.KahnTopo;
import graph.dagsp.DagSP;
import graph.dagsp.DagSP.Result;


import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws Exception {
        Path dataDir = Paths.get("data");
        if (!Files.exists(dataDir) || !Files.isDirectory(dataDir)) {
            System.out.println(" No data directory found.");
            return;
        }

        List<Path> files;
        try (Stream<Path> s = Files.list(dataDir)) {
            files = s.filter(p -> p.toString().endsWith(".json")).sorted().collect(Collectors.toList());
        }

        if (files.isEmpty()) {
            System.out.println(" No JSON datasets in /data folder.");
            return;
        }

        Path resultsDir = Paths.get("results");
        Files.createDirectories(resultsDir);
        Path csv = resultsDir.resolve("results.csv");

        if (!Files.exists(csv)) {
            String header = "dataset,n,m,scc_count,tarjan_ns,dfs_visits,dfs_edges,cond_nodes,cond_edges,dsp_relaxations,critical_len\n";
            Files.writeString(csv, header, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }

        for (Path file : files) {
            String input = file.toString();
            String s = Files.readString(file);
            JsonObject o = JsonParser.parseString(s).getAsJsonObject();
            JsonArray nodes = o.getAsJsonArray("nodes");
            int n = nodes.size();
            Graph g = new Graph(n);
            int m = 0;

            if (o.has("edges")) {
                for (var el : o.getAsJsonArray("edges")) {
                    JsonObject e = el.getAsJsonObject();
                    int u = e.get("u").getAsInt();
                    int v = e.get("v").getAsInt();
                    long w = e.has("w") ? e.get("w").getAsLong() : 1;
                    g.addEdge(u, v, w);
                    m++;
                }
            }

            long t0 = System.nanoTime();
            TarjanSCC tarjan = new TarjanSCC(g);
            List<List<Integer>> comps = tarjan.run();
            long t1 = System.nanoTime();

            Condensation dag = new Condensation(g, tarjan.getCompId(), tarjan.getCompCount());
            int condNodes = dag.compN;
            int condEdges = dag.adj.stream().mapToInt(List::size).sum();

            KahnTopo topo = new KahnTopo();
            topo.run(dag.adj);

            DagSP dsp = new DagSP(dag);
            int sourceComp = tarjan.getCompId()[0];

            dsp.relaxations = 0;
            Result resLong = dsp.longestFrom(sourceComp);

            long best = Long.MIN_VALUE;
            for (long d : resLong.dist) if (d > best) best = d;

            String line = String.join(",",
                    input,
                    String.valueOf(n),
                    String.valueOf(m),
                    String.valueOf(comps.size()),
                    String.valueOf(t1 - t0),
                    String.valueOf(tarjan.dfsVisits),
                    String.valueOf(tarjan.dfsEdges),
                    String.valueOf(condNodes),
                    String.valueOf(condEdges),
                    String.valueOf(dsp.relaxations),
                    String.valueOf(best)
            ) + "\n";

            Files.writeString(csv, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            System.out.println(" Processed " + input + " (critical=" + best + ")");
        }

        System.out.println("\n All results saved in results/results.csv");
    }
}