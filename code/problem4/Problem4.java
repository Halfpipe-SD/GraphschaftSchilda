package code.problem4;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import javax.swing.JLabel;
import javax.swing.JPanel;

import code.utils.BasicWindow;
import code.utils.GraphData;
import code.utils.GraphEdge;
import code.utils.GraphVertex;
import code.utils.JGraphPanel;

public class Problem4 extends BasicWindow {

  public Problem4(String title) throws FileNotFoundException {
    super(title);

    setSize(new Dimension(1000, 600));
    setLayout(new GridLayout(1, 2));
    setLocationRelativeTo(null);

    GraphData graph_input = new GraphData("town_fireworks.json");

    ArrayList<GraphVertex> vertices_output = dijkstra(graph_input);

    Dimension size = new Dimension(getWidth(), getHeight());
    JGraphPanel p1 = new JGraphPanel("Rohdaten", graph_input, "circle");

    add(p1);

    JPanel sidepanel = new JPanel();
    String info = "<h3>Bestimmung des kürzesten Weges mit Dijkstra</h3><br><br>"
        + "<h2>Reihenfolge bei Zündung des Steichholzes:</h2>" + "<br>";

    for (int i = 0; i < vertices_output.size(); i++) {
      info += i + 1 + ". " + vertices_output.get(i).getLabel() + " mit einer Distanz von => "
          + vertices_output.get(i).getValue() + "<br><br>";
    }

    sidepanel.add(new JLabel("<html>" + info + "</html>"));
    add(sidepanel);
  }

  private ArrayList<GraphVertex> dijkstra(GraphData input) {

    ArrayList<GraphVertex> vertices = input.getVertices();
    ArrayList<GraphEdge> edges = input.getEdges();

    // Initialisiere die Distanz im Startknoten mit 0 und in allen anderen Knoten
    // mit ∞.
    for (GraphVertex vertex : vertices) {
      vertex.setValue(Integer.MAX_VALUE);
      vertex.setPredecessor(null);
    }
    vertices.get(0).setValue(0);

    // Speichere alle Knoten in einer Prioritätswarteschlange queue
    PriorityQueue<GraphVertex> queue = new PriorityQueue<GraphVertex>(
        Comparator.comparingInt(GraphVertex::getValue));
    queue.addAll(vertices);

    // Solange es noch unbesuchte Knoten gibt, wähle darunter denjenigen mit
    // minimaler Distanz aus und
    while (!queue.isEmpty()) {
      // Nehme den Knoten mit dem kleinsten Wert aus der Warteschlange
      GraphVertex v = queue.poll();

      // 1. speichere, dass dieser Knoten schon besucht wurde
      v.setVisited(true);

      // 2. berechne für alle noch unbesuchten Nachbarknoten die Summe des jeweiligen
      // Kantengewichtes und der Distanz im aktuellen Knoten
      ArrayList<GraphVertex> neighbors = GraphData.getNeighbors(v, vertices, edges);

      for (GraphVertex n : neighbors) {
        // 3. ist dieser Wert für einen Knoten kleiner als die
        // dort gespeicherte Distanz, aktualisiere sie und setze den aktuellen Knoten
        // als Vorgänger. (Dieser Schritt wird auch als Update bezeichnet. )

        double sum = v.getValue() + GraphData.getWeightSum(v, n, edges);

        if (sum < n.getValue()) {
          n.setValue((int) sum);
          n.setPredecessor(v);
          // Aktualisiere die Prioritätswarteschlange
          queue.remove(n);
          queue.add(n);
        }
      }
    }

    // Sortiere Knoten nach Distanz
    vertices.sort(Comparator.comparingInt(GraphVertex::getValue));

    return vertices;
  }

}