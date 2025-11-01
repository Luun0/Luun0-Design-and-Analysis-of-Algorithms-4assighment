package graph.dagsp;

import graph.topo.Condensation;

import java.util.*;

public class DagSP {
    private final Condensation dag;
    public long relaxations=0;

    public DagSP(Condensation dag){ this.dag = dag; }

    public Result shortestFrom(int src){
        int n = dag.compN;
        long INF = Long.MAX_VALUE/4;
        long[] dist = new long[n];
        int[] parent = new int[n];
        Arrays.fill(dist, INF);
        Arrays.fill(parent, -1);
        dist[src]=0;
        var topo = new graph.topo.KahnTopo();
        boolean ok = topo.run(dag.adj);
        if(!ok) throw new RuntimeException("not a DAG");
        for(int u: topo.order){
            if(dist[u]==INF) continue;
            for(var e: dag.adj.get(u)){
                relaxations++;
                if(dist[e.v] > dist[u] + e.w){
                    dist[e.v] = dist[u] + e.w;
                    parent[e.v] = u;
                }
            }
        }
        return new Result(dist, parent, topo.order);
    }

    public Result longestFrom(int src){
        int n = dag.compN;
        long NEG = Long.MIN_VALUE/4;
        long[] dist = new long[n];
        int[] parent = new int[n];
        Arrays.fill(dist, NEG);
        Arrays.fill(parent, -1);
        dist[src]=0;
        var topo = new graph.topo.KahnTopo();
        boolean ok = topo.run(dag.adj);
        if(!ok) throw new RuntimeException("not a DAG");
        for(int u: topo.order){
            if(dist[u]==NEG) continue;
            for(var e: dag.adj.get(u)){
                relaxations++;
                if(dist[e.v] < dist[u] + e.w){
                    dist[e.v] = dist[u] + e.w;
                    parent[e.v] = u;
                }
            }
        }
        return new Result(dist, parent, topo.order);
    }

    public static class Result {
        public final long[] dist;
        public final int[] parent;
        public final List<Integer> topo;
        public Result(long[] dist,int[] parent,List<Integer> topo){this.dist=dist;this.parent=parent;this.topo=topo;}
        public List<Integer> reconstructPath(int to){
            if(dist[to]==Long.MAX_VALUE/4 || dist[to]==Long.MIN_VALUE/4) return List.of();
            LinkedList<Integer> path = new LinkedList<>();
            int cur = to;
            while(cur!=-1){ path.addFirst(cur); cur = parent[cur]; }
            return path;
        }
    }
}