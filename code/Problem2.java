package code;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

import code.utils.BasicWindow;
import code.utils.Edge;
import code.utils.Graph;
import code.utils.JGraphPanel;
import code.utils.Vertex;

public class Problem2 extends BasicWindow {

  public Problem2(String title) throws FileNotFoundException {
    super(title);

    setSize(new Dimension(500, 460));
    setLayout(new GridLayout(1, 2));
    setLocationRelativeTo(null);

    Graph graph_input = new Graph("town_water.json");
    Graph graph_output = graph_input.deepCopy();

    fordFulkersonMaxFlow(graph_output, "Wasserwerk", "Supermarkt");

    JGraphPanel p1 = new JGraphPanel("Rohdaten", graph_input, "hierarchical");
    JGraphPanel p2 = new JGraphPanel("Flussnetzwerk mit Ford-Fulkerson", graph_output, "hierarchical");

    add(p1);
    add(p2);
  }

  public static double fordFulkersonMaxFlow(Graph input, String source, String sink) {
    double flow = 0;

    // Initialisiere Rest- und Flussgraph
    ArrayList<Edge> flow_edges = new ArrayList<Edge>();
    ArrayList<Edge> residual_edges = new ArrayList<Edge>();
    for (Edge edge : input.getEdges()) {
      residual_edges.add(new Edge(edge.getSource(), edge.getTarget(), edge.getFlow(), edge.getCapacity()));
      flow_edges.add(new Edge(edge.getSource(), edge.getTarget(), 0, edge.getCapacity()));
    }

    while (true) {
      // Finde einen gültigen Weg im Restgraphen und beende die Schleife, wenn keiner
      // mehr existiert
      ArrayList<Edge> augmenting_path = get_augmenting_path_bfs(input.getVertices(), residual_edges, source, sink);
      if (augmenting_path.isEmpty())
        break;

      // Berechne die Flaschenhalskapazität des Weges
      double bottleneck = calculate_bottleneck(augmenting_path);
      if (bottleneck == 0)
        break;

      // Aktualisiere Rest- und Flussgraph
      update_residual_graph(residual_edges, augmenting_path, bottleneck);
      update_flow_graph(flow_edges, augmenting_path, bottleneck);

      // Addiere die Flaschenhalskapazität zum maximalen Fluss
      flow += bottleneck;
    }

    // Entferne alle Kanten, die durch eine benutzte inverse Kante invalide wurden
    flow_edges.removeIf(e -> e.getFlow() > e.getCapacity());

    // Aktualisiere den Outputgraphen und setze den maximalen Fluss
    input.setEdges(flow_edges);
    input.setMaximumFlow(flow);

    return flow;
  }

  private static double calculate_bottleneck(ArrayList<Edge> augmenting_path) {
    double bottleneck = Double.MAX_VALUE;
    // Durchlaufe alle Kanten des gefundenen Weges und aktualisiere den Flaschenhals
    for (Edge edge : augmenting_path) {
      // Überspringe volle Kanten
      if (edge.getFlow() == edge.getCapacity())
        continue;
      if (edge.getCapacity() - edge.getFlow() < bottleneck)
        bottleneck = edge.getCapacity() - edge.getFlow();
    }
    return bottleneck;
  }

  private static void update_flow_graph(ArrayList<Edge> flow_edges, ArrayList<Edge> augmenting_path,
      double bottleneck) {
    for (Edge edge : flow_edges)
      for (Edge path_edge : augmenting_path)
        if (edge.getSource().equals(path_edge.getSource()) && edge.getTarget().equals(path_edge.getTarget()))
          edge.setFlow(edge.getFlow() + bottleneck);
  }

