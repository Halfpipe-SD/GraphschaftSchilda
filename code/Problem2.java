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

    // private AdjazenzMatrix am_input;

    private int num_vertices = 0;

    public Problem2(String title) throws FileNotFoundException, IOException {
        super(title);

        // layout settings
        setSize(new Dimension(510, 600));
        setLayout(new GridLayout(1, 2));
        setLocationRelativeTo(null);

        // Initializiere FileHandler und lese die Daten aus der Datei
        FileHandler fh = new FileHandler("problem2.txt");

        // Erstelle die Adjazenzmatrix und gebe sie in der Konsole aus
        AdjazenzMatrix am_input = new AdjazenzMatrix("Input", fh.getMatrix(), fh.getVertexLetters(), true);
        am_input.printMatrix();

        //Erstelle die Ausgabe-Adjazenzmatrix mit dem Prim Algorithmus
        int[][] matrix_output = fordFulkerson(am_input.getMatrixCopy());

        // Erstelle die Ausgabe-Adjazenzmatrix, gebe sie in der Konsole aus und schreibe
        // sie in eine Datei
        // AdjazenzMatrix am_output = new AdjazenzMatrix("Output", fordFulkerson(), true);
        // am_output.printAndWriteMatrix(title);
        AdjazenzMatrix am_output = new AdjazenzMatrix("Output", matrix_output, fh.getVertexLetters(), true);
        am_output.printAndWriteMatrix(title);

        // Erstelle die Graphen und füge sie dem Fenster hinzu
        JGraphPanel p1 = new JGraphPanel("Rohdaten", am_input, "hierarchical");
        JGraphPanel p2 = new JGraphPanel("Flussnetzwerk mit Ford-Fulkerson", am_output,
                "hierarchical");

        add(p1);
        add(p2);
    }

    // function to find the path with the maximum flow
    private int[][] fordFulkerson(int [][] matrix) {

        // input matrix
        // int[][] matrix = am_input.getMatrix();

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
        while (bfs(matrix, rGraph, s, t, parent)) {

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
        return matrix;
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
}