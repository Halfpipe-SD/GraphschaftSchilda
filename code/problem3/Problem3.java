package code.problem3;

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

public class Problem3 extends BasicWindow {

  public Problem3(String title) throws FileNotFoundException {
    super(title);

    setSize(new Dimension(780, 480));
    setLayout(new GridLayout(1, 2));
    setLocationRelativeTo(null);

    GraphData graph_input = new GraphData("town_energy.json");

    // Erzeuge alle möglichen Kanten mit zufälligen Gewichten
    ArrayList<GraphEdge> random_weighted_edges = generate_all_edges(graph_input.getVertices());
    graph_input.setEdges(random_weighted_edges);

    GraphData graph_output = kruskal(graph_input);

    Dimension size = new Dimension(getWidth() / 2, getHeight());
    JGraphPanel p1 = new JGraphPanel("Rohdaten", size, graph_input, "circle");
    JGraphPanel p2 = new JGraphPanel("Minimum Spanning Tree (Kruskal)", size, graph_output, "circle");

    add(p1);
    add(p2);
  }

  public GraphData kruskal(GraphData input) {

    // Lese die Knoten und Kanten aus den Rohdaten
    ArrayList<GraphVertex> vertices = input.getVertices();
    ArrayList<GraphEdge> edges = input.getEdges();

    // Sortiere die Kanten nach Gewicht
    edges.sort(Comparator.comparingDouble(GraphEdge::getWeight));

    // erstelle einen wald 'f0rest' (eine menge von bäumen), wo jeder knoten ein
    // eigener baum ist
    ArrayList<ArrayList<GraphVertex>> forest = new ArrayList<ArrayList<GraphVertex>>();
    for (GraphVertex v : vertices) {
      ArrayList<GraphVertex> tree = new ArrayList<GraphVertex>();
      tree.add(v);
      forest.add(tree);
    }

    // erstelle eine liste mit den kanten des minimum spanning trees
    ArrayList<GraphEdge> forest_edges = new ArrayList<GraphEdge>(edges);

    // erstelle eine liste für die Ausgabe
    ArrayList<GraphEdge> output_edges = new ArrayList<GraphEdge>();

    // solange der wald nicht leer ist und der baum noch nicht alle knoten enthält
    while (forest_edges.size() > 0) {
      // entferne eine kante (u, v) aus forest
      GraphEdge e = forest_edges.remove(0);

      // Hole bäume von source und target der kante
      ArrayList<GraphVertex> tree_u = null;
      ArrayList<GraphVertex> tree_v = null;
      for (ArrayList<GraphVertex> t : forest) {
        if (t.contains(GraphData.getSourceVertexFromEdge(e, vertices)))
          tree_u = t;
        if (t.contains(GraphData.getTargetVertexFromEdge(e, vertices)))
          tree_v = t;
      }

      // Prüfe ob die kante e von einem vertex ausgeht, der bereits mehr als 5 kanten
      // hat
      GraphVertex source = GraphData.getSourceVertexFromEdge(e, vertices);
      GraphVertex target = GraphData.getTargetVertexFromEdge(e, vertices);
      int maximum_edges = 4;

      if (GraphData.getAdjacentEdges(source, output_edges).size() > maximum_edges
          || GraphData.getAdjacentEdges(target, output_edges).size() > maximum_edges) {
        System.out.println(
            "Kante " + e + " übersprungen, da sie einen Knoten mit mehr als " + maximum_edges + " Kanten verbindet.");
        continue;
      }

      // wenn u und v in verschiedenen Bäumen sind
      if (tree_u != tree_v) {
        // füge kante von u und v zur Ausgabe hinzu
        output_edges.add(e);

        // füge baum von v zu baum von u hinzu (merge)
        for (GraphVertex v : tree_v)
          tree_u.add(v);

        forest.remove(tree_v);
      }
    }

    System.out.println(output_edges);
    System.out.println(output_edges.size());

    return new GraphData(vertices, output_edges, false);
  }

  private ArrayList<GraphEdge> generate_all_edges(ArrayList<GraphVertex> vertices) {
    ArrayList<GraphEdge> edges = new ArrayList<GraphEdge>();

    for (int i = 0; i < vertices.size(); i++) {
      for (int j = i + 1; j < vertices.size(); j++) {
        GraphVertex v1 = vertices.get(i);
        GraphVertex v2 = vertices.get(j);

        // generate random weight and round to 0 decimal places
        double weight = Math.round(Math.random() * 100.0);

        GraphEdge e = new GraphEdge(v1.getLabel(), v2.getLabel(), weight);

        // check if edge already exists
        edges.add(e);

      }
    }
    return edges;
  }
}