package graph.topo;

import graph.Graph;

import java.util.*;

public class Condensation {
    public final int compN;
    public final List<List<Pair>> adj;
    public static class Pair { public final int v; public final long w; public Pair(int v,long w){this.v=v;this.w=w;} }
    public Condensation(Graph g, int[] compId, int compCount){
        this.compN = compCount;
        List<Set<Integer>> tmp = new ArrayList<>();
        for(int i=0;i<compN;i++) tmp.add(new HashSet<>());
        Map<Long, Long> edgeWeight = new HashMap<>();
        for(Graph.Edge e: g.edges){
            int cu = compId[e.u], cv = compId[e.v];
            if(cu!=cv){
                long key = (((long)cu)<<32) | (cv & 0xffffffffL);
                long prev = edgeWeight.getOrDefault(key, Long.MAX_VALUE);
                if(e.w < prev) edgeWeight.put(key, e.w);
                tmp.get(cu).add(cv);
            }
        }
        adj = new ArrayList<>();
        for(int i=0;i<compN;i++){
            List<Pair> list = new ArrayList<>();
            for(int cv: tmp.get(i)){
                long key = (((long)i)<<32) | (cv & 0xffffffffL);
                list.add(new Pair(cv, edgeWeight.getOrDefault(key, 1L)));
            }
            adj.add(list);
        }
    }
}