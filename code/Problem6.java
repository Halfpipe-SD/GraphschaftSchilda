package code;

import static code.Problem2.fordFulkersonMaxFlow;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import code.utils.BasicWindow;
import code.utils.Graph;
import code.utils.Edge;
import code.utils.JGraphPanel;

public class Problem6 extends BasicWindow {

  public Problem6(String title) throws FileNotFoundException {
    super(title);

    setSize(new Dimension(500, 520));
    setLayout(new GridLayout(1, 2));
    setLocationRelativeTo(null);

    Graph graph_input = new Graph("town_traffic.json");
    Graph graph_output = graph_input.deepCopy();

    fordFulkersonMaxFlow(graph_output, "Highway", "Parking");

    // entferne alle inversen kanten für den output des graphen
    ArrayList<Edge> non_inverse = graph_output.getEdges();
    non_inverse.removeIf(e -> e.getCapacity() == 0);
    graph_output.setEdges(non_inverse);

    JGraphPanel p1 = new JGraphPanel("Rohdaten", graph_input, "hierarchical");
    JGraphPanel p2 = new JGraphPanel("Flussnetzwerk mit Ford-Fulkerson", graph_output, "hierarchical");

    add(p1);
    add(p2);
  }
}