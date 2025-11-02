# Assignment 4 — Smart City / Smart Campus Scheduling

**Author:** Zhuparbay Erkebulan  
**Group:** Se-2423

---

## Goal
In this project I used graph algorithms to model smart city or campus tasks.  
There are tasks that depend on each other, and I needed to find:
- Strongly connected components (SCC)
- Build condensation graph (DAG)
- Find topological order
- Calculate shortest and longest paths in the DAG

---

## Algorithms used
- **Tarjan SCC algorithm**
- **Kahn topological sort**
- **Dynamic programming for shortest/longest paths in DAG**

---

## How to run
1. Build project:
   ```bash
   mvn clean package
   java -cp target/assignment4-1.0-SNAPSHOT-jar-with-dependencies.jar Main
   It will read all JSON files from the data/ folder and create file results/results.csv.
   cat results/results.csv
   ```
---
## Project Structure
```
Desighn 4/
├── data/                    ← JSON datasets (small, medium, large)
│    ├── small-1.json
│    ├── small-2.json
│    ├── small-3.json
│    ├── medium-1.json
│    ├── medium-2.json
│    ├── medium-3.json
│    ├── large-1.json
│    ├── large-2.json
│    └── large-3.json
├── results/
│    └── results.csv         ← Generated results after program run
├── src/
│    ├── main/
│    │    ├── java/
│    │    │    ├── graph/
│    │    │    │    ├── scc/        ← Tarjan SCC algorithm
│    │    │    │    ├── topo/       ← Condensation & topological sort
│    │    │    │    └── dagsp/      ← Shortest & longest paths
│    │    │    ├── util/            ← Data generator
│    │    │    └── Main.java        ← Main entry point
│    └── test/
│         └── java/
│              └── Assignment4Tests.java  ← Unit tests
├── pom.xml                   ← Maven configuration
└── README.md                 ← Project report
```
---

## Datasets
I created 9 datasets with different sizes:
- small-1.json
- small-2.json
- small-3.json
- medium-1.json
- medium-2.json
- medium-3.json
- large-1.json
- large-2.json
- large-3.json
---
## Results

| Dataset | n | m | SCCs | Tarjan (ns) | DFS visits | DFS edges | Cond nodes | Cond edges | Relax | Critical len |
|----------|--:|--:|--:|--:|--:|--:|--:|--:|--:|--:|
| data/large-1.json | 25 | 80 | 7 | 224292 | 25 | 80 | 7 | 8 | 5 | 11 |
| data/large-2.json | 30 | 100 | 2 | 37458 | 30 | 100 | 2 | 1 | 1 | 1 |
| data/large-3.json | 40 | 200 | 1 | 54000 | 40 | 200 | 1 | 0 | 0 | 0 |
| data/medium-1.json | 12 | 25 | 3 | 17250 | 12 | 25 | 3 | 2 | 0 | 0 |
| data/medium-2.json | 15 | 18 | 13 | 18583 | 15 | 18 | 13 | 13 | 8 | 32 |
| data/medium-3.json | 18 | 30 | 15 | 28833 | 18 | 30 | 15 | 19 | 7 | 26 |
| data/small-1.json | 6 | 7 | 5 | 12666 | 6 | 7 | 5 | 3 | 2 | 8 |
| data/small-2.json | 8 | 10 | 8 | 79375 | 8 | 10 | 8 | 9 | 5 | 17 |
| data/small-3.json | 10 | 14 | 7 | 10875 | 10 | 14 | 7 | 5 | 2 | 7 |
---
## Conclusion
- Tarjan algorithm works fast for all graphs.
- densation makes graph smaller and easier to work with.
-  shortest and longest path show how to find the main (critical) tasks.
- Bigger graphs take a bit more time, but still very fast.
