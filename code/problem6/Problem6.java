package code.problem6;

import static code.problem2.Problem2.fordFulkersonMaxFlow;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import code.utils.BasicWindow;
import code.utils.GraphData;
import code.utils.GraphEdge;
import code.utils.JGraphPanel;

public class Problem6 extends BasicWindow {

  public Problem6(String title) throws FileNotFoundException {
    super(title);

    setSize(new Dimension(500, 520));
    setLayout(new GridLayout(1, 2));
    setLocationRelativeTo(null);

    GraphData graph_input = new GraphData("town_traffic.json");
    GraphData graph_output = graph_input.deepCopy();

    fordFulkersonMaxFlow(graph_output, "Highway", "Parking");

    // entferne alle inversen kanten für den output des graphen
    ArrayList<GraphEdge> non_inverse = graph_output.getEdges();
    non_inverse.removeIf(e -> e.getCapacity() == 0);
    graph_output.setEdges(non_inverse);

    JGraphPanel p1 = new JGraphPanel("Rohdaten", graph_input, "hierarchical");
    JGraphPanel p2 = new JGraphPanel("Flussnetzwerk mit Ford-Fulkerson", graph_output, "hierarchical");

    add(p1);
    add(p2);
  }
}