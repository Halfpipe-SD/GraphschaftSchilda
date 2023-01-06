package code;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import code.utils.AdjazenzMatrix;
import code.utils.BasicWindow;
import code.utils.Edge;
import code.utils.FileHandler;
import code.utils.JGraphPanel;
import code.utils.Vertex;

public class Problem3 extends BasicWindow {

  public Problem3(String title) throws FileNotFoundException, IOException {
    super(title);

    setSize(new Dimension(600, 400));
    setLayout(new GridLayout(1, 2));
    setLocationRelativeTo(null);

    // Initialisiere FileHandler und lese die Daten aus der Datei
    FileHandler fh = new FileHandler("problem3.txt");

    // Erstelle die Adjazenzmatrix und gebe sie in der Konsole aus
    AdjazenzMatrix am_input = new AdjazenzMatrix("Input", fh.getMatrix(), fh.getVertexLetters(), false);
    am_input.printMatrix();

    int max_edges = 5;
    int[][] matrix_output = kruskal(am_input, max_edges);

    // Erstelle die Ausgabe-Adjazenzmatrix, gebe sie in der Konsole aus und schreibe
    // sie in eine Datei
    AdjazenzMatrix am_output = new AdjazenzMatrix("Output", matrix_output,
        fh.getVertexLetters(), false);
    am_output.printAndWriteMatrix(title);

    // Erstelle die Graphen und füge sie dem Fenster hinzu
    JGraphPanel p1 = new JGraphPanel("Rohdaten", am_input, "circle");
    JGraphPanel p2 = new JGraphPanel("MST (Kruskal, max " + max_edges + " edges)", am_output,
        "circle");

    add(p1);
    add(p2);
  }

  private int[][] kruskal(AdjazenzMatrix input, int max_edges) {

    int[][] matrix = input.getMatrixCopy();
    char[] vertexLetters = input.getVertexLetters();

    ArrayList<Vertex> vertices = new ArrayList<>();

    // Generiere eine Liste aller Knoten
    for (int i = 0; i < matrix.length; i++)
      vertices.add(new Vertex(vertexLetters[i], 0));

    ArrayList<Edge> edges = getEdges(matrix, vertexLetters);

    // Sortiere die Kanten nach Gewicht
    edges.sort(Comparator.comparingInt(Edge::getWeight));

    // erstelle einen wald 'forest' (eine menge von bäumen), wo jeder knoten ein
    // eigener baum ist
    ArrayList<ArrayList<Vertex>> forest = new ArrayList<ArrayList<Vertex>>();
    for (Vertex v : vertices) {
      ArrayList<Vertex> tree = new ArrayList<Vertex>();
      tree.add(v);
      forest.add(tree);
    }

    // erstelle eine liste mit den kanten des minimum spanning trees
    ArrayList<Edge> forest_edges = new ArrayList<Edge>(edges);

    // erstelle eine liste für die Ausgabe
    ArrayList<Edge> output_edges = new ArrayList<Edge>();

    // solange der wald nicht leer ist und der baum noch nicht alle knoten enthält
    while (forest_edges.size() > 0) {
      // entferne eine kante (u, v) aus forest
      Edge e = forest_edges.remove(0);

      // finde die bäume, die mit der Kante e verbunden sind
      ArrayList<Vertex> tree_u = null;
      ArrayList<Vertex> tree_v = null;
      for (ArrayList<Vertex> t : forest) {
        if (t.contains(getSourceVertexFromEdge(e, vertices)))
          tree_u = t;
        if (t.contains(getTargetVertexFromEdge(e, vertices)))
          tree_v = t;
      }

      // Prüfe ob die kante e von einem vertex ausgeht, der bereits mehr als 5 kanten
      // hat
      ArrayList<Edge> source_edges = getAdjacentEdges(e.getSource(), output_edges);
      ArrayList<Edge> target_edges = getAdjacentEdges(e.getTarget(), output_edges);

      if (source_edges.size() >= max_edges || target_edges.size() >= max_edges)
        continue;

      // wenn u und v in gleichen Bäumen sind -> skip
      if (tree_u == tree_v)
        continue;

      // füge kante von u und v zur Ausgabe hinzu
      output_edges.add(e);

      // füge baum von v zu baum von u hinzu (merge)
      for (Vertex v : tree_v)
        tree_u.add(v);

      forest.remove(tree_v);
    }

    // erstelle die Ausgabe-Adjazenzmatrix
    int[][] output_matrix = new int[matrix.length][matrix.length];
    for (Edge e : output_edges) {
      int source = e.getSource() - 'A';
      int target = e.getTarget() - 'A';
      output_matrix[source][target] = matrix[source][target];
      output_matrix[target][source] = matrix[target][source];
    }
    return output_matrix;
  }

  private ArrayList<Edge> getAdjacentEdges(char vertex, ArrayList<Edge> edges) {
    ArrayList<Edge> adjacent_edges = new ArrayList<Edge>();
    for (Edge e : edges)
      if (e.getSource() == vertex || e.getTarget() == vertex)
        adjacent_edges.add(e);
    return adjacent_edges;
  }

  private Vertex getSourceVertexFromEdge(Edge e, ArrayList<Vertex> vertices) {
    for (Vertex v : vertices)
      if (v.getLetter() == e.getSource())
        return v;
    return null;
  }

  private Vertex getTargetVertexFromEdge(Edge e, ArrayList<Vertex> vertices) {
    for (Vertex v : vertices)
      if (v.getLetter() == e.getTarget())
        return v;
    return null;
  }

  private ArrayList<Edge> getEdges(int[][] matrix, char[] vertexLetters) {
    ArrayList<Edge> edges = new ArrayList<>();

    // Iteriere über die Adjazenzmatrix und füge alle Kanten der Liste hinzu
    for (int i = 0; i < matrix.length; i++)
      for (int j = 0; j < matrix.length; j++)
        if (matrix[i][j] != 0)
          edges.add(new Edge(vertexLetters[i], vertexLetters[j], matrix[i][j]));

    return edges;
  }
}