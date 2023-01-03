package code;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

import code.utils.AdjazenzMatrix;
import code.utils.BasicWindow;
import code.utils.FileHandler;
import code.utils.JGraphPanel;

public class Problem2 extends BasicWindow {

    private int max_flow = 0;

    public Problem2(String title) throws FileNotFoundException, IOException {
        super(title);

        // layout settings
        setSize(new Dimension(510, 460));
        setLayout(new GridLayout(1, 2));
        setLocationRelativeTo(null);

        // Initializiere FileHandler und lese die Daten aus der Datei
        FileHandler fh = new FileHandler("problem2.txt");

        // Erstelle die Adjazenzmatrix und gebe sie in der Konsole aus
        AdjazenzMatrix am_input = new AdjazenzMatrix("Input", fh.getMatrix(), fh.getVertexLetters(), true);
        am_input.printMatrix();

        // Erstelle die Ausgabe-Adjazenzmatrix mit dem Prim Algorithmus
        int[][] matrix_output = fordFulkerson(am_input.getMatrixCopy());

        // Erstelle die Ausgabe-Adjazenzmatrix, gebe sie in der Konsole aus und schreibe
        // sie in eine Datei
        AdjazenzMatrix am_output = new AdjazenzMatrix("Output", matrix_output, fh.getVertexLetters(), true);
        am_output.printAndWriteMatrix(title);

        // Setze den Maximalfluss
        am_output.setMaxFlow(max_flow);

        // Erstelle die Graphen und füge sie dem Fenster hinzu
        JGraphPanel p1 = new JGraphPanel("Rohdaten", am_input, "hierarchical");
        JGraphPanel p2 = new JGraphPanel("Flussnetzwerk mit Ford-Fulkerson", am_output,
                "hierarchical");

        add(p1);
        add(p2);
    }

    private int num_vertices = 0;

    // function to find the path with the maximum flow
    private int[][] fordFulkerson(int[][] matrix) {

        // number of vertices in graph
        num_vertices = matrix[0].length;

        // source is the first vertex
        int s = 0;
        // the sink is always the last vertex
        int t = num_vertices - 1;

        int u, v;

        // copy the graph to rGraph for out
        int rGraph[][] = new int[num_vertices][num_vertices];
        for (u = 0; u < num_vertices; u++)
            for (v = 0; v < num_vertices; v++)
                rGraph[u][v] = matrix[u][v];

        // This array is filled by BFS and to store path
        int parent[] = new int[num_vertices];

        // There is no flow initially
        max_flow = 0;

        // add possible path to the flow
        while (bfs(matrix, rGraph, s, t, parent)) {

            // find max flow through the possible paths
            int path_flow = Integer.MAX_VALUE;
            for (u = t; u != s; u = parent[u]) {
                v = parent[u];

                path_flow = Math.min(path_flow, rGraph[v][u]);
            }

            // update the edges
            for (u = t; u != s; u = parent[u]) {

                v = parent[u];
                rGraph[v][u] -= path_flow; // remove path flow from rGraph
                // rGraph[u][v] += path_flow; // add back path flow to rGraph
            }

            // Add path flow to max flow
            max_flow += path_flow;
        }

        return printMatrix(matrix, rGraph);
    }

    boolean bfs(int[][] matrix, int rGraph[][], int s, int t, int parent[]) {

        // int[][] matrix = am_input.getMatrix();
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

    int[][] printMatrix(int[][] matirx, int[][] rGraph) {
        int[][] outputGraph = new int[num_vertices][num_vertices];
        for (int i = 0; i < matirx[0].length; i++) {
            for (int j = 0; j < matirx[0].length; j++) {
                outputGraph[i][j] = matirx[i][j] - rGraph[i][j];
            }
        }
        return outputGraph;
    }
}