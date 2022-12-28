package code;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

import code.utils.AdjazenzMatrix;
import code.utils.BasicWindow;
import code.utils.FileHandler;

public class Problem2 extends BasicWindow {

    private AdjazenzMatrix am_input;

    private int num_vertices = 0;

    public Problem2(String title) throws FileNotFoundException, IOException {
        super(title);

        setSize(new Dimension(510, 600));
        setLayout(new GridLayout(1, 2));
        setLocationRelativeTo(null);

        FileHandler fh = new FileHandler("graph_problem2.txt");

        am_input = new AdjazenzMatrix("Input", fh.getMatrix(), false);
        am_input.printMatrix();

        AdjazenzMatrix am_output = new AdjazenzMatrix("Output", fordFulkerson(), false);
        am_output.printMatrix();
    }

    private int fordFulkerson() {

        // input matrix
        int[][] matrix = am_input.getMatrix();

        // number of vertices in graph
        num_vertices = matrix[0].length;

        // source and sink
        int s = 0;
        int t = num_vertices - 1;

        // // output matrix max_flow
        // int[][] max_flow = matrix;

        int u, v;

        // copy the graph
        int rGraph[][] = new int[num_vertices][num_vertices];
        for (u = 0; u < num_vertices; u++)
            for (v = 0; v < num_vertices; v++)
                rGraph[u][v] = matrix[u][v];

        // This array is filled by BFS and to store path
        int parent[] = new int[num_vertices];

        int max_flow = 0; // There is no flow initially

        // add possible path to the flow
        while (bfs(rGraph, s, t, parent)) {

            // find max flow through the possible paths
            int path_flow = Integer.MAX_VALUE;
            for (v = t; v != s; v = parent[v]) {
                u = parent[v];

                for (int i = 0; i < parent.length; i++) {
                }

                path_flow = Math.min(path_flow, rGraph[u][v]);
            }

            // update the edges
            for (v = t; v != s; v = parent[v]) {
                u = parent[v];
                rGraph[u][v] -= path_flow;
                rGraph[v][u] += path_flow;
            }

            // Add path flow to max flow
            max_flow += path_flow;
            System.out.println("max_path_flow: " + path_flow + " "); // print path flow
        }

        // Return the max flow
        System.out.println("max_flow: " + max_flow);
        return max_flow;
    }

    boolean bfs(int rGraph[][], int s, int t, int parent[]) {

        int[][] matrix = am_input.getMatrix();
        num_vertices = matrix[0].length;

        // array that mark all vertices as not visited
        boolean visited[] = new boolean[num_vertices];
        for (int i = 0; i < num_vertices; ++i)
            visited[i] = false;

        // add start vertex and mark as visited
        LinkedList<Integer> queue = new LinkedList<Integer>();
        queue.add(s);
        visited[s] = true;
        parent[s] = -1;

        // standard bfs loop
        while (queue.size() != 0) {
            int u = queue.poll();
            // System.out.print(u + " "); //bfs vertices

            for (int v = 0; v < num_vertices; v++) {
                if (visited[v] == false && rGraph[u][v] > 0) {
                    // If we find a path from s to t we return true
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
        return false;
    }
}