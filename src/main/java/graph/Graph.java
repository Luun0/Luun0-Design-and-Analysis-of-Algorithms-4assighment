package graph;

import java.util.*;

public class Graph {
    public final int n;
    public final List<Edge> edges = new ArrayList<>();
    public final List<List<Integer>> adj;

    public static class Edge {
        public final int u, v;
        public final long w;
        public Edge(int u, int v, long w){ this.u=u;this.v=v;this.w=w;}
    }

    public Graph(int n){
        this.n=n;
        adj = new ArrayList<>(n);
        for(int i=0;i<n;i++) adj.add(new ArrayList<>());
    }
    public void addEdge(int u,int v,long w){
        edges.add(new Edge(u,v,w));
        adj.get(u).add(edges.size()-1);
    }
}