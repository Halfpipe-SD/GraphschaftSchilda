package code;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.io.IOException;

import code.utils.AdjazenzMatrix;
import code.utils.BasicWindow;
import code.utils.FileHandler;
import code.utils.JGraphPanel;

public class Problem1 extends BasicWindow {

  private int num_vertices = 0;

  public Problem1(String title) throws FileNotFoundException, IOException {
    super(title);

    setSize(new Dimension(480, 650));
    setLayout(new GridLayout(1, 2));
    setLocationRelativeTo(null);

    // Initializiere FileHandler und lese die Daten aus der Datei
    FileHandler fh = new FileHandler("problem1.txt");

    // Erstelle die Adjazenzmatrix und gebe sie in der Konsole aus
    AdjazenzMatrix am_input = new AdjazenzMatrix("Input", fh.getMatrix(), fh.getVertexLetters(), false);
    am_input.printMatrix();

    // Erstelle die Ausgabe-Adjazenzmatrix mit dem Prim Algorithmus
    int[][] matrix_output = prim(am_input.getMatrixCopy());

    // Erstelle die Ausgabe-Adjazenzmatrix, gebe sie in der Konsole aus und schreibe
    // sie in eine Datei
    AdjazenzMatrix am_output = new AdjazenzMatrix("Output", matrix_output, fh.getVertexLetters(), false);
    am_output.printAndWriteMatrix(title);

    // Erstelle die Graphen und füge sie dem Fenster hinzu
    JGraphPanel p1 = new JGraphPanel("Rohdaten", am_input, "hierarchical");
    JGraphPanel p2 = new JGraphPanel("Minimum Spanning Tree (Prim)", am_output,
        "hierarchical");

    add(p1);
    add(p2);
  }

  // Function to construct and print MST for a graph
  // represented using adjacency matrix representation
  private int[][] prim(int[][] matrix) {

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