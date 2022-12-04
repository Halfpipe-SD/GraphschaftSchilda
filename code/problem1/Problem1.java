package code.problem1;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import code.utils.BasicWindow;
import code.utils.GraphData;
import code.utils.GraphEdge;
import code.utils.GraphVertex;
import code.utils.JGraphPanel;

public class Problem1 extends BasicWindow {

  public Problem1(String title) throws FileNotFoundException {
    super(title);

    setSize(new Dimension(510, 600));
    setLayout(new GridLayout(1, 2));
    setLocationRelativeTo(null);

    GraphData graph_input = new GraphData("town_streets.json");

    GraphData graph_output = prim(graph_input);

    Dimension size = new Dimension(getWidth() / 2, getHeight());
    JGraphPanel p1 = new JGraphPanel("Rohdaten", size, graph_input, "hierarchical");
    JGraphPanel p2 = new JGraphPanel("Minimum Spanning Tree (Prim)", size, graph_output, "hierarchical");

    add(p1);
    add(p2);
  }

  private GraphData prim(GraphData input) {

    // Lese die Knoten und Kanten aus den Rohdaten
    ArrayList<GraphVertex> vertices = input.getVertices();
    ArrayList<GraphEdge> edges = input.getEdges();

    // Initialisiere alle Knoten mit ∞, setze den Vorgänger auf null
    for (GraphVertex v : vertices) {
      v.setValue(Integer.MAX_VALUE);
      v.setPredecessor(null);
    }

    // Starte mit beliebigem Startknoten
    // Startknoten bekommt den Wert 0
    GraphVertex start = vertices.get(6);
    start.setValue(0);

    // Speichere alle Knoten in einer geeigneten Datenstruktur Q
    // -> Prioritätswarteschlange
    PriorityQueue<GraphVertex> queue = new PriorityQueue<GraphVertex>(
        Comparator.comparingInt(GraphVertex::getValue));
    queue.addAll(vertices);

    // Solange es noch Knoten in Q gibt...
    while (!queue.isEmpty()) {
      // Wähle den Knoten aus Q mit dem kleinsten Schlüssel (v)
      GraphVertex vertex = queue.poll();
      System.out.println(" -> Polled vertex: " + vertex.getLabel() + " with value: " +
          vertex.getValue());

      // Speichere alle Nachbarn von v in neighbours
      ArrayList<GraphVertex> neighbors = GraphData.getNeighbors(vertex, vertices, edges);

      for (GraphVertex n : neighbors) {
        // Finde Kante zwischen v und n
        for (GraphEdge edge : GraphData.getEdgesBetweenTwoVertices(vertex, n, edges)) {
          // Wenn der Wert der Kante kleiner ist als der Wert des Knotens und der Knoten
          // noch in Q enthalten ist
          if (edge.getWeight() < n.getValue() && queue.contains(n)) {
            // Speichere v als vorgänger von n und passe wert von n an
            n.setValue((int) edge.getWeight());
            n.setPredecessor(vertex);
            // Aktualisiere die Prioritätswarteschlange
            queue.remove(n);
            queue.add(n);
            System.out.println("vertex " + n + " updated key to " + n.getValue());
          }
        }
      }
    }

    // generiere neue kanten für den graphen anhand der vorgänger der knoten
    ArrayList<GraphEdge> new_edges = new ArrayList<GraphEdge>();

    for (GraphVertex v : vertices) {
      if (v.getPredecessor() != null) {
        new_edges.add(new GraphEdge(v.getPredecessor().getLabel(), v.getLabel(), v.getValue()));
      }
    }

    return new GraphData(vertices, new_edges, false);
  }

}