package graph.topo;

import java.util.*;

public class KahnTopo {
    public final List<Integer> order = new ArrayList<>();
    public long pushes=0, pops=0;
    public boolean run(List<List<Condensation.Pair>> adj){
        int n = adj.size();
        int[] indeg = new int[n];
        for(int u=0;u<n;u++) for(var p: adj.get(u)) indeg[p.v]++;
        Deque<Integer> q = new ArrayDeque<>();
        for(int i=0;i<n;i++) if(indeg[i]==0){ q.add(i); pushes++; }
        while(!q.isEmpty()){
            int u = q.removeFirst(); pops++;
            order.add(u);
            for(var p: adj.get(u)){
                indeg[p.v]--;
                if(indeg[p.v]==0){ q.add(p.v); pushes++; }
            }
        }
        return order.size()==n;
    }
}