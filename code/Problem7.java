package code;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JPanel;

import code.utils.AdjazenzMatrix;
import code.utils.BasicWindow;
import code.utils.FileHandler;
import code.utils.JGraphPanel;

public class Problem7 extends BasicWindow {

  private int max_flow = 0;
  private int num_vertices = 0;

  public Problem7(String title) throws FileNotFoundException, IOException {
    super(title);

    // layout settings
    setSize(new Dimension(805, 705));
    setLayout(new GridLayout(2, 1));
    setLocationRelativeTo(null);

    // Initializiere FileHandler und lese die Daten aus der Datei
    FileHandler fh = new FileHandler("problem7.txt");

    int[][] matrix = fh.getMatrix();
    char[] vertex_letters = fh.getVertexLetters();

    // Füge Start- und Endknoten hinzu
    int[][] matrix_new = new int[matrix.length + 2][matrix[0].length + 2];
    char[] vertex_letters_new = new char[vertex_letters.length + 2];

    vertex_letters_new[0] = 's';

    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[i].length; j++)
        matrix_new[i + 1][j + 1] = matrix[i][j];
      vertex_letters_new[i + 1] = vertex_letters[i];
    }

    vertex_letters_new[vertex_letters_new.length - 1] = 'e';

    // Füge die Kanten vom Startknoten s zu der ersten Hälfte der Knoten hinzu
    // und von der zweiten Hälfte der Knoten zum Endknoten e hinzu
    for (int i = 1; i < matrix_new.length - 1; i++) {
      if (i <= matrix_new.length / 2 - 1) {
        matrix_new[0][i] = 1;
        matrix_new[i][matrix_new.length - 1] = 0;
      } else {
        matrix_new[0][i] = 0;
        matrix_new[i][matrix_new.length - 1] = 1;
      }
    }

    // Erstelle die Adjazenzmatrix und gebe sie in der Konsole aus
    AdjazenzMatrix am_input = new AdjazenzMatrix("Input", matrix_new, vertex_letters_new, true);
    am_input.printMatrix();

    // Erstelle die Ausgabe-Adjazenzmatrix mit dem Prim Algorithmus
    int[][] matrix_output = fordFulkerson(am_input.getMatrixCopy());

    // Filtere die inversen Kanten
    for (int i = 0; i < matrix_output.length; i++)
      for (int j = 0; j < matrix_output[i].length; j++)
        if (matrix_output[i][j] < 0)
          matrix_output[i][j] = 0;

    // Erstelle die Ausgabe-Adjazenzmatrizen, gebe sie in der Konsole aus und
    // schreibe sie in eine Datei
    AdjazenzMatrix am_output = new AdjazenzMatrix("Output", matrix_output, vertex_letters_new, true);

    // Setze den Maximalfluss
    am_output.setMaxFlow(max_flow);

    // Erstelle eine neue Ausgabe-Adjazenzmatrix ohne Start- und Endknoten und ohne
    // die Kanten zu diesen Knoten
    int[][] matrix_output2 = new int[matrix_output.length - 2][matrix_output[0].length - 2];
    char[] vertex_letters_new2 = new char[vertex_letters_new.length - 2];

    for (int i = 0; i < matrix_output2.length; i++) {
      for (int j = 0; j < matrix_output2[i].length; j++)
        matrix_output2[i][j] = matrix_output[i + 1][j + 1];
      vertex_letters_new2[i] = vertex_letters_new[i + 1];
    }

    AdjazenzMatrix am_output2 = new AdjazenzMatrix("Output2", matrix_output2, vertex_letters_new2, true);
    am_output2.setMaxFlow(max_flow);
    am_output2.printAndWriteMatrix(title);

    // Erstelle die Graphen und füge sie dem Fenster hinzu
    JGraphPanel p1 = new JGraphPanel("Rohdaten", am_input, "hierarchical");
    JGraphPanel p2 = new JGraphPanel("Flussnetzwerk (Ford-Fulkerson)", am_output,
        "hierarchical");
    JGraphPanel p3 = new JGraphPanel("Aufgabenverteilung", am_output2,
        "hierarchical");

    JPanel upper = new JPanel();
    upper.setLayout(new GridLayout(1, 2));
    upper.add(p1);
    upper.add(p2);
    add(upper);
    add(p3);
  }

  // function to find the path with the maximum flow
  public int[][] fordFulkerson(int[][] matrix) {

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
        rGraph[u][v] += path_flow; // add back path flow to rGraph
      }

      // Add path flow to max flow
      max_flow += path_flow;
    }

    int[][] outputGraph = new int[num_vertices][num_vertices];
    for (int i = 0; i < matrix[0].length; i++)
      for (int j = 0; j < matrix[0].length; j++)
        outputGraph[i][j] = matrix[i][j] - rGraph[i][j];

    return outputGraph;
  }

  private boolean bfs(int[][] matrix, int rGraph[][], int s, int t, int parent[]) {

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