package code;

import static code.Problem2.fordFulkersonMaxFlow;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import code.utils.BasicWindow;
import code.utils.Edge;
import code.utils.Graph;
import code.utils.JGraphPanel;
import code.utils.Vertex;

public class Problem7 extends BasicWindow {

  public Problem7(String title) throws FileNotFoundException {
    super(title);

    setSize(new Dimension(1010, 710));
    setLayout(new GridLayout(2, 1));
    setLocationRelativeTo(null);

    Graph graph_input = new Graph("town_projects.json");

    // Füge Start- und Endknoten hinzu
    Vertex start = new Vertex("Start");
    Vertex end = new Vertex("End");
    graph_input.addVertex(start);
    graph_input.addVertex(end);

    // Füge Kanten für Start- und Endknoten hinzu
    ArrayList<Edge> additional_edges = generate_start_and_end_edges(graph_input, start, end);
    for (Edge edge : additional_edges) {
      graph_input.addEdge(edge);
    }

    // Erzeuge eine echte Kopie des Graphen
    Graph graph_output = graph_input.deepCopy();

    // Berechne den Fluss und aktualisiere den output Graphen
    fordFulkersonMaxFlow(graph_output, start.getLabel(), end.getLabel());

    // Erzeuge das erste Panel mit dem Flussnetzwerk
    JGraphPanel p1 = new JGraphPanel("Flussnetzwerk mit Ford-Fulkerson", graph_output, "hierarchical");
    // Erzeuge das zweite Panel mit dem Flussnetzwerk ohne Start- und Endknoten
    filter_graph(graph_output);
    JGraphPanel p2 = new JGraphPanel("Flussnetzwerk ohne Startknoten, Endknoten und Kanten ohne Fluss", graph_output,
        "hierarchical");

    add(p1);
    add(p2);
  }

  private Graph filter_graph(Graph input) {
    ArrayList<Vertex> vertices = input.getVertices();
    ArrayList<Edge> edges = input.getEdges();

    // Entferne die Start- und Endknoten und deren Kanten
    vertices.removeIf(v -> v.getLabel().equals("Start") || v.getLabel().equals("End"));

    // Entferne alle Kanten die nicht Teil des Flusses sind
    edges.removeIf(e -> e.getSource().equals("Start") || e.getTarget().equals("End"));
    edges.removeIf(e -> e.getFlow() == 0);

    input.setEdges(edges);
    input.setVertices(vertices);
    return input;
  }

  private ArrayList<Edge> generate_start_and_end_edges(Graph graph_input, Vertex start, Vertex end) {
    ArrayList<Edge> additional_edges = new ArrayList<>();
    // Fülle die liste an Start- und Endknoten
    for (Edge e : graph_input.getEdges()) {
      // Füge eine Kante zum Startknoten wenn sie noch nicht existiert
      boolean start_already_exists = false;
      for (Edge edge : additional_edges)
        if (edge.getSource().equals(start.getLabel()) && edge.getTarget().equals(e.getSource()))
          start_already_exists = true;
      if (!start_already_exists)
        additional_edges.add(new Edge(start.getLabel(), e.getSource(), 0, 1));

      // Füge eine Kante zum Endknoten wenn sie noch nicht existiert
      boolean end_already_exists = false;
      for (Edge edge : additional_edges)
        if (edge.getSource().equals(e.getTarget()) && edge.getTarget().equals(end.getLabel()))
          end_already_exists = true;
      if (!end_already_exists)
        additional_edges.add(new Edge(e.getTarget(), end.getLabel(), 0, 1));
    }
    return additional_edges;
  }
}