  private static void update_residual_graph(ArrayList<Edge> residual_edges, ArrayList<Edge> augmenting_path,
      double bottleneck) {
    // Aktualisiere den Restgraphen und füge ggf. Inverse Kanten hinzu
    ArrayList<Edge> reverse_edges = new ArrayList<Edge>();
    for (Edge edge : residual_edges) {
      for (Edge path_edge : augmenting_path) {
        // Aktualisiere die nicht-inverse Kante
        if (edge.getSource().equals(path_edge.getSource()) && edge.getTarget().equals(path_edge.getTarget())) {
          edge.setFlow(edge.getFlow() + bottleneck);
          // Füge eine Inverse Kante hinzu, wenn diese noch nicht existiert
          if (!reverse_edge_exists(residual_edges, edge))
            reverse_edges.add(new Edge(edge.getTarget(), edge.getSource(), 0, edge.getCapacity()));
        }
        // Aktualisiere die Inverse Kante
        if (edge.getSource().equals(path_edge.getTarget()) && edge.getTarget().equals(path_edge.getSource()))
          edge.setFlow(edge.getFlow() - bottleneck);
      }
    }
    residual_edges.addAll(reverse_edges);
  }

  private static boolean reverse_edge_exists(ArrayList<Edge> residual_edges, Edge edge) {
    for (Edge residual_edge : residual_edges)
      if (residual_edge.getSource().equals(edge.getTarget())
          && residual_edge.getTarget().equals(edge.getSource()))
        return true;
    return false;
  }

  private static ArrayList<Edge> get_augmenting_path_bfs(ArrayList<Vertex> vertices, ArrayList<Edge> residual_edges,
      String source, String sink) {

    // Erstelle eine Adjazenzliste aus dem Restgraphen und initialisiere Listen
    ArrayList<ArrayList<Edge>> adjacency_list = new ArrayList<ArrayList<Edge>>();
    for (int i = 0; i < vertices.size(); i++)
      adjacency_list.add(new ArrayList<Edge>());
    for (Edge edge : residual_edges)
      adjacency_list.get(vertices.indexOf(Graph.getVertexByLabel(vertices, edge.getSource()))).add(edge);

    // Erstelle ein Array, das angibt, ob ein Knoten bereits besucht wurde
    boolean[] visited = new boolean[vertices.size()];
    for (int i = 0; i < visited.length; i++)
      visited[i] = false;

    // Erstelle ein Array, das den Vorgänger jedes Knotens speichert
    int[] parent = new int[vertices.size()];
    for (int i = 0; i < parent.length; i++)
      parent[i] = -1;

    // Führe eine Breitensuche mithilfe einer LinkedList durch
    Queue<Integer> queue = new LinkedList<Integer>();
    queue.add(vertices.indexOf(Graph.getVertexByLabel(vertices, source)));

    while (!queue.isEmpty()) {
      int vertex_id = queue.poll();

      // Prüfe, ob der Zielknoten erreicht wurde und beende die Suche ggf.
      if (vertices.get(vertex_id).equals(Graph.getVertexByLabel(vertices, sink)))
        break;

      // Füge alle Nachbarn des aktuellen Knotens zur Queue hinzu
      for (Edge edge : adjacency_list.get(vertex_id)) {
        int neighbor_id = vertices.indexOf(Graph.getVertexByLabel(vertices, edge.getTarget()));
        if (visited[neighbor_id] || edge.getCapacity() - edge.getFlow() <= 0)
          continue;
        queue.add(neighbor_id);
        visited[neighbor_id] = true;
        parent[neighbor_id] = vertex_id;
      }
    }

    // Erstelle den Pfad aus dem Array der Vorgänger
    ArrayList<Edge> augmenting_path = new ArrayList<Edge>();
    int current_vertex = vertices.indexOf(Graph.getVertexByLabel(vertices, sink));
    while (parent[current_vertex] != -1
        && current_vertex != vertices.indexOf(Graph.getVertexByLabel(vertices, source))) {
      Edge edge = Graph.getEdgeBetweenTwoVertices(vertices.get(parent[current_vertex]), vertices.get(current_vertex),
          residual_edges);
      augmenting_path.add(edge);
      current_vertex = parent[current_vertex];
    }
    Collections.reverse(augmenting_path);

    return augmenting_path;
  }

}