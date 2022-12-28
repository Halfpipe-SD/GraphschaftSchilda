package code.old;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import code.utils.BasicWindow;
import code.utils.JGraphPanel;

public class Problem1 extends BasicWindow {

  public Problem1(String title) throws FileNotFoundException {
    super(title);

    setSize(new Dimension(510, 600));
    setLayout(new GridLayout(1, 2));
    setLocationRelativeTo(null);

    Graph graph_input = new Graph("town_streets.json");

    Graph graph_output = prim(graph_input);

    JGraphPanel p1 = new JGraphPanel("Rohdaten", graph_input, "hierarchical");
    JGraphPanel p2 = new JGraphPanel("Minimum Spanning Tree (Prim)", graph_output, "hierarchical");

    add(p1);
    add(p2);
  }

  private Graph prim(Graph input) {

    // Lese die Knoten und Kanten aus den Rohdaten
    ArrayList<Vertex> vertices = input.getVertices();
    ArrayList<Edge> edges = input.getEdges();

    // Initialisiere alle Knoten mit ∞, setze den Vorgänger auf null
    for (Vertex v : vertices) {
      v.setValue(Integer.MAX_VALUE);
      v.setPredecessor(null);
    }

    // Starte mit beliebigem Startknoten
    // Startknoten bekommt den Wert 0
    Vertex start = vertices.get(6);
    start.setValue(0);

    // Speichere alle Knoten in einer geeigneten Datenstruktur Q
    // -> Prioritätswarteschlange
    PriorityQueue<Vertex> queue = new PriorityQueue<Vertex>(
        Comparator.comparingInt(Vertex::getValue));
    queue.addAll(vertices);

    // Solange es noch Knoten in Q gibt...
    while (!queue.isEmpty()) {
      // Wähle den Knoten aus Q mit dem kleinsten Schlüssel (v)
      Vertex vertex = queue.poll();

      // Für jeden Nachbarknoten n von v...
      for (Vertex n : Graph.getNeighbors(vertex, vertices, edges)) {

        // Finde Kante zwischen v und n
        Edge edge = Graph.getEdgeBetweenTwoVertices(vertex, n, edges);

        // Wenn der Wert der Kante kleiner ist als der Wert des Knotens und der Knoten
        // noch in Q enthalten ist
        if (edge.getWeight() >= n.getValue() || !queue.contains(n))
          continue;

        // Speichere v als vorgänger von n und passe wert von n an
        n.setValue((int) edge.getWeight());
        n.setPredecessor(vertex);

        // Aktualisiere die Prioritätswarteschlange
        queue.remove(n);
        queue.add(n);
      }
    }

    // generiere neue kanten für den graphen anhand der vorgänger der knoten
    ArrayList<Edge> new_edges = new ArrayList<Edge>();

    for (Vertex v : vertices) {
      if (v.getPredecessor() != null) {
        new_edges.add(new Edge(v.getPredecessor().getLabel(), v.getLabel(), v.getValue()));
      }
    }

    return new Graph(vertices, new_edges, false);
  }

}