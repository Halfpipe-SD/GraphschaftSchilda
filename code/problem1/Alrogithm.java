package code.problem1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import code.utils.GraphData;
import code.utils.GraphVertex;
import code.utils.GraphEdge;

public class Alrogithm {

  private ArrayList<GraphVertex> vertices;
  private ArrayList<GraphEdge> edges;

  // initialize vertices and edges from graph data
  public Alrogithm(GraphData data) {
    this.vertices = data.getVertices();
    this.edges = data.getEdges();
  }

  // use prim algorithm to get the minimum spanning tree
  public GraphData get_output_graph() {

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
    PriorityQueue<GraphVertex> queue = new PriorityQueue<GraphVertex>(vertices.size(), new VertexComparator());
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
          // Wenn der Wert der Kante kleiner ist als der Wert des Knotens
          // prüfe ob der Knoten noch in Q ist
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

    return new GraphData(this.vertices, new_edges, false);
  }

  private class VertexComparator implements Comparator<GraphVertex> {
    @Override
    public int compare(GraphVertex v1, GraphVertex v2) {
      return v1.getValue() - v2.getValue();
    }
  }
}
