package code;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import code.utils.BasicWindow;
import code.utils.Graph;
import code.utils.Edge;
import code.utils.Vertex;
import code.utils.JGraphPanel;

public class Problem2 extends BasicWindow {

  public Problem2(String title) throws FileNotFoundException {
    super(title);

    setSize(new Dimension(500, 460));
    setLayout(new GridLayout(1, 2));
    setLocationRelativeTo(null);

    Graph graph_input = new Graph("town_water.json");
    Graph graph_output = graph_input.deepCopy();

    fordFulkersonMaxFlow(graph_output, "Wasserwerk", "Supermarkt");

    // entferne alle inversen kanten für den output des graphen
    ArrayList<Edge> non_inverse = graph_output.getEdges();
    non_inverse.removeIf(e -> e.getCapacity() == 0);
    graph_output.setEdges(non_inverse);

    JGraphPanel p1 = new JGraphPanel("Rohdaten", graph_input, "hierarchical");
    JGraphPanel p2 = new JGraphPanel("Flussnetzwerk mit Ford-Fulkerson", graph_output, "hierarchical");

    add(p1);
    add(p2);
  }

  public static double fordFulkersonMaxFlow(Graph input, String source, String sink) {
    // Initialisierung des Graphen und des Startflusses
    Graph residual_graph = new Graph(input.getVertices(), input.getEdges(), true);
    double flow = 0;

    while (true) {
      // Finde einen Validen Weg im Graphen
      ArrayList<Edge> augmenting_path = findAugmentingPaths(residual_graph, source, sink);

      // Beende die Schleife wenn kein Weg gefunden wurde
      if (augmenting_path == null)
        break;

      // Bestimme die Flaschenhalskapazität des Weges und addiere sie zum Fluss
      double bottleneck = findBottleneck(residual_graph, augmenting_path);
      flow += bottleneck;

      // Aktualisiere den Graphen
      residual_graph = updateResidualGraph(residual_graph, augmenting_path, bottleneck, input.getVertices());
    }

    // Setze den maximalen Fluss des Graphen (für die Ausgabe)
    input.setMaximumFlow(flow);

    return flow;
  }

  private static Graph updateResidualGraph(Graph residual_graph, ArrayList<Edge> augmenting_path,
      double bottleneck, ArrayList<Vertex> vertices) {

    // Aktualisiere alle Kanten des Weges
    for (Edge edge : augmenting_path) {
      Vertex source = Graph.getVertexByLabel(vertices, edge.getSource());
      Vertex target = Graph.getVertexByLabel(vertices, edge.getTarget());

      // Aktualisiere den Fluss der Kante
      edge.setFlow(edge.getFlow() + bottleneck);

      // Prüfe ob die Inverse Kante bereits im Graphen ist und aktualisiere den Fluss
      // der Inversen Kante wenn ja
      boolean is_reverse_edge_in_graph = false;
      for (Edge reverse_edge : Graph.getEdgesOfVertex(residual_graph.getEdges(), target)) {
        if (!reverse_edge.getTarget().equals(source.getLabel()))
          break;
        reverse_edge.setFlow(reverse_edge.getFlow() - bottleneck);
        is_reverse_edge_in_graph = true;
      }

      // Füge die Inverse Kante zum Graphen hinzu wenn sie noch nicht vorhanden ist
      if (!is_reverse_edge_in_graph)
        residual_graph.addEdge(new Edge(target.getLabel(), source.getLabel(), -edge.getFlow(), 0));
    }

    return residual_graph;
  }

  private static double findBottleneck(Graph residual_graph, ArrayList<Edge> augmenting_path) {
    double bottleneck = Double.MAX_VALUE;
    // Finde die Flaschenhalskapazität des Weges
    for (Edge edge : augmenting_path) {
      if (residual_graph.getEdges().contains(edge))
        bottleneck = Math.min(bottleneck, edge.getCapacity() - edge.getFlow());
    }
    return bottleneck;
  }

  private static ArrayList<Edge> findAugmentingPaths(Graph residual_graph, String start, String end) {

    ArrayList<Edge> path = new ArrayList<Edge>();
    ArrayList<Edge> augmenting_path = new ArrayList<Edge>();

    Vertex source = Graph.getVertexByLabel(residual_graph.getVertices(), start);
    Vertex sink = Graph.getVertexByLabel(residual_graph.getVertices(), end);

    Stack<Vertex> stack = new Stack<Vertex>();

    // Füge den Source Knoten zum Stack hinzu und setze alle Knoten außer dem Source
    // auf nicht besucht
    stack.push(source);
    for (Vertex vertex : residual_graph.getVertices()) {
      if (vertex != source)
        vertex.setVisited(false);
    }

    while (!stack.isEmpty()) {
      // Entferne den obersten Knoten vom Stack
      Vertex current_vertex = stack.pop();

      // Prüfe alle Kanten des Knotens
      for (Edge edge : Graph.getEdgesOfVertex(residual_graph.getEdges(), current_vertex)) {
        Vertex target_vertex = Graph.getVertexByLabel(residual_graph.getVertices(), edge.getTarget());

        // Überspringe die Kante wenn der Zielknoten bereits besucht wurde oder die
        // Kante keine Kapaiztät mehr hat
        if (target_vertex.isVisited() || edge.getCapacity() - edge.getFlow() <= 0)
          continue;

        target_vertex.setVisited(true);

        // Füge die Kante zum Weg hinzu wenn sie nicht die Inverse Kante zum Source
        // Knoten ist (behobener Bug)
        if (!edge.getTarget().equals(source.getLabel()))
          path.add(edge);

        // Füge den Zielknoten zum Stack hinzu
        stack.push(target_vertex);

        // Prüfe ob der Zielknoten der Sink Knoten ist und füge den Weg zum
        // Ausgangsweg hinzu
        if (target_vertex.equals(sink))
          augmenting_path.addAll(path);
      }
    }

    // ** Erstelle einen neuen Pfad, der nur die Kanten von Source zu Sink enthält
    ArrayList<Edge> new_path = new ArrayList<Edge>();

    if (augmenting_path.size() == 0)
      return null;

    // Finde die Kante zum Sink Knoten und füge sie zum neuen Weg hinzu
    Edge edge_to_sink = null;
    for (Edge edge : augmenting_path) {
      if (edge.getTarget().equals(sink.getLabel()))
        edge_to_sink = edge;
    }
    new_path.add(edge_to_sink);

    // Baue nach und nach den neuen Weg Rückwärts vom Sink Knoten zum Source Knoten
    Edge last_added = edge_to_sink;
    boolean reverse_path_complete = false;
    while (!reverse_path_complete) {
      for (Edge edge : augmenting_path) {
        if (!edge.getTarget().equals(last_added.getSource()))
          continue;
        new_path.add(edge);
        last_added = edge;
        // Neuer Weg ist komplett, wenn die letzte Kante zum Source Knoten führt
        if (last_added.getSource().equals(source.getLabel()))
          reverse_path_complete = true;
      }
    }

    // Invertiere den neuen Weg
    Collections.reverse(new_path);
    return new_path;
  }
}