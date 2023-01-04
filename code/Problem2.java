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
  private int nodes = 0;

  public Problem2(String title) throws FileNotFoundException, IOException {
    super(title);

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

    // Filtere die inversen Kanten
    for (int i = 0; i < matrix_output.length; i++)
      for (int j = 0; j < matrix_output[i].length; j++)
        if (matrix_output[i][j] < 0)
          matrix_output[i][j] = 0;

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

  public int[][] fordFulkerson(int[][] matrix) {

    // Anzahl der Knoten im Graph
    nodes = matrix[0].length;

    // Die Quelle ist im Beispiel der erste Knoten
    int s = 0;
    // Die Senke ist im Beispiel der lezte Knoten
    int t = nodes - 1;

    int u, v;

    // Erzeuge echte Kopie der Matrix für Output
    int output[][] = new int[nodes][nodes];
    for (u = 0; u < nodes; u++)
      for (v = 0; v < nodes; v++)
        output[u][v] = matrix[u][v];

    // Erzeuge ein Eltern Array zum speichern der möglichen BFS-Pfade
    int parent[] = new int[nodes];

    // // There is no flow initially
    // max_flow = 0;

    // Wenn für einen Pfad der BFS möglich ist, überprüfe seinen maximalen Fluss
    while (bfs(matrix, output, s, t, parent)) {

      // Setze den Pfad Fluss auf unendlich
      int path_flow = Integer.MAX_VALUE;
      // Finde den maximalen Fluss durch die möglichen Pfade
      for (u = t; u != s; u = parent[u]) {
        v = parent[u];
        path_flow = Math.min(path_flow, output[v][u]);
      }

      // aktualisiere die Kanten aus dem Eletern Array
      for (u = t; u != s; u = parent[u]) {
        v = parent[u];
        // Ziehe den Fluss-Pfad den Kanten ab
        output[v][u] -= path_flow;
        // Addiere den Fluss-Pfad auf die Inversen Kanten
        output[u][v] += path_flow;
      }

      // Addiere die einzelen Flusspafde auf den maximalen Fluss
      max_flow += path_flow;
    }

    // Ziehe von der Eingabe Matrix die übrigen Flussgewichte ab
    int[][] outputGraph = new int[nodes][nodes];
    for (int i = 0; i < matrix[0].length; i++)
      for (int j = 0; j < matrix[0].length; j++)
        outputGraph[i][j] = matrix[i][j] - output[i][j];

    return outputGraph;
  }

  private boolean bfs(int[][] matrix, int output[][], int s, int t, int parent[]) {

    // Anzahl der Knoten im Graph
    nodes = matrix[0].length;

    // Array das alle Knoten als nicht besucht makiert
    boolean visited[] = new boolean[nodes];
    for (int i = 0; i < nodes; ++i)
      visited[i] = false;

    // Warteschlagnel, die besuchte Knoten als true makiert
    LinkedList<Integer> queue = new LinkedList<Integer>();
    queue.add(s);
    visited[s] = true;
    parent[s] = -1;

    // Standard BFS-Loob, entfernt Knoten aus der Warteschlange die ungleich 0 sind
    while (queue.size() != 0) {
      int u = queue.poll();

      for (int v = 0; v < nodes; v++) {
        if (visited[v] == false && output[u][v] > 0) {
          // Wenn wir einen möglichen Pfad von s nach t finden geben wir true zurück
          if (v == t) {
            parent[v] = u;
            return true;
          }
          // Wenn wir keinen möglichen Pfad finden fügen wir den Kntoen zur Warteschlagne und makieren ihn als besichtigt
          queue.add(v);
          parent[v] = u;
          visited[v] = true;
        }
      }
    }
    return false;
  }
}