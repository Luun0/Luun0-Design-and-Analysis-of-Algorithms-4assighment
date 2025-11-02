package util;

import com.google.gson.*;
import java.nio.file.*;
import java.util.*;

public class DataGenerator {
    public static void main(String[] args) throws Exception {
        create(6,7,"data/small-1.json", true);
        create(8,10,"data/small-2.json", false);
        create(10,14,"data/small-3.json", true);
        create(12,25,"data/medium-1.json", true);
        create(15,18,"data/medium-2.json", false);
        create(18,30,"data/medium-3.json", true);
        create(25,80,"data/large-1.json", true);
        create(30,100,"data/large-2.json", true);
        create(40,200,"data/large-3.json", true);
        System.out.println("Generated 9 files in data/");
    }

    static void create(int n,int m,String path, boolean allowCycles) throws Exception {
        Random rnd = new Random(n*31 + m);
        JsonObject out = new JsonObject();
        JsonArray nodes = new JsonArray();
        for(int i=0;i<n;i++) nodes.add(i);
        JsonArray edges = new JsonArray();
        for(int i=0;i<m;i++){
            int u = rnd.nextInt(n);
            int v = rnd.nextInt(n);
            if(!allowCycles) v = (u + 1 + rnd.nextInt(Math.max(1,n-1)))%n;
            JsonObject e = new JsonObject();
            e.addProperty("u", u);
            e.addProperty("v", v);
            e.addProperty("w", 1 + rnd.nextInt(10));
            edges.add(e);
        }
        out.add("nodes", nodes);
        out.add("edges", edges);
        Files.createDirectories(Path.of(path).getParent());
        Files.writeString(Path.of(path), new GsonBuilder().setPrettyPrinting().create().toJson(out));
    }
}