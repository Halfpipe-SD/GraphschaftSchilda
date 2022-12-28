package code;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.io.IOException;

import code.utils.AdjazenzMatrix;
import code.utils.BasicWindow;
import code.utils.FileHandler;

public class Problem1 extends BasicWindow {

  private AdjazenzMatrix am_input;

  private int num_vertices = 0;

  public Problem1(String title) throws FileNotFoundException, IOException {
    super(title);

    setSize(new Dimension(510, 600));
    setLayout(new GridLayout(1, 2));
    setLocationRelativeTo(null);

    FileHandler fh = new FileHandler("graph_ungerichtet_gewichtet.txt");

    am_input = new AdjazenzMatrix("Input", fh.getMatrix(), false);
    am_input.printMatrix();

    AdjazenzMatrix am_output = new AdjazenzMatrix("Output", prim(), false);
    am_output.printMatrix();
  }

  // Function to construct and print MST for a graph
  // represented using adjacency matrix representation
  private int[][] prim() {
    int[][] matrix = am_input.getMatrix();

    // number of vertices in graph
    num_vertices = matrix[0].length;

    int parents[] = new int[num_vertices];
    int vertices_value[] = new int[num_vertices];
    Boolean mst_vertices[] = new Boolean[num_vertices];

    // Initialisiere alle Knoten mit ∞, setze den Vorgänger auf null
    for (int i = 0; i < num_vertices; i++) {
      vertices_value[i] = Integer.MAX_VALUE;
      mst_vertices[i] = false;
    }

    // Starte mit beliebigem Startknoten, bekommt den Wert 0 und besitzt keinen
    // Vorgänger
    vertices_value[0] = 0;
    parents[0] = -1;

    for (int count = 0; count < num_vertices - 1; count++) {

      int u = minKey(vertices_value, mst_vertices);

      // Add the picked vertex to the MST Set
      mst_vertices[u] = true;

      for (int v = 0; v < num_vertices; v++)

        if (matrix[v][u] != 0 && mst_vertices[v] == false
            && matrix[u][v] < vertices_value[v]) {
          parents[v] = u;
          vertices_value[v] = matrix[v][u];
        }
    }

    for (int i = 1; i < num_vertices; i++) {
      matrix[i][parents[i]] = matrix[parents[i]][i];
    }

    // print edges
    for (int i = 1; i < num_vertices; i++)
      System.out.println(parents[i] + " - " + i + " \t" + matrix[parents[i]][i]);

    return matrix;
  }

  int minKey(int key[], Boolean mstSet[]) {
    // Initialize min value
    int min = Integer.MAX_VALUE;
    int min_index = -1;

    for (int v = 0; v < num_vertices; v++)
      if (mstSet[v] == false && key[v] < min) {
        min = key[v];
        min_index = v;
      }

    return min_index;
  }

}