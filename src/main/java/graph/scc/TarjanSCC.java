package graph.scc;

import graph.Graph;

import java.util.*;

public class TarjanSCC {
    private final Graph g;
    private int time=0;
    private final int[] disc, low, compId;
    private final boolean[] inStack;
    private final Deque<Integer> stack = new ArrayDeque<>();
    private int compCount=0;
    public long dfsVisits=0;
    public long dfsEdges=0;

    public TarjanSCC(Graph g){
        this.g=g;
        disc = new int[g.n];
        Arrays.fill(disc,-1);
        low = new int[g.n];
        compId = new int[g.n];
        Arrays.fill(compId,-1);
        inStack = new boolean[g.n];
    }

    public List<List<Integer>> run(){
        for(int v=0; v<g.n; v++){
            if(disc[v]==-1) dfs(v);
        }
        List<List<Integer>> comps = new ArrayList<>();
        for(int i=0;i<compCount;i++) comps.add(new ArrayList<>());
        for(int v=0; v<g.n; v++) comps.get(compId[v]).add(v);
        return comps;
    }

    private void dfs(int u){
        dfsVisits++;
        disc[u]=low[u]=time++;
        stack.push(u);
        inStack[u]=true;
        for(int ei: g.adj.get(u)){
            dfsEdges++;
            Graph.Edge e = g.edges.get(ei);
            int v = e.v;
            if(disc[v]==-1){
                dfs(v);
                low[u]=Math.min(low[u], low[v]);
            } else if(inStack[v]){
                low[u]=Math.min(low[u], disc[v]);
            }
        }
        if(low[u]==disc[u]){
            while(true){
                int v = stack.pop();
                inStack[v]=false;
                compId[v]=compCount;
                if(v==u) break;
            }
            compCount++;
        }
    }

    public int[] getCompId(){ return compId; }
    public int getCompCount(){ return compCount; }
}