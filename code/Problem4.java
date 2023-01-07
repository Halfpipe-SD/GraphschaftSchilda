package code;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import javax.swing.JLabel;
import javax.swing.JPanel;

import code.utils.AdjazenzMatrix;
import code.utils.BasicWindow;
import code.utils.Edge;
import code.utils.FileHandler;
import code.utils.JGraphPanel;
import code.utils.Vertex;

public class Problem4 extends BasicWindow {

  private ArrayList<Vertex> vertices = new ArrayList<>();

  public Problem4(String title) throws FileNotFoundException, IOException {
    super(title);

    setSize(new Dimension(1020, 460));
    setLayout(new GridLayout(1, 3));
    setLocationRelativeTo(null);

    // Initialisiere FileHandler und lese die Daten aus der Datei
    FileHandler fh = new FileHandler("problem4.txt");

    // Erstelle die Adjazenzmatrix und gebe sie in der Konsole aus
    AdjazenzMatrix am_input = new AdjazenzMatrix("Input", fh.getMatrix(), fh.getVertexLetters(), false);
    am_input.printMatrix();

    int[][] matrix_output = dijkstra(am_input.getMatrix(), am_input.getVertexLetters());

    // Erstelle die Ausgabe-Adjazenzmatrix, gebe sie in der Konsole aus und schreibe
    // sie in eine Datei
    AdjazenzMatrix am_output = new AdjazenzMatrix("Output", matrix_output,
        fh.getVertexLetters(), false);
    am_output.printAndWriteMatrix(title);

    // Erstelle die Graphen und füge sie dem Fenster hinzu
    JGraphPanel p1 = new JGraphPanel("Rohdaten", am_input, "circle");
    JGraphPanel p2 = new JGraphPanel("Kürzester Weg zu jedem Knoten (Dijkstra)", am_output, "circle");

    add(p1);
    add(p2);

    JPanel sidepanel = new JPanel();
    String info = "<h3>Bestimmung des kürzesten Weges mit Dijkstra</h3><br><br>"
        + "<h3>Reihenfolge bei Zündung des Streichholzes:</h3>" + "<br>";

    for (int i = 0; i < vertices.size(); i++)
      info += i + 1 + ". " + vertices.get(i).getLetter() + " mit einer Distanz von => "
          + vertices.get(i).getKey() + "<br><br>";

    sidepanel.add(new JLabel("<html>" + info + "</html>"));
    add(sidepanel);
  }

  /**
   * Laufzeit: O(V^3 * E)
   * 
   * @param matrix
   * @param vertexLetters
   * @return
   */
  private int[][] dijkstra(int[][] matrix, char[] vertexLetters) {

    // Generiere eine Liste aller Knoten
    for (int i = 0; i < matrix.length; i++)
      vertices.add(new Vertex(vertexLetters[i], 0));

    ArrayList<Edge> edges = getEdges(matrix, vertexLetters); // O(V^2)

    // Initialisiere die Distanz im Startknoten mit 0 und in allen anderen Knoten
    // mit ∞.
    for (Vertex vertex : vertices) {
      vertex.setKey(Integer.MAX_VALUE);
      vertex.setPredecessor(null);
    }
    vertices.get(0).setKey(0);

    // Speichere alle Knoten in einer Prioritätswarteschlange queue
    PriorityQueue<Vertex> queue = new PriorityQueue<Vertex>(
        Comparator.comparingInt(Vertex::getKey));
    queue.addAll(vertices);

    // Solange es noch unbesuchte Knoten gibt, wähle darunter denjenigen mit
    // minimaler Distanz aus und
    while (!queue.isEmpty()) {
      // Nehme den Knoten mit dem kleinsten Wert aus der Warteschlange
      Vertex v = queue.poll();

      // 1. speichere, dass dieser Knoten schon besucht wurde
      v.setVisited(true);

      // 2. berechne für alle noch unbesuchten Nachbarknoten die Summe des jeweiligen
      // Kantengewichtes und der Distanz im aktuellen Knoten
      for (Vertex n : getNeighbors(v, vertices, edges)) {

        // 3. ist dieser Wert für einen Knoten kleiner als die
        // dort gespeicherte Distanz, aktualisiere sie und setze den aktuellen Knoten
        // als Vorgänger. (Dieser Schritt wird auch als Update bezeichnet. )
        int sum = v.getKey() + getWeightSum(v, n, edges);

        if (sum >= n.getKey())
          continue;

        n.setKey((int) sum);
        n.setPredecessor(v);
        // Aktualisiere die Prioritätswarteschlange
        queue.remove(n);
        queue.add(n);
      }
    }

    // Sortiere Knoten nach Distanz
    vertices.sort(Comparator.comparingInt(Vertex::getKey));

    // Erstelle eine neue Adjazenzmatrix, die den jeweils kürzesten Weg zu jedem
    // Knoten enthält
    int[][] matrix_output = new int[matrix.length][matrix.length];

    for (Vertex vertex : vertices) {
      if (vertex.getPredecessor() == null)
        continue;
      matrix_output[vertex.getPredecessor().getLetter() - 'A'][vertex.getLetter() - 'A'] = vertex.getKey();
      matrix_output[vertex.getLetter() - 'A'][vertex.getPredecessor().getLetter() - 'A'] = vertex.getKey();
    }

    return matrix_output;
  }

  /**
   * Laufzeit: O(V^2)
   * 
   * @param matrix
   * @param vertexLetters
   * @return
   */
  private ArrayList<Edge> getEdges(int[][] matrix, char[] vertexLetters) {
    ArrayList<Edge> edges = new ArrayList<>();

    // Iteriere über die Adjazenzmatrix und füge alle Kanten der Liste hinzu
    for (int i = 0; i < matrix.length; i++)
      for (int j = 0; j < matrix.length; j++)
        if (matrix[i][j] != 0)
          edges.add(new Edge(vertexLetters[i], vertexLetters[j], matrix[i][j]));

    return edges;
  }

  /**
   * Laufzeit: O(V * E)
   * 
   * @param u
   * @param vertices
   * @param edges
   * @return
   */
  private ArrayList<Vertex> getNeighbors(Vertex u, ArrayList<Vertex> vertices, ArrayList<Edge> edges) {
    // neue Liste für nachbarn
    ArrayList<Vertex> neighbors = new ArrayList<Vertex>();

    for (Edge e : edges) {
      // Überspringe Kanten, die nicht mit dem Knoten verbunden sind
      if (e.getSource() != u.getLetter() && e.getTarget() != u.getLetter())
        continue;
      // Finde vertex v anhand der source/target, der mit u verbunden ist und füge ihn
      // der Liste hinzu
      for (Vertex v : vertices)
        if ((v.getLetter() == e.getSource() || v.getLetter() == e.getTarget()) && v != u)
          neighbors.add(v);
    }
    return neighbors;
  }

  /**
   * Laufzeit: O(E)
   * 
   * @param u
   * @param v
   * @param edges
   * @return
   */
  private int getWeightSum(Vertex u, Vertex v, ArrayList<Edge> edges) {
    int sum = 0;
    for (Edge e : edges) {
      if (e.getSource() == u.getLetter() && e.getTarget() == v.getLetter())
        sum += e.getWeight();
      if (e.getSource() == v.getLetter() && e.getTarget() == u.getLetter())
        sum += e.getWeight();
    }
    return sum;
  }

}