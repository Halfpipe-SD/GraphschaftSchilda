package code;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import javax.swing.JLabel;
import javax.swing.JPanel;

import code.utils.BasicWindow;
import code.utils.Graph;
import code.utils.Edge;
import code.utils.Vertex;
import code.utils.JGraphPanel;

public class Problem4 extends BasicWindow {

  public Problem4(String title) throws FileNotFoundException {
    super(title);

    setSize(new Dimension(1000, 600));
    setLayout(new GridLayout(1, 2));
    setLocationRelativeTo(null);

    Graph graph_input = new Graph("town_fireworks.json");

    ArrayList<Vertex> vertices_output = dijkstra(graph_input);

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

  private ArrayList<Vertex> dijkstra(Graph input) {

    ArrayList<Vertex> vertices = input.getVertices();
    ArrayList<Edge> edges = input.getEdges();

    // Initialisiere die Distanz im Startknoten mit 0 und in allen anderen Knoten
    // mit ∞.
    for (Vertex vertex : vertices) {
      vertex.setValue(Integer.MAX_VALUE);
      vertex.setPredecessor(null);
    }
    vertices.get(0).setValue(0);

    // Speichere alle Knoten in einer Prioritätswarteschlange queue
    PriorityQueue<Vertex> queue = new PriorityQueue<Vertex>(
        Comparator.comparingInt(Vertex::getValue));
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
      for (Vertex n : Graph.getNeighbors(v, vertices, edges)) {

        // 3. ist dieser Wert für einen Knoten kleiner als die
        // dort gespeicherte Distanz, aktualisiere sie und setze den aktuellen Knoten
        // als Vorgänger. (Dieser Schritt wird auch als Update bezeichnet. )
        double sum = v.getValue() + Graph.getWeightSum(v, n, edges);

        if (sum >= n.getValue())
          continue;

        n.setValue((int) sum);
        n.setPredecessor(v);
        // Aktualisiere die Prioritätswarteschlange
        queue.remove(n);
        queue.add(n);
      }
    }

    // Sortiere Knoten nach Distanz
    vertices.sort(Comparator.comparingInt(Vertex::getValue));

    return vertices;
  }

}