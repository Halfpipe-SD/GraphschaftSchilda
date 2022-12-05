package code.problem2;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;

import code.utils.BasicWindow;
import code.utils.GraphData;
import code.utils.GraphEdge;
import code.utils.GraphVertex;
import code.utils.JGraphPanel;

public class Problem2 extends BasicWindow {

    public Problem2(String titel) throws FileNotFoundException {
        super(titel);
        setSize(new Dimension(510, 600));
        setLayout(new GridLayout(1, 2));
        setLocationRelativeTo(null);

        GraphData graph_input = new GraphData("town_water.json");

        GraphData graph_output = get_output_graph(graph_input);

        Dimension size = new Dimension(getWidth() / 2, getHeight());
        JGraphPanel p1 = new JGraphPanel("Rohdaten", size, graph_input);
        JGraphPanel p2 = new JGraphPanel("Minimum Spanning Tree (Prim)", size, graph_output);

        add(p1);
        add(p2);
    }

    static final int V = 9; // vertices in graph

    public GraphData get_output_graph(GraphData input) {
        // Lese die Knoten und Kanten aus den Rohdaten
        ArrayList<GraphVertex> vertices = input.getVertices();
        ArrayList<GraphEdge> edges = input.getEdges();
        return input;
    }

    // searching if there is a way form s to t
    boolean bfs(int rGraph[][], int s, int t, int parent[]) {
        boolean visited[] = new boolean[V];
        for (int i = 0; i < V; ++i)
            visited[i] = false;

        // create array with not visitet vertices
        LinkedList<Integer> queue = new LinkedList<Integer>();
        queue.add(s);
        visited[s] = true;
        parent[s] = -1;

        // bfs loop
        while (queue.size() != 0) {
            int u = queue.poll();

            for (int v = 0; v < V; v++) {
                if (visited[v] == false && rGraph[u][v] > 0) {

                    // return true if we reach the end
                    if (v == t) {
                        parent[v] = u;
                        return true;
                    }
                    queue.add(v);
                    parent[v] = u;
                    visited[v] = true;
                }
            }
        }
        // return false if we don´t reach the end
        return false;
    }

    // return the max flow from s to t
    int fordFulkerson(int graph[][], int s, int t) {
        int u, v;

        // fill the graph with values
        int rGraph[][] = new int[V][V];

        for (u = 0; u < V; u++)
            for (v = 0; v < V; v++)
                rGraph[u][v] = graph[u][v];

        // this array contains the possible pathes of bfs
        int parent[] = new int[V];

        int max_flow = 0;

        //
        while (bfs(rGraph, s, t, parent)) {

            int path_flow = Integer.MAX_VALUE;

            // find the max flow
            for (v = t; v != s; v = parent[v]) {
                u = parent[v];
                path_flow = Math.min(path_flow, rGraph[u][v]);
            }

            // update the edges
            for (v = t; v != s; v = parent[v]) {
                u = parent[v];
                rGraph[u][v] -= path_flow;
                rGraph[v][u] += path_flow;
            }

            // add path_flow to max_flow
            max_flow += path_flow;
        }

        // return the max flow
        return max_flow;
    }

}