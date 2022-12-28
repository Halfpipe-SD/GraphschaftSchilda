package code.old;

import static code.old.Problem2.fordFulkersonMaxFlow;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileNotFoundException;

import code.utils.BasicWindow;
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

    JGraphPanel p1 = new JGraphPanel("Rohdaten", graph_input, "hierarchical");
    JGraphPanel p2 = new JGraphPanel("Flussnetzwerk mit Ford-Fulkerson", graph_output, "hierarchical");

    add(p1);
    add(p2);
  }
}