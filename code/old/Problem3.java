package code.old;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;

import code.utils.BasicWindow;

public class Problem3 extends BasicWindow {

  public Problem3(String title) throws FileNotFoundException {
    super(title);

    setSize(new Dimension(780, 480));
    setLayout(new GridLayout(1, 2));
    setLocationRelativeTo(null);

    Graph graph_input = new Graph("town_energy.json");

    // Erzeuge alle möglichen Kanten mit zufälligen Gewichten
    ArrayList<Edge> random_weighted_edges = generate_all_edges(graph_input.getVertices());
    graph_input.setEdges(random_weighted_edges);

    int max_edges = 5;
    Graph graph_output = kruskal(graph_input, max_edges);

    JGraphPanel p1 = new JGraphPanel("Rohdaten", graph_input, "circle");
    JGraphPanel p2 = new JGraphPanel("MST (Kruskal, max " + max_edges + " edges)", graph_output, "circle");

    add(p1);
    add(p2);
  }

  private Graph kruskal(Graph input, int max_edges) {

    // Lese die Knoten und Kanten aus den Rohdaten
    ArrayList<Vertex> vertices = input.getVertices();
    ArrayList<Edge> edges = input.getEdges();

    // Sortiere die Kanten nach Gewicht
    edges.sort(Comparator.comparingDouble(Edge::getWeight));

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
        if (t.contains(Graph.getSourceVertexFromEdge(e, vertices)))
          tree_u = t;
        if (t.contains(Graph.getTargetVertexFromEdge(e, vertices)))
          tree_v = t;
      }

      // Prüfe ob die kante e von einem vertex ausgeht, der bereits mehr als 5 kanten
      // hat
      ArrayList<Edge> source_edges = Graph.getAdjacentEdges(e.getSource(), output_edges);
      ArrayList<Edge> target_edges = Graph.getAdjacentEdges(e.getTarget(), output_edges);

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

    return new Graph(vertices, output_edges, false);
  }

  private ArrayList<Edge> generate_all_edges(ArrayList<Vertex> vertices) {
    ArrayList<Edge> output = new ArrayList<Edge>();
    for (int i = 0; i < vertices.size(); i++) {
      for (int j = i + 1; j < vertices.size(); j++) {
        // generate random weight and add new edge to output
        output.add(new Edge(vertices.get(i).getLabel(),
            vertices.get(j).getLabel(), Math.round(Math.random() * 100.0)));
      }
    }
    return output;
  }
}